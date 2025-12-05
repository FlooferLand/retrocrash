//? if fabric {
package com.flooferland.retrocrash.loaders.fabric;

import com.flooferland.retrocrash.util.Comp;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.client.gui.screens.Screen;

import static com.flooferland.retrocrash.RetroCrashConfig.Companion.DEFAULT;
import static com.flooferland.retrocrash.RetroCrashMod.config;

public class RetroCrashModMenu implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return this::makeScreen;
	}

	private void onSave() {
		config.save();
	}

	private Screen makeScreen(Screen parent) {
		var builder = ConfigBuilder.create()
			.setParentScreen(parent)
			.setTitle(Comp.of("Retro Crash Screen"))
			.setSavingRunnable(this::onSave);
		var entryBuilder = builder.entryBuilder();

		var general = builder.getOrCreateCategory(Comp.of("General"));

		general.addEntry(
			entryBuilder.startBooleanToggle(Comp.of("Native look"), config.nativeLook)
				.setDefaultValue(DEFAULT.nativeLook)
				.setTooltip(Comp.of("When enabled, the theme changes to better resemble your operating system's"))
				.setSaveConsumer(newValue -> config.nativeLook = newValue)
				.build()
		);

		return builder.build();
	}
}

//?}