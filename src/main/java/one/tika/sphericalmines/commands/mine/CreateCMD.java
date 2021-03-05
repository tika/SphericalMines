package one.tika.sphericalmines.commands.mine;

import one.tika.sphericalmines.Mine;
import one.tika.sphericalmines.Perm;
import one.tika.sphericalmines.SLocation;
import one.tika.sphericalmines.SphericalMines;
import one.tika.sphericalmines.listeners.ChatListener;
import one.tika.tide.command.SubcommandBase;
import one.tika.tide.utils.CollectionUtil;
import one.tika.tide.utils.Hue;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class CreateCMD extends SubcommandBase {
    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public void perform(CommandSender commandSender, String[] strings) {
        if (!Perm.CREATE.handle(commandSender)) return;

        Player player = (Player) commandSender;
        Hue.message(player, "&bThis will run you through a mine setup.");

        Map<Material, Double> mineMaterials = CollectionUtil.map(Material.STONE, 100D);

        Hue.message(player, "&aEnter a mine name");
        ChatListener.awaitingInput.put(player.getUniqueId(), (_name) -> {

            if (!_name.matches("^[a-zA-Z0-9_]*$")) {
                Hue.message(player, "&cThe name " + _name + " is not allowed. [a-Z, 1-9]");
                return;
            }

            if (SphericalMines.getInstance().getMineHandler().getAll().stream().anyMatch(mine -> mine.getName().equals(_name))) {
                Hue.message(player, "&cThere is already a mine with this name!");
                return;
            }

            Hue.message(player, "&aEnter a radius");

            ChatListener.awaitingInput.put(player.getUniqueId(), (_radius) -> {
                double radius;

                try {
                    radius = Double.parseDouble(_radius.trim());
                } catch (NumberFormatException e) {
                    Hue.message(player, "&c" + _radius + " is not a double!");
                    return;
                }

                Mine mine = new Mine(_name, radius,
                        SLocation.fromBukkitLocation(player.getLocation()),
                        mineMaterials,
                        SLocation.fromBukkitLocation(player.getLocation().clone().add(0, radius, 0))
                );

                SphericalMines.getInstance().getMineHandler().add(mine);

                mine.reset();

                player.sendMessage("Completed setup? " + mine.getName());
            });
        });
    }

    @Override
    public List<String> getParameters(CommandSender commandSender, String[] strings) {
        return null;
    }
}
