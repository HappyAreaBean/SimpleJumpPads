package cc.happyareabean.jumppad.config;

import cc.happyareabean.jumppad.utils.LocationSerialization;
import de.exlll.configlib.Serializer;
import org.bukkit.Location;

public class LocationSerializer implements Serializer<Location, String> {
    @Override
    public String serialize(Location element) {
        return LocationSerialization.serialize(element);
    }

    @Override
    public Location deserialize(String element) {
        return LocationSerialization.deserialize(element);
    }
}
