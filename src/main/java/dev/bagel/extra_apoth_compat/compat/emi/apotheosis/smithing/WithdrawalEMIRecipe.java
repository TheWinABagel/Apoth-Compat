package dev.bagel.extra_apoth_compat.compat.emi.apotheosis.smithing;

import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class WithdrawalEMIRecipe extends SocketingEMIRecipe {
    public WithdrawalEMIRecipe(EmiIngredient addition, ResourceLocation id, List<EmiIngredient> gems) {
        super(addition, id, gems);
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 62, 1);
        widgets.addSlot(EmiStack.EMPTY, 0, 0);
        widgets.addGeneratedSlot(r -> getStack(r, 2), uniq, 18, 0);
        widgets.addSlot(this.addition, 36, 0);
        widgets.addGeneratedSlot(r -> getStack(r, 0), uniq, 94, 0).recipeContext(this);
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
