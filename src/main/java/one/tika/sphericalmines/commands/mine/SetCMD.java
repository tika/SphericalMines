package one.tika.sphericalmines.commands.mine;

import one.tika.sphericalmines.*;
import one.tika.tide.command.SubcommandBase;
import one.tika.tide.data.Config;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

public class SetCMD extends SubcommandBase {
    private final Config msg = SphericalMines.getInstance().getMessages();
    private final MineHandler handler = SphericalMines.getInstance().getMineHandler();
    private final String[] settings = { "name", "radius", "center", "materials", "spawn" };

    @Override
    public String getName() {
        return "set";
    }

    @Override
    public String getDescription() {
        return "Edits a property of a mine";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    // /mine set <mine> <setting> <value>
    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!Perm.SET.handle(sender)) return;

        if (args.length == 1) {
            sender.sendMessage(msg.getMessageValue("arg-not-provided").replace("%arg%", "mine"));
            return;
        } else if (args.length == 2) {
            sender.sendMessage(msg.getMessageValue("arg-not-provided").replace("%arg%", "setting"));
            return;
        } else if (args.length < 4) {
            sender.sendMessage(msg.getMessageValue("arg-not-provided").replace("%arg%", "value"));
            return;
        }

        String mineName = args[1];
        String setting = args[2];
        String value = args[3];

        Optional<Mine> _mine = SphericalMines.getInstance().getMineHandler().get(mineName);

        // Invalid mine
        if (!_mine.isPresent()) {
            sender.sendMessage(msg.getMessageValue("invalid-mine").replace("%mine%", mineName));
            return;
        }

        // Invalid setting
        if (Arrays.stream(settings).noneMatch(setting::equalsIgnoreCase)) {
            sender.sendMessage(msg.getMessageValue("set.invalid-setting").replace("%setting%", setting));
            return;
        }

        Mine mine = _mine.get();

        switch (setting.toLowerCase()) {
            case "name":
                if (handler.get(value).isPresent()) {
                    sender.sendMessage(msg.getMessageValue("name-taken"));
                    return;
                }

                handler.remove(mine.getName());

                mine.setName(value);

                sender.sendMessage(msg.getMessageValue("set.updated-setting")
                        .replace("%setting%", setting.toLowerCase())
                        .replace("%value%", value.toLowerCase())
                );

                break;
            case "radius":
                double radius;

                try {
                    radius = Double.parseDouble(value.trim());
                } catch (NumberFormatException e) {
                    sender.sendMessage(msg.getMessageValue("invalid-radius").replace("%radius%", value));
                    return;
                }

                handler.remove(mine.getName());

                mine.setRadius(radius);

                sender.sendMessage(msg.getMessageValue("set.updated-setting")
                        .replace("%setting%", setting.toLowerCase())
                        .replace("%value%", value.toLowerCase())
                );

                break;
            case "center":
                if (value.equals("here")) {
                    if (!(sender instanceof Player)) {
                        sender.sendMessage(msg.getMessageValue("set.only-player"));
                        return;
                    }

                    handler.remove(mine.getName());

                    mine.setCenter(SLocation.fromBukkitLocation(((Player) sender).getLocation()));

                    handler.add(mine);

                    sender.sendMessage(msg.getMessageValue("set.updated-setting")
                            .replace("%setting%", setting.toLowerCase())
                            .replace("%value%", value.toLowerCase())
                    );

                    break;
                }

                // /mine set <mine> center <world> 5 5 5
                String world = args[3];

                if (Bukkit.getServer().getWorld(world) == null) {
                    sender.sendMessage(msg.getMessageValue("set.invalid-world"));
                    return;
                }

                if (args.length < 7) {
                    sender.sendMessage(msg.getMessageValue("set.coords-not-provided"));
                    return;
                }

                int x, y, z;

                try {
                    x = parseInt(args[4]);
                    y = parseInt(args[5]);
                    z = parseInt(args[6]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(msg.getMessageValue("set.invalid-coords"));
                    return;
                }

                handler.remove(mine.getName());

                mine.setCenter(new SLocation(world, x, y, z));
                sender.sendMessage(msg.getMessageValue("set.updated-coords")
                        .replace("%setting%", setting.toLowerCase())
                        .replace("%world%", world)
                        .replace("%xyz%", x + " " + y + " " + z)
                );

                handler.add(mine);
                return;
            case "materials":
                // /mine set <mine> materials stone,15 coal_ore,75

                Map<Material, Double> mineMaterials = new HashMap<>();
                // an easier way for us to keep track of the total chance ( must be = 100 )
                double totalChance = 0;

                for (int i = 3; i < args.length; i++) {
                    String[] mat = args[i].split(":");

                    Material material = Material.getMaterial(mat[0]);

                    if (material == null) {
                        sender.sendMessage(msg.getMessageValue("set.invalid-material")
                                .replace("%material%", mat[0]));
                        return;
                    }

                    double chance;

                    try {
                        chance = Double.parseDouble(mat[1]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(msg.getMessageValue("set.invalid-chance")
                                .replace("%chance%", mat[1]));
                        return;
                    }

                    if (totalChance >= 100) {
                        sender.sendMessage(msg.getMessageValue("set.too-high-total"));
                        return;
                    }

                    totalChance += chance;

                    mineMaterials.put(material, chance);
                }

                if (totalChance < 100) {
                    sender.sendMessage(msg.getMessageValue("set.too-low-total"));
                    return;
                }

                handler.remove(mine.getName());

                // set mine materials
                mine.setMaterials(mineMaterials);

                handler.add(mine);

                sender.sendMessage(msg.getMessageValue("set.updated-setting")
                        .replace("%setting%", setting.toLowerCase())
                        .replace("%value%", value.toLowerCase())
                );

                break;
            case "spawn":
                if (value.equals("here")) {
                    if (!(sender instanceof Player)) {
                        sender.sendMessage(msg.getMessageValue("set.only-player"));
                        return;
                    }

                    handler.remove(mine.getName());

                    mine.setCenter(SLocation.fromBukkitLocation(((Player) sender).getLocation()));

                    handler.add(mine);

                    sender.sendMessage(msg.getMessageValue("set.updated-setting")
                            .replace("%setting%", setting.toLowerCase())
                            .replace("%value%", value.toLowerCase())
                    );

                    break;
                }

                // /mine set <mine> center <world> 5 5 5
                String sWorld = args[3];

                if (Bukkit.getServer().getWorld(sWorld) == null) {
                    sender.sendMessage(msg.getMessageValue("set.invalid-world"));
                    return;
                }

                if (args.length < 7) {
                    sender.sendMessage(msg.getMessageValue("set.coords-not-provided"));
                    return;
                }

                int _x, _y, _z;

                try {
                    _x = parseInt(args[4]);
                    _y = parseInt(args[5]);
                    _z = parseInt(args[6]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(msg.getMessageValue("set.invalid-coords"));
                    return;
                }

                handler.remove(mine.getName());

                mine.setMineSpawn(new SLocation(sWorld, _x, _y, _z));
                sender.sendMessage(msg.getMessageValue("set.updated-coords")
                        .replace("%setting%", setting.toLowerCase())
                        .replace("%world%", sWorld)
                        .replace("%xyz%", _x + " " + _y + " " + _z)
                );

                handler.add(mine);
        }
    }

    @Override
    public List<String> getParameters(CommandSender sender, String[] args) {
        if (args.length == 2) {
            return handler.getAll().stream().map(Mine::getName).collect(Collectors.toList());
        } else if (args.length == 3) {
            return Arrays.asList(settings);
        }

        return Collections.singletonList("[<value>]");
    }
}
