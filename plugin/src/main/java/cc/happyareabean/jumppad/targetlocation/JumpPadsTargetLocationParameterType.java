package cc.happyareabean.jumppad.targetlocation;

import org.jetbrains.annotations.NotNull;
import revxrsal.commands.autocomplete.SuggestionProvider;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.exception.CommandErrorException;
import revxrsal.commands.node.ExecutionContext;
import revxrsal.commands.parameter.ParameterType;
import revxrsal.commands.parameter.PrioritySpec;
import revxrsal.commands.stream.MutableStringStream;

public class JumpPadsTargetLocationParameterType implements ParameterType<BukkitCommandActor, JumpPadsTargetLocation> {
    @Override
    public JumpPadsTargetLocation parse(@NotNull MutableStringStream input, @NotNull ExecutionContext<BukkitCommandActor> context) {
        var inputString = input.readString();
        var target = JumpPadsTargetLocation.getByCommandSuggestion(inputString);

        if (target == null) {
            throw new CommandErrorException("Invalid option.");
        }

        return target;
    }

    @Override
    public @NotNull PrioritySpec parsePriority() {
        return PrioritySpec.lowest();
    }

    @Override
    public @NotNull SuggestionProvider<BukkitCommandActor> defaultSuggestions() {
        return context -> JumpPadsTargetLocation.getCommandSuggestions();
    }
}
