package one.tika.sphericalmines;

import one.tika.sphericalmines.commands.MineCMD;
import one.tika.tide.TidePlugin;
import one.tika.tide.utils.CollectionUtil;
import org.bukkit.Material;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

public final class SphericalMines extends TidePlugin {
    private static SphericalMines instance;
    private Mine mine;
    private MineHandler mineHandler;

    @Override
    public void onEnable() {
        instance = this;

        mineHandler = new MineHandler();

        this.mine = new Mine("test", UUID.randomUUID(), 25, new SLocation("world", 0, 150, 0), CollectionUtil.map(
                Material.STONE, 50D,
                Material.DIAMOND_ORE, 50D
        ), new SLocation("world", 0, 175, 0));

        try {
            mineHandler.loadData();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        mineHandler.add(mine);

        prettyPrintDescription("&3");
        registerCommands(new MineCMD());
    }

    @Override
    public void onDisable() {
        try {
            mineHandler.saveData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Mine getMine() {
        return mine;
    }

    public static SphericalMines getInstance() {
        return instance;
    }
}
