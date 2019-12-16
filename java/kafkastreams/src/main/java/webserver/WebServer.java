package webserver;

import models.AdCampaign;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.state.HostInfo;
import static spark.Spark.*;


public class WebServer {

    private KafkaStreams streams;
    private HostInfo hostInfo;

    public WebServer(KafkaStreams streams, HostInfo hostInfo){
        this.hostInfo = hostInfo;
        this.streams = streams;
    }


    public void startServer(){
        port(this.hostInfo.port());
        this.initRoutes();
    }

    private void initRoutes(){
        AdCampaignController adController = new AdCampaignController(this.streams,this.hostInfo);

        JsonTransformer serializerJson = new JsonTransformer();

        get("/campaign/stats",adController.getAllAdCampaign,serializerJson);
        get("/campaign/stats/:id",adController.getCampaign,serializerJson);
    }

}
