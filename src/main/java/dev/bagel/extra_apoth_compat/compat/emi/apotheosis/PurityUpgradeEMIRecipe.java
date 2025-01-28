package dev.bagel.extra_apoth_compat.compat.emi.apotheosis;

import dev.emi.emi.api.neoforge.NeoForgeEmiIngredient;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.widget.WidgetHolder;
import dev.shadowsoffire.apotheosis.socket.gem.Purity;
import dev.shadowsoffire.apotheosis.socket.gem.cutting.PurityUpgradeRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class PurityUpgradeEMIRecipe extends GemCuttingEMIRecipe<PurityUpgradeRecipe> {
    public PurityUpgradeEMIRecipe(ResourceLocation id, PurityUpgradeRecipe recipe, GemCuttingEMIRecipe.StackInfo stackInfo) {
        super(getId(id, stackInfo), getInputs(recipe, stackInfo.input()), stackInfo.output(), recipe);
    }

    public Purity getPurity() {
        return this.recipe.purity();
    }

    private static NonNullList<EmiIngredient> getInputs(PurityUpgradeRecipe recipe, EmiIngredient input) {
        NonNullList<EmiIngredient> list = NonNullList.create();
        list.add(input);
        list.add(input);
        list.add(EmiIngredient.of(recipe.left().stream().map(NeoForgeEmiIngredient::of).toList()));
        list.add(EmiIngredient.of(recipe.right().stream().map(NeoForgeEmiIngredient::of).toList()));
        return list;
    }

    private static ResourceLocation getId(ResourceLocation old, StackInfo info) {
        return ResourceLocation.fromNamespaceAndPath(old.getNamespace(), "/" + old.getPath() + "/"+ info.gem().getId().toString().replaceAll("[.:]", "/"));
    }
}
