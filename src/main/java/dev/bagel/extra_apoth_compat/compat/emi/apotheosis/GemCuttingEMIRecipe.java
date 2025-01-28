package dev.bagel.extra_apoth_compat.compat.emi.apotheosis;

import dev.bagel.extra_apoth_compat.compat.emi.Constants;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import dev.shadowsoffire.apotheosis.Apotheosis;
import dev.shadowsoffire.apotheosis.socket.gem.Gem;
import dev.shadowsoffire.apotheosis.socket.gem.GemRegistry;
import dev.shadowsoffire.apotheosis.socket.gem.cutting.GemCuttingRecipe;
import dev.shadowsoffire.apotheosis.socket.gem.cutting.PurityUpgradeRecipe;
import net.minecraft.resources.ResourceLocation;

import java.util.Comparator;
import java.util.List;

public abstract class GemCuttingEMIRecipe<T extends GemCuttingRecipe> implements EmiRecipe {
    public static final ResourceLocation TEXTURES = Apotheosis.loc("textures/gui/gem_cutting_jei.png");
    public static Comparator<EmiRecipe> SORTER = (o1, o2) -> {
        if (o1 instanceof PurityUpgradeEMIRecipe r1 && o2 instanceof PurityUpgradeEMIRecipe r2) {
            return r1.getPurity().compareTo(r2.getPurity());
        }
        return 0;
    };

    protected final ResourceLocation id;
    protected final List<EmiIngredient> inputs;
    protected final EmiStack output;
    protected final T recipe;

    public GemCuttingEMIRecipe(ResourceLocation id, List<EmiIngredient> inputs, EmiStack output, T recipe) {
        this.id = id;
        this.inputs = inputs;
        this.output = output;
        this.recipe = recipe;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return Constants.Apotheosis.GEM_CUTTING;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return inputs;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(output);
    }

    @Override
    public int getDisplayWidth() {
        return 148;
    }

    @Override
    public int getDisplayHeight() {
        return 78;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(TEXTURES, 0, 0, 148, 78, 0, 0);

        widgets.addSlot(getInputs().get(0), 48, 37); //Center item (input)

        widgets.addSlot(getInputs().get(1), 48, 4); //Top Item (input)
        widgets.addSlot(getInputs().get(2), 18, 56); //Left Item (input)
        widgets.addSlot(getInputs().get(3), 76, 56); //Right Item (input)

        widgets.addSlot(getOutputs().get(0), 117, 35).recipeContext(this); //Output
    }

    public record StackInfo(EmiStack input, EmiStack output, Gem gem) {

        public static StackInfo of(Gem g, PurityUpgradeRecipe pur) {
            return new StackInfo(EmiStack.of(GemRegistry.createGemStack(g, pur.purity())), EmiStack.of(GemRegistry.createGemStack(g, pur.purity().next())), g);
        }
    }
}
