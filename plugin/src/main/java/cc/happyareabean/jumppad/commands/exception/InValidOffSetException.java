package cc.happyareabean.jumppad.commands.exception;

import revxrsal.commands.exception.ThrowableFromCommand;

@ThrowableFromCommand
public class InValidOffSetException extends RuntimeException {

    public InValidOffSetException(String message) {
        super(message);
    }

}
