package dev.bagel.extra_apoth_compat.compat.emi.apotheosis.salvaging;

public enum IngredientType {
    DEFAULT,
    GEM,
    AFFIX;

    public boolean hidesFillButton() {
        return this == AFFIX;
    }
}
