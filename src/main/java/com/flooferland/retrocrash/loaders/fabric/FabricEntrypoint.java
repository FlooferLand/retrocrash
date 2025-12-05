//? if fabric {
package com.flooferland.retrocrash.loaders.fabric;

import com.flooferland.retrocrash.RetroCrashMod;
import net.fabricmc.api.ModInitializer;

public class FabricEntrypoint implements ModInitializer {
    @Override
    public void onInitialize() {
        RetroCrashMod.initialize();
    }
}
//?}
