package dev.bagel.extra_apoth_compat.mixin;

import dev.bagel.extra_apoth_compat.ModConfig;
import dev.bagel.extra_apoth_compat.ModMixinConfig;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class MixinPlugin implements IMixinConfigPlugin {

    @Override
    public void onLoad(String mixinPackage) {
        ModMixinConfig.load();
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
//        ExtraApothCompat.LOGGER.info("Mixin class name: {}, target is {}", mixinClassName, targetClassName);
        if (!mixinClassName.startsWith(MixinPlugin.class.getPackageName())) {
            return true; //Only configure our stuff
        }
        for (String str : ModMixinConfig.disabledMixins) {
            if (str.equals(mixinClassName)) return false;
        }
        String[] mixinName = mixinClassName.split("\\.");
        String[] targetName = targetClassName.split("\\.");
        if (ModConfig.Loaded.JEI && mixinName[4].equals("disable_jei") && ModMixinConfig.disableJeiLoading) {
            return isLoaded(targetName[2]);
        }
        if (mixinName[4].equals("AttributesGuiHideUnchangedMixin")) {
            return ModMixinConfig.hideUnchangedProperly;
        }

        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    private boolean isLoaded(String modId) {
        return switch (modId) {
            case "apotheosis" -> ModConfig.Loaded.APOTHEOSIS;
            case "apothic_enchanting" -> ModConfig.Loaded.APOTHIC_ENCHANTING;
            case "apothic_spawners" -> ModConfig.Loaded.APOTHIC_SPAWNERS;
            default -> false;
        };
    }
}
