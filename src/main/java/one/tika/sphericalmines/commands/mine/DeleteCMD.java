package one.tika.sphericalmines.commands.mine;

import one.tika.sphericalmines.Mine;
import one.tika.sphericalmines.Perm;
import one.tika.sphericalmines.SphericalMines;
import one.tika.tide.command.SubcommandBase;
import one.tika.tide.data.Config;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Optional;

public class DeleteCMD extends SubcommandBase {
    private final Config msg = SphericalMines.getInstance().getMessages();

    @Override
    public String getName() {
        return "delete";
    }

    @Override
    public String getDescription() {
        return "Deletes a mine";
    }

    @Override
    public String[] getAliases() {
        return new String[]{ "remove" };
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!Perm.DELETE.handle(sender)) return;

        if (args.length < 2) {
            sender.sendMessage(msg.getMessageValue("arg-not-provided").replace("%arg%", "mine"));
            return;
        }

        Optional<Mine> mine = SphericalMines.getInstance().getMineHandler().get(args[1]);

        if (!mine.isPresent()) {
            sender.sendMessage(msg.getMessageValue("invalid-mine").replace("%mine%", args[1]));
            return;
        }

        mine.get().delete();
        sender.sendMessage(msg.getMessageValue("delete.success").replace("%mine%", args[1]));
    }

    @Override
    public List<String> getParameters(CommandSender commandSender, String[] strings) {
        return null;
    }
}
