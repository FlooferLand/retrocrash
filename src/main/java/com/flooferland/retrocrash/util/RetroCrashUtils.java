package com.flooferland.retrocrash.util;

import net.minecraft.CrashReport;
import org.jetbrains.annotations.Nullable;

public final class RetroCrashUtils {
	/*? if >1.19 */ static net.minecraft.util.RandomSource random = net.minecraft.util.RandomSource.create();

	public static String getFriendlyReport(@Nullable CrashReport report) {
		if (report == null) return "Failed to get the crash report";
		return //? if >1.21 {
			report.getFriendlyReport(net.minecraft.ReportType.CRASH);
		//?} else {
			/*report.getFriendlyReport();
		*///?}
	}

	public static int randomInt(int min, int max) {
		//? if >1.19 {
		return random.nextInt(min, max);
		//? } else {
		/*return org.apache.commons.lang3.RandomUtils.nextInt(min, max);
		*///? }
	}
}
