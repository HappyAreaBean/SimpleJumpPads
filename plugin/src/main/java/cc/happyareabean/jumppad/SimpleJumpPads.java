package cc.happyareabean.jumppad;

import cc.happyareabean.jumppad.commands.JumpPadsCommand;
import cc.happyareabean.jumppad.commands.JumpPadsVisitor;
import cc.happyareabean.jumppad.manager.JumpPadsManager;
import lombok.Getter;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SingleLineChart;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.Lamp;
import revxrsal.commands.bukkit.BukkitLamp;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;

@Getter
public final class SimpleJumpPads extends JavaPlugin {

    public static SimpleJumpPads INSTANCE;

    private Lamp<BukkitCommandActor> commandHandler;

    private JumpPadsManager jumpPadsManager;

    @Override
    public void onEnable() {
        INSTANCE = this;

        jumpPadsManager = new JumpPadsManager(this);
        jumpPadsManager.init();

        registerCommands();
        registerMetrics(31959);
    }

    private void registerCommands() {
        var builder = BukkitLamp.builder(this)
                .accept(new JumpPadsVisitor());

        commandHandler = builder.build();
        commandHandler.register(new JumpPadsCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void registerMetrics(int pluginId) {
        Metrics metrics = new Metrics(this, pluginId);

        metrics.addCustomChart(new SingleLineChart("total_jumppads_created", () -> jumpPadsManager.getJumpPadConfig().getJumpPads().size()));
    }


}
