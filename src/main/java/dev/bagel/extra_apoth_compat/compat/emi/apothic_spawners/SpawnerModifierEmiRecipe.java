package dev.bagel.extra_apoth_compat.compat.emi.apothic_spawners;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.bagel.extra_apoth_compat.compat.emi.extension.HoverTextWidget;
import dev.bagel.extra_apoth_compat.compat.emi.extension.HoverTextureWidget;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTooltipComponents;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.TextWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import dev.shadowsoffire.apothic_spawners.ApothicSpawners;
import dev.shadowsoffire.apothic_spawners.modifiers.SpawnerModifier;
import dev.shadowsoffire.apothic_spawners.modifiers.StatModifier;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.Arrays;
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
        List<EmiIngredient> inputs = getInputs();
//        widgets.addDrawable(-169, 0, 169, 75, this::draw);
        if (recipe.getOffhandInput() == Ingredient.EMPTY) {
            widgets.addTexture(TEXTURES, 1, 31, 28, 34, 0, 88);
        }

        widgets.addTooltip((mouseX, mouseY) -> List.of(EmiTooltipComponents.of(ApothicSpawners.lang("misc", "mainhand"))), 0, 13, 10, 12);
        widgets.addSlot(inputs.getFirst(), 11, 11);
        if (recipe.getOffhandInput() != Ingredient.EMPTY) {
            widgets.addSlot(inputs.get(1), 11, 48);
            //split arrow
            widgets.addTexture(TEXTURES, 10, 28, 18, 19, 10, 28);
            //bottom question mark
            HoverTextureWidget.create(widgets, TEXTURES, 0, 50, 10, 12, -1, 50)
                    .hoverTexture(0, 75)
                    .tooltip(List.of(EmiTooltipComponents.of(ApothicSpawners.lang("misc", "mainhand"))));
        }
        else {
            //Add curved arrow when no offhand input
            widgets.addTexture(TEXTURES, 1, 31, 28, 34, 0, 88);

        }
        //Top question mark
        HoverTextureWidget.create(widgets, TEXTURES, 0, 13, 10, 12, -1, 13)
                .hoverTexture(0, 75)
                .tooltip(List.of(EmiTooltipComponents.of(ApothicSpawners.lang("misc", "mainhand"))));


//        widgets.addTooltip(List.of(EmiTooltipComponents.of(ApothicSpawners.lang("misc", "rclick_spawner"))), 33, 30, 16, 16);

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
        for (StatModifier<?> s : recipe.getStatModifiers()) {
            String value = s.getFormattedValue();
            if ("true".equals(value)) value = "+";
            else if ("false".equals(value)) value = "-";
            else if (s.value() instanceof Number num && num.intValue() > 0) value = "+" + value;
            Component msg = ApothicSpawners.lang("misc", "concat", value, s.stat().name());
//            int width = font.width(msg);
//            boolean hover = mouseX >= left - width && mouseX < left && mouseY >= top && mouseY < top + font.lineHeight + 1;
//            gfx.drawString(font, msg, left - font.width(msg), top, hover ? 0x8080FF : 0x333333, false);
            List<Component> list = new ArrayList<>();
            list.add(s.stat().name().withStyle(ChatFormatting.GREEN, ChatFormatting.UNDERLINE));
            list.add(s.stat().desc().withStyle(ChatFormatting.GRAY));
            if (s.value() instanceof Number) {
                StatModifier<Number> n = (StatModifier<Number>) s;
                if (s.min().isPresent() || s.max().isPresent()) list.add(Component.literal(" "));
                if (s.min().isPresent()) list.add(ApothicSpawners.lang("misc", "min_value", n.stat().formatValue(n.min().get())).withStyle(ChatFormatting.GRAY));
                if (s.max().isPresent()) list.add(ApothicSpawners.lang("misc", "max_value", n.stat().formatValue(n.max().get())).withStyle(ChatFormatting.GRAY));
            }

            HoverTextWidget.create(widgets, msg, left - font.width(msg), top, 0x8080FF, 0x333333, false).tooltipText(list)/*.horizontalAlign(TextWidget.Alignment.CENTER).verticalAlign(TextWidget.Alignment.END)*/;

            int maxWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            maxWidth = maxWidth - (maxWidth - 210) / 2 - 210;

            if (false) {
//                List<Component> list = new ArrayList<>();
                list.add(s.stat().name().withStyle(ChatFormatting.GREEN, ChatFormatting.UNDERLINE));
                list.add(s.stat().desc().withStyle(ChatFormatting.GRAY));
                if (s.value() instanceof Number) {
                    StatModifier<Number> n = (StatModifier<Number>) s;
                    if (s.min().isPresent() || s.max().isPresent()) list.add(Component.literal(" "));
                    if (s.min().isPresent()) list.add(ApothicSpawners.lang("misc", "min_value", n.stat().formatValue(n.min().get())).withStyle(ChatFormatting.GRAY));
                    if (s.max().isPresent()) list.add(ApothicSpawners.lang("misc", "max_value", n.stat().formatValue(n.max().get())).withStyle(ChatFormatting.GRAY));
                }
//                renderComponentTooltip(scn, gfx, list, left + 6, (int) mouseY, maxWidth, font);
            }

            top += font.lineHeight + 2;
        }
    }

    public void draw(GuiGraphics gfx, double mouseX, double mouseY, float delta) {
        Screen scn = Minecraft.getInstance().screen;
        Font font = Minecraft.getInstance().font;
        if (scn == null) return; // We need this to render tooltips, bail if it's not there.
//        if (mouseX >= -1 && mouseX < 9 && mouseY >= 13 && mouseY < 13 + 12) {
//            gfx.blit(TEXTURES, -1, 13, 0, 0, 75, 10, 12, 256, 256);
//            gfx.renderComponentTooltip(font, List.of(ApothicSpawners.lang("misc", "mainhand")), (int) mouseX, (int) mouseY);
//        }
//        else
//        if (mouseX >= -1 && mouseX < 9 && mouseY >= 50 && mouseY < 50 + 12 && recipe.getOffhandInput() != Ingredient.EMPTY) {
//            gfx.blit(TEXTURES, -1, 50, 0, 0, 75, 10, 12, 256, 256);
//            gfx.renderComponentTooltip(font, Arrays.asList(ApothicSpawners.lang("misc", "offhand"), ApothicSpawners.lang("misc", "not_consumed").withStyle(ChatFormatting.GRAY)), (int) mouseX,
//                    (int) mouseY);
//        }
//        else
//        if (mouseX >= 33 && mouseX < 33 + 16 && mouseY >= 30 && mouseY < 30 + 16) {
//            gfx.renderComponentTooltip(font, List.of(ApothicSpawners.lang("misc", "rclick_spawner")), (int) mouseX, (int) mouseY);
//        }
//
//        PoseStack mvStack = gfx.pose();
//        mvStack.pushPose();
//        mvStack.translate(0, 0.5, 0);
//        gfx.renderFakeItem(new ItemStack(Items.SPAWNER), 31, 29);
//        mvStack.popPose();

        int top = 75 / 2 - recipe.getStatModifiers().size() * (font.lineHeight + 2) / 2 + 2;
        int left = 168;
        for (StatModifier<?> s : recipe.getStatModifiers()) {
            String value = s.getFormattedValue();
            if ("true".equals(value)) value = "+";
            else if ("false".equals(value)) value = "-";
            else if (s.value() instanceof Number num && num.intValue() > 0) value = "+" + value;
            Component msg = ApothicSpawners.lang("misc", "concat", value, s.stat().name());
            int width = font.width(msg);
            boolean hover = mouseX >= left - width && mouseX < left && mouseY >= top && mouseY < top + font.lineHeight + 1;
            gfx.drawString(font, msg, left - font.width(msg), top, hover ? 0x8080FF : 0x333333, false);

            int maxWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            maxWidth = maxWidth - (maxWidth - 210) / 2 - 210;

            if (hover) {
                List<Component> list = new ArrayList<>();
                list.add(s.stat().name().withStyle(ChatFormatting.GREEN, ChatFormatting.UNDERLINE));
                list.add(s.stat().desc().withStyle(ChatFormatting.GRAY));
                if (s.value() instanceof Number) {
                    StatModifier<Number> n = (StatModifier<Number>) s;
                    if (s.min().isPresent() || s.max().isPresent()) list.add(Component.literal(" "));
                    if (s.min().isPresent()) list.add(ApothicSpawners.lang("misc", "min_value", n.stat().formatValue(n.min().get())).withStyle(ChatFormatting.GRAY));
                    if (s.max().isPresent()) list.add(ApothicSpawners.lang("misc", "max_value", n.stat().formatValue(n.max().get())).withStyle(ChatFormatting.GRAY));
                }
                renderComponentTooltip(scn, gfx, list, left + 6, (int) mouseY, maxWidth, font);
            }

            top += font.lineHeight + 2;
        }
    }

    private static void renderComponentTooltip(Screen scn, GuiGraphics gfx, List<Component> list, int x, int y, int maxWidth, Font font) {
        List<FormattedText> text = list.stream().map(c -> font.getSplitter().splitLines(c, maxWidth, c.getStyle())).flatMap(List::stream).toList();
        gfx.renderComponentTooltip(font, text, x, y, ItemStack.EMPTY);
    }

    public boolean consumesOffhand() {
        return recipe.consumesOffhand();
    }
}
