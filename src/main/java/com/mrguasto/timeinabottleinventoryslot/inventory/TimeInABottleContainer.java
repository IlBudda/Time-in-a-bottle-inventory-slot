package com.mrguasto.timeinabottleinventoryslot.inventory;

import com.mrguasto.timeinabottleinventoryslot.attachment.ModAttachments;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class TimeInABottleContainer implements Container {
    private final Player player;
    
    public TimeInABottleContainer(Player player) {
        this.player = player;
    }
    
    @Override
    public int getContainerSize() {
        return 1;
    }
    
    @Override
    public boolean isEmpty() {
        return getItem(0).isEmpty();
    }
    
    @Override
    public ItemStack getItem(int slot) {
        if (slot == 0) {
            return player.getData(ModAttachments.TIME_IN_A_BOTTLE_SLOT);
        }
        return ItemStack.EMPTY;
    }
    
    @Override
    public ItemStack removeItem(int slot, int amount) {
        if (slot == 0) {
            ItemStack current = getItem(0);
            if (!current.isEmpty()) {
                ItemStack result = current.split(amount);
                setItem(0, current);
                return result;
            }
        }
        return ItemStack.EMPTY;
    }
    
    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        if (slot == 0) {
            ItemStack current = getItem(0);
            setItem(0, ItemStack.EMPTY);
            return current;
        }
        return ItemStack.EMPTY;
    }
    
    @Override
    public void setItem(int slot, ItemStack stack) {
        if (slot == 0) {
            player.setData(ModAttachments.TIME_IN_A_BOTTLE_SLOT, stack);
            setChanged();
        }
    }
    
    @Override
    public void setChanged() {
        // Notifica che il container è cambiato
    }
    
    @Override
    public boolean stillValid(Player player) {
        return this.player == player;
    }
    
    @Override
    public void clearContent() {
        setItem(0, ItemStack.EMPTY);
    }
}
