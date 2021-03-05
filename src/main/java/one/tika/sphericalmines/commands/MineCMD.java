package one.tika.sphericalmines.commands;

import one.tika.sphericalmines.commands.mine.CreateCMD;
import one.tika.sphericalmines.commands.mine.ResetCMD;
import one.tika.sphericalmines.commands.mine.SetCMD;
import one.tika.tide.command.CommandBase;
import one.tika.tide.command.SubcommandBase;
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
        return Arrays.asList(new ResetCMD(), new CreateCMD(), new SetCMD());
    }

    @Override
    public void perform(CommandSender commandSender, Command command, String s, String[] strings) {
        commandSender.sendMessage("Mine command");
    }
}
