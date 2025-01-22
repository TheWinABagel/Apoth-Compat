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

        cfg.setCategoryComment("mixins", "");
        curiosCompat = cfg.getBoolean("Curios Compat", "compat", true, "If curios compatibility should be loaded");
//        disabledMixins = cfg.getStringList("Disabled Mixins", "compat", new String[]{""}, "Disabled Mixins.");
        if (cfg.hasChanged()) {
            cfg.save();
        }
    }

    public static class Loaded {
        public static final boolean APOTHEOSIS = ModList.get().isLoaded("apotheosis");
        public static final boolean APOTHIC_ENCHANTING = ModList.get().isLoaded("apothic_enchanting");
        public static final boolean APOTHIC_SPAWNERS = ModList.get().isLoaded("apothic_spawners");

        public static final boolean EMI = ModList.get().isLoaded("emi");
        public static final boolean CURIOS = ModList.get().isLoaded("curios");

        public static final boolean PUFFISH = ModList.get().isLoaded("puffish_skills");

    }
}
