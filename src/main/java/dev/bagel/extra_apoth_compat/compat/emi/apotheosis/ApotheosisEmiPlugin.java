package dev.bagel.extra_apoth_compat.compat.emi.apotheosis;

import dev.bagel.extra_apoth_compat.ExtraApothCompat;
import dev.bagel.extra_apoth_compat.compat.emi.Constants;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.stack.EmiStack;
import dev.shadowsoffire.apotheosis.Apoth;
import dev.shadowsoffire.apotheosis.Apotheosis;
import dev.shadowsoffire.apotheosis.item.PotionCharmItem;
import dev.shadowsoffire.apotheosis.socket.gem.GemRegistry;
import dev.shadowsoffire.apotheosis.socket.gem.cutting.BasicGemCuttingRecipe;
import dev.shadowsoffire.apotheosis.socket.gem.cutting.PurityUpgradeRecipe;
import dev.shadowsoffire.apothic_enchanting.table.infusion.InfusionRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.ShapedRecipe;

import java.util.ArrayList;
import java.util.List;

public class ApotheosisEmiPlugin {

    public static void register(EmiRegistry registry) {
        registry.addCategory(Constants.Apotheosis.GEM_CUTTING);
        registry.addWorkstation(Constants.Apotheosis.GEM_CUTTING, EmiStack.of(Apoth.Blocks.GEM_CUTTING_TABLE.value()));
        registry.setDefaultComparison(EmiStack.of(Apoth.Items.GEM.value()), Constants.Apotheosis.GEM_COMPARISON);

        registry.getRecipeManager().getAllRecipesFor(Apoth.RecipeTypes.GEM_CUTTING).forEach(holder -> {
            if (holder.value() instanceof BasicGemCuttingRecipe bgcr) {
                registry.addRecipe(new BasicGemCuttingEMIRecipe(bgcr, holder.id()));
            } else if (holder.value() instanceof PurityUpgradeRecipe pur) {
                List<GemCuttingEMIRecipe.StackInfo> gems = new ArrayList<>();
                GemRegistry.INSTANCE.getValues().stream().forEachOrdered(g -> {
                    if (pur.purity().isAtLeast(g.getMinPurity())) {
                        gems.add(GemCuttingEMIRecipe.StackInfo.of(g, pur));
                    }
                });
                for (GemCuttingEMIRecipe.StackInfo info : gems) {
                    registry.addRecipe(new PurityUpgradeEMIRecipe(holder.id(), pur, info));
                }

            } else {
                ExtraApothCompat.LOGGER.error("Unknown gem cutting recipe type {} with id of {}! Report to Extra Apoth Compat's GitHub!", holder.value(), holder.id());
            }
        });

        registry.setDefaultComparison(EmiStack.of(Apoth.Items.POTION_CHARM.value()), Constants.Apotheosis.CHARM_COMPARISON);
        ResourceLocation charmId = Apotheosis.loc("potion_charm");
        registry.removeRecipes(charmId);
        registry.getRecipeManager().byKey(charmId).ifPresent(charmRecipe ->
                BuiltInRegistries.POTION.holders()
                        .filter(PotionCharmItem::isValidPotion)
                        .forEach(p -> {
                            registry.addRecipe(new PotionCharmEMIRecipe((ShapedRecipe) charmRecipe.value(), p));
                        })
        );

        ResourceLocation charmInfusionId = Apotheosis.loc("infusion/potion_charm");
        registry.removeRecipes(charmInfusionId);
        registry.getRecipeManager().byKey(charmInfusionId).ifPresent(charmRecipe ->
                BuiltInRegistries.POTION.holders()
                        .filter(PotionCharmItem::isValidPotion)
                        .forEach(p -> {
                            ItemStack charm = PotionContents.createItemStack(Apoth.Items.POTION_CHARM.value(), p);
                            ResourceLocation id = ResourceLocation.parse(Apotheosis.loc("/infusion/potion_charm") + "/" + BuiltInRegistries.POTION.getKey(p.value()).getPath());
                            registry.addRecipe(new CharmInfusionEMIRecipe((InfusionRecipe) charmRecipe.value(), id, charm));
                        })
        );
    }
}
