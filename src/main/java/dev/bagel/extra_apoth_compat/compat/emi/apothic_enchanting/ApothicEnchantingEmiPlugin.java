package dev.bagel.extra_apoth_compat.compat.emi.apothic_enchanting;

import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import dev.shadowsoffire.apotheosis.Apotheosis;
import dev.shadowsoffire.apothic_enchanting.Ench;
import net.minecraft.world.level.block.Blocks;

public class ApothicEnchantingEmiPlugin {
    public static EmiRecipeCategory ENCHANTING = new EmiRecipeCategory(Apotheosis.loc("enchanting"),
            EmiStack.of(Blocks.ENCHANTING_TABLE)/*, ZenithEMIPlugin.simplifiedRenderer(0, 0),
            (r1, r2) -> Float.compare(((EnchantingEMIRecipe) r1).getEterna(), ((EnchantingEMIRecipe) r2).getEterna())*/);

    public static void register(EmiRegistry registry) {
        registry.addCategory(ENCHANTING);
        registry.addWorkstation(ENCHANTING, EmiStack.of(Blocks.ENCHANTING_TABLE));
        registry.getRecipeManager().getAllRecipesFor(Ench.RecipeTypes.INFUSION).forEach(holder -> {
            registry.addRecipe(new EnchantingEmiRecipe(holder));
        });
    }
}
