package dev.bagel.extra_apoth_compat.compat.emi.apothic_enchanting;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.bagel.extra_apoth_compat.compat.emi.EmiConstants;
import dev.bagel.extra_apoth_compat.compat.emi.extension.HoverTextWidget;
import dev.bagel.extra_apoth_compat.compat.emi.extension.WidgetHolderExtension;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTooltipComponents;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.api.widget.WidgetHolder;
import dev.shadowsoffire.apothic_enchanting.ApothicEnchanting;
import dev.shadowsoffire.apothic_enchanting.Ench;
import dev.shadowsoffire.apothic_enchanting.table.ApothEnchantmentScreen;
import dev.shadowsoffire.apothic_enchanting.table.EnchantingStatRegistry;
import dev.shadowsoffire.apothic_enchanting.table.infusion.InfusionRecipe;
import dev.shadowsoffire.apothic_enchanting.util.TooltipUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EnchantmentNames;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public class EnchantingEmiRecipe implements EmiRecipe {
    public static final ResourceLocation TEXTURES = ApothicEnchanting.loc("textures/gui/enchanting_jei.png");

    private final InfusionRecipe recipe;
    private final ResourceLocation id;
    protected final EmiIngredient input;
    protected EmiStack output;

    public EnchantingEmiRecipe(RecipeHolder<InfusionRecipe> recipe) {
        this(recipe.value(), recipe.id(),  EmiIngredient.of(recipe.value().getInput()), EmiStack.of(recipe.value().getOutput()));
    }

    public EnchantingEmiRecipe(InfusionRecipe recipe, ResourceLocation id, EmiIngredient input, EmiStack output) {
        this.recipe = recipe;
        this.id = id;
        this.input = input;
        this.output = output;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return EmiConstants.ApothicEnchanting.ENCHANTING;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return this.id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(this.input);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(this.output);
    }

    @Override
    public int getDisplayWidth() {
        return 170;
    }

    @Override
    public int getDisplayHeight() {
        return 56;
    }

    @Override
    public void addWidgets(WidgetHolder wdgs) {
        WidgetHolderExtension widgets = WidgetHolderExtension.of(wdgs);
        widgets.addTexture(TEXTURES, 0, 0, 170, 56, 0, 0);

        List<Component> infusion = new ArrayList<>();
        Component infusionName = Enchantment.getFullname(Minecraft.getInstance().level.holderOrThrow(Ench.Enchantments.INFUSION), 1);
        infusion.add(Component.translatable("container.enchant.clue", infusionName).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        widgets.addComponentTooltip(infusion, 57, 4, 108, 19);
        widgets.addHoverTextureWidget(TEXTURES, 57, 4, 108, 19, 57, 4).hoverTexture(0, 71).tooltip(infusion.stream().map(EmiTooltipComponents::of).toList());

        Font font = Minecraft.getInstance().font;
        EnchantingStatRegistry.Stats stats = this.recipe.getRequirements();
        EnchantingStatRegistry.Stats maxStats = this.recipe.getMaxRequirements();
        widgets.addText(TooltipUtil.lang("gui", "enchant.eterna"), 16, 26, 0x3DB53D, false);
        widgets.addText(TooltipUtil.lang("gui", "enchant.quanta"), 16, 36, 0xFC5454, false);
        widgets.addText(TooltipUtil.lang("gui", "enchant.arcana"), 16, 46, 0xA800A8, false);
        int level = (int) stats.eterna();

        String s = "" + level;
        int width = 86 - font.width(s);
        EnchantmentNames.getInstance().initSeed(this.recipe.hashCode());
        FormattedText itextproperties = EnchantmentNames.getInstance().getRandomName(font, width);
//        int color = hover ? 16777088 : 6839882;
        int color = 16777088;
        drawWordWrap(font, itextproperties, 77, 6, width, 16777088, 6839882, widgets, bound -> {
            return new Bounds(57, 4, 108, 19);
        });
        color = 8453920;
        widgets.addText(Component.literal(s), 77 + width, 13, 8453920, true);
//        HoverTextWidget.create(widgets, Component.literal(s), 77 + width, 13, 6839882, 16777088, true).setHoverBounds(bound ->{
//            return new Bounds(57, 4, 108, 19);
//        });

//        widgets.addText(Component.literal(s), 77 + width, 13, color, true);
//        gfx.drawString(font, s, 77 + width, 13, color);

        int[] pos = {getBarLength(stats.eterna()), getBarLength(stats.quanta()), getBarLength(stats.arcana())};
        if (stats.eterna() > 0) {
            widgets.addTexture(TEXTURES, 56, 27, pos[0], 5, 0, 56);
//            gfx.blit(TEXTURES, 56, 27, 0, 56, pos[0], 5, 256, 256);
        }
        if (stats.quanta() > 0) {
            widgets.addTexture(TEXTURES, 56, 37, pos[1], 5, 0, 61);
//            gfx.blit(TEXTURES, 56, 37, 0, 61, pos[1], 5, 256, 256);
        }
        if (stats.arcana() > 0) {
            widgets.addTexture(TEXTURES, 56, 47, pos[2], 5, 0, 66);
//            gfx.blit(TEXTURES, 56, 47, 0, 66, pos[2], 5, 256, 256);
        }
        RenderSystem.enableBlend();
        if (maxStats.eterna() > 0) {
            widgets.addTexture(TEXTURES, 56 + pos[0], 27, getBarLength(maxStats.eterna() - stats.eterna()), 5, pos[0], 90);
//            gfx.blit(TEXTURES, 56 + pos[0], 27, pos[0], 90, getBarLength(maxStats.eterna() - stats.eterna()), 5, 256, 256);
        }
        if (maxStats.quanta() > 0) {
            widgets.addTexture(TEXTURES, 56 + pos[1], 37, getBarLength(maxStats.eterna() - stats.eterna()), 5, pos[1], 90);
//            gfx.blit(TEXTURES, 56 + pos[1], 37, pos[1], 95, getBarLength(maxStats.quanta() - stats.quanta()), 5, 256, 256);
        }
        if (maxStats.arcana() > 0) {
            widgets.addTexture(TEXTURES, 56 + pos[2], 47, getBarLength(maxStats.eterna() - stats.eterna()), 5, pos[2], 90);
//            gfx.blit(TEXTURES, 56 + pos[2], 47, pos[2], 100, getBarLength(maxStats.arcana() - stats.arcana()), 5, 256, 256);
        }

//        List<Component> infusion = new ArrayList<>();
//        Component infusionName = Enchantment.getFullname(Minecraft.getInstance().level.holderOrThrow(Ench.Enchantments.INFUSION), 1);
//        infusion.add(Component.translatable("container.enchant.clue", infusionName).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
//        widgets.addTooltip(infusion.stream().map(EmiTooltipComponents::of).toList(), 57,4,108,19);
        int finalColor = color;
        widgets.addDrawable(0, 0, 170, 56, (gfx, mouseX, mouseY, delta) -> {
            boolean hover = false;
//            if (mouseX > 57 && mouseX <= 57 + 108 && mouseY > 4 && mouseY <= 4 + 19) {
//                gfx.blit(TEXTURES, 57, 4, 0, 0, 71, 108, 19, 256, 256);
//                hover = true;
//            }

//            Font font = Minecraft.getInstance().font;
//            EnchantingStatRegistry.Stats stats = this.recipe.getRequirements();
//            EnchantingStatRegistry.Stats maxStats = this.recipe.getMaxRequirements();
//            gfx.drawString(font, TooltipUtil.lang("gui", "enchant.eterna"), 16, 26, 0x3DB53D, false);
//            gfx.drawString(font, TooltipUtil.lang("gui", "enchant.quanta"), 16, 36, 0xFC5454, false);
//            gfx.drawString(font, TooltipUtil.lang("gui", "enchant.arcana"), 16, 46, 0xA800A8, false);
//            int level = (int) stats.eterna();

//            String s = "" + level;
//            int width = 86 - font.width(s);
//            EnchantmentNames.getInstance().initSeed(this.recipe.hashCode());
//            FormattedText itextproperties = EnchantmentNames.getInstance().getRandomName(font, width);
//            int color = hover ? 16777088 : 6839882;
//            drawWordWrap(font, itextproperties, 77, 6, width, color, gfx);
//            color = 8453920;
//            gfx.drawString(font, s, 77 + width, 13, color);
//
//            int[] pos = {getBarLength(stats.eterna()), getBarLength(stats.quanta()), getBarLength(stats.arcana())};
//            if (stats.eterna() > 0) {
//                gfx.blit(TEXTURES, 56, 27, 0, 56, pos[0], 5, 256, 256);
//            }
//            if (stats.quanta() > 0) {
//                gfx.blit(TEXTURES, 56, 37, 0, 61, pos[1], 5, 256, 256);
//            }
//            if (stats.arcana() > 0) {
//                gfx.blit(TEXTURES, 56, 47, 0, 66, pos[2], 5, 256, 256);
//            }
//            RenderSystem.enableBlend();
//            if (maxStats.eterna() > 0) {
//                gfx.blit(TEXTURES, 56 + pos[0], 27, pos[0], 90, getBarLength(maxStats.eterna() - stats.eterna()), 5, 256, 256);
//            }
//            if (maxStats.quanta() > 0) {
//                gfx.blit(TEXTURES, 56 + pos[1], 37, pos[1], 95, getBarLength(maxStats.quanta() - stats.quanta()), 5, 256, 256);
//            }
//            if (maxStats.arcana() > 0) {
//                gfx.blit(TEXTURES, 56 + pos[2], 47, pos[2], 100, getBarLength(maxStats.arcana() - stats.arcana()), 5, 256, 256);
//            }
            RenderSystem.disableBlend();
            gfx.pose().pushPose();
            gfx.pose().translate(0, 0, 100);
//            drawWordWrap(font, itextproperties, 77, 6, width, finalColor, gfx);
            /*if (hover) {
                List<Component> list = new ArrayList<>();
                Component infusionName = Enchantment.getFullname(Minecraft.getInstance().level.holderOrThrow(Ench.Enchantments.INFUSION), 1);
                list.add(Component.translatable("container.enchant.clue", infusionName).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                gfx.renderComponentTooltip(font, list, mouseX, mouseY);
            } else*/
//            if (mouseX > 56 && mouseX <= 56 + 110 && mouseY > 26 && mouseY <= 27 + 5) {
//                List<Component> list = new ArrayList<>();
//                list.add(TooltipUtil.lang("gui", "enchant.eterna").withStyle(ChatFormatting.GREEN));
//                if (maxStats.eterna() == stats.eterna()) {
//                    list.add(TooltipUtil.lang("info", "eterna_exact", stats.eterna(), 100).withStyle(ChatFormatting.GRAY));
//                } else {
//                    list.add(TooltipUtil.lang("info", "eterna_at_least", stats.eterna(), 100).withStyle(ChatFormatting.GRAY));
//                    if (maxStats.eterna() > -1)
//                        list.add(TooltipUtil.lang("info", "eterna_at_most", maxStats.eterna(), 100).withStyle(ChatFormatting.GRAY));
//                }
//                gfx.renderComponentTooltip(font, list, mouseX, mouseY);
//            } else if (mouseX > 56 && mouseX <= 56 + 110 && mouseY > 36 && mouseY <= 37 + 5) {
//                List<Component> list = new ArrayList<>();
//                list.add(TooltipUtil.lang("gui", "enchant.quanta").withStyle(ChatFormatting.RED));
//                if (maxStats.quanta() == stats.quanta()) {
//                    list.add(TooltipUtil.lang("info", "percent_exact", stats.quanta()).withStyle(ChatFormatting.GRAY));
//                } else {
//                    list.add(TooltipUtil.lang("info", "percent_at_least", stats.quanta()).withStyle(ChatFormatting.GRAY));
//                    if (maxStats.quanta() > -1)
//                        list.add(TooltipUtil.lang("info", "percent_at_most", maxStats.quanta()).withStyle(ChatFormatting.GRAY));
//                }
//                gfx.renderComponentTooltip(font, list, (int) mouseX, (int) mouseY);
//            } else if (mouseX > 56 && mouseX <= 56 + 110 && mouseY > 46 && mouseY <= 47 + 5) {
//                List<Component> list = new ArrayList<>();
//                list.add(TooltipUtil.lang("gui", "enchant.arcana").withStyle(ChatFormatting.DARK_PURPLE));
//                if (maxStats.arcana() == stats.arcana()) {
//                    list.add(TooltipUtil.lang("info", "percent_exact", stats.arcana()).withStyle(ChatFormatting.GRAY));
//                } else {
//                    list.add(TooltipUtil.lang("info", "percent_at_least", stats.arcana()).withStyle(ChatFormatting.GRAY));
//                    if (maxStats.arcana() > -1)
//                        list.add(TooltipUtil.lang("info", "percent_at_most", maxStats.arcana()).withStyle(ChatFormatting.GRAY));
//                }
//                gfx.renderComponentTooltip(font, list, mouseX, mouseY);
//            }

            gfx.pose().popPose();
        });//*/
        widgets.addTooltip((mouseX, mouseY) -> {
            List<Component> list = new ArrayList<>();
            if (mouseX > 56 && mouseX <= 56 + 110 && mouseY > 26 && mouseY <= 27 + 5) {
                list.add(TooltipUtil.lang("gui", "enchant.eterna").withStyle(ChatFormatting.GREEN));
                if (maxStats.eterna() == stats.eterna()) {
                    list.add(TooltipUtil.lang("info", "eterna_exact", stats.eterna(), 100).withStyle(ChatFormatting.GRAY));
                } else {
                    list.add(TooltipUtil.lang("info", "eterna_at_least", stats.eterna(), 100).withStyle(ChatFormatting.GRAY));
                    if (maxStats.eterna() > -1)
                        list.add(TooltipUtil.lang("info", "eterna_at_most", maxStats.eterna(), 100).withStyle(ChatFormatting.GRAY));
                }
//                gfx.renderComponentTooltip(font, list, mouseX, mouseY);
            } else if (mouseX > 56 && mouseX <= 56 + 110 && mouseY > 36 && mouseY <= 37 + 5) {
                list.add(TooltipUtil.lang("gui", "enchant.quanta").withStyle(ChatFormatting.RED));
                if (maxStats.quanta() == stats.quanta()) {
                    list.add(TooltipUtil.lang("info", "percent_exact", stats.quanta()).withStyle(ChatFormatting.GRAY));
                } else {
                    list.add(TooltipUtil.lang("info", "percent_at_least", stats.quanta()).withStyle(ChatFormatting.GRAY));
                    if (maxStats.quanta() > -1)
                        list.add(TooltipUtil.lang("info", "percent_at_most", maxStats.quanta()).withStyle(ChatFormatting.GRAY));
                }
//                gfx.renderComponentTooltip(font, list, (int) mouseX, (int) mouseY);
            } else if (mouseX > 56 && mouseX <= 56 + 110 && mouseY > 46 && mouseY <= 47 + 5) {
                list.add(TooltipUtil.lang("gui", "enchant.arcana").withStyle(ChatFormatting.DARK_PURPLE));
                if (maxStats.arcana() == stats.arcana()) {
                    list.add(TooltipUtil.lang("info", "percent_exact", stats.arcana()).withStyle(ChatFormatting.GRAY));
                } else {
                    list.add(TooltipUtil.lang("info", "percent_at_least", stats.arcana()).withStyle(ChatFormatting.GRAY));
                    if (maxStats.arcana() > -1)
                        list.add(TooltipUtil.lang("info", "percent_at_most", maxStats.arcana()).withStyle(ChatFormatting.GRAY));
                }
//                gfx.renderComponentTooltip(font, list, mouseX, mouseY);
            }
            return list.stream().map(EmiTooltipComponents::of).toList();
        }, 0, 0, 170, 56);



        widgets.addSlot(this.input, 5, 5);
        widgets.addSlot(this.output, 36, 5).recipeContext(this);

        //TODO EMI-ify above to make this weird hack not necessary (recipe textures rendering behind EMI textures)
//        widgets.addButton(0, 0, 0, 0, 0, 0, TEXTURES, () -> false, (mouseX, mouseY, button) -> {});
    }

    public static int getBarLength(float stat) {
        return ApothEnchantmentScreen.getBarLength(stat);
    }

    public static void drawWordWrap(Font font, FormattedText pText, int pX, int pY, int pMaxWidth, int pColor, GuiGraphics gfx) {
        for (FormattedCharSequence formattedcharsequence : font.split(pText, pMaxWidth)) {
            gfx.drawString(font, formattedcharsequence, pX, pY, pColor, false);
            pY += 9;
        }
    }

    public static void drawWordWrap(Font font, FormattedText pText, int pX, int pY, int pMaxWidth, int idleColor, int hoverColor, WidgetHolder widgets, UnaryOperator<Bounds> bounds) {
        for (FormattedCharSequence formattedcharsequence : font.split(pText, pMaxWidth)) {
            HoverTextWidget.create(widgets, formattedcharsequence, pX, pY, idleColor, hoverColor, false).setHoverBounds(bounds);
//            widgets.addText(formattedcharsequence, pX, pY, pColor, false);
//            gfx.drawString(font, formattedcharsequence, pX, pY, pColor, false);
            pY += 9;
        }
    }

    public float getEterna() {
        return this.recipe.getRequirements().eterna();
    }
}
