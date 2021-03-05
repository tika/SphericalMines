package one.tika.sphericalmines;

import com.sk89q.worldedit.math.BlockVector3;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class SLocation {
    String world;
    int x, y, z;

    public SLocation(String world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static SLocation fromBukkitLocation(Location location) {
        return new SLocation(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public Location toBukkitLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z);
    }

    public BlockVector3 toBlockVector3() {
        return BlockVector3.at(x, y, z);
    }
}
