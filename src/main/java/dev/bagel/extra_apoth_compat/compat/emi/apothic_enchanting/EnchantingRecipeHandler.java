package dev.bagel.extra_apoth_compat.compat.emi.apothic_enchanting;

import com.google.common.collect.Lists;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import dev.shadowsoffire.apothic_enchanting.table.ApothEnchantmentMenu;
import net.minecraft.world.inventory.Slot;

import java.util.List;

public class EnchantingRecipeHandler implements StandardRecipeHandler<ApothEnchantmentMenu> {

    @Override
    public List<Slot> getInputSources(ApothEnchantmentMenu handler) {
        List<Slot> list = Lists.newArrayList();
        list.add(handler.getSlot(0));
        int invStart = 2;
        for (int i = invStart; i < invStart + 36; i++) {
            list.add(handler.getSlot(i));
        }
        return list;
    }

    @Override
    public List<Slot> getCraftingSlots(ApothEnchantmentMenu handler) {
        return List.of(handler.slots.get(0));
    }

    @Override
    public boolean supportsRecipe(EmiRecipe recipe) {
        return recipe.getCategory() == ApothicEnchantingEmiPlugin.ENCHANTING;
    }
}
