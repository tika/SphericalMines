package one.tika.sphericalmines;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MineHandler {
    private final SphericalMines plugin = SphericalMines.getInstance();
    private final File dataFolder;
    private final Map<UUID, Mine> data = new HashMap<>();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public MineHandler() {
        this.dataFolder = new File(plugin.getDataFolder(), "data");

        if (!this.dataFolder.exists()) {
            this.dataFolder.mkdirs();
        }
    }

    public void add(final Mine mine) {
        data.put(mine.getId(), mine);
    }

    public Mine get(final UUID uuid) {
        return data.get(uuid);
    }

    public void remove(final UUID uuid) {
        data.remove(uuid);
    }

    public void loadData() throws FileNotFoundException {
        for (final File file : dataFolder.listFiles()) {
            final Mine mine = gson.fromJson(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8), Mine.class);
            data.put(mine.getId(), mine);
        }
    }

    public void saveData() throws IOException {
        for (final Mine testObject : data.values()) {
            final String id = testObject.getId().toString();
            final File file = new File(dataFolder, id + ".json");
            Files.write(file.toPath(), gson.toJson(testObject, Mine.class).getBytes(StandardCharsets.UTF_8));
        }
    }
}
