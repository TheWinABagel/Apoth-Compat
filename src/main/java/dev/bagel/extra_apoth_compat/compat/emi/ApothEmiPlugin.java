package dev.bagel.extra_apoth_compat.compat.emi;

import dev.bagel.extra_apoth_compat.ModConfig;
import dev.bagel.extra_apoth_compat.compat.emi.apotheosis.ApotheosisEmiPlugin;
import dev.bagel.extra_apoth_compat.compat.emi.apotheosis.CharmInfusionEMIRecipe;
import dev.bagel.extra_apoth_compat.compat.emi.apotheosis.PotionCharmEMIRecipe;
import dev.bagel.extra_apoth_compat.compat.emi.apothic_enchanting.ApothicEnchantingEmiPlugin;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.stack.Comparison;
import dev.emi.emi.api.stack.EmiStack;
import dev.shadowsoffire.apotheosis.Apoth;
import dev.shadowsoffire.apotheosis.Apotheosis;
import dev.shadowsoffire.apotheosis.item.PotionCharmItem;
import dev.shadowsoffire.apotheosis.recipe.PotionCharmRecipe;
import dev.shadowsoffire.apothic_enchanting.table.infusion.InfusionRecipe;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.Unbreakable;
import net.minecraft.world.item.crafting.ShapedRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@EmiEntrypoint
public class ApothEmiPlugin implements EmiPlugin {

    @Override
    public void register(EmiRegistry registry) {
        if (ModConfig.Loaded.APOTHIC_ENCHANTING) {
            ApothicEnchantingEmiPlugin.register(registry);
        }

        if (ModConfig.Loaded.APOTHEOSIS) {
            ApotheosisEmiPlugin.register(registry);
        }
    }
}
