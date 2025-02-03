package dev.bagel.extra_apoth_compat.compat.emi.apotheosis.salvaging;

import com.google.common.collect.Lists;
import dev.bagel.extra_apoth_compat.ModConfig;
import dev.bagel.extra_apoth_compat.compat.emi.EmiConstants;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.handler.EmiCraftContext;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import dev.shadowsoffire.apotheosis.affix.salvaging.SalvagingMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.world.inventory.Slot;

import java.util.ArrayList;
import java.util.List;

public class SalvagingRecipeHandler implements StandardRecipeHandler<SalvagingMenu> {

    @Override
    public List<Slot> getInputSources(SalvagingMenu handler) {
        List<Slot> list = Lists.newArrayList();
        list.add(handler.getSlot(0));
        int invStart = 12;
        for (int i = invStart; i < invStart + 36; i++) {
            list.add(handler.getSlot(i));
        }
        return list;
    }

    @Override
    public List<Slot> getCraftingSlots(SalvagingMenu handler) {
        List<Slot> list = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Slot slot = handler.slots.get(i);
            if (!slot.hasItem()) {
                list.add(slot);
            }
        }
        if (list.isEmpty()) {
            list.add(handler.slots.get(0));
        }
        return list;
    }

    @Override
    public boolean craft(EmiRecipe rec, EmiCraftContext<SalvagingMenu> context) {
        boolean action = StandardRecipeHandler.super.craft(rec, context);
        Minecraft client = Minecraft.getInstance();
        if (action) {
            SalvagingMenu sh = context.getScreenHandler();
            if (/*context.getType() == EmiCraftContext.Type.CRAFTABLE || */ModConfig.pressButtonOnFillSalvaging) {
                client.gameMode.handleInventoryButtonClick(sh.containerId, 0);
            }
        }
        return action;
    }

    @Override
    public boolean supportsRecipe(EmiRecipe recipe) {
        return recipe.getCategory() == EmiConstants.Apotheosis.SALVAGING && ((SalvagingEMIRecipe) recipe).getData().type().hidesFillButton();
    }
}
