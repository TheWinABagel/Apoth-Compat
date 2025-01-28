package dev.bagel.extra_apoth_compat;

import dev.shadowsoffire.apothic_attributes.ApothicAttributes;
import dev.shadowsoffire.placebo.config.Configuration;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.LoadingModList;

public class ModConfig {
    public static boolean curiosCompat = true;
    public static boolean puffishCompat = true;
    public static boolean emiCompat = true;


    public static void load() {
        Configuration cfg = new Configuration(ApothicAttributes.getConfigFile(ExtraApothCompat.MODID));
        cfg.setTitle("Extra Apoth Compat Mixin Config");

        cfg.setCategoryComment("mixins", "");
        curiosCompat = cfg.getBoolean("Curios Compat", "compat", true, "If Curios compatibility should be loaded.");
        puffishCompat = cfg.getBoolean("Puffish Compat", "compat", true, "If Puffish Skills compatibility should be loaded.");
        emiCompat = cfg.getBoolean("Puffish Compat", "compat", true, "If EMI compatibility should be loaded.");
        if (cfg.hasChanged()) {
            cfg.save();
        }
    }

    public static class Loaded {
        public static final boolean APOTHEOSIS = isLoaded("apotheosis");
        public static final boolean APOTHIC_ENCHANTING = isLoaded("apothic_enchanting");
        public static final boolean APOTHIC_SPAWNERS = isLoaded("apothic_spawners");

        public static final boolean JEI = isLoaded("jei");
        public static final boolean EMI = isLoaded("emi");
        public static final boolean CURIOS = isLoaded("curios");

        public static final boolean PUFFISH = isLoaded("puffish_skills");

        private static boolean isLoaded(String modId) {
            return LoadingModList.get().getModFileById(modId) != null;
        }
    }
}
