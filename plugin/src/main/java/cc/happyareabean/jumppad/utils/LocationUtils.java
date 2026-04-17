package cc.happyareabean.jumppad.utils;

import org.bukkit.Location;

public class LocationUtils {

    public static String prettyLoc(Location location) {
        return (location.getWorld() != null ? location.getWorld().getName() + ", " : "") + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ();
    }

}
