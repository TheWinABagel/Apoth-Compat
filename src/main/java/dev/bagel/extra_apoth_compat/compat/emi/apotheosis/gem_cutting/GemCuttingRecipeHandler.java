package dev.bagel.extra_apoth_compat.compat.emi.apotheosis.gem_cutting;

import com.google.common.collect.Lists;
import dev.bagel.extra_apoth_compat.ExtraApothCompat;
import dev.bagel.extra_apoth_compat.compat.emi.Constants;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.handler.EmiCraftContext;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.platform.EmiClient;
import dev.emi.emi.registry.EmiRecipeFiller;
import dev.shadowsoffire.apotheosis.socket.gem.cutting.GemCuttingMenu;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GemCuttingRecipeHandler implements StandardRecipeHandler<GemCuttingMenu> {

    @Override
    public List<Slot> getInputSources(GemCuttingMenu handler) {
        List<Slot> list = Lists.newArrayList();
        list.add(handler.getSlot(0));
        int invStart = 4;
        for (int i = invStart; i < invStart + 36; i++) {
            list.add(handler.getSlot(i));
        }
        return list;
    }

    @Override
    public List<Slot> getCraftingSlots(GemCuttingMenu handler) {
        List<Slot> slots = new ArrayList<>();
        slots.add(handler.slots.get(0));
        slots.add(handler.slots.get(1));
        slots.add(handler.slots.get(2));
        slots.add(handler.slots.get(3));
        return slots;
    }

    private boolean craftDefault(EmiRecipe recipe, EmiCraftContext<GemCuttingMenu> context) {
        List<ItemStack> stacks = Filler.getStacks(this, recipe, context.getScreen(), context.getAmount());
        if (stacks != null) {
            if (stacks != null) {
                Minecraft.getInstance().setScreen(context.getScreen());
                if (!EmiClient.onServer) {
                    return EmiRecipeFiller.clientFill(this, recipe, context.getScreen(), stacks, context.getDestination());
                } else {
                    EmiClient.sendFillRecipe(this, context.getScreen(), context.getScreenHandler().containerId, switch (context.getDestination()) {
                        case NONE -> 0;
                        case CURSOR -> 1;
                        case INVENTORY -> 2;
                    }, stacks, recipe);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean craft(EmiRecipe rec, EmiCraftContext<GemCuttingMenu> context) {
        boolean action = craftDefault(rec, context);
        Minecraft client = Minecraft.getInstance();
        if (action) {
            GemCuttingMenu sh = context.getScreenHandler();
            client.gameMode.handleInventoryButtonClick(sh.containerId, 0);
            ExtraApothCompat.LOGGER.info("Destination:{}", context.getDestination());
            if (context.getDestination() == EmiCraftContext.Destination.CURSOR) {
                client.gameMode.handleInventoryMouseClick(sh.containerId, GemCuttingMenu.BASE_SLOT, 0, ClickType.PICKUP, client.player);
            } else if (context.getDestination() == EmiCraftContext.Destination.INVENTORY) {
                client.gameMode.handleInventoryMouseClick(sh.containerId, GemCuttingMenu.BASE_SLOT, 0, ClickType.QUICK_MOVE, client.player);
            }
        }
        return action;
    }

    @Override
    public boolean canCraft(EmiRecipe recipe, EmiCraftContext<GemCuttingMenu> context) {
        Object2LongMap<EmiStack> used = new Object2LongOpenHashMap<>();
        outer:
        for (EmiIngredient ingredient : recipe.getInputs()) {
            if (ingredient.isEmpty()) {
                continue;
            }
            for (EmiStack stack : ingredient.getEmiStacks()) {
                long desired = stack.getAmount() * context.getAmount();
                if (context.getInventory().inventory.containsKey(stack)) {
                    EmiStack identity = context.getInventory().inventory.get(stack);
                    long alreadyUsed = used.getOrDefault(identity, 0);
                    long available = identity.getAmount() - alreadyUsed;
                    if (available >= desired) {
                        used.put(identity, desired + alreadyUsed);
                        continue outer;
                    }
                }
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean supportsRecipe(EmiRecipe recipe) {
        return recipe.getCategory() == Constants.Apotheosis.GEM_CUTTING;
    }
}
