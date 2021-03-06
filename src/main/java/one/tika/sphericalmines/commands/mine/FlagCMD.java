package one.tika.sphericalmines.commands.mine;

import one.tika.sphericalmines.Mine;
import one.tika.sphericalmines.MineHandler;
import one.tika.sphericalmines.Perm;
import one.tika.sphericalmines.SphericalMines;
import one.tika.tide.command.SubcommandBase;
import one.tika.tide.data.Config;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FlagCMD extends SubcommandBase {
    private final Config msg = SphericalMines.getInstance().getMessages();
    private final String[] flags = { "silent_reset", "auto_reset" };
    private final MineHandler handler = SphericalMines.getInstance().getMineHandler();

    @Override
    public String getName() {
        return "flag";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    // /mine flag <mine> <flag> <value>
    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!Perm.FLAG.handle(sender)) return;

        // Flags are kind of like settings for each mine, e.g:
        //  - "silent_reset" = Does this mine announce to players when it is reset?
        //  - "auto_reset" = When does this mine reset?

        if (args.length == 1) {
            sender.sendMessage(msg.getMessageValue("arg-not-provided").replace("%arg%", "mine"));
            return;
        } else if (args.length == 2) {
            sender.sendMessage(msg.getMessageValue("arg-not-provided").replace("%arg%", "setting"));
            return;
        } else if (args.length < 4) {
            sender.sendMessage(msg.getMessageValue("arg-not-provided").replace("%arg%", "value"));
            return;
        }

        String mineName = args[1];
        String flag = args[2];
        String value = args[3];

        Optional<Mine> _mine = SphericalMines.getInstance().getMineHandler().get(mineName);

        // Invalid mine
        if (!_mine.isPresent()) {
            sender.sendMessage(msg.getMessageValue("invalid-mine").replace("%mine%", mineName));
            return;
        }

        // Invalid flag
        if (Arrays.stream(flags).noneMatch(flag::equalsIgnoreCase)) {
            sender.sendMessage(msg.getMessageValue("flag.invalid-flag").replace("%flag%", flag));
            return;
        }

        Mine mine = _mine.get();

        switch (flag.toLowerCase()) {
            case "silent_reset":
                handler.remove(mine.getName());

                mine.setSilentReset(Boolean.parseBoolean(value));
                sender.sendMessage(msg.getMessageValue("flag.success")
                        .replace("%flag%", flag)
                        .replace("%value%", value)
                );

                handler.add(mine);

                break;
            case "auto_reset":
                String numStr = StringUtils.chop(value);

                if (!NumberUtils.isNumber(numStr)) {
                    sender.sendMessage(msg.getMessageValue("flag.invalid-number")
                            .replace("%value%", value));
                    return;
                }

                handler.remove(mine.getName());

                // check if value = "false"
                mine.setAutoReset(value.equalsIgnoreCase("false") ? null : value);
                handler.add(mine);

                break;
        }
    }

    @Override
    public List<String> getParameters(CommandSender sender, String[] args) {
        if (args.length == 2) {
            return SphericalMines.getInstance().getMineHandler().getAll().stream().map(Mine::getName).collect(Collectors.toList());
        } else if (args.length == 3) {
            return Arrays.asList(flags);
        }

        return Collections.singletonList("[<value>]");
    }
}
