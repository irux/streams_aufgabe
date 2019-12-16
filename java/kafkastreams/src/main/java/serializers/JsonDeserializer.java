package serializers;

import com.google.gson.Gson;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class JsonDeserializer<T> implements Deserializer<T> {


    private Gson gson = new Gson();
    private Class<T> deserializedClass;

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        if(deserializedClass == null) {
            deserializedClass = (Class<T>) configs.get("class");
        }
    }

    @Override
    public T deserialize(String s, byte[] bytes) {
        if(bytes == null){
            return null;
        }
        String jsonData = new String(bytes);
        return (T) gson.fromJson(jsonData,deserializedClass);
    }
}
