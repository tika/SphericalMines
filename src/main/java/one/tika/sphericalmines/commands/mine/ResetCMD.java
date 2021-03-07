package one.tika.sphericalmines.commands.mine;

import one.tika.sphericalmines.Mine;
import one.tika.sphericalmines.Perm;
import one.tika.sphericalmines.SphericalMines;
import one.tika.tide.command.SubcommandBase;
import one.tika.tide.data.Config;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ResetCMD extends SubcommandBase {
    private final Config msg = SphericalMines.getInstance().getMessages();

    @Override
    public String getName() {
        return "reset";
    }

    @Override
    public String getDescription() {
        return "Resets a mine";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!Perm.RESET.handle(sender)) return;

        if (args.length < 2) {
            sender.sendMessage(msg.getMessageValue("arg-not-provided").replace("%arg%", "mine"));
            return;
        }

        Optional<Mine> mine = SphericalMines.getInstance().getMineHandler().get(args[1]);

        if (!mine.isPresent()) {
            sender.sendMessage(msg.getMessageValue("invalid-mine").replace("%mine%", args[1]));
            return;
        }

        mine.get().reset();

        sender.sendMessage(msg.getMessageValue("reset.success").replace("%mine%", mine.get().getName()));
    }

    @Override
    public List<String> getParameters(CommandSender commandSender, String[] args) {
        if (args.length == 1) {
            return SphericalMines.getInstance().getMineHandler().getAll().stream().map(Mine::getName).collect(Collectors.toList());
        }

        return null;
    }
}
