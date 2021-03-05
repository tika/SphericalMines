package one.tika.sphericalmines.commands.mine;

import one.tika.sphericalmines.Mine;
import one.tika.sphericalmines.Perm;
import one.tika.sphericalmines.SphericalMines;
import one.tika.tide.command.SubcommandBase;
import one.tika.tide.data.Config;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SetCMD extends SubcommandBase {
    private final Config msg = SphericalMines.getInstance().getMessages();
    private final String[] settings = { "name", "radius", "center", "materials", "spawn" };

    @Override
    public String getName() {
        return "set";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    // /mine set <mine> <setting> <value>
    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!Perm.SET.handle(sender)) return;

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
        String setting = args[2];
        String value = args[3];

        Optional<Mine> _mine = SphericalMines.getInstance().getMineHandler().get(mineName);

        // Invalid mine
        if (!_mine.isPresent()) {
            sender.sendMessage(msg.getMessageValue("invalid-mine").replace("%mine%", mineName));
            return;
        }

        // Invalid setting
        if (Arrays.stream(settings).noneMatch(setting::equalsIgnoreCase)) {
            sender.sendMessage(msg.getMessageValue("set.invalid-setting").replace("%setting%", setting));
            return;
        }

        Mine mine = _mine.get();

        SphericalMines.getInstance().getMineHandler().remove(mine.getName());

        switch (setting.toLowerCase()) {
            case "name":
                mine.setName(value);
                sender.sendMessage(msg.getMessageValue("set.updated-setting")
                        .replace("%setting%", setting.toLowerCase())
                        .replace("%value%", value.toLowerCase())
                );
                break;
        }

        SphericalMines.getInstance().getMineHandler().add(mine);
    }

    @Override
    public List<String> getParameters(CommandSender sender, String[] args) {
        if (args.length == 2) {
            return Arrays.asList(settings);
        } else if (args.length == 3) {

            // Value

        }

        return null;
    }
}
