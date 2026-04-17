package cc.happyareabean.jumppad.utils;

import org.bukkit.util.Vector;

public class VectorSerialization {

    public static Vector deserialize(String input) {
        if (input == null || input.equals("null")) {
            return null;
        }
        String[] attributes = input.split(":");

        Double x = null;
        Double y = null;
        Double z = null;

        for (String attribute : attributes) {
            String[] split = attribute.split(";");

            if (split[0].equalsIgnoreCase("#x")) {
                x = Double.parseDouble(split[1]);
                continue;
            }

            if (split[0].equalsIgnoreCase("#y")) {
                y = Double.parseDouble(split[1]);
                continue;
            }

            if (split[0].equalsIgnoreCase("#z")) {
                z = Double.parseDouble(split[1]);
            }
        }

        if (x == null || y == null || z == null) {
            return null;
        }

        return new Vector(x, y, z);
    }

    public static String serialize(Vector location) {
        if (location == null) {
            return "null";
        }
        return ":#x;" + location.getX() +
                ":#y;" + location.getY() +
                ":#z;" + location.getZ();
    }

}
