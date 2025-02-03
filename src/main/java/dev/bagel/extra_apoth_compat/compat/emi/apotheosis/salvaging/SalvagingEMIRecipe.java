package dev.bagel.extra_apoth_compat.compat.emi.apotheosis.salvaging;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.bagel.extra_apoth_compat.compat.emi.EmiConstants;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import dev.shadowsoffire.apotheosis.Apotheosis;
import dev.shadowsoffire.apotheosis.affix.salvaging.SalvagingRecipe;
import dev.shadowsoffire.apotheosis.socket.gem.GemRegistry;
import dev.shadowsoffire.apotheosis.socket.gem.Purity;
import dev.shadowsoffire.apotheosis.util.AffixItemIngredient;
import dev.shadowsoffire.apotheosis.util.GemIngredient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SalvagingEMIRecipe implements EmiRecipe {
    private static final ResourceLocation TEXTURES = Apotheosis.loc("textures/gui/salvage_jei.png");

    private final RecipeHolder<SalvagingRecipe> recipe;
    private final ResourceLocation id;
    private final IngredientData input;
    private final List<SalvagingRecipe.OutputData> outputs;

    public SalvagingEMIRecipe(RecipeHolder<SalvagingRecipe> recipe) {
        this.recipe = recipe;
        this.id = recipe.id();
        this.input = getIngredient(recipe.value().getInput());
        this.outputs = recipe.value().getOutputs();
    }

    private static IngredientData getIngredient(Ingredient ingredient) {
        ICustomIngredient custom = ingredient.getCustomIngredient();
        switch (custom) {
            case GemIngredient(Purity purity) -> {
                if (GemRegistry.INSTANCE.getValues().isEmpty()) {
                    return IngredientData.nullData(ingredient);
                }
                return new IngredientData(EmiIngredient.of(Ingredient.of(GemRegistry.INSTANCE.getValues().stream()
                        .filter(g -> purity.isAtLeast(g.getMinPurity()))
                        .map(g -> GemRegistry.createGemStack(g, purity)))), purity, null, IngredientType.GEM);
            }
            case AffixItemIngredient aii -> {
                return new IngredientData(EmiIngredient.of(ingredient), null, aii.getRarity(), IngredientType.AFFIX);
            }
            case null, default -> {
                return IngredientData.nullData(ingredient);
            }
        }
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return EmiConstants.Apotheosis.SALVAGING;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(input.ingredient());
    }

    @Override
    public List<EmiStack> getOutputs() {
        return outputs.stream().map(outputData -> EmiStack.of(outputData.stack())).toList();
    }

    @Override
    public int getDisplayWidth() {
        return 98;
    }

    @Override
    public int getDisplayHeight() {
        return 74;
    }

    @Override
    public @Nullable RecipeHolder<?> getBackingRecipe() {
        return recipe;
    }

    @Override
    public boolean supportsRecipeTree() {
        return false;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(TEXTURES, 0, 0, 98, 74, 0, 0);
        widgets.addSlot(this.input.ingredient(), 4, 28);
        int idx = 0;
        for (var d : this.outputs) {
            widgets.addSlot(EmiStack.of(d.stack()), 58 + 18 * (idx % 2), 10 + 18 * (idx / 2)).recipeContext(this);
            idx++;
        }

        widgets.addDrawable(0, 0, 98, 74, (draw, mouseX, mouseY, delta) -> {
            Font font = Minecraft.getInstance().font;
            PoseStack pose = draw.pose();

            int i = 0;
            for (var d : outputs) {
                pose.pushPose();
                pose.translate(0, 0, 200);
                String text = String.format("%d-%d", d.min(), d.max());

                float x = 59 + 18 * (i % 2) + (16 - font.width(text) * 0.5F);
                float y = 23F + 18 * ((float) i / 2);

                float scale = 0.5F;

                pose.scale(scale, scale, 1);
                draw.drawString(font, text, (int) (x / scale), (int) (y / scale), 0xFFFFFF);

                i++;
                pose.popPose();
            }
        });
    }

    public IngredientData getData() {
        return this.input;
    }
}
