package com.mrguasto.timeinabottleinventoryslot.events;

import com.mrguasto.timeinabottleinventoryslot.Timeinabottleinventoryslot;
import com.mrguasto.timeinabottleinventoryslot.attachment.ModAttachments;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = Timeinabottleinventoryslot.MODID)
public class CustomSlotItemTicker {

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;

        ItemStack stack = player.getData(ModAttachments.TIMEINABOTTLESLOT);
        if (!isTemporalPouch(stack)) return;

        stack.inventoryTick(player.level(), player, null);
        player.setData(ModAttachments.TIMEINABOTTLESLOT, stack);
    }

    private static boolean isTemporalPouch(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }

        Identifier key = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if (key != null && key.getNamespace().equals("gag") && key.getPath().equals("time_sand_pouch")) {
            return true;
        }

        // Fallback string-based
        String itemId = stack.getItem().toString().toLowerCase();
        String descId = stack.getItem().getDescriptionId().toLowerCase();
        return descId.contains("time_sand_pouch")
                || itemId.contains("time_sand_pouch")
                || descId.contains("timesandpouch")
                || itemId.contains("timesandpouch");
    }
}