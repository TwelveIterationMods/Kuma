package net.blay09.mods.kuma.fabric;

import com.mojang.blaze3d.platform.InputConstants;
import net.blay09.mods.kuma.api.InputBinding;
import net.blay09.mods.kuma.api.KeyModifier;
import net.blay09.mods.kuma.api.KeyModifiers;
import net.blay09.mods.kuma.api.Kuma;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.resources.Identifier;

public class FabricKumaExample implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        Kuma.createKeyMapping(Identifier.fromNamespaceAndPath("kuma_example", "screen_test"))
                .withDefault(InputBinding.key(InputConstants.KEY_E, KeyModifiers.of(KeyModifier.CONTROL)))
                .handleScreenInput(event -> {
                    System.out.println("Screen input triggered");
                    return true;
                })
                .build();
    }
}
