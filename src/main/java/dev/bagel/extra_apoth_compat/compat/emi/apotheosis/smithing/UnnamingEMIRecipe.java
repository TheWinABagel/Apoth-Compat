package dev.bagel.extra_apoth_compat.compat.emi.apotheosis.smithing;

import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.shadowsoffire.apotheosis.affix.AffixHelper;
import dev.shadowsoffire.apotheosis.loot.LootController;
import dev.shadowsoffire.apotheosis.loot.LootRarity;
import dev.shadowsoffire.apotheosis.loot.RarityRegistry;
import dev.shadowsoffire.apotheosis.tiers.GenContext;
import dev.shadowsoffire.placebo.reload.DynamicHolder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Random;

public class UnnamingEMIRecipe extends ApothSmithingEMIRecipe {

    public UnnamingEMIRecipe(EmiIngredient addition, ResourceLocation id) {
        super(addition, id, () -> Component.translatable("emi.extra_apoth_compat.unnaming.info").withStyle(ChatFormatting.DARK_RED));
    }

    @Override
    protected EmiStack getStack(Random r, int slot) {
        ItemStack named = createLootItem(TOOLS.get(r.nextInt(0, TOOLS.size())).getItemStack());
        ItemStack out = named.copy();
        DynamicHolder<LootRarity> rarity = AffixHelper.getRarity(out);
        if (!rarity.isBound()) return EmiStack.EMPTY; //uh oh
        // args[1] will be set to the item's underlying name. args[0] will be ignored.
        Component comp = Component.translatable("%2$s", "", "").withStyle(Style.EMPTY.withColor(rarity.get().color()));
        AffixHelper.setName(out, comp);
        return switch (slot) {
            case 0 -> EmiStack.of(named);
            case 1 -> addition.getEmiStacks().get(0);
            case 2 -> EmiStack.of(out);
            default -> throw new RuntimeException("whar");
        };
    }

    public ItemStack createLootItem(ItemStack stack) {
        List<LootRarity> rarites = RarityRegistry.getSortedRarities();
        LootRarity rarity = rarites.get(rand.nextInt(0, rarites.size()));
        return LootController.createLootItem(stack, rarity, GenContext.forPlayer(Minecraft.getInstance().player));
    }

/*    public static ItemStack createLootItem(ItemStack stack) {
        GenContext ctx = GenContext.forPlayer(Minecraft.getInstance().player);
        Random rand = EmiUtil.RANDOM;
        List<LootRarity> rarites = RarityRegistry.getSortedRarities();
        LootRarity rarity = rarites.get(rand.nextInt(0, rarites.size()));
        stack.set(Apoth.Components.AFFIXES, ItemAffixes.EMPTY);
        AffixHelper.setRarity(stack, rarity);
        LootCategory cat = LootCategory.forItem(stack);

        for (LootRule rule : rarity.getRules(cat)) {
            rule.execute(stack, rarity, ctx);
        }

        ItemAffixes loaded = stack.getOrDefault(Apoth.Components.AFFIXES, ItemAffixes.EMPTY);
        if (loaded.size() == 0) {
            return stack;
        }

        List<Affix> nameList = new ArrayList<>(loaded.size());
        for (DynamicHolder<Affix> a : loaded.keySet()) {
            nameList.add(a.get());
        }

        rand.setSeed(ctx.rand().nextLong());
        Collections.shuffle(nameList, rand);
        String key = nameList.size() > 1 ? "misc.apotheosis.affix_name.three" : "misc.apotheosis.affix_name.two";
        MutableComponent name = Component.translatable(key, nameList.get(0).getName(true), "", nameList.size() > 1 ? nameList.get(1).getName(false) : "").withStyle(Style.EMPTY.withColor(rarity.color()));
        AffixHelper.setName(stack, name);

        return stack;
    }*/
}
