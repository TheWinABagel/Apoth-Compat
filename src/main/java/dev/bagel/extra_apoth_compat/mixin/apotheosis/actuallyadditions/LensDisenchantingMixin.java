package dev.bagel.extra_apoth_compat.mixin.apotheosis.actuallyadditions;

import de.ellpeck.actuallyadditions.mod.items.lens.LensDisenchanting;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Pseudo
@Mixin(value = LensDisenchanting.class, remap = false)
public class LensDisenchantingMixin {
    @Redirect(method = "invoke",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;getAllEnchantments(Lnet/minecraft/core/HolderLookup$RegistryLookup;)Lnet/minecraft/world/item/enchantment/ItemEnchantments;",
                    remap = true)
    )
    private ItemEnchantments fixExtraEnchants(ItemStack instance, HolderLookup.RegistryLookup registryLookup) {
        return instance.getTagEnchantments();
    }
}
