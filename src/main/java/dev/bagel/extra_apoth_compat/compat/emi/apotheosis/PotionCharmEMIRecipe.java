package dev.bagel.extra_apoth_compat.compat.emi.apotheosis;

import com.google.common.collect.Lists;
import dev.bagel.extra_apoth_compat.ExtraApothCompat;
import dev.emi.emi.EmiPort;
import dev.emi.emi.EmiUtil;
import dev.emi.emi.api.recipe.EmiCraftingRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.shadowsoffire.apotheosis.Apoth;
import dev.shadowsoffire.apotheosis.Apotheosis;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;

import java.util.List;

public class PotionCharmEMIRecipe extends EmiCraftingRecipe {

    public PotionCharmEMIRecipe(ShapedRecipe recipe, Holder<Potion> potion) {
        super(padIngredients(recipe, potion), EmiStack.of(PotionContents.createItemStack(Apoth.Items.POTION_CHARM.value(), potion)), getPotionId(potion), false);
        setRemainders(input, recipe);
    }

    public static void setRemainders(List<EmiIngredient> input, CraftingRecipe recipe) {
        try {
            TransientCraftingContainer inv = EmiUtil.getCraftingInventory();
            for (int i = 0; i < input.size(); i++) {
                if (input.get(i).isEmpty()) {
                    continue;
                }
                for (int j = 0; j < input.size(); j++) {
                    if (j == i) {
                        continue;
                    }
                    if (!input.get(j).isEmpty()) {
                        inv.setItem(j, input.get(j).getEmiStacks().get(0).getItemStack().copy());
                    }
                }
                List<EmiStack> stacks = input.get(i).getEmiStacks();
                for (EmiStack stack : stacks) {
                    inv.setItem(i, stack.getItemStack().copy());
                    CraftingInput cri = CraftingInput.of(inv.getWidth(), inv.getHeight(), inv.getItems());
                    if (cri.width() <= 3 && cri.height() <= 3) {
                        ItemStack remainder = recipe.getRemainingItems(cri).get((i / 3 * cri.width()) + (i % 3));
                        if (!remainder.isEmpty()) {
                            stack.setRemainder(EmiStack.of(remainder));
                        }
                    }
                }
                inv.clearContent();
            }
        } catch (Exception e) {
            ExtraApothCompat.LOGGER.error("Exception thrown setting remainders for {}", EmiPort.getId(recipe));
            e.printStackTrace();
        }
    }

    private static List<EmiIngredient> padIngredients(ShapedRecipe recipe, Holder<Potion> potion) {
        List<EmiIngredient> list = Lists.newArrayList();

        ItemStack pot = PotionContents.createItemStack(Items.POTION, potion);
        int i = 0;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                if (x >= recipe.getWidth() || y >= recipe.getHeight() || i >= recipe.getIngredients().size()) {
                    list.add(EmiStack.EMPTY);
                } else {
                    Ingredient ing = recipe.getIngredients().get(i);
                    if (ing.test(pot)) {
                        list.add(EmiStack.of(pot));
                    } else {
                        list.add(EmiIngredient.of(ing));
                    }
                    i++;
                }
            }
        }
        return list;
    }

    private static ResourceLocation getPotionId(Holder<Potion> potion) {
        return ResourceLocation.parse(Apotheosis.loc("/potion_charm") + "/" + BuiltInRegistries.POTION.getKey(potion.value()).getPath());
    }
}