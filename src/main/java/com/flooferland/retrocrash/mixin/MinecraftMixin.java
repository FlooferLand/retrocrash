package com.flooferland.retrocrash.mixin;

import com.flooferland.retrocrash.RetroCrashWindow;
import com.flooferland.retrocrash.util.RetroCrashUtils;
import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

import static com.flooferland.retrocrash.RetroCrashMod.sayGoodbye;

@Mixin(value = Minecraft.class, priority = 500)
public class MinecraftMixin {
	//? if >1.21 {
	@Inject(method = "crash", at = @At("HEAD"))
	private static void beforeExit(Minecraft minecraft, File gameDirectory, CrashReport report, CallbackInfo ci) {
		RetroCrashWindow.spawn(Minecraft.getInstance(), report);
	}
	//?} else {
	/*@Inject(method = "crash", at = @At("HEAD"))
	private static void beforeExit(CrashReport report, CallbackInfo ci) {
		RetroCrashWindow.spawn(Minecraft.getInstance(), report);
	}
	*///?}
	
	@Inject(method = "run", at = @At("HEAD"))
	private void initDevCrash(CallbackInfo ci) {
		if (RetroCrashUtils.devShouldCrash())
			sayGoodbye();
	}
}
