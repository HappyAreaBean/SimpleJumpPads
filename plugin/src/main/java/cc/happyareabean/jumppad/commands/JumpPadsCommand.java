package cc.happyareabean.jumppad.commands;

import cc.happyareabean.jumppad.SimpleJumpPad;
import cc.happyareabean.jumppad.commands.exception.InValidOffSetException;
import cc.happyareabean.jumppad.manager.JumpPadsManager;
import cc.happyareabean.jumppad.object.JumpPadsDirection;
import cc.happyareabean.jumppad.object.JumpPadsObject;
import cc.happyareabean.jumppad.suggestionprovider.JumpPadsDirectionSuggestion;
import cc.happyareabean.jumppad.suggestionprovider.ParticleTypeSuggestion;
import cc.happyareabean.jumppad.suggestionprovider.SoundTypeSuggestion;
import cc.happyareabean.jumppad.targetlocation.JumpPadsTargetLocation;
import cc.happyareabean.jumppad.utils.CommandUtils;
import cc.happyareabean.jumppad.utils.LocationUtils;
import cc.happyareabean.jumppad.utils.VectorHelper;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.commons.lang3.BooleanUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.CommandPlaceholder;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.annotation.Range;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import revxrsal.commands.help.Help;

@SuppressWarnings({"unused"})
@Command({"simplejumppads", "sjp"})
@CommandPermission("simplejumppads.admin")
public class JumpPadsCommand {

    private final JumpPadsManager manager = SimpleJumpPad.INSTANCE.getJumpPadsManager();

    @CommandPlaceholder
    public void help(BukkitCommandActor actor, @Range(min= 1) @Default("1") int page, Help.RelatedCommands<BukkitCommandActor> help) {
        CommandUtils.handleHelpMenu(actor, page, help, 8);
    }

    @Subcommand("add")
    public void add(BukkitCommandActor actor, double horizonBoost, double vertBoost, JumpPadsDirection direction, @Optional Boolean top) {
        Player player = actor.requirePlayer();
        Location location = getTargetBlockLocation(player);

        if (location == null) {
            actor.error("Unable to find target location.");
            return;
        }

        JumpPadsObject jumpPads = JumpPadsObject.create()
                .direction(direction.name())
                .material(location.getBlock().getType().name())
                .horizontalBoost(horizonBoost)
                .verticalBoost(vertBoost)
                .location(location)
                .build();

        manager.addJumpPads(jumpPads);
        manager.reload();

        actor.reply(Component.text("JumpPads has been successfully added!", NamedTextColor.GREEN)
                .appendSpace()
                .append(Component.text("[%s]".formatted(manager.getTargetLocation(player).getReadable()), NamedTextColor.GRAY)));
        info(actor, false);
    }

    @Subcommand("remove")
    public void remove(BukkitCommandActor actor) {
        Player player = actor.requirePlayer();

        Location location = getTargetBlockLocation(player);

        if (!manager.isValidLocation(location)) {
            actor.error("Invalid location!");
            return;
        }

        manager.removeJumpPads(location);

        actor.reply("&aJumpPads has been successfully removed! &7(%s)".formatted(LocationUtils.prettyLoc(location)));
    }

    @Subcommand("edit general material")
    public void editMaterial(BukkitCommandActor actor) {
        Player player = actor.requirePlayer();

        Location location = getTargetBlockLocation(player);

        if (!manager.isValidLocation(location)) {
            actor.error("Invalid location!");
            return;
        }

        Material material = player.getInventory().getItemInMainHand().getType();

        manager.getJumpPadsObject(location).setMaterial(material.name());
        manager.reload();

        actor.reply("&aJumpPads material has been set to &6%s&a!".formatted(material.name()));
    }

    @Subcommand("edit general horizontalBoost")
    public void editHorizontalBoost(BukkitCommandActor actor, double value) {
        Player player = actor.requirePlayer();

        Location location = getTargetBlockLocation(player);

        if (!manager.isValidLocation(location)) {
            actor.error("Invalid location!");
            return;
        }

        manager.getJumpPadsObject(location).setHorizontalBoost(value);
        manager.reload();

        actor.reply("&aJumpPads horizontal boost has been set to &6%s&a!".formatted(value));
    }

    @Subcommand("edit general verticalBoost")
    public void editVerticalBoost(BukkitCommandActor actor, double value) {
        Player player = actor.requirePlayer();

        Location location = getTargetBlockLocation(player);

        if (!manager.isValidLocation(location)) {
            actor.error("Invalid location!");
            return;
        }

        manager.getJumpPadsObject(location).setVerticalBoost(value);
        manager.reload();

        actor.reply("&aJumpPads vertical boost has been set to &6%s&a!".formatted(value));
    }

    @Subcommand("edit general direction")
    public void editDirection(BukkitCommandActor actor, @JumpPadsDirectionSuggestion String value) {
        Player player = actor.requirePlayer();

        Location location = getTargetBlockLocation(player);

        if (!manager.isValidLocation(location)) {
            actor.error("Invalid location!");
            return;
        }

        manager.getJumpPadsObject(location).setDirection(value);
        manager.reload();

        actor.reply("&aJumpPads direction has been set to &6%s&a!".formatted(value));
    }

    @Subcommand("edit sound type")
    public void editSoundType(BukkitCommandActor actor, @SoundTypeSuggestion String value) {
        Player player = actor.requirePlayer();

        Location location = getTargetBlockLocation(player);

        if (!manager.isValidLocation(location)) {
            actor.error("Invalid location!");
            return;
        }

        manager.getJumpPadsObject(location).getSound().setType(value);
        manager.reload();

        actor.reply("&aJumpPads sound type has been set to &6%s&a!".formatted(value));
    }

    @Subcommand("edit sound volume")
    public void editSoundType(BukkitCommandActor actor, float value) {
        Player player = actor.requirePlayer();

        Location location = getTargetBlockLocation(player);

        if (!manager.isValidLocation(location)) {
            actor.error("Invalid location!");
            return;
        }

        manager.getJumpPadsObject(location).getSound().setVolume(value);
        manager.reload();

        actor.reply("&aJumpPads sound volume has been set to &6%s&a!".formatted(value));
    }

    @Subcommand("edit sound pitch")
    public void editSoundPitch(BukkitCommandActor actor, float value) {
        Player player = actor.requirePlayer();

        Location location = getTargetBlockLocation(player);

        if (!manager.isValidLocation(location)) {
            actor.error("Invalid location!");
            return;
        }

        manager.getJumpPadsObject(location).getSound().setPitch(value);
        manager.reload();

        actor.reply("&aJumpPads sound pitch has been set to &6%s&a!".formatted(value));
    }

    @Subcommand("edit sound enable")
    public void editSoundEnabled(BukkitCommandActor actor, boolean value) {
        Player player = actor.requirePlayer();

        Location location = getTargetBlockLocation(player);

        if (!manager.isValidLocation(location)) {
            actor.error("Invalid location!");
            return;
        }

        JumpPadsObject.Sound sound = manager.getJumpPadsObject(location).getSound();

        boolean old = sound.isEnabled();
        sound.setEnabled(!old);
        manager.reload();

        actor.reply("&aJumpPads sounds has been %s&a.".formatted(
                BooleanUtils.toString(!old, "&6enabled", "&cdisabled")));
    }

    @Subcommand("edit sound source")
    public void editSoundSource(BukkitCommandActor actor, Sound.Source source) {
        Player player = actor.requirePlayer();

        Location location = getTargetBlockLocation(player);

        if (!manager.isValidLocation(location)) {
            actor.error("Invalid location!");
            return;
        }

        manager.getJumpPadsObject(location).getSound().setSource(source);
        manager.reload();

        actor.reply("&aJumpPads sound source has been set to &6%s&a!".formatted(source.name()));
    }

    @Subcommand("edit sound followPlayer")
    public void editSoundFollowPlayer(BukkitCommandActor actor, @Optional Boolean value) {
        Player player = actor.requirePlayer();
        Location location = getTargetBlockLocation(player);

        if (!manager.isValidLocation(location)) {
            actor.error("Invalid location!");
            return;
        }

        var object = manager.getJumpPadsObject(location);
        var followPlayer = object.getSound().isFollowPlayer();
        var valueResult = value == null ? !followPlayer : value;

        object.getSound().setFollowPlayer(valueResult);
        manager.reload();

        if (value == null) {
            actor.reply("&aJumpPads sound follow player has been toggled to &6%s&a!".formatted(valueResult));
            return;
        }

        actor.reply("&aJumpPads sound follow player has been set to &6%s&a!".formatted(valueResult));
    }

    @Subcommand("edit particle enable")
    public void editParticleEnabled(BukkitCommandActor actor, boolean value) {
        Player player = actor.requirePlayer();

        Location location = getTargetBlockLocation(player);

        if (!manager.isValidLocation(location)) {
            actor.error("Invalid location!");
            return;
        }

        JumpPadsObject.Particle particle = manager.getJumpPadsObject(location).getParticle();

        boolean old = particle.isEnabled();
        particle.setEnabled(!old);
        manager.reload();

        actor.reply("&aJumpPads particle has been %s&a.".formatted(
                BooleanUtils.toString(!old, "&6enabled", "&cdisabled")));
    }

    @Subcommand("edit particle type")
    public void editParticleType(BukkitCommandActor actor, @ParticleTypeSuggestion String value) {
        Player player = actor.requirePlayer();

        Location location = getTargetBlockLocation(player);

        if (!manager.isValidLocation(location)) {
            actor.error("Invalid location!");
            return;
        }

        manager.getJumpPadsObject(location).getParticle().setType(value);
        manager.reload();

        actor.reply("&aJumpPads particle type has been set to &6%s&a!".formatted(value));
    }

    @Subcommand("edit particle amount")
    public void editParticleAmount(BukkitCommandActor actor, int value) {
        Player player = actor.requirePlayer();

        Location location = getTargetBlockLocation(player);

        if (!manager.isValidLocation(location)) {
            actor.error("Invalid location!");
            return;
        }

        manager.getJumpPadsObject(location).getParticle().setCount(value);
        manager.reload();

        actor.reply("&aJumpPads particle amount has been set to &6%s&a!".formatted(value));
    }

    @Subcommand("edit particle speed")
    public void editParticleSpeed(BukkitCommandActor actor, double value) {
        Player player = actor.requirePlayer();

        Location location = getTargetBlockLocation(player);

        if (!manager.isValidLocation(location)) {
            actor.error("Invalid location!");
            return;
        }

        manager.getJumpPadsObject(location).getParticle().setSpeed(value);
        manager.reload();

        actor.reply("&aJumpPads particle speed has been set to &6%s&a!".formatted(value));
    }

    @Subcommand("edit particle offset")
    public void editParticleOffSet(BukkitCommandActor actor, String value) {
        Player player = actor.requirePlayer();

        Location location = getTargetBlockLocation(player);

        if (!manager.isValidLocation(location)) {
            actor.error("Invalid location!");
            return;
        }

        String[] split = value.split(",");
        if (split.length < 3) {
            actor.error("Invalid particle offset format! Valid format: x,y,z");
            return;
        }

        try {
            double x = Double.parseDouble(split[0]);
            double y = Double.parseDouble(split[1]);
            double z = Double.parseDouble(split[2]);

            Vector vector = new Vector(x, y, z);
            manager.getJumpPadsObject(location).getParticle().setOffSet(vector);
            manager.reload();

            actor.reply("&aJumpPads particle offset has been set to &6%s&a!".formatted(VectorHelper.prettyVector(vector)));
        } catch (NumberFormatException | NullPointerException ex) {
            throw new InValidOffSetException(value);
        }
    }

    @Subcommand("edit particle offsetX")
    public void editParticleOffSetX(BukkitCommandActor actor, double value) {
        Player player = actor.requirePlayer();

        Location location = getTargetBlockLocation(player);

        if (!manager.isValidLocation(location)) {
            actor.error("Invalid location!");
            return;
        }

        manager.getJumpPadsObject(location).getParticle().getOffSet().setX(value);
        manager.reload();

        actor.reply("&aJumpPads particle offset X has been set to &6%s&a!".formatted(value));
    }

    @Subcommand("edit particle offsetY")
    public void editParticleOffSetY(BukkitCommandActor actor, double value) {
        Player player = actor.requirePlayer();

        Location location = getTargetBlockLocation(player);

        if (!manager.isValidLocation(location)) {
            actor.error("Invalid location!");
            return;
        }

        manager.getJumpPadsObject(location).getParticle().getOffSet().setY(value);
        manager.reload();

        actor.reply("&aJumpPads particle offset Y has been set to &6%s&a!".formatted(value));
    }

    @Subcommand("edit particle offsetZ")
    public void editParticleOffSetZ(BukkitCommandActor actor, double value) {
        Player player = actor.requirePlayer();

        Location location = getTargetBlockLocation(player);

        if (!manager.isValidLocation(location)) {
            actor.error("Invalid location!");
            return;
        }

        manager.getJumpPadsObject(location).getParticle().getOffSet().setZ(value);
        manager.reload();

        actor.reply("&aJumpPads particle offset Z has been set to &6%s&a!".formatted(value));
    }

    @Subcommand("edit particle radius")
    public void editParticleRadius(BukkitCommandActor actor, int value) {
        Player player = actor.requirePlayer();

        Location location = getTargetBlockLocation(player);

        if (!manager.isValidLocation(location)) {
            actor.error("Invalid location!");
            return;
        }

        manager.getJumpPadsObject(location).getParticle().setRadius(value);
        manager.reload();

        actor.reply("&aJumpPads particle radius has been updated to &6%s&a!".formatted(value));
    }

    @Subcommand("edit sound byDistance")
    public void editParticleByDistance(BukkitCommandActor actor, @Optional Boolean value) {
        Player player = actor.requirePlayer();
        Location location = getTargetBlockLocation(player);

        if (!manager.isValidLocation(location)) {
            actor.error("Invalid location!");
            return;
        }

        var object = manager.getJumpPadsObject(location);
        var byDistance = object.getParticle().isByDistance();
        var valueResult = value == null ? !byDistance : value;

        object.getParticle().setByDistance(valueResult);
        manager.reload();

        if (value == null) {
            actor.reply("&aJumpPads particle by distance has been toggled to &6%s&a!".formatted(valueResult));
            return;
        }

        actor.reply("&aJumpPads particle by distance has been set to &6%s&a!".formatted(valueResult));
    }

    @Subcommand("reload")
    public void reload(BukkitCommandActor actor) {

        manager.reload();

        actor.reply("&aJumpPads has been successfully reloaded.");
    }

    @Subcommand("info")
    public void info(BukkitCommandActor actor, @Default("false") boolean detailed) {
        Player player = actor.requirePlayer();

        Location location = getTargetBlockLocation(player);

        if (!manager.isValidLocation(location)) {
            actor.error("Invalid location!");
            return;
        }

        JumpPadsObject object = manager.getJumpPadsObject(location);
        JumpPadsObject.Sound sound = object.getSound();
        JumpPadsObject.Particle particle = object.getParticle();

        actor.reply("&8---------------------------------");
        actor.reply("&6&lJumpPads Info");
        actor.reply("&f  Location: &e" + LocationUtils.prettyLoc(location));
        actor.reply("&f  Material: &e" + object.getMaterial());
        actor.reply("&f  Direction: &e" + object.getDirection());
        actor.reply("&f  Horizontal Boost: &e" + object.getHorizontalBoost());
        actor.reply("&f  Vertical Boost: &e" + object.getVerticalBoost());
        if (detailed) {
            actor.reply("&6&lSounds");
            actor.reply("&f  Enabled: " + BooleanUtils.toString(sound.isEnabled(), "&a", "&c") + sound.isEnabled());
            actor.reply("&f  Type: &e" + sound.getType());
            actor.reply("&f  Volume: &e" + sound.getVolume());
            actor.reply("&f  Pitch: &e" + sound.getPitch());
            actor.reply("&6&lParticle");
            actor.reply("&f  Enabled: &e" + BooleanUtils.toString(particle.isEnabled(), "&a", "&c") + particle.isEnabled());
            actor.reply("&f  Type: &e" + particle.getType());
            actor.reply("&f  Count: &e" + particle.getCount());
            actor.reply("&f  Speed: &e" + particle.getSpeed());
            actor.reply("&f  OffSet: &e" + VectorHelper.prettyVector(particle.getOffSet()));
        }
        actor.reply("&8---------------------------------");
    }

    @Subcommand("editmode")
    public void editMode(BukkitCommandActor actor, @Optional Player target) {
        Player player = target != null ? target : actor.requirePlayer();

        // TODO
//        PlayerProfile profile = PlayerManager.loadProfile(player.getUniqueId());
//        boolean old = profile.isJumpPadsEdit();
//        profile.setJumpPadsEdit(!old);
//
//        actor.reply("&aJumpPads edit mode has been %s&a.".formatted(
//                BooleanUtils.toString(!old, "&6enabled", "&cdisabled")));
    }

    @Subcommand("targetmode")
    public void targetMode(BukkitCommandActor actor, @Optional JumpPadsTargetLocation targetLocation) {
        var player = actor.requirePlayer();

        if (targetLocation == null) {
            var get = manager.getTargetLocation(player);
            actor.reply("&aCurrent target mode: &6" + get.getReadable());

            return;
        }

        manager.setTargetLocation(player, targetLocation);
        actor.reply("&aTarget mode has been updated.");
    }

    private @Nullable Location getTargetBlockLocation(Player player) {
        switch (manager.getTargetLocation(player)) {
            case TARGET_BLOCK -> {
                Block block = player.getTargetBlockExact(5);

                return block != null ? block.getLocation().clone() : null;
            }
            case PLAYER_LOCATION -> {
                return player.getLocation().getBlock().getLocation().clone();
            }
        }
        return null;
    }

}
