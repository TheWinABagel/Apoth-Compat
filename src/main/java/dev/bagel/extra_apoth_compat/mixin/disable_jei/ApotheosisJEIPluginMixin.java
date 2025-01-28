package dev.bagel.extra_apoth_compat.mixin.disable_jei;

import dev.bagel.extra_apoth_compat.ModConfig;
import dev.shadowsoffire.apotheosis.compat.jei.AdventureJEIPlugin;
import dev.shadowsoffire.apothic_enchanting.compat.EnchJEIPlugin;
import dev.shadowsoffire.apothic_spawners.compat.SpawnerJEIPlugin;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(value = {AdventureJEIPlugin.class, EnchJEIPlugin.class, SpawnerJEIPlugin.class}, remap = false)
public abstract class ApotheosisJEIPluginMixin implements IModPlugin {

    @Inject(method = "registerRecipes", at = @At("HEAD"), cancellable = true)
    private void extra_apoth_compat$removeRecipes(IRecipeRegistration reg, CallbackInfo ci) {
        if (ModConfig.emiCompat) ci.cancel();
    }

    @Inject(method = "registerCategories", at = @At("HEAD"), cancellable = true)
    private void extra_apoth_compat$removeCategories(IRecipeCategoryRegistration reg, CallbackInfo ci) {
        if (ModConfig.emiCompat) ci.cancel();
    }
}
