package com.flooferland.retrocrash.mixin;

import com.flooferland.retrocrash.RetroCrashWindow;
import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Mixin(value = Minecraft.class, priority = 500)
public class MinecraftMixin {
	@Inject(method = "emergencySaveAndCrash", at = @At("HEAD"))
	private static void beforeSave(CrashReport crashReport, CallbackInfo ci) {
		RetroCrashWindow.prepare();
	}

	@Inject(method = "crash", at = @At(value = "HEAD", target = "Ljava/lang/System;exit(I)V"))
	private static void beforeExit(Minecraft minecraft, File file, CrashReport report, CallbackInfo ci) {
		RetroCrashWindow.spawn(minecraft, report);
	}
}
