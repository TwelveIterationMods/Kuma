package net.blay09.mods.kuma.neoforge.example;

import com.mojang.blaze3d.platform.InputConstants;
import net.blay09.mods.kuma.api.*;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(value = "kuma_example", dist = Dist.CLIENT)
public class NeoForgeKumaExample {

    public NeoForgeKumaExample(IEventBus modEventBus) {
        Kuma.createKeyMapping(Identifier.fromNamespaceAndPath("kuma_example", "screen_test"))
                .withDefault(InputBinding.key(InputConstants.KEY_E, KeyModifiers.of(KeyModifier.CONTROL)))
                .handleScreenInput(event -> {
                    System.out.println("Screen input triggered");
                    return true;
                })
                .build();
    }
}
