package dev.bagel.extra_apoth_compat.compat.emi.apothic_enchanting;

import dev.bagel.extra_apoth_compat.ExtraApothCompat;
import dev.bagel.extra_apoth_compat.compat.emi.EmiConstants;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiInfoRecipe;
import dev.emi.emi.api.stack.EmiStack;
import dev.shadowsoffire.apothic_enchanting.Ench;
import dev.shadowsoffire.apothic_enchanting.util.TooltipUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class ApothicEnchantingEmiPlugin {

    public static void register(EmiRegistry registry) {
        registry.addCategory(EmiConstants.ApothicEnchanting.ENCHANTING);
        registry.addWorkstation(EmiConstants.ApothicEnchanting.ENCHANTING, EmiStack.of(Blocks.ENCHANTING_TABLE));
        registry.addRecipeHandler(Ench.Menus.ENCHANTING_TABLE, new EnchantingRecipeHandler());

        registry.getRecipeManager().getAllRecipesFor(Ench.RecipeTypes.INFUSION).forEach(holder -> {
            registry.addRecipe(new EnchantingEmiRecipe(holder));
        });

        Registry<Enchantment> enchants = Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.ENCHANTMENT);
        Holder<Enchantment> randomEnch = enchants.getRandomElementOf(EnchantmentTags.IN_ENCHANTING_TABLE, Minecraft.getInstance().level.random).orElse(enchants.getAny().get());
        Holder<Enchantment> randomCurse = enchants.getRandomElementOf(EnchantmentTags.CURSE, Minecraft.getInstance().level.random).orElse(enchants.getAny().get());

        ItemStack enchDiaSword = new ItemStack(Items.DIAMOND_SWORD);
        enchDiaSword.enchant(randomEnch, 1);

        ItemStack cursedDiaSword = new ItemStack(Items.DIAMOND_SWORD);
        cursedDiaSword.enchant(randomCurse, 1);

        ItemStack enchBook = new ItemStack(Items.ENCHANTED_BOOK);
        enchBook.enchant(randomEnch, 1);//todo was missing in apoth, report

        registry.addRecipe(new DummyEMIAnvilRecipe(
                cursedDiaSword,
                new ItemStack(Ench.Items.PRISMATIC_WEB),
                new ItemStack(Items.DIAMOND_SWORD),
                ExtraApothCompat.synthLoc("prismatic_cobweb")));

        registry.addRecipe(new DummyEMIAnvilRecipe(
                enchDiaSword,
                new ItemStack(Ench.Items.SCRAP_TOME),
                enchBook,
                ExtraApothCompat.synthLoc("scrap_tome")));

        registry.addRecipe(new DummyEMIAnvilRecipe(
                new ItemStack(Blocks.DAMAGED_ANVIL),
                new ItemStack(Blocks.IRON_BLOCK),
                new ItemStack(Blocks.ANVIL),
                ExtraApothCompat.synthLoc("anvil_repair")));

        registry.addRecipe(new EmiInfoRecipe(List.of(EmiStack.of(new ItemStack(Items.ENCHANTING_TABLE))), List.of(TooltipUtil.lang("info", "enchanting")), ExtraApothCompat.synthLoc("enchanting_info")));
        registry.addRecipe(new EmiInfoRecipe(List.of(EmiStack.of(new ItemStack(Ench.Items.LIBRARY))), List.of(TooltipUtil.lang("info", "library")), ExtraApothCompat.synthLoc("library_info")));
    }
}
