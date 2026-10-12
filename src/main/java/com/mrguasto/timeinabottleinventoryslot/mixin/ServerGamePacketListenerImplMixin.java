package com.mrguasto.timeinabottleinventoryslot.mixin;

import com.mrguasto.timeinabottleinventoryslot.inventory.TimeInABottleSlot;
import net.minecraft.network.protocol.game.ServerboundSetCreativeModeSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * In creativo il client sincronizza gli slot dell'inventario con ServerboundSetCreativeModeSlotPacket,
 * ma vanilla accetta solo gli indici 1-45: il nostro slot (46) verrebbe ignorato dal server.
 */
@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin {

    @Shadow
    public ServerPlayer player;

    @Inject(method = "handleSetCreativeModeSlot", at = @At("HEAD"), cancellable = true)
    private void handleTimeInABottleSlot(ServerboundSetCreativeModeSlotPacket packet, CallbackInfo ci) {
        AbstractContainerMenu menu = this.player.inventoryMenu;
        int index = packet.slotNum();
        if (index < 0 || index >= menu.slots.size()) return;

        Slot slot = menu.slots.get(index);
        if (!(slot instanceof TimeInABottleSlot)) return;

        // E' un pacchetto per il nostro slot: lo gestiamo noi e basta, vanilla lo scarterebbe.
        ci.cancel();
        if (!this.player.hasInfiniteMaterials()) return;

        ItemStack stack = packet.itemStack();
        if (!stack.isItemEnabled(this.player.level().enabledFeatures())) return;
        if (!stack.isEmpty() && (!slot.mayPlace(stack) || stack.getCount() > slot.getMaxStackSize())) return;

        slot.setByPlayer(stack);
        menu.setRemoteSlot(index, stack);
        menu.broadcastChanges();
    }
}
