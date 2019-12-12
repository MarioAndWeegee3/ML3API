package marioandweegee3.ml3api.config;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Configuration with dynamic keys
 */
public final class Config implements JsonSerializer<Config>, JsonDeserializer<Config> {
    private Map<String, Object> properties;

    /**
     * Makes an empty Config
     */
    private Config() {
        properties = new HashMap<>();
    }

    /**
     * Returns the object in {@link #properties} that corresponds to the given key.
     *
     * For some {@link JsonElement}s, you will need to pass a particular type:
     * <ul>
     * <li>{@link JsonArray} - {@link List}</li>
     * <li>{@link JsonObject} - {@link Map} or use {@link #getSubConfig(String)}</li>
     * </ul>
     * If you know that an Object can be parsed, you can also pass its class.
     * @param <T> Any class. 
     * @param key The key in {@link #properties}
     * @param type The {@link Class} of the return value. Corresponds to T
     * @return A value of type T, or null if none is found or the type is incorrect
     */
    public <T> T get(String key, Class<T> type) {
        Object object = properties.get(key);
        if (type.isInstance(object)) {
            return type.cast(object);
        } else {
            return null;
        }
    }

    /**
     * Gets a Config at the given key. Can also be used to parse any {@link JsonObject}
     * @param key The key in {@link #properties}
     * @return A Config value at the key, or null if none is found or if the element cannot be represented as a Config.
     */
    @SuppressWarnings("unchecked")
    public Config getSubConfig(String key){
        Object object = properties.get(key);
        if(object instanceof Map){
            Map<String, Object> map = (Map<String, Object>) object;
            Builder builder = new Builder();
            for(String mapKey : map.keySet()){
                builder = builder.add(mapKey, map.get(mapKey));
            }
            return builder.build();
        } else if(object instanceof Config){
            return (Config) object;
        } else {
            return null;
        }
    }

    /**
     * Sets the value in the sub-config
     * @param configKey The sub-config key
     * @param key The element key
     * @param val The value to be set
     */
    public void setSubConfigVal(String configKey, String key, Object val){
        Config config = getSubConfig(configKey);
        config.set(key, val);
        set(configKey, config);
    }
    /**
     * Sets the value in the sub-config's sub-config
     * @param configKey The sub-config key
     * @param subConfigKey The sub-config's sub-config
     * @param key The element key
     * @param val The value to be set
     */
    public void setSubSubConfigVal(String configKey, String subConfigKey, String key, Object val){
        Config config = getSubConfig(configKey);
        Config subConfig = config.getSubConfig(subConfigKey);
        subConfig.set(key, val);
        config.set(subConfigKey, subConfig);
        set(configKey, config);
    }

    /**
     * Returns the value at the given key
     * @param key The key in {@link #properties}
     * @return The value in {@link #properties} as an Integer, or null if it is null
     */
    public Integer getInt(String key){
        Number number = get(key, Number.class);
        if(number == null){
            return null;
        } else {
            return number.intValue();
        }
    }

    public Double getDouble(String key){
        Number number = get(key, Number.class);
        if(number == null){
            return null;
        } else {
            return number.doubleValue();
        }
    }

    public String getString(String key){
        return get(key, String.class);
    }

    public Object getObject(String key){
        return properties.get(key);
    }

    public void set(String key, Object val) {
        properties.put(key, val);
    }

    @Override
    public JsonElement serialize(Config src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        for (Map.Entry<String, Object> entry : src.properties.entrySet()) {
            json.add(entry.getKey(), context.serialize(entry.getValue()));
        }
        return json;
    }

    @Override
    public Config deserialize(JsonElement j, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        if(j.isJsonObject()){
            Config config = new Config();
            JsonObject jsonObject = (JsonObject)j;
            for(Map.Entry<String, JsonElement> entry : jsonObject.entrySet()){
                config.properties.put(entry.getKey(), context.deserialize(entry.getValue(), Object.class));
            }
            return config;
        } else {
            throw new JsonParseException("Invalid json");
        }
    }

    @Override
    public String toString() {
        String s = "";
        for(Map.Entry<String, Object> entry : properties.entrySet()){
            s += entry.getKey() + ":" + entry.getValue().toString() + "; ";
        }
        return s;
    }

    public static final class Builder {
        private Config config;

        public Builder(){
            config = new Config();
        }

        public Builder add(String key, Object defaultValue){
            config.properties.put(key, defaultValue);
            return this;
        }

        public Config build(){
            return config;
        }
    }
}