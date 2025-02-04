package dev.bagel.extra_apoth_compat.compat.emi.apothic_enchanting;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.bagel.extra_apoth_compat.compat.emi.EmiConstants;
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

        drawWordWrap(font, itextproperties, 77, 6, width, 16777088, 6839882, widgets, bound -> {
            return new Bounds(57, 4, 108, 19);
        });

        widgets.addText(Component.literal(s), 77 + width, 13, 8453920, true);

        int[] pos = {getBarLength(stats.eterna()), getBarLength(stats.quanta()), getBarLength(stats.arcana())};
        if (stats.eterna() > 0) {
            widgets.addTexture(TEXTURES, 56, 27, pos[0], 5, 0, 56);
        }
        if (stats.quanta() > 0) {
            widgets.addTexture(TEXTURES, 56, 37, pos[1], 5, 0, 61);
        }
        if (stats.arcana() > 0) {
            widgets.addTexture(TEXTURES, 56, 47, pos[2], 5, 0, 66);
        }
        RenderSystem.enableBlend();
        if (maxStats.eterna() > 0) {
            widgets.addTexture(TEXTURES, 56 + pos[0], 27, getBarLength(maxStats.eterna() - stats.eterna()), 5, pos[0], 90);
        }
        if (maxStats.quanta() > 0) {
            widgets.addTexture(TEXTURES, 56 + pos[1], 37, getBarLength(maxStats.eterna() - stats.eterna()), 5, pos[1], 90);
        }
        if (maxStats.arcana() > 0) {
            widgets.addTexture(TEXTURES, 56 + pos[2], 47, getBarLength(maxStats.eterna() - stats.eterna()), 5, pos[2], 90);
        }
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
            } else if (mouseX > 56 && mouseX <= 56 + 110 && mouseY > 36 && mouseY <= 37 + 5) {
                list.add(TooltipUtil.lang("gui", "enchant.quanta").withStyle(ChatFormatting.RED));
                if (maxStats.quanta() == stats.quanta()) {
                    list.add(TooltipUtil.lang("info", "percent_exact", stats.quanta()).withStyle(ChatFormatting.GRAY));
                } else {
                    list.add(TooltipUtil.lang("info", "percent_at_least", stats.quanta()).withStyle(ChatFormatting.GRAY));
                    if (maxStats.quanta() > -1)
                        list.add(TooltipUtil.lang("info", "percent_at_most", maxStats.quanta()).withStyle(ChatFormatting.GRAY));
                }
            } else if (mouseX > 56 && mouseX <= 56 + 110 && mouseY > 46 && mouseY <= 47 + 5) {
                list.add(TooltipUtil.lang("gui", "enchant.arcana").withStyle(ChatFormatting.DARK_PURPLE));
                if (maxStats.arcana() == stats.arcana()) {
                    list.add(TooltipUtil.lang("info", "percent_exact", stats.arcana()).withStyle(ChatFormatting.GRAY));
                } else {
                    list.add(TooltipUtil.lang("info", "percent_at_least", stats.arcana()).withStyle(ChatFormatting.GRAY));
                    if (maxStats.arcana() > -1)
                        list.add(TooltipUtil.lang("info", "percent_at_most", maxStats.arcana()).withStyle(ChatFormatting.GRAY));
                }
            }
            return list.stream().map(EmiTooltipComponents::of).toList();
        }, 0, 0, 170, 56);

        widgets.addSlot(this.input, 5, 5);
        widgets.addSlot(this.output, 36, 5).recipeContext(this);
    }

    public static int getBarLength(float stat) {
        return ApothEnchantmentScreen.getBarLength(stat);
    }

    public static void drawWordWrap(Font font, FormattedText pText, int pX, int pY, int pMaxWidth, int idleColor, int hoverColor, WidgetHolderExtension widgets, UnaryOperator<Bounds> bounds) {
        for (FormattedCharSequence formattedcharsequence : font.split(pText, pMaxWidth)) {
            widgets.addHoverTextWidget(formattedcharsequence, pX, pY, idleColor, hoverColor, false).setHoverBounds(bounds);
            pY += 9;
        }
    }

    public float getEterna() {
        return this.recipe.getRequirements().eterna();
    }
}
