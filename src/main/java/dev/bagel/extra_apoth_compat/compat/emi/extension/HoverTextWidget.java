package dev.bagel.extra_apoth_compat.compat.emi.extension;

import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.api.widget.TextWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import dev.emi.emi.api.widget.WidgetTooltipHolder;
import dev.emi.emi.runtime.EmiDrawContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

public class HoverTextWidget extends TextWidget implements WidgetTooltipHolder<HoverTextWidget> {
    private static final Minecraft CLIENT = Minecraft.getInstance();

    protected final int hoveredColor;
    protected Bounds containedArea = null;
    private BiFunction<Integer, Integer, List<ClientTooltipComponent>> tooltipSupplier = (mouseX, mouseY) -> List.of();

    public HoverTextWidget(FormattedCharSequence text, int x, int y, int color, int hoverColor, boolean shadow) {
        super(text, x, y, color, shadow);
        this.hoveredColor = hoverColor;
    }

    @Override
    public void render(GuiGraphics draw, int mouseX, int mouseY, float delta) {
        EmiDrawContext context = EmiDrawContext.wrap(draw);
        context.push();
        int xOff = horizontalAlignment.offset(CLIENT.font.width(text));
        int yOff = verticalAlignment.offset(CLIENT.font.lineHeight);
        context.matrices().translate(xOff, yOff, 300);
        Bounds bounds = containedArea == null ? getBounds() : containedArea;
        int color = bounds.contains(mouseX, mouseY) ? this.color : this.hoveredColor;
        if (shadow) {
            context.drawTextWithShadow(text, x, y, color);
        } else {
            context.drawText(text, x, y, color);
        }
        context.pop();
    }

    @Override
    public Bounds getBounds() {
        return super.getBounds();
    }

    public static HoverTextWidget create(WidgetHolder holder, FormattedCharSequence text, int x, int y, int color, int hoverColor, boolean shadow) {
        return holder.add(new HoverTextWidget(text, x, y, color, hoverColor, shadow));
    }

    public static HoverTextWidget create(WidgetHolder holder, Component text, int x, int y, int color, int hoverColor, boolean shadow) {
        return holder.add(new HoverTextWidget(text.getVisualOrderText(), x, y, color, hoverColor, shadow));
    }

    @Override
    public HoverTextWidget tooltip(BiFunction<Integer, Integer, List<ClientTooltipComponent>> tooltipSupplier) {
        this.tooltipSupplier = tooltipSupplier;
        return this;
    }

    @Override
    public List<ClientTooltipComponent> getTooltip(int mouseX, int mouseY) {
        return tooltipSupplier.apply(mouseX, mouseY);
    }

    public HoverTextWidget setHoverBounds(UnaryOperator<Bounds> containedArea) {
        this.containedArea = containedArea.apply(getBounds());
        return this;
    }
}
