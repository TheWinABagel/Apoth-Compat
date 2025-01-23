package dev.bagel.extra_apoth_compat.compat.emi.apothic_spawners;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.bagel.extra_apoth_compat.compat.emi.extension.HoverTextWidget;
import dev.bagel.extra_apoth_compat.compat.emi.extension.HoverTextureWidget;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTooltipComponents;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import dev.shadowsoffire.apothic_spawners.ApothicSpawners;
import dev.shadowsoffire.apothic_spawners.modifiers.SpawnerModifier;
import dev.shadowsoffire.apothic_spawners.modifiers.StatModifier;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;

public class SpawnerModifierEmiRecipe implements EmiRecipe {
    public static final ResourceLocation TEXTURES = ApothicSpawners.loc("textures/gui/spawner_jei.png");

    private final ResourceLocation id;
    private final SpawnerModifier recipe;

    public SpawnerModifierEmiRecipe(RecipeHolder<SpawnerModifier> holder) {
        this.id = holder.id();
        this.recipe = holder.value();
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return ApothicSpawnersEmiPlugin.SPAWNER_MODIFIER;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(EmiIngredient.of(recipe.getMainhandInput()), EmiIngredient.of(recipe.getOffhandInput()));
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(EmiStack.of(Blocks.SPAWNER));
    }

    @Override
    public boolean supportsRecipeTree() {
        return false;
    }

    @Override
    public List<EmiIngredient> getCatalysts() {
        return List.of(EmiStack.of(Blocks.SPAWNER));
    }

    @Override
    public int getDisplayWidth() {
        return 169;
    }

    @Override
    public int getDisplayHeight() {
        return 75;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        if (recipe.getOffhandInput() == Ingredient.EMPTY) {
            widgets.addTexture(TEXTURES, 1, 31, 28, 34, 0, 88);
        }

        widgets.addTooltip((mouseX, mouseY) -> List.of(EmiTooltipComponents.of(ApothicSpawners.lang("misc", "mainhand"))), 0, 13, 10, 12);
        widgets.addSlot(getInputs().getFirst(), 11, 11);
        if (recipe.getOffhandInput() != Ingredient.EMPTY) {
            widgets.addSlot(getInputs().get(1), 11, 48);
            //split arrow
            widgets.addTexture(TEXTURES, 10, 28, 18, 19, 10, 28);
            //bottom question mark
            HoverTextureWidget.create(widgets, TEXTURES, 0, 50, 10, 12, -1, 50)
                    .hoverTexture(0, 75)
                    .tooltip(List.of(EmiTooltipComponents.of(ApothicSpawners.lang("misc", "mainhand"))));
        } else {
            //Add curved arrow when no offhand input
            widgets.addTexture(TEXTURES, 1, 31, 28, 34, 0, 88);

        }
        //Top question mark
        HoverTextureWidget.create(widgets, TEXTURES, 0, 13, 10, 12, -1, 13)
                .hoverTexture(0, 75)
                .tooltip(List.of(EmiTooltipComponents.of(ApothicSpawners.lang("misc", "mainhand"))));

        //Fake Spawner stack
        widgets.addDrawable(31, 29, 16, 16, (draw, mouseX, mouseY, delta) -> {
            PoseStack mvStack = draw.pose();
            mvStack.pushPose();
            mvStack.translate(0, 0.5, 0);
            draw.renderFakeItem(new ItemStack(Items.SPAWNER), 0, 0);
            mvStack.popPose();
        }).tooltip(List.of(EmiTooltipComponents.of(ApothicSpawners.lang("misc", "rclick_spawner"))));
        Font font = Minecraft.getInstance().font;
        int top = 75 / 2 - recipe.getStatModifiers().size() * (Minecraft.getInstance().font.lineHeight + 2) / 2 + 2;
        int left = 168;
        //Description Text
        for (StatModifier<?> s : recipe.getStatModifiers()) {
            String value = s.getFormattedValue();
            if ("true".equals(value)) value = "+";
            else if ("false".equals(value)) value = "-";
            else if (s.value() instanceof Number num && num.intValue() > 0) value = "+" + value;
            Component msg = ApothicSpawners.lang("misc", "concat", value, s.stat().name());
            List<Component> list = new ArrayList<>();
            list.add(s.stat().name().withStyle(ChatFormatting.GREEN, ChatFormatting.UNDERLINE));
            list.add(s.stat().desc().withStyle(ChatFormatting.GRAY));

            if (s.value() instanceof Number) {
                StatModifier<Number> n = (StatModifier<Number>) s;
                if (s.min().isPresent() || s.max().isPresent()) list.add(Component.literal(" "));
                if (s.min().isPresent())
                    list.add(ApothicSpawners.lang("misc", "min_value", n.stat().formatValue(n.min().get())).withStyle(ChatFormatting.GRAY));
                if (s.max().isPresent())
                    list.add(ApothicSpawners.lang("misc", "max_value", n.stat().formatValue(n.max().get())).withStyle(ChatFormatting.GRAY));
            }

            HoverTextWidget.create(widgets, msg, left - font.width(msg), top, 0x8080FF, 0x333333, false).tooltipText(list);
            top += font.lineHeight + 2;
        }
    }
}
