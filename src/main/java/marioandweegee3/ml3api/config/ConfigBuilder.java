package marioandweegee3.ml3api.config;

public class ConfigBuilder {
    private Config config;

    public ConfigBuilder(){
            config = Config.empty();
        }

    public ConfigBuilder add(String key, Object defaultValue) {
        config.set(key, defaultValue);
        return this;
    }

    public Config build() {
        return config;
    }
}