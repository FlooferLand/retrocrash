//? if neoforge {
/*package com.flooferland.retrocrash.loaders.neoforge;

import com.flooferland.retrocrash.RetroCrashMod;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod("retrocrash")
public class NeoforgeEntrypoint {
    public NeoforgeEntrypoint() {
        RetroCrashMod.initialize();

	    //? if crash {
	    var initTime = System.currentTimeMillis();
		NeoForge.EVENT_BUS.addListener((ClientTickEvent.Post event) -> {
			if (System.currentTimeMillis() > initTime + 300) RetroCrashMod.sayGoodbye();
		});
	    // }
    }
}
//?}
*/