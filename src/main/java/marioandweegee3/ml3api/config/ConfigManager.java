package marioandweegee3.ml3api.config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.stream.JsonReader;

import marioandweegee3.ml3api.LibMain;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

public class ConfigManager {
    public static final ConfigManager INSTANCE = new ConfigManager();

    private Map<Identifier, Pair<File, Config>> configs;

    private ConfigManager() {
        this.configs = new HashMap<>();
    }

    public void set(Identifier id, Config defaultConfig) {
        File dir = new File(FabricLoader.getInstance().getConfigDirectory(), id.getNamespace());
        dir.mkdirs();

        File file = new File(dir, id.getPath() + ".json");
        
        configs.put(id, new Pair<>(file, defaultConfig));
    }

    public Config getConfig(Identifier id) {
        Pair<File, Config> pair = configs.get(id);
        File file = pair.getLeft();
        try {
            if (file.createNewFile()) {
                writeFile(file, pair.getRight());
                return pair.getRight();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JsonReader reader = new JsonReader(new FileReader(file));
            return LibMain.gson.fromJson(reader, Config.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pair.getRight();
    }

    private void writeFile(File file, Config config) throws IOException {
        FileWriter writer = new FileWriter(file);
        writer.write(LibMain.gson.toJson(config));
        writer.close();
    }

    public void write(Identifier id){
        Pair<File, Config> pair = configs.get(id);
        try {
            writeFile(pair.getLeft(), pair.getRight());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refresh(Identifier id){
        if(configs.containsKey(id)){
            Pair<File, Config> pair = configs.get(id);
            Config newConfig = getConfig(id);
            configs.put(id, new Pair<>(pair.getLeft(), newConfig));
        }
    }
}