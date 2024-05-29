package net.blay09.mods.kuma.neoforge;

import net.blay09.mods.kuma.KumaRuntime;
import net.blay09.mods.kuma.KumaRuntimeFactory;

public class NeoForgeKumaRuntimeFactory implements KumaRuntimeFactory {
    @Override
    public KumaRuntime create() {
        return new NeoForgeKumaRuntime();
    }
}

