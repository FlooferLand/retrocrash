package com.flooferland.retrocrash;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public class RetroCrashMod {
    public static final Logger LOGGER = LogUtils.getLogger();

    public static void initialize() {
		LOGGER.info("Initialized!");
    }
}
