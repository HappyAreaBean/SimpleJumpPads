package cc.happyareabean.jumppad.listener;

import cc.happyareabean.jumppad.SimpleJumpPad;
import cc.happyareabean.jumppad.manager.JumpPadsManager;
import cc.happyareabean.jumppad.object.JumpPadsObject;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Location;
import org.bukkit.Registry;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

@RequiredArgsConstructor
public class JumpPadsListener implements Listener {

    private final JumpPadsManager manager;

    @SuppressWarnings("PatternValidation")
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Block block = player.getLocation().getBlock();
        Location location = block.getLocation();

        if (!e.hasChangedBlock()) return;
        if (!manager.isValidLocation(location)) return;
        if (!manager.isValidBlock(location)) return;
        //if (PlayerManager.loadProfile(player.getUniqueId()).isJumpPadsEdit()) return;

        JumpPadsObject jumpPads = manager.getJumpPadsObject(location);

        double horizontalBoost = jumpPads.getHorizontalBoost();
        double verticalBoost = jumpPads.getVerticalBoost();
        String direction = jumpPads.getDirection();

        Vector boostVector = getBoostVector(player, direction, horizontalBoost, verticalBoost);
        player.setVelocity(boostVector);

        JumpPadsObject.Sound jumpPadsSound = jumpPads.getSound();
        if (jumpPadsSound.isEnabled() && !jumpPadsSound.getType().isEmpty()) {
            org.bukkit.Sound sound = Registry.SOUND_EVENT.get(Key.key(jumpPadsSound.getType()));
            if (sound != null) {
                var bSound = Sound.sound(sound, jumpPadsSound.getSource(), jumpPadsSound.getVolume(), jumpPadsSound.getPitch());

                if (jumpPadsSound.isFollowPlayer()) {
                    player.playSound(bSound, Sound.Emitter.self());
                } else {
                    player.playSound(bSound);
                }
            }
        }
    }

    private Vector getBoostVector(Player player, String direction, double horizontalBoost, double verticalBoost) {
        switch (direction.toUpperCase()) {
            case "NORTH":
                return new Vector(0, verticalBoost, -horizontalBoost);
            case "SOUTH":
                return new Vector(0, verticalBoost, horizontalBoost);
            case "EAST":
                return new Vector(horizontalBoost, verticalBoost, 0);
            case "WEST":
                return new Vector(-horizontalBoost, verticalBoost, 0);
            case "NORTH_EAST":
                return new Vector(horizontalBoost, verticalBoost, -horizontalBoost).normalize().multiply(horizontalBoost);
            case "NORTH_WEST":
                return new Vector(-horizontalBoost, verticalBoost, -horizontalBoost).normalize().multiply(horizontalBoost);
            case "SOUTH_EAST":
                return new Vector(horizontalBoost, verticalBoost, horizontalBoost).normalize().multiply(horizontalBoost);
            case "SOUTH_WEST":
                return new Vector(-horizontalBoost, verticalBoost, horizontalBoost).normalize().multiply(horizontalBoost);
            case "PLAYER_DIRECTION":
                return player.getLocation().getDirection().setY(verticalBoost).normalize().multiply(horizontalBoost);
            case "UP":
                return new Vector(0, verticalBoost, 0);
            case "DOWN":
                return new Vector(0, -verticalBoost, 0);
            default:
                try {
                    double angle = Double.parseDouble(direction);
                    double rad = Math.toRadians(angle);
                    return new Vector(Math.cos(rad) * horizontalBoost, verticalBoost, Math.sin(rad) * horizontalBoost);
                } catch (NumberFormatException e) {
                    SimpleJumpPad.INSTANCE.getSLF4JLogger().warn("Invalid direction in config: {}", direction);
                    return new Vector(0, 0, 0);
                }
        }
    }

}
