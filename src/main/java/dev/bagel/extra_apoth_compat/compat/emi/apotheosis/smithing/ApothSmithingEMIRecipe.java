package dev.bagel.extra_apoth_compat.compat.emi.apotheosis.smithing;

import dev.emi.emi.EmiUtil;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
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
    protected final int uniq = EmiUtil.RANDOM.nextInt();
    protected final Random rand = EmiUtil.RANDOM;
    protected static final List<EmiStack> TOOLS;
    protected final Supplier<Component> outputTooltip;

    static {
        List<EmiStack> valid_tools = new ArrayList<>(EmiTags.getRawValues(Tags.Items.TOOLS));
        valid_tools.addAll(EmiTags.getRawValues(Tags.Items.ARMORS));
        TOOLS = valid_tools.stream().filter(emiStack -> LootCategory.forItem(emiStack.getItemStack()) != LootCategory.NONE).toList();
    }

    public ApothSmithingEMIRecipe(EmiIngredient addition, ResourceLocation id, Supplier<Component> outputTooltip) {
        super(EmiStack.EMPTY, EmiStack.of(Apoth.Blocks.AUGMENTING_TABLE.value()), addition, EmiStack.of(Apoth.Blocks.AUGMENTING_TABLE.value()), id);
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
        widgets.addGeneratedSlot(r -> getStack(r, 0), uniq, 18, 0);
        widgets.addSlot(this.addition, 36, 0);
        widgets.addGeneratedSlot(r -> getStack(r, 2), uniq, 94, 0).recipeContext(this).appendTooltip(outputTooltip.get());
    }

    protected abstract EmiStack getStack(Random r, int slot);


//    private EmiStack getStack(Random r, int i) {
//        EmiStack input = this.input.getEmiStacks().get(r.nextInt(this.input.getEmiStacks().size()));
//        EmiStack addition = this.addition.getEmiStacks().get(r.nextInt(this.addition.getEmiStacks().size()));
//        SmithingRecipeInput inv = new SmithingRecipeInput(template.getEmiStacks().get(0).getItemStack(), input.getItemStack(), addition.getItemStack());
//        Minecraft client = Minecraft.getInstance();
//        return new EmiStack[] {
//                input,
//                addition,
//                EmiStack.of(recipe.assemble(inv, client.level.registryAccess()))
//        }[i];
//    }
}
