package one.tika.sphericalmines.commands.mine;

import one.tika.sphericalmines.Mine;
import one.tika.sphericalmines.Perm;
import one.tika.sphericalmines.SphericalMines;
import one.tika.tide.command.SubcommandBase;
import one.tika.tide.utils.Hue;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ResetCMD extends SubcommandBase {
    @Override
    public String getName() {
        return "reset";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!Perm.RESET.handle(sender)) return;

        Optional<Mine> mine = SphericalMines.getInstance().getMineHandler().get(args[1]);

        if (!mine.isPresent()) {
            sender.sendMessage(Hue.colorize("&cCould not find a mine with name: " + args[1]));
            sender.sendMessage(Hue.colorize("&bValid mines: " +
                    SphericalMines.getInstance().getMineHandler().getAll().stream().map(Mine::getName).collect(Collectors.toList())));
            return;
        }

        mine.get().reset();

        sender.sendMessage(Hue.colorize("&aSuccessfully reset " + mine.get().getName()));
    }

    @Override
    public List<String> getParameters(CommandSender commandSender, String[] args) {
        if (args.length > 1) {
            return SphericalMines.getInstance().getMineHandler().getAll().stream().map(Mine::getName).collect(Collectors.toList());
        }

        return null;
    }
}
