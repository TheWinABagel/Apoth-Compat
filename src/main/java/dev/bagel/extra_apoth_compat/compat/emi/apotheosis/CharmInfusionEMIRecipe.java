package dev.bagel.extra_apoth_compat.compat.emi.apotheosis;

import dev.bagel.extra_apoth_compat.compat.emi.apothic_enchanting.EnchantingEmiRecipe;
import dev.emi.emi.api.stack.EmiStack;
import dev.shadowsoffire.apothic_enchanting.table.infusion.InfusionRecipe;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Unbreakable;

public class CharmInfusionEMIRecipe extends EnchantingEmiRecipe {

    public CharmInfusionEMIRecipe(InfusionRecipe recipe, ResourceLocation id, ItemStack charm) {
        super(recipe, id, EmiStack.of(charm.copy()), getOutput(charm));
    }

    public static EmiStack getOutput(ItemStack charm) {
        ItemStack out = charm.copy();
        out.set(DataComponents.UNBREAKABLE, new Unbreakable(true));
        return EmiStack.of(out);
    }
}
