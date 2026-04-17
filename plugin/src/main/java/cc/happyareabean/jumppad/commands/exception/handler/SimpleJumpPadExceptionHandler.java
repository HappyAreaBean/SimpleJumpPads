package cc.happyareabean.jumppad.commands.exception.handler;

import cc.happyareabean.jumppad.commands.exception.InValidOffSetException;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.bukkit.exception.BukkitExceptionHandler;

public class SimpleJumpPadExceptionHandler extends BukkitExceptionHandler {

    @HandleException
    public void onInvalidOffSetException(InValidOffSetException e, BukkitCommandActor actor) {
        actor.error("The offset '%s' is not a valid integer."
                .formatted(e.getMessage()));
    }

}
