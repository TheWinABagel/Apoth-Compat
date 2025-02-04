package dev.bagel.extra_apoth_compat.compat.emi.apotheosis;

import dev.bagel.extra_apoth_compat.ExtraApothCompat;
import dev.bagel.extra_apoth_compat.compat.emi.EmiConstants;
import dev.bagel.extra_apoth_compat.compat.emi.apotheosis.gem_cutting.BasicGemCuttingEMIRecipe;
import dev.bagel.extra_apoth_compat.compat.emi.apotheosis.gem_cutting.GemCuttingEMIRecipe;
import dev.bagel.extra_apoth_compat.compat.emi.apotheosis.gem_cutting.GemCuttingRecipeHandler;
import dev.bagel.extra_apoth_compat.compat.emi.apotheosis.gem_cutting.PurityUpgradeEMIRecipe;
import dev.bagel.extra_apoth_compat.compat.emi.apotheosis.salvaging.SalvagingEMIRecipe;
import dev.bagel.extra_apoth_compat.compat.emi.apotheosis.salvaging.SalvagingRecipeHandler;
import dev.bagel.extra_apoth_compat.compat.emi.apotheosis.smithing.SocketingEMIRecipe;
import dev.bagel.extra_apoth_compat.compat.emi.apotheosis.smithing.SocketingSigilEMIRecipe;
import dev.bagel.extra_apoth_compat.compat.emi.apotheosis.smithing.UnnamingEMIRecipe;
import dev.bagel.extra_apoth_compat.compat.emi.apotheosis.smithing.WithdrawalEMIRecipe;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.shadowsoffire.apotheosis.Apoth;
import dev.shadowsoffire.apotheosis.Apotheosis;
import dev.shadowsoffire.apotheosis.affix.UnnamingRecipe;
import dev.shadowsoffire.apotheosis.item.PotionCharmItem;
import dev.shadowsoffire.apotheosis.socket.AddSocketsRecipe;
import dev.shadowsoffire.apotheosis.socket.SocketingRecipe;
import dev.shadowsoffire.apotheosis.socket.WithdrawalRecipe;
import dev.shadowsoffire.apotheosis.socket.gem.GemRegistry;
import dev.shadowsoffire.apotheosis.socket.gem.Purity;
import dev.shadowsoffire.apotheosis.socket.gem.cutting.BasicGemCuttingRecipe;
import dev.shadowsoffire.apotheosis.socket.gem.cutting.PurityUpgradeRecipe;
import dev.shadowsoffire.apotheosis.util.ApothSmithingRecipe;
import dev.shadowsoffire.apothic_enchanting.table.infusion.InfusionRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.SmithingRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApotheosisEmiPlugin {

    public static void register(EmiRegistry registry) {
        gemCutting(registry);
        charm(registry);
        smithing(registry);
    }

    private static void smithing(EmiRegistry registry) {
        registry.addCategory(EmiConstants.Apotheosis.SALVAGING);
        registry.addWorkstation(EmiConstants.Apotheosis.SALVAGING, EmiStack.of(Apoth.Blocks.SALVAGING_TABLE.value()));
        registry.addRecipeHandler(Apoth.Menus.SALVAGE, new SalvagingRecipeHandler());
        registry.getRecipeManager().getAllRecipesFor(Apoth.RecipeTypes.SALVAGING).forEach(holder -> {
            registry.addRecipe(new SalvagingEMIRecipe(holder));
        });

        List<RecipeHolder<SmithingRecipe>> recipes = registry.getRecipeManager().getAllRecipesFor(RecipeType.SMITHING).stream()
                .filter(holder -> holder.value() instanceof ApothSmithingRecipe)
                .toList();
        for (RecipeHolder<SmithingRecipe> holder : recipes) {
            if (holder.value() instanceof AddSocketsRecipe asr) {
                registry.removeRecipes(holder.id());
                registry.addRecipe(new SocketingSigilEMIRecipe(EmiIngredient.of(asr.getInput()), ExtraApothCompat.synthesise(holder.id())));
            } else if (holder.value() instanceof SocketingRecipe || holder.value() instanceof WithdrawalRecipe) {
                registry.removeRecipes(holder.id());
                List<EmiIngredient> gems = new ArrayList<>();
                GemRegistry.INSTANCE.getValues().forEach(gem -> {
                    Arrays.stream(Purity.values()).forEach(purity -> {
                        if (purity.isAtLeast(gem.getMinPurity())) {
                            gems.add(EmiStack.of(GemRegistry.createGemStack(gem, purity)));
                        }
                    });
                });
                if (holder.value() instanceof SocketingRecipe) {
                    registry.addRecipe(new SocketingEMIRecipe(EmiStack.of(Apoth.Items.GEM.value()), ExtraApothCompat.synthesise(holder.id()), gems));
                } else {
                    registry.addRecipe(new WithdrawalEMIRecipe(EmiStack.of(Apoth.Items.SIGIL_OF_WITHDRAWAL.value()), ExtraApothCompat.synthesise(holder.id()), gems));
                }
            } else if (holder.value() instanceof UnnamingRecipe) {
                registry.removeRecipes(holder.id());
                registry.addRecipe(new UnnamingEMIRecipe(EmiStack.of(Apoth.Items.SIGIL_OF_UNNAMING.value()), ExtraApothCompat.synthesise(holder.id())));
            } else {
                ExtraApothCompat.LOGGER.error("Unknown Apotheosis Smithing recipe type {} with id of {}! Report to Extra Apoth Compat's GitHub so support can be added!", holder.value(), holder.id());
            }
        }
    }

    private static void gemCutting(EmiRegistry registry) {
        registry.addCategory(EmiConstants.Apotheosis.GEM_CUTTING);
        registry.addWorkstation(EmiConstants.Apotheosis.GEM_CUTTING, EmiStack.of(Apoth.Blocks.GEM_CUTTING_TABLE.value()));
        registry.setDefaultComparison(EmiStack.of(Apoth.Items.GEM.value()), EmiConstants.Apotheosis.GEM_COMPARISON);
        registry.addRecipeHandler(Apoth.Menus.GEM_CUTTING, new GemCuttingRecipeHandler());

        registry.getRecipeManager().getAllRecipesFor(Apoth.RecipeTypes.GEM_CUTTING).forEach(holder -> {
            if (holder.value() instanceof BasicGemCuttingRecipe bgcr) {
                registry.addRecipe(new BasicGemCuttingEMIRecipe(bgcr, holder.id()));
            } else if (holder.value() instanceof PurityUpgradeRecipe pur) {
                List<GemCuttingEMIRecipe.StackInfo> gems = new ArrayList<>();
                GemRegistry.INSTANCE.getValues().stream().forEachOrdered(g -> {
                    if (pur.purity().isAtLeast(g.getMinPurity())) { //todo was reversed in apoth, report
                        gems.add(GemCuttingEMIRecipe.StackInfo.of(g, pur));
                    }
                });
                for (GemCuttingEMIRecipe.StackInfo info : gems) {
                    registry.addRecipe(new PurityUpgradeEMIRecipe(holder.id(), pur, info));
                }

            } else {
                ExtraApothCompat.LOGGER.error("Unknown gem cutting recipe type {} with id of {}! Report to Extra Apoth Compat's GitHub so support can be added!", holder.value(), holder.id());
            }
        });
    }

    private static void charm(EmiRegistry registry) {
        registry.setDefaultComparison(EmiStack.of(Apoth.Items.POTION_CHARM.value()), EmiConstants.Apotheosis.CHARM_COMPARISON);
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
