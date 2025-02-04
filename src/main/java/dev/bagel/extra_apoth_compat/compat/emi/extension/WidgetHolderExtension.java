package dev.bagel.extra_apoth_compat.compat.emi.extension;

import dev.emi.emi.api.render.EmiTooltipComponents;
import dev.emi.emi.api.widget.TooltipWidget;
import dev.emi.emi.api.widget.Widget;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class WidgetHolderExtension implements WidgetHolder {
    private final WidgetHolder parent;

    private WidgetHolderExtension(WidgetHolder parent) {
        this.parent = parent;
    }

    public static WidgetHolderExtension of(WidgetHolder parent) {
        return new WidgetHolderExtension(parent);
    }

    @Override
    public int getWidth() {
        return parent.getWidth();
    }

    @Override
    public int getHeight() {
        return parent.getHeight();
    }

    @Override
    public <T extends Widget> T add(T widget) {
        return parent.add(widget);
    }

    public TooltipWidget addComponentTooltip(List<Component> tooltip, int x, int y, int width, int height) {
        return addTooltip((mx, my) -> tooltip.stream().map(EmiTooltipComponents::of).toList(), x, y, width, height);
    }

    public HoverTextureWidget addHoverTextureWidget(ResourceLocation texture, int x, int y, int width, int height, int u, int v) {
        return add(new HoverTextureWidget(texture, x, y, width, height, u, v));
    }

    public HoverTextureWidget addHoverTextureWidget(ResourceLocation texture, int x, int y, int width, int height, int u, int v,
                                                    int regionWidth, int regionHeight, int textureWidth, int textureHeight) {
        return add(new HoverTextureWidget(texture, x, y, width, height, u, v,
                regionWidth, regionHeight, textureWidth, textureHeight));
    }
}
