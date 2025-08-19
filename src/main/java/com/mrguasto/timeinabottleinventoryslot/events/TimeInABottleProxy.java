package com.mrguasto.timeinabottleinventoryslot.events;

import com.mrguasto.timeinabottleinventoryslot.Timeinabottleinventoryslot;
import com.mrguasto.timeinabottleinventoryslot.attachment.ModAttachments;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = Timeinabottleinventoryslot.MODID)
public class TimeInABottleProxy {
    
    // Evento per debug e logging
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        ItemStack timeInABottleStack = player.getData(ModAttachments.TIME_IN_A_BOTTLE_SLOT);
        
        if (!timeInABottleStack.isEmpty()) {
            Timeinabottleinventoryslot.LOGGER.info("Player {} logged in with Time in a Bottle: {}", 
                player.getName().getString(), timeInABottleStack.getDisplayName().getString());
        }
    }
    
    // Evento per sincronizzare eventuali modifiche
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            // Mantieni l'oggetto dopo la morte se configurato
            Player oldPlayer = event.getOriginal();
            Player newPlayer = event.getEntity();
            
            ItemStack timeInABottleStack = oldPlayer.getData(ModAttachments.TIME_IN_A_BOTTLE_SLOT);
            if (!timeInABottleStack.isEmpty()) {
                newPlayer.setData(ModAttachments.TIME_IN_A_BOTTLE_SLOT, timeInABottleStack.copy());
                Timeinabottleinventoryslot.LOGGER.info("Transferred Time in a Bottle after death for player: {}", 
                    newPlayer.getName().getString());
            }
        }
    }
}
