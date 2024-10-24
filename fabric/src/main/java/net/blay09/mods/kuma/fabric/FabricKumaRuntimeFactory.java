package net.blay09.mods.kuma.fabric;

import net.blay09.mods.kuma.KumaRuntime;
import net.blay09.mods.kuma.KumaRuntimeFactory;

public class FabricKumaRuntimeFactory implements KumaRuntimeFactory {
    @Override
    public KumaRuntime create() {
        return new FabricKumaRuntime();
    }
}

