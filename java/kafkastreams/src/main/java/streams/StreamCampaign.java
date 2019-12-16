package streams;

import models.AdCampaign;
import models.AdCampaignStatistics;
import org.apache.kafka.common.serialization.*;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;

import org.apache.kafka.streams.state.HostInfo;

import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import processors.AdCampaingProcessor;
import serializers.JsonDeserializer;
import serializers.JsonSerializer;
import service.FactorySerde;

import webserver.WebServer;


import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class StreamCampaign {

    public static void main(String[] args) {
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "campaign");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        Topology builder = new Topology();

        FactorySerde factorySerde = FactorySerde.getInstanceFactory();
        Serde<AdCampaign> serdeAdCampaign = factorySerde.generateJsonSerde(AdCampaign.class);
        Serde<AdCampaignStatistics> serdeAdCampaingStatistics = factorySerde.generateJsonSerde(AdCampaignStatistics.class);
        Serde<String> serdeString = factorySerde.generateNativeSerde(String.class);

        StoreBuilder<KeyValueStore<String, AdCampaignStatistics>> clicksCampaigns = Stores.keyValueStoreBuilder(
                Stores.persistentKeyValueStore("clicksCampaigns"),
                Serdes.String(),
                serdeAdCampaingStatistics);

        builder.addSource("Source", serdeString.deserializer(),serdeAdCampaign.deserializer(),"ad_campaign")
                .addProcessor("Process",() -> new AdCampaingProcessor("clicksCampaigns"), "Source")
                .addStateStore(clicksCampaigns,"Process")
                .addSink("Sink","ad_campaign_statistics",serdeString.serializer(),serdeAdCampaingStatistics.serializer(),"Process");


        final KafkaStreams kafka = new KafkaStreams(builder,props);
        final CountDownLatch latch = new CountDownLatch(1);

        HostInfo info = new HostInfo("localhost",8080);

        WebServer server = new WebServer(kafka,info);

        server.startServer();


        Runtime.getRuntime().addShutdownHook(new Thread( () -> {
            kafka.close();
            latch.countDown();
        }));

        try {
            System.out.println("Starting stream processing");
            kafka.start();
            latch.await();
        } catch (InterruptedException e) {
            System.out.println("Error happend");
            System.exit(1);
        }
        System.exit(0);

    }
}
