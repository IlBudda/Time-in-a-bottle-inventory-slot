package com.mrguasto.timeinabottleinventoryslot.attachment;

import com.mrguasto.timeinabottleinventoryslot.Timeinabottleinventoryslot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import java.util.function.Supplier;

public class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = 
        DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Timeinabottleinventoryslot.MODID);

    public static final Supplier<AttachmentType<ItemStack>> TIME_IN_A_BOTTLE_SLOT = 
        ATTACHMENT_TYPES.register("time_in_a_bottle_slot", () -> 
            AttachmentType.builder(() -> ItemStack.EMPTY)
                .serialize(ItemStack.CODEC)
                .build()
        );
}
