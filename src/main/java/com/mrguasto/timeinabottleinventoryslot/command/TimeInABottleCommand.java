package com.mrguasto.timeinabottleinventoryslot.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mrguasto.timeinabottleinventoryslot.attachment.ModAttachments;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class TimeInABottleCommand {
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("timeinabottle")
            .then(Commands.literal("add")
                .executes(TimeInABottleCommand::addTimeInABottle))
            .then(Commands.literal("remove")
                .executes(TimeInABottleCommand::removeTimeInABottle))
            .then(Commands.literal("check")
                .executes(TimeInABottleCommand::checkTimeInABottle))
        );
    }
    
    private static int addTimeInABottle(CommandContext<CommandSourceStack> context) {
        if (context.getSource().getEntity() instanceof Player player) {
            // Per il test, aggiungiamo una bottiglia di vetro come placeholder
            ItemStack bottleStack = new ItemStack(Items.GLASS_BOTTLE);
            bottleStack.set(net.minecraft.core.component.DataComponents.CUSTOM_NAME, Component.literal("Time in a Bottle (Test)"));
            
            player.setData(ModAttachments.TIME_IN_A_BOTTLE_SLOT, bottleStack);
            context.getSource().sendSuccess(() -> Component.literal("Added Time in a Bottle to custom slot!"), false);
            return 1;
        }
        return 0;
    }
    
    private static int removeTimeInABottle(CommandContext<CommandSourceStack> context) {
        if (context.getSource().getEntity() instanceof Player player) {
            player.setData(ModAttachments.TIME_IN_A_BOTTLE_SLOT, ItemStack.EMPTY);
            context.getSource().sendSuccess(() -> Component.literal("Removed Time in a Bottle from custom slot!"), false);
            return 1;
        }
        return 0;
    }
    
    private static int checkTimeInABottle(CommandContext<CommandSourceStack> context) {
        if (context.getSource().getEntity() instanceof Player player) {
            ItemStack stack = player.getData(ModAttachments.TIME_IN_A_BOTTLE_SLOT);
            if (stack.isEmpty()) {
                context.getSource().sendSuccess(() -> Component.literal("Custom slot is empty"), false);
            } else {
                context.getSource().sendSuccess(() -> Component.literal("Custom slot contains: " + stack.getDisplayName().getString()), false);
            }
            return 1;
        }
        return 0;
    }
}
