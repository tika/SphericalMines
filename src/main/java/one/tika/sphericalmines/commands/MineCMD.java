package one.tika.sphericalmines.commands;

import one.tika.sphericalmines.commands.mine.*;
import one.tika.tide.command.CommandBase;
import one.tika.tide.command.SubcommandBase;
import one.tika.tide.utils.Hue;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class MineCMD extends CommandBase {
    @Override
    public String getName() {
        return "mine";
    }

    @Override
    public List<SubcommandBase> getSubcommands() {
        return Arrays.asList(
            new ResetCMD(),
            new CreateCMD(),
            new SetCMD(),
            new DeleteCMD(),
            new FlagCMD()
        );
    }

    @Override
    public void perform(CommandSender sender, Command command, String s, String[] strings) {

        sender.sendMessage(Hue.colorize("&7------- &5Spherical&d&lMines &7-------"));
        sender.sendMessage(Hue.colorize("&7Commands: "));

        for (SubcommandBase subcmd : getSubcommands()) {
            sender.sendMessage(Hue.colorize("&8 - &d" + subcmd.getName() + "&8: &7" + subcmd.getDescription()));
        }

        sender.sendMessage(Hue.colorize("&7-----------------------------"));
    }
}
