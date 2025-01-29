package dev.bagel.extra_apoth_compat.compat.emi.apotheosis.smithing;

import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.shadowsoffire.apotheosis.socket.SocketHelper;
import dev.shadowsoffire.apotheosis.socket.SocketedGems;
import dev.shadowsoffire.apotheosis.socket.gem.GemInstance;
import dev.shadowsoffire.apotheosis.socket.gem.GemRegistry;
import dev.shadowsoffire.apotheosis.tiers.GenContext;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SocketingEMIRecipe extends ApothSmithingEMIRecipe {
    private static final List<EmiStack> TOOLS_INPUT = TOOLS.stream().map(s -> {
        ItemStack copy = s.getItemStack().copy();
        SocketHelper.setSockets(copy, 1);
        return EmiStack.of(copy);
    }).toList();

    private static final List<EmiStack> TOOLS_OUTPUT = TOOLS_INPUT.stream().map(EmiStack::getItemStack).map(ItemStack::copy).map(s -> {
        ItemStack copy = s.copy();
        return EmiStack.of(copy);
    }).toList();

    private final List<EmiIngredient> gems;
    private ItemStack currentGem = ItemStack.EMPTY;

    public SocketingEMIRecipe(EmiIngredient addition, ResourceLocation id, List<EmiIngredient> gems) {
        super(addition, id);
        this.gems = gems;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(EmiIngredient.of(gems));
    }

    @Override
    protected EmiStack getStack(Random r, int slot) {
        int next = r.nextInt(TOOLS_INPUT.size());
        EmiStack input = TOOLS_INPUT.get(next);
        EmiStack output = TOOLS_OUTPUT.get(next);

        ItemStack result = output.getItemStack();
        currentGem = getGem(result, slot);

        return switch (slot) {
            case 0 -> input;
            case 1 -> EmiStack.of(currentGem);
            case 2 -> EmiStack.of(result);
            default -> throw new RuntimeException("whar");
        };
    }

    private ItemStack getGem(ItemStack tool, int slot) {
        ItemStack gem = slot == firstSlot() ? getValidGem(tool, 0) : currentGem;
        SocketHelper.setSockets(tool, 1);
        List<GemInstance> gems = new ArrayList<>(List.of(GemInstance.EMPTY));
        gems.set(0, GemInstance.socketed(tool, gem, 0));
        SocketHelper.setGems(tool, new SocketedGems(gems));
        return gem;
    }

    private ItemStack getValidGem(ItemStack tool, int iterations) {
        ItemStack gemStack = GemRegistry.createRandomGemStack(GenContext.forPlayer(Minecraft.getInstance().player));
        if (GemInstance.unsocketed(gemStack).canApplyTo(tool)) {
            return gemStack;
        } else if (iterations < 20) {
            return getValidGem(tool, ++iterations);
        } else {
            //bail if cant find a valid gem, it won't render correctly but that's better than a stack overflow
            return ItemStack.EMPTY;
        }
    }

    protected int firstSlot() {
        return 0;
    }
}
