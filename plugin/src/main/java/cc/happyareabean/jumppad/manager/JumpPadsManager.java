package cc.happyareabean.jumppad.manager;

import cc.happyareabean.jumppad.SimpleJumpPads;
import cc.happyareabean.jumppad.config.JumpPadsData;
import cc.happyareabean.jumppad.listener.JumpPadsListener;
import cc.happyareabean.jumppad.object.JumpPadsObject;
import cc.happyareabean.jumppad.targetlocation.JumpPadsTargetLocation;
import de.exlll.configlib.YamlConfigurationStore;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JumpPadsManager {

    private final SimpleJumpPads plugin;
    @Getter
    private YamlConfigurationStore<JumpPadsData> store;
    private JumpPadsData config;
    private final Path configPath = SimpleJumpPads.INSTANCE.getDataFolder().toPath().resolve("jumppads.yml");

    @Getter
    private final List<Location> jumpPadsLocations = new ArrayList<>();

    @Getter
    private final Map<UUID, JumpPadsTargetLocation> targetLocationMap = new HashMap<>();

    public JumpPadsManager(SimpleJumpPads plugin) {
        this.plugin = plugin;
    }

    public void init() {
        store = new YamlConfigurationStore<>(JumpPadsData.class, JumpPadsData.PROPERTIES);
        updateConfig();

        loadJumpPadsLocations();
        loadJumpPadsParticles();

        plugin.getServer().getPluginManager().registerEvents(new JumpPadsListener(this), plugin);
        plugin.getServer().getPluginManager().registerEvents(new JumpPadsManagerListener(), plugin);
    }

    private void loadJumpPadsParticles() {

        config.getJumpPads().forEach(o -> {
            JumpPadsObject.Particle particle = o.getParticle();

            if (!particle.isEnabled()) return;
            if (particle.getType().isEmpty()) return;

            new BukkitRunnable() {
                @Override
                public void run() {

                    Location location = o.getLocation().clone();
                    JumpPadsObject object = getJumpPadsObject(location);

                    if (!isValidLocation(location)) {
                        cancel();
                        return;
                    }

                    JumpPadsObject.Particle particle = object.getParticle();

                    new com.destroystokyo.paper.ParticleBuilder(Particle.valueOf(particle.getType()))
                            .count(particle.getCount())
                            .offset(particle.getOffSet().getX(), particle.getOffSet().getY(), particle.getOffSet().getZ())
                            .location(object.getLocation())
                            .receivers(particle.getRadius(), particle.isByDistance())
                            .extra(particle.getSpeed())
                            .spawn();

                }
            }.runTaskTimer(plugin, 0, 20);

        });

    }

    public boolean isValidLocation(Location location) {
        if (location == null) return false;

        return jumpPadsLocations.contains(location);
    }

    public boolean isValidBlock(Location location) {
        JumpPadsObject jumpPads = getJumpPadsObject(location);
        if (jumpPads == null) return false;

        Material material = Material.matchMaterial(jumpPads.getMaterial());
        if (material == null) return false;

        return location.getBlock().getType() == material;
    }

    public JumpPadsObject getJumpPadsObject(Location location) {
        return config.getJumpPads().stream().filter(j -> j.getLocation().equals(location.getBlock().getLocation())).findFirst().orElse(null);
    }

    public boolean addJumpPads(JumpPadsObject jumpPadsObject) {
        return config.getJumpPads().add(jumpPadsObject);
    }

    public void removeJumpPads(Location location) {
        JumpPadsObject object = getJumpPadsObject(location);

        if (object == null) return;

        config.getJumpPads().remove(object);

        reload();
    }

    public void setTargetLocation(Player player, JumpPadsTargetLocation jumpPadsTargetLocation) {
        targetLocationMap.put(player.getUniqueId(), jumpPadsTargetLocation);
    }

    public JumpPadsTargetLocation getTargetLocation(Player player) {
        return targetLocationMap.getOrDefault(player.getUniqueId(), JumpPadsTargetLocation.TARGET_BLOCK);
    }

    public void removeTargetLocation(Player player) {
        targetLocationMap.remove(player.getUniqueId());
    }

    public boolean isSetTargetLocation(Player player) {
        return targetLocationMap.containsKey(player.getUniqueId());
    }

    public void save() {
        store.save(config, configPath);
    }

    public void load() {
        config = store.load(configPath);
    }

    public void reload() {
        Bukkit.getScheduler().cancelTasks(plugin);
        save();
        load();
        loadJumpPadsLocations();
        loadJumpPadsParticles();
    }

    private void loadJumpPadsLocations() {
        jumpPadsLocations.clear();
        config.getJumpPads().forEach(j -> {
            jumpPadsLocations.add(j.getLocation());
        });
    }

    public void updateConfig() {
        config = store.update(configPath);
    }

    public JumpPadsData getJumpPadConfig() {
        return config;
    }
}
