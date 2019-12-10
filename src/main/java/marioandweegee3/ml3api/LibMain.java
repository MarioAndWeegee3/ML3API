package marioandweegee3.ml3api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import marioandweegee3.ml3api.config.Config;
import marioandweegee3.ml3api.config.ConfigManager;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class LibMain implements ModInitializer {
    public static final Logger logger = LogManager.getLogger();

    public static final Gson gson = new GsonBuilder().registerTypeAdapter(Config.class, new Config()).setPrettyPrinting().create();

    public static Config test;

    @Override
    public void onInitialize() {
        ConfigManager.INSTANCE.set(new Identifier("ml3lib:test"), new Config.Builder().add("number", 42).add("string", "something").add("array", new Object[]{
            1,2,3,4,"you expected 5, but it was me, Dio!"
        }).add("identifier", new Identifier("stone")).add("inner", new TestClass()).build());

        test = ConfigManager.INSTANCE.getConfig(new Identifier("ml3lib:test"));
    }

    static class TestClass {
        public String name = "";
        public int number = 0;
        public boolean flag = false;
        @Override
        public String toString() {
            return name + " " + number + " " + flag;
        }
    }

}