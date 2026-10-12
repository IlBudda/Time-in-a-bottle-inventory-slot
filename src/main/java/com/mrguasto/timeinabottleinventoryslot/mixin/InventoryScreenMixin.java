package com.mrguasto.timeinabottleinventoryslot.mixin;

import com.mrguasto.timeinabottleinventoryslot.inventory.TimeInABottleSlot;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
@SuppressWarnings("removal") // getGuiLeft/getGuiTop: NeoForge-added accessors for the protected leftPos/topPos
public class InventoryScreenMixin {

    // Cornice 18x18 stile vanilla
    private static final Identifier SLOT_FRAME = Identifier.fromNamespaceAndPath("timeinabottleinventoryslot", "textures/gui/slotframe.png");

    // Ghost icon 16x16 da mostrare quando lo slot è vuoto
    private static final Identifier SLOT_GHOST = Identifier.fromNamespaceAndPath("timeinabottleinventoryslot", "textures/gui/slotghost.png");

    @Inject(method = "extractBackground", at = @At("TAIL"))
    private void renderTimeInABottleSlotBackground(GuiGraphicsExtractor guiGraphics, float partialTick, int mouseX, int mouseY, CallbackInfo ci) {
        InventoryScreen screen = (InventoryScreen) (Object) this;

        for (Slot slot : screen.getMenu().slots) {
            if (slot instanceof TimeInABottleSlot) {
                // slot.x/slot.y puntano al contenuto 16x16
                // La cornice 18x18 parte 1px prima
                int frameX = screen.getGuiLeft() + slot.x - 1;
                int frameY = screen.getGuiTop() + slot.y - 1;

                // Disegna cornice 18x18 (texture deve essere 18x18 stile vanilla)
                guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SLOT_FRAME, frameX, frameY, 0, 0, 18, 18, 18, 18);

                // Ghost icon 16x16 centrata SOLO se lo slot è vuoto
                if (!slot.hasItem()) {
                    int ghostX = screen.getGuiLeft() + slot.x;
                    int ghostY = screen.getGuiTop() + slot.y;
                    guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SLOT_GHOST, ghostX, ghostY, 0, 0, 16, 16, 16, 16);
                }

                break;
            }
        }
    }
}
