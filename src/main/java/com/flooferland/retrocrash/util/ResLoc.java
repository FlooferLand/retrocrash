package com.flooferland.retrocrash.util;

import net.minecraft.resources.ResourceLocation;

public final class ResLoc {
	public static ResourceLocation ofVanilla(String path) {
		return ResourceLocation.tryBuild(ResourceLocation.DEFAULT_NAMESPACE, path);
	}
}
