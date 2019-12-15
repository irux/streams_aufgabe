package serializers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.kafka.common.serialization.Serializer;

import java.lang.reflect.Type;
import java.nio.charset.Charset;

public class JsonSerializer<T> implements Serializer<T> {

    private Gson gson = new Gson();


    @Override
    public byte[] serialize(String s, T t) {
        return gson.toJson(t).getBytes(Charset.forName("UTF-8"));
    }
}
