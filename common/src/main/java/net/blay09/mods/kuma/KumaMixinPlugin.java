package net.blay09.mods.kuma;

import com.google.common.collect.ImmutableMap;
import net.blay09.mods.kuma.api.Kuma;
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class KumaMixinPlugin implements IMixinConfigPlugin {

    private static final Map<String, Supplier<Boolean>> mixinApplyConditions = ImmutableMap.of(
            "net.blay09.mods.kuma.mixin.controlling.ControllingKeyEntryMixin",
            () -> Kuma.__getRuntime().isModInstalled("controlling")
    );

    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public @Nullable String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        final var condition = mixinApplyConditions.get(mixinClassName);
        return condition == null || condition.get();
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public @Nullable List<String> getMixins() {
        return null;
    }

    @Override
    public void postApply(String targetClassName, ClassNode classNode, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void preApply(String targetClassName, ClassNode classNode, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
