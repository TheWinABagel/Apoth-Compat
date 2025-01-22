package dev.bagel.extra_apoth_compat.compat.emi.extension;

import dev.emi.emi.api.widget.ButtonWidget;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BooleanSupplier;

public class CustomButtonWidget extends ButtonWidget {
    public CustomButtonWidget(int x, int y, int width, int height, int u, int v, BooleanSupplier isActive, ClickAction action) {
        super(x, y, width, height, u, v, isActive, action);
    }

    public CustomButtonWidget(int x, int y, int width, int height, int u, int v, ResourceLocation texture, BooleanSupplier isActive, ClickAction action) {
        super(x, y, width, height, u, v, texture, isActive, action);
    }
}
