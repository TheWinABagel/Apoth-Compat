package dev.bagel.extra_apoth_compat;

import dev.shadowsoffire.apothic_attributes.ApothicAttributes;
import dev.shadowsoffire.placebo.config.Configuration;
import net.neoforged.fml.ModList;

public class ModConfig {

    public static boolean curiosCompat = true;
    public static String[] disabledMixins = new String[0];

    public static void load() {
        Configuration cfg = new Configuration(ApothicAttributes.getConfigFile(ExtraApothCompat.MODID));
        cfg.setTitle("Apotheosis Compat Config");

        curiosCompat = cfg.getBoolean("Curios Compat", "compat", true, "If curios compatabilty should be loaded");
//        disabledMixins = cfg.getStringList("Disabled Mixins", "compat", new String[]{""}, "Disabled Mixins.");
        if (cfg.hasChanged()) {
            cfg.save();
        }
    }

    public static class Loaded {
        public static boolean APOTHEOSIS = ModList.get().isLoaded("apotheosis");
        public static boolean EMI = ModList.get().isLoaded("emi");
    }
}
