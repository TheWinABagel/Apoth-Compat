package dev.bagel.extra_apoth_compat.compat.emi.apotheosis.smithing;

import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import dev.shadowsoffire.apotheosis.socket.SocketHelper;
import dev.shadowsoffire.apotheosis.socket.SocketedGems;
import dev.shadowsoffire.apotheosis.socket.gem.GemInstance;
import dev.shadowsoffire.apotheosis.socket.gem.GemRegistry;
import dev.shadowsoffire.apotheosis.socket.gem.Purity;
import dev.shadowsoffire.apotheosis.socket.gem.UnsocketedGem;
import dev.shadowsoffire.apotheosis.tiers.GenContext;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class SocketingEMIRecipe extends ApothSmithingEMIRecipe {
    private static final List<EmiStack> TOOLS_INPUT = TOOLS.stream().map(s -> {
        ItemStack copy = s.getItemStack().copy();
        SocketHelper.setSockets(copy, 1);
        return EmiStack.of(copy);
    }).toList();

    private static final List<EmiStack> TOOLS_OUTPUT = TOOLS_INPUT.stream().map(EmiStack::getItemStack).map(ItemStack::copy).map(EmiStack::of).toList();

    private final List<EmiIngredient> gems;
    private ItemStack currentGem = ItemStack.EMPTY;

    public SocketingEMIRecipe(EmiIngredient addition, ResourceLocation id, List<EmiIngredient> gems) {
        super(addition, id, () -> Component.translatable("emi.extra_apoth_compat.socketing.info").withStyle(ChatFormatting.GOLD));
        this.gems = gems;
    }

    public SocketingEMIRecipe(EmiIngredient addition, ResourceLocation id, List<EmiIngredient> gems, Supplier<Component> component) {
        super(addition, id, component);
        this.gems = gems;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(EmiIngredient.of(gems));
    }

//    @Override
//    public void addWidgets(WidgetHolder widgets) {
//        widgets.addTexture(EmiTexture.EMPTY_ARROW, 62, 1);
//        widgets.addSlot(EmiStack.EMPTY, 0, 0);
//        widgets.addGeneratedSlot(r -> getStack(r, 0), uniq, 18, 0);
//        widgets.addGeneratedSlot(r -> getStack(r, 1), uniq, 36, 0);
//        widgets.addGeneratedSlot(r -> getStack(r, 2), uniq, 94, 0).recipeContext(this).appendTooltip(outputTooltip.get());
//    }

    @Override
    protected EmiStack getStack(Random r, int slot) {
        int next = r.nextInt(TOOLS_INPUT.size());
        EmiStack output = TOOLS_OUTPUT.get(next);

        ItemStack result = output.getItemStack();
//        ItemStack gemStack = getGem(result, slot);
        currentGem = getGem(result, slot);

        return switch (slot) {
            case 0 -> TOOLS_INPUT.get(next);
            case 1 -> EmiStack.of(currentGem);
            case 2 -> EmiStack.of(result);
            default -> throw new RuntimeException("whar");
        };
    }

    @Override
    protected boolean isGenerated(int slot) {
        return true;
    }

    private ItemStack getGem(ItemStack tool, int slot) {
        ItemStack gem = slot == firstSlot() ? getValidGem(tool) : currentGem;
        SocketHelper.setSockets(tool, 1);
        List<GemInstance> gems = new ArrayList<>(List.of(GemInstance.EMPTY));
        gems.set(0, GemInstance.socketed(tool, gem, 0));
        SocketHelper.setGems(tool, new SocketedGems(gems));
        return gem;
    }

    private ItemStack getValidGem(ItemStack tool) {
        var context = GenContext.forPlayer(Minecraft.getInstance().player);
        var gem = GemRegistry.INSTANCE.getRandomItem(context, gem1 -> gem1.isValidIn(tool, ItemStack.EMPTY, Purity.random(context)));
        if (gem == null) {
            return GemRegistry.createRandomGemStack(context);
        }
        return GemRegistry.createGemStack(gem, Purity.random(context));

//        ItemStack gemStack = GemRegistry.createRandomGemStack(GenContext.forPlayer(Minecraft.getInstance().player));
//        if (UnsocketedGem.of(gemStack).canApplyTo(tool)) {
//            return gemStack;
//        } else if (iterations < 20) {
//            return getValidGem(tool, ++iterations);
//        } else {
//            //bail if cant find a valid gem, it won't render correctly but that's better than a stack overflow
//            return ItemStack.EMPTY;
//        }
    }

    protected int firstSlot() {
        return 2;
    }
}
