package com.mrguasto.timeinabottleinventoryslot.client;

import com.mrguasto.timeinabottleinventoryslot.Timeinabottleinventoryslot;
import com.mrguasto.timeinabottleinventoryslot.attachment.ModAttachments;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;

@EventBusSubscriber(modid = Timeinabottleinventoryslot.MODID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof InventoryScreen inventoryScreen) {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                // Log per debug - ora lo slot è gestito nativamente dal mixin
                Timeinabottleinventoryslot.LOGGER.info("Inventory screen opened for player: {}", player.getName().getString());
                
                // Ottieni l'oggetto salvato nello slot personalizzato
                ItemStack timeInABottleStack = player.getData(ModAttachments.TIME_IN_A_BOTTLE_SLOT);
                if (!timeInABottleStack.isEmpty()) {
                    Timeinabottleinventoryslot.LOGGER.info("Player has Time in a Bottle: {}", timeInABottleStack.getDisplayName().getString());
                }
            }
        }
    }
}
