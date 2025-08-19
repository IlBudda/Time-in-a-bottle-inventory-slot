package com.mrguasto.timeinabottleinventoryslot.mixin;

import com.mrguasto.timeinabottleinventoryslot.inventory.TimeInABottleSlot;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public class InventoryScreenMixin {
    
    private static final ResourceLocation SLOT_BACKGROUND = ResourceLocation.fromNamespaceAndPath("timeinabottleinventoryslot", "textures/gui/slot_background.png");
    
    @Inject(method = "renderBg", at = @At("TAIL"))
    private void renderTimeInABottleSlotBackground(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY, CallbackInfo ci) {
        InventoryScreen screen = (InventoryScreen) (Object) this;
        
        // Trova il nostro slot personalizzato e disegna la texture
        for (Slot slot : screen.getMenu().slots) {
            if (slot instanceof TimeInABottleSlot) {
                int slotX = screen.getGuiLeft() + slot.x;
                int slotY = screen.getGuiTop() + slot.y;
                
                // Disegna lo slot 16x16 con background #8b8b8b
                this.renderSlotBorder(guiGraphics, slotX, slotY);
                
                // Renderizza il PNG placeholder SOLO se lo slot è vuoto
                if (!slot.hasItem()) {
                    // PNG 16x16 perfettamente centrato nello slot 16x16
                    guiGraphics.blit(SLOT_BACKGROUND, slotX, slotY, 0, 0, 16, 16, 16, 16);
                }
                
                break; // Dovrebbe esserci solo uno slot
            }
        }
    }
    
    // Metodo per disegnare lo slot 16x16 con background #8b8b8b
    private void renderSlotBorder(GuiGraphics guiGraphics, int x, int y) {
        // Colori per lo slot 16x16
        int darkColor = 0xFF373737;   // Grigio scuro per ombra (bordo superiore e sinistro)
        int lightColor = 0xFFFFFFFF;  // Bianco per luce (bordo inferiore e destro)
        int bgColor = 0xFF8B8B8B;     // Background #8b8b8b come richiesto
        
        // Background dello slot 16x16
        guiGraphics.fill(x, y, x + 16, y + 16, bgColor);
        
        // Bordo superiore (scuro - ombra)
        guiGraphics.fill(x, y, x + 16, y + 1, darkColor);
        
        // Bordo sinistro (scuro - ombra)
        guiGraphics.fill(x, y, x + 1, y + 16, darkColor);
        
        // Bordo inferiore (chiaro - luce)
        guiGraphics.fill(x, y + 15, x + 16, y + 16, lightColor);
        
        // Bordo destro (chiaro - luce)
        guiGraphics.fill(x + 15, y, x + 16, y + 16, lightColor);
        
        // Area interna dello slot (background #8b8b8b)
        guiGraphics.fill(x + 1, y + 1, x + 15, y + 15, bgColor);
    }
}
