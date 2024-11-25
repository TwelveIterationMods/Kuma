package net.blay09.mods.kuma.forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(value = "kuma_api")
public class ForgeKumaAPI {

    public ForgeKumaAPI(IEventBus modEventBus) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            ForgeKumaAPIClient.init(modEventBus);
        }
    }
}
