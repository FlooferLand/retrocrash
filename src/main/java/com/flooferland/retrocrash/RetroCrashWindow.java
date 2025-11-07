package com.flooferland.retrocrash;

import net.minecraft.CrashReport;
import net.minecraft.ReportType;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public final class RetroCrashWindow {
	static @Nullable Minecraft minecraft;
	static @Nullable CrashReport report;
	static @Nullable JFrame frame;
	static String gameName;
	static String gameNameCapitalized;

	public static void prepare() {
		System.setProperty("java.awt.headless", "false");

		var isMinceraft = RandomSource.create().nextIntBetweenInclusive(0, 20) == 3;
		gameName = isMinceraft ? "minceraft" : "minecraft";
		gameNameCapitalized = gameName.substring(0, 1).toUpperCase() + gameName.substring(1);
	}

	static void run() {
		if (minecraft == null) return;
		if (report == null) return;
		var mc = minecraft.getWindow();

		frame = new JFrame("Minecraft") ;
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setSize(mc.getWidth(), mc.getHeight());
		frame.setLocation(mc.getX(), mc.getY());

		// Main
		var mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBackground(new Color(46, 52, 68));
		mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
		frame.setContentPane(mainPanel);

		// Logo
		var logo = new JLabel(gameName.toUpperCase());
		var logoImage = getLogo();
		if (logoImage != null) {
			logo.setIconTextGap(0);
			logo.setText(null);
			logo.setIcon(new ImageIcon(logoImage));
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
		textArea.setText(report.getFriendlyReport(ReportType.CRASH));
		textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 18));
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
		content.setBorder(new EmptyBorder(10, 35, 115, 35));
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

		var logo = resources.getResource(ResourceLocation.withDefaultNamespace("textures/gui/title/" + gameName + ".png")).orElse(null);
		if (logo == null) return null;
		try (var stream = logo.open()) {
			Image image = ImageIO.read(stream);
			image = image.getScaledInstance(300, 75, Image.SCALE_SMOOTH);
			return image;
		} catch (IOException e) {
			RetroCrashMod.LOGGER.error("Unable to load {}'s logo: ", gameNameCapitalized);
		}
		return null;
	}

	public static void spawn(Minecraft minecraft, CrashReport report) {
		RetroCrashWindow.minecraft = minecraft;
		RetroCrashWindow.report = report;

		// Shutting off Minecraft's window to replace it
		long glfwWindow = minecraft.getWindow().getWindow();
		if (glfwWindow > 0) {
			GLFW.glfwHideWindow(glfwWindow);
		}

		// Running the window
		prepare();
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
