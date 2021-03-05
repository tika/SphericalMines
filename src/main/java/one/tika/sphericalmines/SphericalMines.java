package one.tika.sphericalmines;

import one.tika.sphericalmines.commands.MineCMD;
import one.tika.sphericalmines.listeners.ChatListener;
import one.tika.tide.TidePlugin;
import one.tika.tide.data.Config;

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

        try {
            mineHandler.loadData();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        prettyPrintDescription("&3");
        registerCommands(new MineCMD());
        registerEvents(new ChatListener());
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
