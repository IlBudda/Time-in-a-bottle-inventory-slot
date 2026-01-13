package com.mrguasto.timeinabottleinventoryslot;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import com.mrguasto.timeinabottleinventoryslot.attachment.ModAttachments;
import com.mrguasto.timeinabottleinventoryslot.integration.TimeInABottleIntegration;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Timeinabottleinventoryslot.MODID)
public class Timeinabottleinventoryslot {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "timeinabottleinventoryslot";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public Timeinabottleinventoryslot(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        
        // Register attachments
        ModAttachments.ATTACHMENT_TYPES.register(modEventBus);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // BUILD DEBUG - stampa versione e verifica risorse
        LOGGER.info("=== TIMEINABOTTLEINVENTORYSLOT BUILD DEBUG ===");
        LOGGER.info("BUILD_TIMESTAMP: 2026-01-11T14:30:00Z");
        LOGGER.info("Checking texture resources...");
        checkResource("textures/gui/slotframe.png");
        checkResource("textures/gui/slotghost.png");
        LOGGER.info("=== END BUILD DEBUG ===");

        LOGGER.info("Time in a Bottle Inventory Slot mod loaded!");
        TimeInABottleInventoryMod.init();

        // Register Time in a Bottle integration
        event.enqueueWork(() -> {
            TimeInABottleIntegration.registerCustomSlotSearch();
        });
    }

    private void checkResource(String path) {
        String fullPath = "/assets/" + MODID + "/" + path;
        var stream = getClass().getResourceAsStream(fullPath);
        if (stream != null) {
            try { stream.close(); } catch (Exception ignored) {}
            LOGGER.info("  [OK] Found: {}", fullPath);
        } else {
            LOGGER.error("  [MISSING] Not found: {}", fullPath);
        }
    }
}
