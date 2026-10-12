package com.mrguasto.timeinabottleinventoryslot.mixin;

import com.mrguasto.timeinabottleinventoryslot.attachment.ModAttachments;
import com.mrguasto.timeinabottleinventoryslot.inventory.TimeInABottleContainer;
import com.mrguasto.timeinabottleinventoryslot.inventory.TimeInABottleSlot;
import net.neoforged.fml.ModList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryMenu.class)
public abstract class InventoryMenuMixin extends AbstractContainerMenu {
    
    protected InventoryMenuMixin() {
        super(null, 0);
    }
    
    @Inject(method = "<init>(Lnet/minecraft/world/entity/player/Inventory;ZLnet/minecraft/world/entity/player/Player;)V", at = @At("TAIL"))
    private void addTimeInABottleSlot(Inventory inventory, boolean active, Player player, CallbackInfo ci) {
        // Crea un container personalizzato per il nostro slot
        TimeInABottleContainer container = new TimeInABottleContainer(player);
        
        // Posizione di default (77, 8): a destra del modello del giocatore.
        // Extreme Sound Muffler mette il suo pulsante proprio li', quindi se e' presente scendiamo di uno slot.
        int y = isSoundMufflerLoaded() ? 26 : 8;
        this.addSlot(new TimeInABottleSlot(container, 0, 77, y));
    }

    private static boolean isSoundMufflerLoaded() {
        ModList mods = ModList.get();
        return mods != null && (mods.isLoaded("extremesoundmuffler") || mods.isLoaded("extreme_sound_muffler"));
    }
}
