package net.blay09.mods.kuma.forge;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(value = "kuma_api")
public class ForgeKumaAPI {

    public ForgeKumaAPI(FMLJavaModLoadingContext context) {
        if (FMLEnvironment.dist.isClient()) {
            ForgeKumaAPIClient.init(context.getModEventBus());
        }
    }
}
