package dev.bagel.extra_apoth_compat;

import dev.shadowsoffire.placebo.config.Configuration;
import net.neoforged.fml.loading.FMLPaths;

import java.io.File;

public class ModMixinConfig {
    private static final File configDir = new File(FMLPaths.CONFIGDIR.get().toFile(), "apotheosis");

    public static String[] disabledMixins = new String[0];
    public static boolean disableJeiLoading = true;

    public static boolean hideUnchangedProperly = true;

    public static void load() {
        Configuration cfg = new Configuration(getConfigFile(EACConstants.MODID + "_mixins"));
        disabledMixins = cfg.getStringList("Disabled Mixins", "disable", new String[]{""}, "Disabled Mixins. Only use if you know what you're doing! Check the class structure here (https://github.com/TheWinABagel/Apoth-Compat/tree/main/src/main/java/dev/bagel/extra_apoth_compat/mixin) first.");
        disableJeiLoading = cfg.getBoolean("Disable JEI Loading", "disable", true, "If Apotheosis JEI support loading should be cancelled via mixin. If disabled, and JEI and EMI are both loaded, there will be duplicate recipes in EMI!");

        hideUnchangedProperly = cfg.getBoolean("Hide Unchanged Properly", "fixes", true, "Gets rid of unnecessary attributes in the attributes menu when the \"Hide Unchanged\" button is active.\nDetails for nerds: By default, it does not properly hide dynamic attributes with no active modifiers. Setting this to true will hide *all* unchanged modifiers.");

        if (cfg.hasChanged()) {
            cfg.save();
        }
    }

    public static File getConfigFile(String path) {
        return new File(configDir, path + ".cfg");
    }
}
