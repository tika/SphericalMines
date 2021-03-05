package one.tika.sphericalmines;

import one.tika.tide.utils.Hue;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permissible;

public enum Perm {

    RESET,
    SET,
    CREATE;

    private final String prefix = "sphere";

    public String getNode() {
        return prefix + "." + this.toString().toLowerCase();
    }

    public boolean has(Permissible permissible) {
        return permissible.hasPermission(getNode());
    }

    // TRUE = Can use, FALSE = Cannot use
    public boolean handle(CommandSender sender) {
        if (!has(sender)) {
            sender.sendMessage(Hue.colorize("&cYou do not have permission to use this command!"));
            return false;
        }

        return true;
    }

}
