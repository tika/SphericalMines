package one.tika.sphericalmines.commands.mine;

import one.tika.sphericalmines.*;
import one.tika.tide.command.SubcommandBase;
import one.tika.tide.data.Config;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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

        handler.remove(mine.getName());

        switch (setting.toLowerCase()) {
            case "name":
                if (handler.get(value).isPresent()) {
                    sender.sendMessage(msg.getMessageValue("name-taken"));
                    return;
                }

                mine.setName(value);
                break;
            case "radius":
                double radius;

                try {
                    radius = Double.parseDouble(value.trim());
                } catch (NumberFormatException e) {
                    sender.sendMessage(msg.getMessageValue("invalid-radius").replace("%radius%", value));
                    return;
                }

                mine.setRadius(radius);
                break;
            case "center":
                handler.add(mine);
                if (value.equals("here")) {
                    if (!(sender instanceof Player)) {
                        sender.sendMessage(msg.getMessageValue("set.only-player"));
                        return;
                    }

                    mine.setCenter(SLocation.fromBukkitLocation(((Player) sender).getLocation()));
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
                // TODO
                return;
            case "spawn":
                handler.add(mine);
                if (value.equals("here")) {
                    if (!(sender instanceof Player)) {
                        sender.sendMessage(msg.getMessageValue("set.only-player"));
                        return;
                    }

                    mine.setCenter(SLocation.fromBukkitLocation(((Player) sender).getLocation()));
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
                return;
        }

        sender.sendMessage(msg.getMessageValue("set.updated-setting")
                .replace("%setting%", setting.toLowerCase())
                .replace("%value%", value.toLowerCase())
        );
        handler.add(mine);
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
