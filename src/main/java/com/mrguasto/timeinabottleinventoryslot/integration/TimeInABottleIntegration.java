package com.mrguasto.timeinabottleinventoryslot.integration;

import com.mrguasto.timeinabottleinventoryslot.attachment.ModAttachments;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Optional;

public class TimeInABottleIntegration {
    
    private static final String TIAB_MOD_ID = "tiab";
    private static boolean integrationAttempted = false;
    
    public static void registerCustomSlotSearch() {
        if (integrationAttempted) return;
        integrationAttempted = true;
        
        if (!ModList.get().isLoaded(TIAB_MOD_ID)) {
            System.out.println("[TimeInABottleInventorySlot] Time in a Bottle mod not found, skipping integration");
            return;
        }
        
        try {
            // Get the API class
            Class<?> apiClass = Class.forName("org.mangorage.tiab.common.api.ICommonTimeInABottleAPI");
            Class<?> searchInterface = Class.forName("org.mangorage.tiab.common.api.ITiabItemSearch");
            
            // Get the API instance
            Object apiSupplier = apiClass.getField("COMMON_API").get(null);
            Object api = ((java.util.function.Supplier<?>) apiSupplier).get();
            
            // Create our search handler using a proxy
            Object searchHandler = Proxy.newProxyInstance(
                searchInterface.getClassLoader(),
                new Class<?>[]{searchInterface},
                new CustomSlotSearchHandler()
            );
            
            // Register our search handler
            Method registerMethod = apiClass.getMethod("registerItemSearch", searchInterface);
            registerMethod.invoke(api, searchHandler);
            
            System.out.println("[TimeInABottleInventorySlot] Successfully registered custom slot search handler");
            
        } catch (Exception e) {
            System.err.println("[TimeInABottleInventorySlot] Failed to register Time in a Bottle integration: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static class CustomSlotSearchHandler implements InvocationHandler {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Class<?> returnType = method.getReturnType();

            // Metodi di java.lang.Object chiamati sul proxy
            switch (method.getName()) {
                case "hashCode": return System.identityHashCode(proxy);
                case "equals": return args != null && args.length == 1 && proxy == args[0];
                case "toString": return "TimeInABottleInventorySlot search handler";
                default: break;
            }

            if (returnType == ItemStack.class || returnType == Object.class) {
                ItemStack found = ItemStack.EMPTY;

                if ("findItem".equals(method.getName()) && args != null) {
                    for (Object arg : args) {
                        if (arg instanceof Player player) {
                            // Controlla prima il nostro slot personalizzato
                            ItemStack customSlotItem = player.getData(ModAttachments.TIMEINABOTTLESLOT);
                            if (!customSlotItem.isEmpty() && isTimeInABottleItem(customSlotItem)) {
                                found = customSlotItem;
                            }
                            break;
                        }
                    }
                }

                // Mai null: Time in a Bottle si aspetta uno ItemStack (vuoto se non trovato)
                return found;
            }

            if (returnType == Optional.class) {
                return Optional.empty();
            }
            if (returnType == boolean.class) {
                return false;
            }
            if (returnType == int.class) {
                return 0;
            }
            return null;
        }

        private boolean isTimeInABottleItem(ItemStack stack) {
            if (stack.isEmpty()) return false;
            
            String itemId = stack.getItem().toString().toLowerCase();
            String descriptionId = stack.getItem().getDescriptionId().toLowerCase();
            
            return itemId.contains("timeinabottle") || 
                   itemId.contains("time_in_a_bottle") || 
                   descriptionId.contains("timeinabottle") ||
                   descriptionId.contains("time_in_a_bottle");
        }
    }
}
