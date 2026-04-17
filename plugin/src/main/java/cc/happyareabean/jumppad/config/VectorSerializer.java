package cc.happyareabean.jumppad.config;

import cc.happyareabean.jumppad.utils.VectorSerialization;
import de.exlll.configlib.Serializer;
import org.bukkit.util.Vector;

public class VectorSerializer implements Serializer<Vector, String> {
    @Override
    public String serialize(Vector element) {
        return VectorSerialization.serialize(element);
    }

    @Override
    public Vector deserialize(String element) {
        return VectorSerialization.deserialize(element);
    }
}
