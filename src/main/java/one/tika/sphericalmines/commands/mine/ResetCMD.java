package one.tika.sphericalmines.commands.mine;

import one.tika.sphericalmines.SphericalMines;
import one.tika.tide.command.SubcommandBase;
import org.bukkit.command.CommandSender;

import java.util.List;

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
    public void perform(CommandSender commandSender, String[] strings) {
        SphericalMines.getInstance().getMine().reset();
    }

    @Override
    public List<String> getParameters(CommandSender commandSender, String[] strings) {
        return null;
    }
}
