package dev.bagel.extra_apoth_compat.compat.emi;

import dev.bagel.extra_apoth_compat.ModConfig;
import dev.bagel.extra_apoth_compat.compat.emi.apotheosis.ApotheosisEmiPlugin;
import dev.bagel.extra_apoth_compat.compat.emi.apothic_enchanting.ApothicEnchantingEmiPlugin;
import dev.bagel.extra_apoth_compat.compat.emi.apothic_spawners.ApothicSpawnersEmiPlugin;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;

@EmiEntrypoint
public class ApothEmiPlugin implements EmiPlugin {

    @Override
    public void register(EmiRegistry registry) {
        if (!ModConfig.emiCompat) return;
        if (ModConfig.Loaded.APOTHIC_ENCHANTING) {
            ApothicEnchantingEmiPlugin.register(registry);
        }

        if (ModConfig.Loaded.APOTHEOSIS) {
            ApotheosisEmiPlugin.register(registry);
        }

        if (ModConfig.Loaded.APOTHIC_SPAWNERS) {
            ApothicSpawnersEmiPlugin.register(registry);
        }
    }
}
