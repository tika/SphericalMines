package one.tika.sphericalmines;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import one.tika.tide.utils.Hue;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class MineHandler {
    private final File dataFolder;
    private final List<Mine> data = new ArrayList<>();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public MineHandler() {
        SphericalMines plugin = SphericalMines.getInstance();
        this.dataFolder = new File(plugin.getDataFolder(), "data");

        if (!this.dataFolder.exists()) {
            this.dataFolder.mkdirs();
        }
    }

    public void add(final Mine mine) {
        data.add(mine);
    }

    public Optional<Mine> get(final String name) {
        return data.stream().filter(mine -> mine.getName().equals(name)).findFirst();
    }

    public void remove(final String name) {
        data.removeIf(mine -> mine.getName().equals(name));

        // Find the file
        Optional<File> file = Arrays.stream(dataFolder.listFiles()).filter(f -> f.getName().equals(name + ".json")).findFirst();

        if (file.isPresent()) {
            if (!file.get().delete()) {
                Hue.get(SphericalMines.getInstance()).print("&cCould not delete mine file!");
            }
        }
    }

    public List<Mine> getAll() {
        return data;
    }

    public void loadData() throws FileNotFoundException {
        for (final File file : dataFolder.listFiles()) {
            final Mine mine = gson.fromJson(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8), Mine.class);
            data.add(mine);
        }
    }

    public void saveData() throws IOException {
        for (final Mine testObject : data) {
            final String name = testObject.getName();
            final File file = new File(dataFolder, name + ".json");
            Files.write(file.toPath(), gson.toJson(testObject, Mine.class).getBytes(StandardCharsets.UTF_8));
        }
    }
}
