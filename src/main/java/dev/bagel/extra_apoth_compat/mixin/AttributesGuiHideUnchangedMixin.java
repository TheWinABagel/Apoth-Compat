package dev.bagel.extra_apoth_compat.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.shadowsoffire.apothic_attributes.client.AttributesGui;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Predicate;
import java.util.stream.Stream;

//thank u muon for this
@Mixin(value = AttributesGui.class, remap = false)
public class AttributesGuiHideUnchangedMixin {
    @Shadow
    protected static boolean hideUnchanged;

    @SuppressWarnings("unchecked")
    @WrapOperation(
            method = "refreshData",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/stream/Stream;filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;",
                    ordinal = 2
            )
    )
    private <T> Stream<T> modifyHideUnchangedFilter(Stream<T> instance, Predicate<? super T> predicate, Operation<Stream<T>> original) {
        Stream<AttributeInstance> filtered = (Stream<AttributeInstance>)original.call(instance, predicate);

        if (hideUnchanged) {
            filtered = filtered.filter(ai -> !ai.getModifiers().isEmpty());
        }

        return (Stream<T>) filtered;
    }
}