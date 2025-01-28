package dev.bagel.extra_apoth_compat.compat.emi.apothic_spawners;

import dev.bagel.extra_apoth_compat.ExtraApothCompat;
import dev.bagel.extra_apoth_compat.compat.emi.EmiConstants;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiInfoRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.shadowsoffire.apothic_spawners.ASConfig;
import dev.shadowsoffire.apothic_spawners.ASObjects;
import dev.shadowsoffire.apothic_spawners.ApothicSpawners;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApothicSpawnersEmiPlugin {

    public static void register(EmiRegistry registry) {
        registry.addCategory(EmiConstants.ApothicSpawners.SPAWNER_MODIFIER);
        registry.addWorkstation(EmiConstants.ApothicSpawners.SPAWNER_MODIFIER, EmiStack.of(Blocks.SPAWNER));
        registry.getRecipeManager().getAllRecipesFor(ASObjects.SPAWNER_MODIFIER.get()).forEach(holder -> {
            registry.addRecipe(new SpawnerModifierEmiRecipe(holder));
        });
        List<SpawnEggItem> eggs = new ArrayList<>();
        SpawnEggItem.eggs().forEach(eggs::add);
        List<EmiIngredient> ing = List.of(EmiIngredient.of(eggs.stream().map(EmiStack::of).toList()));
        String localized = ApothicSpawners.lang("info", "capturing", ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(ASConfig.capturingDropChance * 100)).getString();

        if (ASConfig.spawnerSilkLevel == -1) {
            registry.addRecipe(new EmiInfoRecipe(List.of(EmiStack.of(Blocks.SPAWNER)), List.of(ApothicSpawners.lang("info", "spawner.no_silk")),
                    ExtraApothCompat.synthLoc("spawner_no_silk")));
        }
        else if (ASConfig.spawnerSilkLevel == 0) {
            registry.addRecipe(new EmiInfoRecipe(List.of(EmiStack.of(Blocks.SPAWNER)), List.of(ApothicSpawners.lang("info", "always_drop")),
                    ExtraApothCompat.synthLoc("spawner_always_drop")));
        }
        else {
            Minecraft.getInstance().level.holder(Enchantments.SILK_TOUCH).ifPresent(silk -> {
                registry.addRecipe(new EmiInfoRecipe(List.of(EmiStack.of(Blocks.SPAWNER)), List.of(ApothicSpawners.lang("info", "spawner", ((MutableComponent) Enchantment.getFullname(silk, ASConfig.spawnerSilkLevel)).withStyle(ChatFormatting.DARK_BLUE))),
                        ExtraApothCompat.synthLoc("spawner_always_drop")));
            });
        }

        registry.addRecipe(new EmiInfoRecipe(ing, Arrays.stream(localized.split("\\\\n"))
                .map(string -> (Component) Component.literal(string)).toList(), ExtraApothCompat.synthLoc("spawn_egg")));
    }
}
