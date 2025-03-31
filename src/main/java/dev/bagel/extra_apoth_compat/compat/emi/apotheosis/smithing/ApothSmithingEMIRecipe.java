package dev.bagel.extra_apoth_compat.compat.emi.apotheosis.smithing;

import dev.emi.emi.EmiUtil;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import dev.emi.emi.recipe.EmiSmithingRecipe;
import dev.emi.emi.registry.EmiTags;
import dev.shadowsoffire.apotheosis.Apoth;
import dev.shadowsoffire.apotheosis.loot.LootCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.Tags;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public abstract class ApothSmithingEMIRecipe extends EmiSmithingRecipe {
    protected final Random rand = EmiUtil.RANDOM;
    protected final int uniq = rand.nextInt();
    protected static final List<EmiStack> TOOLS;

    public static final int INPUT = 0;
    public static final int ADDITION = 1;
    public static final int OUTPUT = 2;

    protected final Supplier<Component> outputTooltip;

    static {
        List<EmiStack> valid_tools = new ArrayList<>(EmiTags.getRawValues(Tags.Items.TOOLS));
        valid_tools.addAll(EmiTags.getRawValues(Tags.Items.ARMORS));
        TOOLS = valid_tools.stream().filter(emiStack -> !LootCategory.forItem(emiStack.getItemStack()).isNone()).toList();
    }

    public ApothSmithingEMIRecipe(EmiIngredient addition, ResourceLocation id, Supplier<Component> outputTooltip) {
        super(EmiStack.EMPTY, EmiStack.EMPTY, addition, EmiStack.EMPTY, id);
        this.outputTooltip = outputTooltip;
    }

    @Override
    public boolean supportsRecipeTree() {
        return false;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 62, 1);
        widgets.addSlot(EmiStack.EMPTY, 0, 0);
        addSlot(widgets, 18, 0, INPUT);
        addSlot(widgets, 36, 0, ADDITION);
        addSlot(widgets, 94, 0, OUTPUT).recipeContext(this).appendTooltip(outputTooltip.get());
//        widgets.addGeneratedSlot(r -> getStack(r, 0), uniq, 18, 0);
//        widgets.addSlot(this.addition, 36, 0);
//        widgets.addGeneratedSlot(r -> getStack(r, 2), uniq, 94, 0).recipeContext(this).appendTooltip(outputTooltip.get());
    }

    protected SlotWidget addSlot(WidgetHolder widgets, int x, int y, int slot) {
        if (isGenerated(slot)) {
            return widgets.addGeneratedSlot(r -> getStack(r, slot), uniq, x, y);
        } else return widgets.addSlot(getStack(rand, slot), x, y);
    }

    protected abstract EmiStack getStack(Random r, int slot);

    protected abstract boolean isGenerated(int slot);
}
