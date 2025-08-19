package com.mrguasto.timeinabottleinventoryslot.events;

import com.mrguasto.timeinabottleinventoryslot.attachment.ModAttachments;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

@EventBusSubscriber(modid = "timeinabottleinventoryslot")
public class PlayerDeathHandler {
    
    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player && !player.level().isClientSide) {
            // Ottieni l'oggetto dal nostro slot personalizzato
            ItemStack slotItem = player.getData(ModAttachments.TIME_IN_A_BOTTLE_SLOT);
            
            if (!slotItem.isEmpty()) {
                // Crea un ItemEntity per far cadere l'oggetto
                ItemEntity itemEntity = new ItemEntity(
                    player.level(), 
                    player.getX(), 
                    player.getY(), 
                    player.getZ(), 
                    slotItem.copy()
                );
                
                // Aggiungi un po' di velocità casuale come fanno gli altri oggetti
                itemEntity.setDeltaMovement(
                    (player.getRandom().nextFloat() - 0.5F) * 0.1F,
                    player.getRandom().nextFloat() * 0.05F,
                    (player.getRandom().nextFloat() - 0.5F) * 0.1F
                );
                
                // Spawna l'oggetto nel mondo
                player.level().addFreshEntity(itemEntity);
                
                // Rimuovi l'oggetto dal nostro slot
                player.setData(ModAttachments.TIME_IN_A_BOTTLE_SLOT, ItemStack.EMPTY);
            }
        }
    }
}
