package com.flooferland.retrocrash.util;

import net.minecraft.resources.ResourceLocation;

public final class ResLoc {
	public static ResourceLocation ofVanilla(String path) {
		//? if >1.19 {
		return ResourceLocation.tryBuild(ResourceLocation.DEFAULT_NAMESPACE, path);
		//? } else {
		/*return new ResourceLocation("minecraft", path);
		*///? }
	}
}
