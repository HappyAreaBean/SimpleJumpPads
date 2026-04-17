package cc.happyareabean.jumppad.commands.exception;

import revxrsal.commands.exception.ThrowableFromCommand;

@ThrowableFromCommand
public class UnknownCommandException extends RuntimeException {

    public UnknownCommandException(String message) {
        super(message);
    }

}
