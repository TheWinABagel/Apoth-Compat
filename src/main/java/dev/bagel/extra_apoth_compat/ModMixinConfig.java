package dev.bagel.extra_apoth_compat;

import dev.shadowsoffire.apothic_attributes.ApothicAttributes;
import dev.shadowsoffire.placebo.config.Configuration;

public class ModMixinConfig {
    public static String[] disabledMixins = new String[0];
    public static boolean disableJeiLoading = true;

    public static void load() {
        Configuration cfg = new Configuration(ApothicAttributes.getConfigFile(ExtraApothCompat.MODID + "_mixins"));
        disabledMixins = cfg.getStringList("Disabled Mixins", "disable", new String[]{""}, "Disabled Mixins. Only use if you know what you're doing! Check the class structure first.");
        disableJeiLoading = cfg.getBoolean("Disable JEI Loading", "disable", true, "If Apotheosis JEI support loading should be cancelled via mixin. May slightly improve load times");
        if (cfg.hasChanged()) {
            cfg.save();
        }
    }
}
