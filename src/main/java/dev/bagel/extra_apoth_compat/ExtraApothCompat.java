package dev.bagel.extra_apoth_compat;

import com.mojang.logging.LogUtils;
import dev.bagel.extra_apoth_compat.compat.CuriosCompat;
import dev.bagel.extra_apoth_compat.compat.PuffishCompat;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

@Mod(EACConstants.MODID)
public class ExtraApothCompat {

    public static final String MODID = EACConstants.MODID;
    public static final Logger LOGGER = LogUtils.getLogger();

    public ExtraApothCompat(IEventBus modEventBus) {
        modEventBus.addListener(this::commonSetup);
    }

    @SubscribeEvent
    private void commonSetup(final FMLCommonSetupEvent event) {
        ModConfig.load();
         if (ModConfig.Loaded.CURIOS && ModConfig.curiosCompat) {
//             event.enqueueWork(CuriosCompat::init);
         }

        if (ModConfig.Loaded.PUFFISH && ModConfig.puffishCompat) {
            event.enqueueWork(PuffishCompat::init);
        }
    }

    public static ResourceLocation loc(String id) {
        return ResourceLocation.fromNamespaceAndPath(MODID, id);
    }

    public static ResourceLocation synthLoc(String id) {
        return loc("/"+id);
    }

    public static ResourceLocation synthesise(ResourceLocation id) {
        return id.withPrefix("/");
    }
}
