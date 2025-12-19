package net.blay09.mods.kuma.forge.example;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(value = "kuma_example")
public class ForgeKumaExample {

    public ForgeKumaExample(FMLJavaModLoadingContext context) {
        if (FMLEnvironment.dist.isClient()) {
            ForgeKumaExampleClient.init(context.getModBusGroup());
        }
    }
}
