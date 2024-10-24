package net.blay09.mods.kuma.forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(value = "kuma_api")
public class ForgeKumaAPI {

    public ForgeKumaAPI() {
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            ForgeKumaAPIClient.init();
        });
    }
}
