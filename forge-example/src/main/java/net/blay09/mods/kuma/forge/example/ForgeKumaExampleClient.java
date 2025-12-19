package net.blay09.mods.kuma.forge.example;

import com.mojang.blaze3d.platform.InputConstants;
import net.blay09.mods.kuma.api.*;
import net.minecraft.resources.Identifier;
import net.minecraftforge.eventbus.api.bus.BusGroup;

public class ForgeKumaExampleClient {
    public static void init(BusGroup modBusGroup) {
        Kuma.createKeyMapping(Identifier.fromNamespaceAndPath("kuma_example", "screen_test"))
                .withDefault(InputBinding.key(InputConstants.KEY_E, KeyModifiers.of(KeyModifier.CONTROL)))
                .handleScreenInput(event -> {
                    System.out.println("Screen input triggered");
                    return true;
                })
                .build();
    }
}
