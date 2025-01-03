package dev.bagel.extra_apoth_compat;

import dev.shadowsoffire.placebo.config.Configuration;
import net.neoforged.fml.ModList;

public class ModConfig {

    public static boolean shouldLoad = true;
    public static String[] disabledMixins = new String[0];

    public static void load() {
        Configuration cfg = new Configuration(ExtraApothCompat.MODID);
        cfg.setTitle("Apotheosis Compat Config");

        shouldLoad = cfg.getBoolean("Load Test", "compat", true, "if test should be loaded");
        disabledMixins = cfg.getStringList("Disabled Mixins", "compat", new String[]{""}, "Disabled Mixins.");
        if (cfg.hasChanged()) {
            cfg.save();
        }
    }

    public static class Loaded {
        public static boolean APOTHEOSIS = ModList.get().isLoaded("apotheosis");
    }
}
