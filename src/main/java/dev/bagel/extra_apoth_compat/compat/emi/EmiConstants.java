package dev.bagel.extra_apoth_compat.compat.emi;

import dev.bagel.extra_apoth_compat.ExtraApothCompat;
import dev.bagel.extra_apoth_compat.compat.emi.apotheosis.gem_cutting.PurityUpgradeEMIRecipe;
import dev.bagel.extra_apoth_compat.compat.emi.apotheosis.salvaging.SalvagingEMIRecipe;
import dev.bagel.extra_apoth_compat.compat.emi.apothic_enchanting.EnchantingEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.Comparison;
import dev.emi.emi.api.stack.EmiStack;
import dev.shadowsoffire.apotheosis.Apoth;
import dev.shadowsoffire.apotheosis.socket.gem.Gem;
import dev.shadowsoffire.apotheosis.socket.gem.GemRegistry;
import dev.shadowsoffire.apotheosis.socket.gem.Purity;
import dev.shadowsoffire.placebo.reload.DynamicHolder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.block.Blocks;

import java.util.Comparator;

public class EmiConstants {
    public static class ApothicSpawners {

        public static EmiRecipeCategory SPAWNER_MODIFIER = new EmiRecipeCategory(ExtraApothCompat.loc("spawner_modifier"),
                EmiStack.of(Blocks.SPAWNER), EmiStack.of(Blocks.SPAWNER), (r1, r2) -> -r1.getId().compareNamespaced(r2.getId()));
    }

    public static class ApothicEnchanting {

        public static EmiRecipeCategory ENCHANTING = new EmiRecipeCategory(ExtraApothCompat.loc("enchanting"),
                EmiStack.of(Blocks.ENCHANTING_TABLE), EmiStack.of(Blocks.ENCHANTING_TABLE),
                (r1, r2) -> Float.compare(((EnchantingEmiRecipe) r1).getEterna(), ((EnchantingEmiRecipe) r2).getEterna()));
    }

    public static class Apotheosis {

        public static final Comparison CHARM_COMPARISON = Comparison.of((a, b) -> {
            PotionContents first = a.getItemStack().getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
            PotionContents second = b.getItemStack().getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
            if (!first.equals(second) || first.equals(PotionContents.EMPTY)) return false;
            boolean aHas = a.getItemStack().has(DataComponents.UNBREAKABLE);
            boolean bHas = b.getItemStack().has(DataComponents.UNBREAKABLE);
            if (aHas && !bHas || bHas && !aHas) return false;
            return true;
        });

        public static final Comparison GEM_COMPARISON = Comparison.of((a, b) -> {
            DynamicHolder<Gem> aGem = a.getItemStack().getOrDefault(Apoth.Components.GEM, GemRegistry.INSTANCE.emptyHolder());
            DynamicHolder<Gem> bGem = b.getItemStack().getOrDefault(Apoth.Components.GEM, GemRegistry.INSTANCE.emptyHolder());
            if (!aGem.isBound() || !bGem.isBound()) return false;
            if (aGem.is(bGem.getId())) {
                Purity aRarity = a.getItemStack().get(Apoth.Components.PURITY);
                Purity bRarity = b.getItemStack().get(Apoth.Components.PURITY);

                return aRarity != null && aRarity.equals(bRarity);
            }
            return false;
        });

        public static Comparator<EmiRecipe> GEM_CUTTING_COMPARISON = Comparator.comparing(o -> ((PurityUpgradeEMIRecipe) o).getPurity());

        public static EmiRecipeCategory GEM_CUTTING = new EmiRecipeCategory(ExtraApothCompat.loc("gem_cutting"), EmiStack.of(Apoth.Blocks.GEM_CUTTING_TABLE.value()), EmiStack.of(Apoth.Blocks.GEM_CUTTING_TABLE.value()), GEM_CUTTING_COMPARISON);

        public static EmiRecipeCategory SALVAGING = new EmiRecipeCategory(ExtraApothCompat.loc("salvaging"), EmiStack.of(Apoth.Blocks.SALVAGING_TABLE.value()), EmiStack.of(Apoth.Blocks.SALVAGING_TABLE.value()), (a, b) -> {
            if (a instanceof SalvagingEMIRecipe r1 && b instanceof SalvagingEMIRecipe r2) {
                return r1.getData().compareTo(r2.getData());
            }
            return 0;
        });
    }
}
