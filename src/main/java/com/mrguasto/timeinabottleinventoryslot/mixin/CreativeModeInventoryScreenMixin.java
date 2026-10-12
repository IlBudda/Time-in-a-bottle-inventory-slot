package com.mrguasto.timeinabottleinventoryslot.mixin;

import com.mrguasto.timeinabottleinventoryslot.inventory.TimeInABottleSlot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * Mostra lo slot anche nella scheda "Inventario" del menu creativo.
 * Li' vanilla ricostruisce gli slot a mano (SlotWrapper) e quelli extra finiscono sopra la hotbar.
 */
@SuppressWarnings("removal") // getGuiLeft/getGuiTop: NeoForge-added accessors for the protected leftPos/topPos
@Mixin(CreativeModeInventoryScreen.class)
public class CreativeModeInventoryScreenMixin {

    private static final Identifier SLOT_FRAME = Identifier.fromNamespaceAndPath("timeinabottleinventoryslot", "textures/gui/slotframe.png");
    private static final Identifier SLOT_GHOST = Identifier.fromNamespaceAndPath("timeinabottleinventoryslot", "textures/gui/slotghost.png");

    // Speculare allo scudo (35, 20) rispetto al modello del giocatore.
    private static final int SLOT_X = 127;
    private static final int SLOT_Y = 20;

    @Inject(method = "selectTab", at = @At("TAIL"))
    private void repositionTimeInABottleSlot(CreativeModeTab tab, CallbackInfo ci) {
        if (tab.getType() != CreativeModeTab.Type.INVENTORY) return;

        Slot slot = findWrappedSlot();
        if (slot != null) {
            ((SlotAccessor) slot).timeinabottle$setX(SLOT_X);
            ((SlotAccessor) slot).timeinabottle$setY(SLOT_Y);
        }
    }

    @Inject(method = "extractBackground", at = @At("TAIL"))
    private void renderTimeInABottleSlotBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        CreativeModeInventoryScreen screen = (CreativeModeInventoryScreen) (Object) this;
        if (!screen.isInventoryOpen()) return;

        Slot slot = findWrappedSlot();
        if (slot == null) return;

        graphics.blit(RenderPipelines.GUI_TEXTURED, SLOT_FRAME, screen.getGuiLeft() + slot.x - 1, screen.getGuiTop() + slot.y - 1, 0, 0, 18, 18, 18, 18);
        if (!slot.hasItem()) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, SLOT_GHOST, screen.getGuiLeft() + slot.x, screen.getGuiTop() + slot.y, 0, 0, 16, 16, 16, 16);
        }
    }

    /** Lo SlotWrapper del creativo che corrisponde (stesso indice) al nostro slot nel menu dell'inventario. */
    private Slot findWrappedSlot() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return null;

        CreativeModeInventoryScreen screen = (CreativeModeInventoryScreen) (Object) this;
        List<Slot> inventorySlots = mc.player.inventoryMenu.slots;
        List<Slot> creativeSlots = screen.getMenu().slots;

        for (int i = 0; i < inventorySlots.size() && i < creativeSlots.size(); i++) {
            if (inventorySlots.get(i) instanceof TimeInABottleSlot) {
                return creativeSlots.get(i);
            }
        }
        return null;
    }
}
