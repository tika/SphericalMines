package one.tika.sphericalmines.commands.mine;

import one.tika.sphericalmines.Perm;
import one.tika.tide.command.SubcommandBase;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class SetCMD extends SubcommandBase {
    private final String[] settings = { "name", "radius", "center", "materials", "spawn" };

    @Override
    public String getName() {
        return "set";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    // /mine set <setting> <value>
    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!Perm.SET.handle(sender)) return;

        sender.sendMessage("Settings: " + settings);
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
