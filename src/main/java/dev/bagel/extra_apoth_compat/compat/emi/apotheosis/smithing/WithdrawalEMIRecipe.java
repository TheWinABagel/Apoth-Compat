package dev.bagel.extra_apoth_compat.compat.emi.apotheosis.smithing;

import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class WithdrawalEMIRecipe extends SocketingEMIRecipe {
    public WithdrawalEMIRecipe(EmiIngredient addition, ResourceLocation id, List<EmiIngredient> gems) {
        super(addition, id, gems, () -> Component.translatable("emi.extra_apoth_compat.withdrawal.info").withStyle(ChatFormatting.DARK_RED));
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 62, 1);
        widgets.addSlot(EmiStack.EMPTY, 0, 0);
        addSlot(widgets, 18, 0, OUTPUT);
//        addSlot(widgets, 18, 0, ADDITION);
        widgets.addSlot(this.addition, 36, 0);
        addSlot(widgets, 94, 0, INPUT).recipeContext(this).appendTooltip(outputTooltip.get());
    }

    @Override
    protected int firstSlot() {
        return 2;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(this.addition);
    }
}
