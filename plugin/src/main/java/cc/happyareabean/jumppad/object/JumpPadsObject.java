package cc.happyareabean.jumppad.object;

import cc.happyareabean.jumppad.config.LocationSerializer;
import cc.happyareabean.jumppad.config.VectorSerializer;
import de.exlll.configlib.Configuration;
import de.exlll.configlib.SerializeWith;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;

@Configuration
@Getter
@Setter
@Builder(builderMethodName = "create")
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class JumpPadsObject {

    @Builder.Default private String material = "SLIME_BLOCK";
    @Builder.Default private double horizontalBoost = 5.0;
    @Builder.Default private double verticalBoost = 2.0;
    @Builder.Default private String direction = JumpPadsDirection.PLAYER_DIRECTION.name();

    @Builder.Default private Particle particle = new Particle();

    @Builder @Configuration @Data @AllArgsConstructor @NoArgsConstructor
    @ToString
    public static class Particle {

        @Builder.Default private boolean enabled = false;
        @Builder.Default private String type = "";

        @SerializeWith(serializer = VectorSerializer.class)
        @Builder.Default private Vector offSet = new Vector(1.0, 0.25, 1.0);

        @Builder.Default private double speed = 0d;
        @Builder.Default private int count = 10;

        @Builder.Default private int radius = 32;
        @Builder.Default private boolean byDistance = true;

    }

    @Builder.Default private Sound sound = new Sound();

    @Builder @Configuration @Data @AllArgsConstructor @NoArgsConstructor
    @ToString
    public static class Sound {

        @Builder.Default private boolean enabled = false;
        @Builder.Default private String type = "";
        @Builder.Default private net.kyori.adventure.sound.Sound.Source source = net.kyori.adventure.sound.Sound.Source.BLOCK;
        @Builder.Default private float volume = 1.0f;
        @Builder.Default private float pitch = 1.0f;
        @Builder.Default private boolean followPlayer = true;

    }

    @SerializeWith(serializer = LocationSerializer.class)
    @Builder.Default private Location location = new Location(Bukkit.getWorlds().getFirst(), 0, 0, 0, 0, 0);
}
