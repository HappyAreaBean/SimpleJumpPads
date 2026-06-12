package cc.happyareabean.jumppad.utils;

import cc.happyareabean.jumppad.SimpleJumpPad;
import com.google.common.base.Strings;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.Nullable;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.command.ExecutableCommand;
import revxrsal.commands.help.Help;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static cc.happyareabean.jumppad.utils.ColorUtils.MM;
import static net.kyori.adventure.text.Component.space;
import static net.kyori.adventure.text.Component.text;

public class CommandUtils {

    public static void handleHelpMenu(BukkitCommandActor actor,
                                      int page,
                                      Help.CommandList<BukkitCommandActor> commands,
                                      int elementsPerPage,
                                      @Nullable String filterFor) {
        List<ExecutableCommand<BukkitCommandActor>> list = new ArrayList<>();

        for (ExecutableCommand<BukkitCommandActor> command : commands.all()) {
            if (filterFor != null) {
                var usage = command.usage();
                if (!usage.startsWith(filterFor)) continue;
            }
            if (!command.permission().isExecutableBy(actor)) continue;
            list.add(command);
        }

        var componentList = new ArrayList<Component>();
        var commandList = Help.paginate(list, page, elementsPerPage);
        var numberOfPages = Help.numberOfPages(list.size(), elementsPerPage);

        var header = processHeader(SimpleJumpPad.INSTANCE.getPluginMeta().getName(), "=", 48);
        var footer = processFooter("=", 48);

        componentList.add(header);
        componentList.add(Component.empty());
        commandList.forEach(command -> {
            var usage = command.usage();
            var description = command.description();
            var component = text()
                    .appendSpace()
                    .append(text("●", NamedTextColor.DARK_GRAY))
                    .appendSpace()
                    .append(text("/", NamedTextColor.YELLOW))
                    .append(text(usage, NamedTextColor.YELLOW)
                            .replaceText(builder -> {
                                builder.match(Pattern.compile("[<>\\[\\]]"))
                                        .replacement((matchResult, builder1) -> builder1.color(NamedTextColor.GREEN));
                            })
                            .clickEvent(ClickEvent.suggestCommand("/" + command.path()))
                            .hoverEvent(HoverEvent.showText(text(description != null ? description : "This command has no description.", NamedTextColor.GRAY))));
            componentList.add(component.build());
        });
        componentList.add(Component.empty());
        if (numberOfPages > 1) {
            componentList.add(paginateNavigation(page, numberOfPages, "/simplejumppad %s"));
        } else {
            componentList.add(footer);
        }
        actor.reply(Component.join(JoinConfiguration.newlines(), componentList));
    }

    public static void handleHelpMenu(BukkitCommandActor actor, int page, Help.CommandList<BukkitCommandActor> commands, int elementsPerPage) {
        handleHelpMenu(actor, page, commands, elementsPerPage, null);
    }

    public static Component paginateNavigation(int currentPage, int maxPage, String commandFormat) {
        int previousPage = currentPage - 1;
        int nextPage = currentPage + 1;

        boolean havePreviousPage = previousPage != 0;
        boolean haveNextPage = maxPage != currentPage;

        TextComponent.Builder pageText = text()
                .color(NamedTextColor.GOLD);

        pageText.append(MM.deserialize("<reset><dark_gray>================<gray>[ </reset>"));

        pageText.append(text("⬅", !havePreviousPage ? NamedTextColor.DARK_GRAY : null)
                .decorate(TextDecoration.BOLD)
                .clickEvent(havePreviousPage ? ClickEvent.runCommand(String.format(commandFormat, previousPage)) : null)
                .hoverEvent(havePreviousPage ? text(String.format("Page %s", previousPage)).color(NamedTextColor.GOLD) : null));

        pageText.append(text("  ▍  ").decorate(TextDecoration.BOLD));

        pageText.append(text("➡", !haveNextPage ? NamedTextColor.DARK_GRAY : null)
                .decorate(TextDecoration.BOLD)
                .clickEvent(haveNextPage ? ClickEvent.runCommand(String.format(commandFormat, nextPage)) : null)
                .hoverEvent(haveNextPage ? text(String.format("Page %s", nextPage)).color(NamedTextColor.GOLD) : null));

        pageText.append(MM.deserialize(" <reset><gray>]<dark_gray>================</reset>"));

        return pageText.build();
    }

    public static Component processHeader(String name, String symbol, int totalLength) {
        int nameChars = name.length();
        int nameShould = totalLength - nameChars;
        int side = nameShould / 2;

        String bar = Strings.repeat(symbol, side);

        return Component.textOfChildren(
                text(bar, NamedTextColor.GREEN),
                text("[", NamedTextColor.DARK_GRAY),
                space(),
                text(name, NamedTextColor.YELLOW, TextDecoration.BOLD),
                space(),
                text("]", NamedTextColor.DARK_GRAY),
                text(bar, NamedTextColor.GREEN)
        );
    }

    public static Component processFooter(String symbol, int totalLength) {
        return text(Strings.repeat(symbol, totalLength), NamedTextColor.DARK_GRAY);
    }
}
