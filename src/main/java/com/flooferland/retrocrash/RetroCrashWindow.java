package com.flooferland.retrocrash;

import com.flooferland.retrocrash.util.ResLoc;
import com.flooferland.retrocrash.util.RetroCrashUtils;
import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public final class RetroCrashWindow {
	static @Nullable Minecraft minecraft;
	static @Nullable CrashReport report;
	static @Nullable JFrame frame;
	static String gameName;
	static String gameNameCapitalized;
	static @Nullable Throwable error;

	public static void prepare() {
		// Workaround to fix a Swing crash (Minecraft's main thread runs headless and this breaks it)
		System.setProperty("java.awt.headless", "false");

		var isMinceraft = RandomSource.create().nextIntBetweenInclusive(0, 100) == 13;
		gameName = isMinceraft ? "minceraft" : "minecraft";
		gameNameCapitalized = gameName.substring(0, 1).toUpperCase() + gameName.substring(1);
	}

	static void run() {
		if (minecraft == null) return;
		if (report == null) return;
		var mc = minecraft.getWindow();

		frame = new JFrame("Minecraft") ;
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(mc.getWidth(), mc.getHeight());
		frame.setLocation(mc.getX(), mc.getY());

		// Main
		var mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBackground(new Color(46, 52, 68));
		mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
		frame.setContentPane(mainPanel);

		// Logo
		var logo = new JLabel();
		var logoImage = getLogo();
		if (logoImage != null) {
			logo.setIconTextGap(0);
			logo.setText(null);
			logo.setIcon(new ImageIcon(logoImage));
		} else {
			logo.setText(gameName.toUpperCase());
			logo.setForeground(new Color(145, 145, 145));
			logo.setFont(new Font(Font.MONOSPACED, Font.BOLD + Font.ITALIC, 64));
			logo.setHorizontalAlignment(SwingConstants.CENTER);
		}
		if (error != null) {
			RetroCrashMod.LOGGER.error(
				String.format("Unable to load %s's logo: ", gameNameCapitalized),
				error
			);
		}
		logo.setHorizontalAlignment(SwingConstants.CENTER);
		logo.setBorder(new EmptyBorder(15, 0, 0, 0));
		mainPanel.add(logo, BorderLayout.NORTH);

		// Crash report area
		var textArea = new JTextArea() {
			@Override
			protected void paintComponent(Graphics g) {
				var g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				super.paintComponent(g);
			}
		};
		textArea.setText(RetroCrashUtils.getFriendlyReport(report));
		textArea.setFont(new Font(Font.MONOSPACED, Font.BOLD, 15));
		textArea.setForeground(Color.BLACK);
		textArea.setBackground(Color.WHITE);
		textArea.setEditable(false);
		textArea.setLineWrap(false);
		textArea.setMargin(new Insets(3, 3, 3, 3));
		textArea.setCaretPosition(0);

		var scrollPane = new JScrollPane(textArea);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(null);

		// Content wrapper
		var content = new JPanel();
		content.setLayout(new BorderLayout());
		content.setBackground(new Color(46, 52, 68));
		content.setBorder(new EmptyBorder(10, 45, 125, 45));
		content.add(scrollPane, BorderLayout.CENTER);

		// Window stuff
		mainPanel.add(content, BorderLayout.CENTER);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				synchronized (RetroCrashWindow.class) {
					RetroCrashWindow.class.notifyAll();
				}
			}
		});
		frame.setVisible(true);
	}

	@Nullable
	static Image getLogo() {
		if (minecraft == null) return null;
		var resources = minecraft.getResourceManager();

		var logo = resources.getResource(ResLoc.ofVanilla("textures/gui/title/" + gameName + ".png")).orElse(null);
		if (logo == null) return null;
		try (var stream = logo.open()) {
			BufferedImage image = ImageIO.read(stream);
			//? if <1.20 {
				try { image = mergeLogo(image); }
				catch (Exception throwable) {
					error = throwable;
					return null;
				}
			//? }
			return image.getScaledInstance(310, 80, Image.SCALE_SMOOTH);
		} catch (IOException e) {
			error = e;
		}
		return null;
	}

	//? if <1.20 {
	static BufferedImage mergeLogo(BufferedImage full) throws RasterFormatException, IllegalArgumentException {
		final int partHeight = 45;
		final int minecPartWidth = 154;
		final int raftPartWidth = 118;
		final int combinedWidth = minecPartWidth + raftPartWidth;

		var minecPart = full.getSubimage(0, 0, minecPartWidth, partHeight);
		var raftPart = full.getSubimage(0, partHeight, raftPartWidth, partHeight);

		// Merging back together the logo, since Mojang used to split it into 2 separate UV rects for some reason
		var merged = new BufferedImage(combinedWidth, partHeight + 30, BufferedImage.TYPE_INT_ARGB);
		var graphics = merged.createGraphics();
		graphics.drawImage(minecPart, 0, 0, null);
		graphics.drawImage(raftPart, minecPartWidth, 0, null);
		graphics.dispose();

		// Returning the image the same size as the other resize code expects
		var scaled = merged.getScaledInstance(1024, 256, Image.SCALE_DEFAULT);
		var buffered = new BufferedImage(1024, 256, BufferedImage.TYPE_INT_ARGB);
		buffered.createGraphics().drawImage(scaled, 0, 0, null);
		return buffered;
	}
	//? }

	public static void spawn(Minecraft minecraft, CrashReport report) {
		RetroCrashWindow.minecraft = minecraft;
		RetroCrashWindow.report = report;
		prepare();

		RetroCrashMod.LOGGER.info("SPAWNING!");

		// Shutting off Minecraft's window to replace it
		var window = minecraft.getWindow();
		long glfwWindow = //? if >1.21.7 {
		/*window.handle();
		*///?} else {
		window.getWindow();
		//?}
		if (glfwWindow > 0) {
			GLFW.glfwHideWindow(glfwWindow);
		}

		// Running the window
		var thread = new Thread(() -> {
			try {
				SwingUtilities.invokeAndWait(RetroCrashWindow::run);
				synchronized (RetroCrashWindow.class) {
					RetroCrashWindow.class.wait();
				}
			} catch (Exception e) {
				RetroCrashMod.LOGGER.error("Failed to show window", e);
			}
		}, "RetroCrash-Window");
		thread.setDaemon(false);
		thread.start();

		try {
			thread.join();
		} catch (InterruptedException e) {
			RetroCrashMod.LOGGER.error("Failed to show window", e);
		}
	}
}
