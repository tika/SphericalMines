package one.tika.sphericalmines.commands.mine;

import one.tika.sphericalmines.Mine;
import one.tika.sphericalmines.Perm;
import one.tika.sphericalmines.SLocation;
import one.tika.sphericalmines.SphericalMines;
import one.tika.sphericalmines.listeners.ChatListener;
import one.tika.tide.command.SubcommandBase;
import one.tika.tide.data.Config;
import one.tika.tide.utils.CollectionUtil;
import one.tika.tide.utils.Hue;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class CreateCMD extends SubcommandBase {
    private final Config msg = SphericalMines.getInstance().getMessages();

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String getDescription() {
        return "Creates a mine";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public void perform(CommandSender commandSender, String[] strings) {
        if (!Perm.CREATE.handle(commandSender)) return;

        Player player = (Player) commandSender;
        Hue.message(player, msg.getMessageValue("create-setup.intro"));

        Map<Material, Double> mineMaterials = CollectionUtil.map(Material.STONE, 100D);

        Hue.message(player, msg.getMessageValue("create-setup.name"));
        ChatListener.awaitingInput.put(player.getUniqueId(), (_name) -> {

            if (!_name.matches("^[a-zA-Z0-9_]*$")) {
                Hue.message(player, msg.getMessageValue("create-setup.invalid-name").replace("%name%", _name));
                return;
            }

            if (SphericalMines.getInstance().getMineHandler().get(_name).isPresent()) {
                Hue.message(player, msg.getMessageValue("name-taken"));
                return;
            }

            Hue.message(player, msg.getMessageValue("create-setup.radius"));

            ChatListener.awaitingInput.put(player.getUniqueId(), (_radius) -> {
                double radius;

                try {
                    radius = Double.parseDouble(_radius.trim());
                } catch (NumberFormatException e) {
                    Hue.message(player, msg.getMessageValue("invalid-radius").replace("%radius%", _radius));
                    return;
                }

                Mine mine = new Mine(_name, radius,
                        SLocation.fromBukkitLocation(player.getLocation()),
                        mineMaterials,
                        SLocation.fromBukkitLocation(player.getLocation().clone().add(0, radius, 0))
                );

                SphericalMines.getInstance().getMineHandler().add(mine);

                Hue.message(player, msg.getMessageValue("create-setup.success").replace("%mine%", mine.getName()));
            });
        });
    }

    @Override
    public List<String> getParameters(CommandSender commandSender, String[] strings) {
        return null;
    }
}
