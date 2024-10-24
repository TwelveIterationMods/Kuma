package net.blay09.mods.kuma;

import java.util.ServiceLoader;

public class KumaRuntimeSpi {
    public static KumaRuntime create() {
        var loader = ServiceLoader.load(KumaRuntimeFactory.class);
        var factory = loader.findFirst().orElseThrow();
        return factory.create();
    }
}
