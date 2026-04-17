package cc.happyareabean.jumppad.manager;

import cc.happyareabean.jumppad.SimpleJumpPad;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class JumpPadsManagerListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        var player = event.getPlayer();

        SimpleJumpPad.INSTANCE.getJumpPadsManager().removeTargetLocation(player);
    }

}
