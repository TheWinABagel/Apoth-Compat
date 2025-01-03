package dev.bagel.extra_apoth_compat.compat;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.shadowsoffire.apothic_attributes.client.ModifierSource;
import dev.shadowsoffire.apothic_attributes.client.ModifierSourceType;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.puffish.skillsmod.api.SkillsAPI;

import java.util.Comparator;
import java.util.function.BiConsumer;

public class PuffishCompat {
    public static void init() {}

    public static ModifierSourceType<AttributeModifier> PUFFISH_MODIFIER =
            ModifierSourceType.register(new ModifierSourceType<>() {

                @Override
                public void extract(LivingEntity entity, BiConsumer<AttributeModifier, ModifierSource<?>> map) {
                    entity.getAttributes().getSyncableAttributes().forEach(attributeInstance -> {
                        for (AttributeModifier modifier : attributeInstance.getModifiers()) {
                            if (modifier.id().getNamespace().equals(SkillsAPI.MOD_ID)) {
                                map.accept(modifier, new PuffishModifierSource(modifier));
                            }
                        }
                    });
                }

                @Override
                public int getPriority() {
                    return 50;
                }
            });


    public static class PuffishModifierSource extends ModifierSource<AttributeModifier> {
        public PuffishModifierSource(AttributeModifier data) {
            super(PUFFISH_MODIFIER, Comparator.comparing(AttributeModifier::amount), data);

        }

        @Override
        public void render(GuiGraphics gfx, Font font, int x, int y) {
            PoseStack pose = gfx.pose();
            pose.pushPose();
            float scale = 0.5F;
            pose.scale(scale, scale, 1);
            pose.translate(1 + x / scale, 1 + y / scale, 0);
            gfx.renderFakeItem(new ItemStack(Items.PUFFERFISH), 0, 0);
            pose.popPose();
        }
    }
}
