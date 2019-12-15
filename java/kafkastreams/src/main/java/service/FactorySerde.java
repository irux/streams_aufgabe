package service;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import serializers.JsonDeserializer;
import serializers.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

public class FactorySerde {
    private static FactorySerde instance;
    Map<String,Object> config = new HashMap<>();

    public  static FactorySerde getInstanceFactory(){
        if(instance == null){
            instance = new FactorySerde();
        }
        return instance;
    }

    private FactorySerde(){

    }

    public <T> Serde<T> generateJsonSerde(Class<T> descriptionClass){
        config.put("class",descriptionClass);
        JsonSerializer<T> serializer = new JsonSerializer<>();
        JsonDeserializer<T> deserializer = new JsonDeserializer<>();
        deserializer.configure(config,false);
        return Serdes.serdeFrom(serializer,deserializer);
    }

    public <T> Serde<T> generateNativeSerde(Class<T> nativeType){
        return Serdes.serdeFrom(nativeType);
    }

}
