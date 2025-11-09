package com.flooferland.retrocrash.util;

import net.minecraft.network.chat.Component;

/** Version-independant abstraction for Component */
public final class Comp {
	public static Component of(String text) {
		//? if >1.19 {
		return Component.literal(text);
		//? } else {
		/*return new net.minecraft.network.chat.TextComponent(text);
		*///? }
	}
}
