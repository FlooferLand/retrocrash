//? if fabric {
package com.flooferland.retrocrash.loaders.fabric;

import com.flooferland.retrocrash.RetroCrashMod;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class FabricEntrypoint implements ModInitializer {
    @Override
    public void onInitialize() {
        RetroCrashMod.initialize();

        //? if crash {
		var initTime = System.currentTimeMillis();
        ClientTickEvents.END_CLIENT_TICK.register(minecraft -> {
	        if (System.currentTimeMillis() > initTime + 300) RetroCrashMod.sayGoodbye();
		});
		// }
    }
}
//?}
