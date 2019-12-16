package webserver;

import models.AdCampaignStatistics;
import org.apache.kafka.connect.errors.NotFoundException;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.HostInfo;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import static spark.Spark.*;

public class AdCampaignController {

    private KafkaStreams streams;
    private HostInfo hostInfo;


    AdCampaignController(KafkaStreams streams, HostInfo hostInfo){
        this.streams = streams;
        this.hostInfo = hostInfo;
    }


    public Route getAllAdCampaign = (Request request, Response respose) ->{
        final ReadOnlyKeyValueStore<String, AdCampaignStatistics> store = streams.store("clicksCampaigns", QueryableStoreTypes.keyValueStore());

        if(store == null){
            halt(404,"NotFoundRecord");
        }

        List<AdCampaignStatistics> listOfAdStats = new ArrayList<AdCampaignStatistics>();

        KeyValueIterator<String,AdCampaignStatistics> iter = store.all();
        while(iter.hasNext()){
            KeyValue<String,AdCampaignStatistics> pair = iter.next();
            listOfAdStats.add(pair.value);
        }

        return listOfAdStats;

    };


    public Route getCampaign = (Request request, Response respose) ->{
        final ReadOnlyKeyValueStore<String, AdCampaignStatistics> store = streams.store("clicksCampaigns", QueryableStoreTypes.keyValueStore());

        if(store == null){
            halt(404,"NotFoundRecord");
        }

        String id = request.params(":id");

        AdCampaignStatistics adStats = store.get(id);

        if(adStats == null){
            halt(404,"NotFoundRecord");
        }

        return adStats;

    };

}
