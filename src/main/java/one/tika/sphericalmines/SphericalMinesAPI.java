package one.tika.sphericalmines;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.Map;
import java.util.Optional;

public class SphericalMinesAPI {

    private SphericalMinesAPI() {}

    /**
     * Checks if a location is within a mine
     * @param location The location to be checked
     * @return Whether the location is within a mine
     */
    public static boolean inMine(Location location) {
        return getMineAt(location).isPresent();
    }

    /**
     * Gets a mine from a name
     * @param name The name of the mine wanted
     * @return The mine specified
     */
    public static Optional<Mine> getMine(String name) {
        return SphericalMines.getInstance().getMineHandler().get(name);
    }

    /**
     * Gets a mine at a specified location
     * @param location A location to be queried
     * @return The mine at that location
     */
    public static Optional<Mine> getMineAt(Location location) {
        return SphericalMines.getInstance().getMineHandler().getAll().stream().filter(mine -> mine.inMine(location)).findFirst();
    }

    /**
     * Creates a mine and inserts it into data
     * @return The new mine created
     */
    public static Mine createMine(String name, double radius, SLocation center, Map<Material, Double> mineMaterials, SLocation mineSpawn) {
        Mine mine = new Mine(name, radius, center, mineMaterials, mineSpawn);
        SphericalMines.getInstance().getMineHandler().add(mine);
        return mine;
    }
}
