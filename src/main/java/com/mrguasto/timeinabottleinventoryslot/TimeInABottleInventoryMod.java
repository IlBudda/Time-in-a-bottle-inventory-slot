package com.mrguasto.timeinabottleinventoryslot;

public class TimeInABottleInventoryMod {

    public static void init() {
        Timeinabottleinventoryslot.LOGGER.info("Time in a Bottle Inventory Slot mod initialized!");
    }

    public static boolean isTimeInABottle(String itemName) {
        if (itemName == null || itemName.isEmpty()) return false;
        String lowerName = itemName.toLowerCase();
        return lowerName.contains("time") && lowerName.contains("bottle");
    }
}
