package marioandweegee3.ml3api.config;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public final class Config implements JsonSerializer<Config>, JsonDeserializer<Config> {
    protected Map<String, Object> properties;

    public Config() {
        properties = new HashMap<>();
    }

    public <T> T get(String key, Class<T> type) {
        Object object = properties.get(key);
        if (type.isInstance(object)) {
            return type.cast(object);
        } else {
            return null;
        }
    }

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

    public void setSubConfigVal(String configKey, String key, Object val){
        Config config = getSubConfig(configKey);
        config.set(key, val);
        set(configKey, config);
    }

    public void setSubSubConfigVal(String configKey, String subConfigKey, String key, Object val){
        Config config = getSubConfig(configKey);
        Config subConfig = config.getSubConfig(subConfigKey);
        subConfig.set(key, val);
        config.set(subConfigKey, subConfig);
        set(configKey, config);
    }

    public int getInt(String key){
        return get(key, Double.class).intValue();
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