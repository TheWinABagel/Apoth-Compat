package dev.bagel.extra_apoth_compat.compat.emi.apotheosis.gem_cutting;

import com.google.common.collect.Lists;
import dev.bagel.extra_apoth_compat.ExtraApothCompat;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import dev.emi.emi.api.stack.Comparison;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.registry.EmiRecipeFiller;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

class Filler {
    //mfw have to copy a whole method to change 1 call
    public static <T extends AbstractContainerMenu> @Nullable List<ItemStack> getStacks(StandardRecipeHandler<T> handler, EmiRecipe recipe, AbstractContainerScreen<T> screen, int amount) {
        try {
            T screenHandler = screen.getMenu();
            if (handler != null) {
                List<Slot> slots = handler.getInputSources(screenHandler);
                List<Slot> craftingSlots = handler.getCraftingSlots(recipe, screenHandler);
                List<EmiIngredient> ingredients = recipe.getInputs();
                List<DiscoveredItem> discovered = Lists.newArrayList();
                Object2IntMap<EmiStack> weightDivider = new Object2IntOpenHashMap<>();
                for (int i = 0; i < ingredients.size(); i++) {
                    List<DiscoveredItem> d = Lists.newArrayList();
                    EmiIngredient ingredient = ingredients.get(i);
                    List<EmiStack> emiStacks = ingredient.getEmiStacks();
                    if (ingredient.isEmpty()) {
                        discovered.add(null);
                        continue;
                    }
                    for (int e = 0; e < emiStacks.size(); e++) {
                        EmiStack stack = emiStacks.get(e);
                        slotLoop:
                        for (Slot s : slots) {
                            ItemStack ss = s.getItem();
                            if (EmiStack.of(s.getItem()).isEqual(stack)) {
                                for (DiscoveredItem di : d) {
                                    if (ItemStack.isSameItemSameComponents(ss, di.stack)) {
                                        di.amount += ss.getCount();
                                        continue slotLoop;
                                    }
                                } //Changed from ingredient.getAmount() to stack.getAmount(). Pain
                                d.add(new DiscoveredItem(stack, ss, ss.getCount(), (int) stack.getAmount(), ss.getMaxStackSize()));
                            }
                        }
                    }
                    DiscoveredItem biggest = null;
                    for (DiscoveredItem di : d) {
                        ExtraApothCompat.LOGGER.info("Disovered item: {}", di);
                        if (biggest == null) {
                            biggest = di;
                        } else {
                            int a = di.amount / (weightDivider.getOrDefault(di.ingredient, 0) + di.consumed);
                            int ba = biggest.amount / (weightDivider.getOrDefault(biggest.ingredient, 0) + biggest.consumed);
                            if (ba < a) {
                                biggest = di;
                            }
                        }
                    }
                    if (biggest == null || i >= craftingSlots.size()) {
                        return null;
                    }
                    Slot slot = craftingSlots.get(i);
                    if (slot == null) {
                        return null;
                    }
                    weightDivider.put(biggest.ingredient, weightDivider.getOrDefault(biggest.ingredient, 0) + biggest.consumed);
                    biggest.max = Math.min(biggest.max, slot.getMaxStackSize());
                    discovered.add(biggest);
                }
                if (discovered.isEmpty()) {
                    return null;
                }

                List<DiscoveredItem> unique = Lists.newArrayList();
                outer:
                for (DiscoveredItem di : discovered) {
                    if (di == null) {
                        continue;
                    }
                    for (DiscoveredItem ui : unique) {
                        if (ItemStack.isSameItemSameComponents(di.stack, ui.stack)) {
                            ui.consumed += di.consumed;
                            continue outer;
                        }
                    }
                    unique.add(new DiscoveredItem(di.ingredient, di.stack, di.amount, di.consumed, di.max));
                }
                int maxAmount = Integer.MAX_VALUE;
                for (DiscoveredItem ui : unique) {
                    if (!ui.catalyst()) {
                        maxAmount = Math.min(maxAmount, ui.amount / ui.consumed);
                        maxAmount = Math.min(maxAmount, ui.max);
                    }
                }
                maxAmount = Math.min(maxAmount, amount + EmiRecipeFiller.batchesAlreadyPresent(recipe, handler, screen));

                if (maxAmount == 0) {
                    return null;
                }

                List<ItemStack> desired = Lists.newArrayList();
                for (int i = 0; i < discovered.size(); i++) {
                    DiscoveredItem di = discovered.get(i);
                    if (di != null) {
                        ItemStack is = di.stack.copy();
                        int a = di.catalyst() ? di.consumed : di.consumed * maxAmount;
                        is.setCount(a);
                        desired.add(is);
                    } else {
                        desired.add(ItemStack.EMPTY);
                    }
                }
                return desired;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class DiscoveredItem {
        private static final Comparison COMPARISON = Comparison.DEFAULT_COMPARISON;
        public EmiStack ingredient;
        public ItemStack stack;
        public int consumed;
        public int amount;
        public int max;

        public DiscoveredItem(EmiStack ingredient, ItemStack stack, int amount, int consumed, int max) {
            this.ingredient = ingredient;
            this.stack = stack.copy();
            this.amount = amount;
            this.consumed = consumed;
            this.max = max;
        }

        public boolean catalyst() {
            return ingredient.getRemainder().isEqual(ingredient, COMPARISON);
        }
    }
}
