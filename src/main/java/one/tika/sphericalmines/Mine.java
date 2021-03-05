package one.tika.sphericalmines;

import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.util.EditSessionBuilder;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.factory.SphereRegionFactory;
import com.sk89q.worldedit.world.block.BlockType;
import one.tika.tide.utils.CollectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class Mine {

    private String name;
    private double radius;
    private SLocation center;
    private Map<Material, Double> mineMaterials;
    private SLocation mineSpawn;

    public Mine(String name, double radius, SLocation center, Map<Material, Double> mineMaterials, SLocation mineSpawn) {
        this.name = name;
        this.radius = radius;
        this.center = center;
        this.mineMaterials = mineMaterials;
        this.mineSpawn = mineSpawn;
    }

    // name
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // center
    public SLocation getCenter() {
        return center;
    }

    public void setCenter(SLocation center) {
        this.center = center;
    }

    // radius
    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    // mineMaterials
    public void addMaterial(Material material, double chance) {
        mineMaterials.put(material, chance);
    }

    public void setMaterial(Material material, double chance) {
        mineMaterials.replace(material, chance);
    }

    public double getChance(Material material) {
        return mineMaterials.get(material);
    }

    public Map<Material, Double> getMaterials() {
        return mineMaterials;
    }

    public void setMaterials(Map<Material, Double> mineMaterials) {
        this.mineMaterials = mineMaterials;
    }

    // mineSpawn
    public SLocation getMineSpawn() {
        return mineSpawn;
    }

    public void setMineSpawn(SLocation mineSpawn) {
        this.mineSpawn = mineSpawn;
    }

    // ########### Utils ###########
    public Material getRandomMaterial(Random random) {
        return CollectionUtil.getWeightedRandom(mineMaterials.entrySet().stream(), random);
    }

    public double getVolume() {
        return (4D / 3D) * 3.14 * (radius * radius * radius);
    }

    public List<Block> getBlocks() {
        List<Block> blocks = new ArrayList<>((int) Math.round(getVolume())); // Set initial capacity to area of mine
        World world = Bukkit.getWorld(center.world);
        getRegion().forEach(loc -> blocks.add(world.getBlockAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())));
        return blocks;
    }

    public boolean inMine(Location location) {
        return getRegion().contains(BlockVector3.at(location.getX(), location.getY(), location.getZ()));
    }

    public void reset() {
        long startTime = System.currentTimeMillis();
        EditSession editSession = new EditSessionBuilder(FaweAPI.getWorld(center.world)).fastmode(true).build();

        for (Block block : getBlocks()) {
            editSession.setBlock(BlockVector3.at(block.getX(), block.getY(), block.getZ()),
                    BlockType.REGISTRY.get(getRandomMaterial(new Random()).name().toLowerCase()));
        }

        editSession.flushQueue();

        long timeTaken = System.currentTimeMillis() - startTime;

        getPlayersInMine().forEach(player -> player.teleport(mineSpawn.toBukkitLocation()));
    }

    public List<Player> getPlayersInMine() {
        if (Bukkit.getOnlinePlayers().size() == 0) return null;
        return Bukkit.getOnlinePlayers().stream().filter(player -> inMine(player.getLocation())).collect(Collectors.toList());
    }

    public void delete() {
        SphericalMines.getInstance().getMineHandler().remove(getName());
    }

    public Region getRegion() {
        return new SphereRegionFactory().createCenteredAt(center.toBlockVector3(), radius);
    }

}
