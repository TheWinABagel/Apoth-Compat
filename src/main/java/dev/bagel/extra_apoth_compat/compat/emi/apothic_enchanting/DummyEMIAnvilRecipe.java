package dev.bagel.extra_apoth_compat.compat.emi.apothic_enchanting;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DummyEMIAnvilRecipe implements EmiRecipe {

    private final ResourceLocation id;
    private final EmiIngredient first;
    private final EmiIngredient second;
    private final EmiStack out;

    public DummyEMIAnvilRecipe(ItemStack first, ItemStack second, ItemStack out, ResourceLocation id) {
        this.id = id;
        this.first = EmiStack.of(first);
        this.second = EmiStack.of(second);
        this.out = EmiStack.of(out);
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return VanillaEmiRecipeCategories.ANVIL_REPAIRING;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return this.id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(this.first, this.second);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(this.out);
    }

    @Override
    public boolean supportsRecipeTree() {
        return false;
    }

    @Override
    public int getDisplayWidth() {
        return 125;
    }

    @Override
    public int getDisplayHeight() {
        return 18;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(EmiTexture.PLUS, 27, 3);
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 75, 1);
        widgets.addSlot(first, 0, 0);
        widgets.addSlot(second, 49, 0);
        widgets.addSlot(out, 107, 0).recipeContext(this);
    }
}
