package one.tika.sphericalmines;

import one.tika.sphericalmines.commands.MineCMD;
import one.tika.sphericalmines.listeners.BreakListener;
import one.tika.sphericalmines.listeners.ChatListener;
import one.tika.tide.TidePlugin;
import one.tika.tide.data.Config;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;

import java.io.FileNotFoundException;
import java.io.IOException;

public final class SphericalMines extends TidePlugin {
    private static SphericalMines instance;
    private MineHandler mineHandler;
    private Config messages;

    @Override
    public void onEnable() {
        instance = this;
        mineHandler = new MineHandler();
        messages = new Config(this, "messages.yml");

        try {
            mineHandler.loadData();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        prettyPrintDescription("&3");
        registerCommands(new MineCMD());
        registerEvents(new ChatListener(), new BreakListener());

        // AutoReset
        for (Mine mine : mineHandler.getAll()) {
            if (mine.getAutoReset() == null || mine.getAutoReset().charAt(mine.getAutoReset().length() - 1) != 's') continue;
            int time = Integer.parseInt(StringUtils.chop(mine.getAutoReset()));
            Bukkit.getScheduler().runTaskTimer(this, mine::reset, 0, time * 20L);
        }
    }

    @Override
    public void onDisable() {
        try {
            mineHandler.saveData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Config getMessages() {
        return messages;
    }
    public MineHandler getMineHandler() {
        return mineHandler;
    }

    public static SphericalMines getInstance() {
        return instance;
    }
}
