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

        Kuma.createKeyMapping(Identifier.fromNamespaceAndPath("kuma_example", "f4_test"))
                .withDefault(InputBinding.key(InputConstants.KEY_F4, KeyModifiers.none()))
                .handleWorldInput(event -> {
                    if (!Kuma.isDown(InputConstants.getKey("key.keyboard.f3"))) {
                        System.out.println("F4 input triggered");
                        return true;
                    }
                    return false;
                })
                .build();

        Kuma.createKeyMapping(Identifier.fromNamespaceAndPath("kuma_example", "alt_1"))
                .withDefault(InputBinding.key(InputConstants.KEY_1, KeyModifiers.of(KeyModifier.ALT)))
                .skipRegistration()
                .handleWorldInput(event -> {
                    System.out.println("ALT+1 input triggered");
                    return true;
                })
                .build();
    }
}
