package dev.bagel.extra_apoth_compat.compat.emi.apotheosis.smithing;

import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.shadowsoffire.apotheosis.socket.SocketHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Random;

public class SocketingSigilEMIRecipe extends ApothSmithingEMIRecipe {
    private static final List<EmiStack> TOOLS_MODIFIED = TOOLS.stream().map(s -> {
        ItemStack copy = s.getItemStack().copy();
        SocketHelper.setSockets(copy, 1);
        return EmiStack.of(copy);
    }).toList();

    public SocketingSigilEMIRecipe(EmiIngredient addition, ResourceLocation id, int maxSockets) {
        super(addition, id, () -> Component.translatable("emi.extra_apoth_compat.socketing_sigil.info", maxSockets).withStyle(ChatFormatting.GOLD));
    }

    @Override
    protected EmiStack getStack(Random r, int slot) {
        int next = r.nextInt(TOOLS.size());
        EmiStack input = TOOLS.get(next);
        EmiStack output = TOOLS_MODIFIED.get(next);
        return switch (slot) {
            case 0 -> input;
            case 1 -> this.addition.getEmiStacks().get(0);
            case 2 -> output;
            default -> throw new RuntimeException("whar");
        };
    }
}
