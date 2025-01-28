package dev.bagel.extra_apoth_compat.compat.emi.apotheosis;

import dev.emi.emi.api.neoforge.NeoForgeEmiIngredient;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.shadowsoffire.apotheosis.socket.gem.cutting.BasicGemCuttingRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;

public class BasicGemCuttingEMIRecipe extends GemCuttingEMIRecipe<BasicGemCuttingRecipe> {

    public BasicGemCuttingEMIRecipe(BasicGemCuttingRecipe recipe, ResourceLocation id) {
        super(id, getInputs(recipe), EmiStack.of(recipe.output()), recipe);
    }

    private static NonNullList<EmiIngredient> getInputs(BasicGemCuttingRecipe recipe) {
        NonNullList<EmiIngredient> list = NonNullList.create();
        list.add(EmiIngredient.of(recipe.top().stream().map(NeoForgeEmiIngredient::of).toList()));
        list.add(EmiIngredient.of(recipe.left().stream().map(NeoForgeEmiIngredient::of).toList()));
        list.add(EmiIngredient.of(recipe.right().stream().map(NeoForgeEmiIngredient::of).toList()));
        return NonNullList.create();
    }
}
