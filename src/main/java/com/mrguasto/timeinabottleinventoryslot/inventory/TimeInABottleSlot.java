package com.mrguasto.timeinabottleinventoryslot.inventory;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class TimeInABottleSlot extends Slot {
    
    public TimeInABottleSlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }
    
    @Override
    public boolean mayPlace(ItemStack stack) {
        // Only allow Time in a Bottle items or Temporal Pouch
        return isTimeInABottle(stack) || isTemporalPouch(stack);
    }
    
    @Override
    public int getMaxStackSize() {
        return 1; // Only allow one Time in a Bottle at a time
    }
    
    // Metodo per identificare questo slot come speciale per il rendering
    public boolean isTimeInABottleSlot() {
        return true;
    }
    
    private boolean isTimeInABottle(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }

        // Check if the item is from the Time in a Bottle mod
        String itemId = stack.getItem().toString();
        return itemId.contains("timeinabottle") ||
               itemId.contains("time_in_a_bottle") ||
               stack.getItem().getDescriptionId().contains("timeinabottle");
    }

    private boolean isTemporalPouch(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }

        ResourceLocation key = BuiltInRegistries.ITEM.getKey(stack.getItem());
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
