package com.mrguasto.timeinabottleinventoryslot.mixin;

import com.mrguasto.timeinabottleinventoryslot.inventory.TimeInABottleSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InventoryMenu.class)
public class InventoryMenuQuickMoveMixin {
    
    @Inject(method = "quickMoveStack", at = @At("HEAD"), cancellable = true)
    private void customQuickMoveFromTimeInABottleSlot(Player player, int index, CallbackInfoReturnable<ItemStack> cir) {
        InventoryMenu menu = (InventoryMenu) (Object) this;
        Slot slot = menu.slots.get(index);
        
        // Se lo shift-click proviene dal nostro slot personalizzato
        if (slot instanceof TimeInABottleSlot && slot.hasItem()) {
            ItemStack itemStack = slot.getItem();
            ItemStack originalStack = itemStack.copy();
            
            // Prima prova a mettere nella hotbar (slot 0-8)
            if (!this.moveItemStackTo(menu, itemStack, 0, 9, false)) {
                // Se la hotbar è piena, prova l'inventario principale (slot 9-35)
                if (!this.moveItemStackTo(menu, itemStack, 9, 36, false)) {
                    // Se anche l'inventario principale è pieno, non fare nulla
                    cir.setReturnValue(ItemStack.EMPTY);
                    return;
                }
            }
            
            if (itemStack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            
            if (itemStack.getCount() == originalStack.getCount()) {
                cir.setReturnValue(ItemStack.EMPTY);
                return;
            }
            
            slot.onTake(player, itemStack);
            cir.setReturnValue(originalStack);
        }
    }
    
    // Metodo helper per spostare gli ItemStack
    private boolean moveItemStackTo(InventoryMenu menu, ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
        boolean flag = false;
        int i = startIndex;
        if (reverseDirection) {
            i = endIndex - 1;
        }

        if (stack.isStackable()) {
            while (!stack.isEmpty()) {
                if (reverseDirection) {
                    if (i < startIndex) {
                        break;
                    }
                } else if (i >= endIndex) {
                    break;
                }

                Slot slot = menu.slots.get(i);
                ItemStack itemstack = slot.getItem();
                if (!itemstack.isEmpty() && ItemStack.isSameItemSameComponents(stack, itemstack)) {
                    int j = itemstack.getCount() + stack.getCount();
                    int maxSize = Math.min(slot.getMaxStackSize(), stack.getMaxStackSize());
                    if (j <= maxSize) {
                        stack.setCount(0);
                        itemstack.setCount(j);
                        slot.setChanged();
                        flag = true;
                    } else if (itemstack.getCount() < maxSize) {
                        stack.shrink(maxSize - itemstack.getCount());
                        itemstack.setCount(maxSize);
                        slot.setChanged();
                        flag = true;
                    }
                }

                if (reverseDirection) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        if (!stack.isEmpty()) {
            if (reverseDirection) {
                i = endIndex - 1;
            } else {
                i = startIndex;
            }

            while (true) {
                if (reverseDirection) {
                    if (i < startIndex) {
                        break;
                    }
                } else if (i >= endIndex) {
                    break;
                }

                Slot slot = menu.slots.get(i);
                ItemStack itemstack = slot.getItem();
                if (itemstack.isEmpty() && slot.mayPlace(stack)) {
                    if (stack.getCount() > slot.getMaxStackSize()) {
                        slot.setByPlayer(stack.split(slot.getMaxStackSize()));
                    } else {
                        slot.setByPlayer(stack.split(stack.getCount()));
                    }

                    slot.setChanged();
                    flag = true;
                    break;
                }

                if (reverseDirection) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        return flag;
    }
}
