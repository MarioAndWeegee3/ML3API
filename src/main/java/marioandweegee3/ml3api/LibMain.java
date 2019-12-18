package marioandweegee3.ml3api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import marioandweegee3.ml3api.config.Config;
import marioandweegee3.ml3api.config.ConfigBuilder;
import marioandweegee3.ml3api.config.ConfigManager;
import marioandweegee3.ml3api.registry.RegistryHelper;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class LibMain implements ModInitializer {
    public static final Gson gson = new GsonBuilder().registerTypeAdapter(Config.class, new ConfigBuilder().build()).setPrettyPrinting().create();

    public static final RegistryHelper helper = new RegistryHelper("ml3api");

    public static Config test;

    @Override
    public void onInitialize() {
        ConfigManager.INSTANCE.set(new Identifier("ml3lib:test"), new ConfigBuilder().add("number", 42).add("string", "something").add("array", new Object[]{
            1,2,3,4,"you expected 5, but it was me, Dio!"
        }).add("identifier", new Identifier("stone")).add("inner", new TestClass()).build());

        test = ConfigManager.INSTANCE.getConfig(new Identifier("ml3lib:test"));

        helper.log(test.toString());
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