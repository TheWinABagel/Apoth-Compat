package dev.bagel.extra_apoth_compat.compat.emi.extension;

import dev.emi.emi.api.widget.TextureWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import dev.emi.emi.runtime.EmiDrawContext;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class HoverTextureWidget extends TextureWidget {
    boolean hasHover = false;
    int altU, altV;

    public HoverTextureWidget(ResourceLocation texture, int x, int y, int width, int height, int u, int v, int regionWidth, int regionHeight, int textureWidth, int textureHeight) {
        super(texture, x, y, width, height, u, v, regionWidth, regionHeight, textureWidth, textureHeight);
    }

    public HoverTextureWidget(ResourceLocation texture, int x, int y, int width, int height, int u, int v) {
        super(texture, x, y, width, height, u, v);
    }

    public HoverTextureWidget hoverTexture(int u, int v) {
        hasHover = true;
        altU = u;
        altV = v;
        return this;
    }

    @Override
    public void render(GuiGraphics draw, int mouseX, int mouseY, float delta) {
        EmiDrawContext context = EmiDrawContext.wrap(draw);
        context.resetColor();
        if (hasHover && getBounds().contains(mouseX, mouseY)) {
            context.drawTexture(texture, x, y, width, height, altU, altV, regionWidth, regionHeight, textureWidth, textureHeight);
        } else {
            context.drawTexture(texture, x, y, width, height, u, v, regionWidth, regionHeight, textureWidth, textureHeight);
        }
    }

    public static HoverTextureWidget create(WidgetHolder holder, ResourceLocation texture, int x, int y, int width, int height, int u, int v) {
        return holder.add(new HoverTextureWidget(texture, x, y, width, height, u, v));
    }

    public static HoverTextureWidget create(WidgetHolder holder, ResourceLocation texture, int x, int y, int width, int height, int u, int v,
                                     int regionWidth, int regionHeight, int textureWidth, int textureHeight) {
        return holder.add(new HoverTextureWidget(texture, x, y, width, height, u, v,
                regionWidth, regionHeight, textureWidth, textureHeight));
    }
}
