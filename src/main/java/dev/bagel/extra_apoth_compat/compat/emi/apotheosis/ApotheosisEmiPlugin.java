package dev.bagel.extra_apoth_compat.compat.emi.apotheosis;

import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.stack.Comparison;
import dev.emi.emi.api.stack.EmiStack;
import dev.shadowsoffire.apotheosis.Apoth;
import dev.shadowsoffire.apotheosis.Apotheosis;
import dev.shadowsoffire.apotheosis.item.PotionCharmItem;
import dev.shadowsoffire.apothic_enchanting.table.infusion.InfusionRecipe;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.ShapedRecipe;

public class ApotheosisEmiPlugin {
    public static void register(EmiRegistry registry) {
        Comparison charmComparison = Comparison.of((a, b) -> {
            PotionContents first = a.getItemStack().getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
            PotionContents second = b.getItemStack().getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
            if (!first.equals(second) || first.equals(PotionContents.EMPTY)) return false;
            boolean aHas = a.getItemStack().has(DataComponents.UNBREAKABLE);
            boolean bHas = b.getItemStack().has(DataComponents.UNBREAKABLE);
            if (aHas && !bHas || bHas && !aHas) return false;
            return true;
        });
        registry.setDefaultComparison(EmiStack.of(Apoth.Items.POTION_CHARM.value()), charmComparison);
        ResourceLocation charmId = Apotheosis.loc("potion_charm");
        registry.removeRecipes(charmId);
        registry.getRecipeManager().byKey(charmId).ifPresent(charmRecipe ->
                BuiltInRegistries.POTION.holders()
                        .filter(PotionCharmItem::isValidPotion)
                        .forEach(p -> {
                            registry.addRecipe(new PotionCharmEMIRecipe((ShapedRecipe) charmRecipe.value(), p));
                        }));

        ResourceLocation charmInfusionId = Apotheosis.loc("infusion/potion_charm");
        registry.removeRecipes(charmInfusionId);
        registry.getRecipeManager().byKey(charmInfusionId).ifPresent(charmRecipe ->
                BuiltInRegistries.POTION.holders()
                        .filter(PotionCharmItem::isValidPotion)
                        .forEach(p -> {
                            ItemStack charm = PotionContents.createItemStack(Apoth.Items.POTION_CHARM.value(), p);
                            ResourceLocation id = ResourceLocation.parse(Apotheosis.loc("/infusion/potion_charm") + "/" + BuiltInRegistries.POTION.getKey(p.value()).getPath());
                            registry.addRecipe(new CharmInfusionEMIRecipe((InfusionRecipe) charmRecipe.value(), id, charm));
                        }));
    }
}
