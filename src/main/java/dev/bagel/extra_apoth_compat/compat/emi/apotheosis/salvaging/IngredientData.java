package dev.bagel.extra_apoth_compat.compat.emi.apotheosis.salvaging;

import dev.emi.emi.api.stack.EmiIngredient;
import dev.shadowsoffire.apotheosis.loot.LootRarity;
import dev.shadowsoffire.apotheosis.socket.gem.Purity;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @param ingredient Base ingredient
 * @param type       The type of ingredient given, GEM is sorted first, then AFFIX, followed by everything else
 */
public record IngredientData(EmiIngredient ingredient, @Nullable Purity purity,
                             @Nullable LootRarity rarity, IngredientType type) implements Comparable<IngredientData> {

    public static IngredientData nullData(Ingredient ingredient) {
        return new IngredientData(EmiIngredient.of(ingredient), null, null, IngredientType.DEFAULT);
    }

    @Override
    public int compareTo(@NotNull IngredientData that) {
        switch (type) {
            case GEM -> {
                switch (that.type) {
                    case GEM -> { //both gem
                        assert that.purity != null;
                        assert purity != null;
                        return purity.compareTo(that.purity);
                    }
                    case AFFIX, DEFAULT -> { //Gem recipes show first
                        return -1;
                    }
                }
            }
            case AFFIX -> {
                switch (that.type) {
                    case GEM -> { //this affix, that gem
                        return 1;
                    }
                    case AFFIX -> { //both affix
                        assert rarity != null;
                        assert that.rarity != null;
                        return Integer.compare(rarity.sortIndex(), that.rarity.sortIndex());
                    }
                    case DEFAULT -> { //this affix, that default
                        return -1;
                    }
                }
            }
            case DEFAULT -> {
                switch (that.type) {
                    case GEM, AFFIX -> { //Default to showing after gem and affix recipes
                        return 1;
                    }
                    case DEFAULT -> { //Don't sort
                        return 0;
                    }
                }
            }
        }
        return 0;
    }
}
