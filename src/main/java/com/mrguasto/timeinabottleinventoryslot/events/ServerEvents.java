package com.mrguasto.timeinabottleinventoryslot.events;

import com.mrguasto.timeinabottleinventoryslot.Timeinabottleinventoryslot;
import com.mrguasto.timeinabottleinventoryslot.command.TimeInABottleCommand;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = Timeinabottleinventoryslot.MODID)
public class ServerEvents {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        TimeInABottleCommand.register(event.getDispatcher());
        Timeinabottleinventoryslot.LOGGER.info("Registered Time in a Bottle commands");
    }
}
