package com.flooferland.retrocrash.util;

import net.minecraft.CrashReport;
import org.jetbrains.annotations.Nullable;

public final class RetroCrashUtils {
	public static String getFriendlyReport(@Nullable CrashReport report) {
		if (report == null) return "Failed to get the crash report";
		return //? if >1.21 {
			report.getFriendlyReport(ReportType.CRASH);
		//?} else {
			/*report.getFriendlyReport();
		*///?}
	}
}
