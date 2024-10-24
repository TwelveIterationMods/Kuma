package net.blay09.mods.kuma.forge;

import net.blay09.mods.kuma.KumaRuntime;
import net.blay09.mods.kuma.KumaRuntimeFactory;

public class ForgeKumaRuntimeFactory implements KumaRuntimeFactory {
    @Override
    public KumaRuntime create() {
        return new ForgeKumaRuntime();
    }
}

