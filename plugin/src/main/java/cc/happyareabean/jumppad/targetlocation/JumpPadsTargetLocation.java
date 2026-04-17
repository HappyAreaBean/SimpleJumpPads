package cc.happyareabean.jumppad.targetlocation;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
@Getter
public enum JumpPadsTargetLocation {
    PLAYER_LOCATION("Player Location", "playerLocation"),
    TARGET_BLOCK("Target Block", "targetBlock"),
    ;

    private final String readable;
    private final String commandSuggestion;
    private static final Map<String, JumpPadsTargetLocation> BY_COMMAND = Maps.newHashMap();

    static {
        for (JumpPadsTargetLocation value : JumpPadsTargetLocation.values()) {
            BY_COMMAND.put(value.commandSuggestion, value);
        }
    }

    @Nullable
    public static JumpPadsTargetLocation getByCommandSuggestion(String string) {
        return BY_COMMAND.get(string);
    }

    public static Set<String> getCommandSuggestions() {
        return BY_COMMAND.keySet();
    }
}
