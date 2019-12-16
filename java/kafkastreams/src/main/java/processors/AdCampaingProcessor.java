package processors;

import models.AdCampaign;
import models.AdCampaignStatistics;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.PunctuationType;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;

import java.time.Duration;

public class AdCampaingProcessor implements Processor<String, AdCampaign> {

    private ProcessorContext context;
    private KeyValueStore<String, AdCampaignStatistics> kvStore;
    private String nameStore;

    public AdCampaingProcessor(String nameStore){
        this.nameStore = nameStore;
    }

    @Override
    public void init(ProcessorContext processorContext) {
        this.context = processorContext;
        this.kvStore = (KeyValueStore<String,AdCampaignStatistics>) this.context.getStateStore(this.nameStore);
    }

    @Override
    public void process(String s, AdCampaign s2) {

        if(s2.getCamp_id() == null){
            return;
        }

        if(!this.isAdCampaingStatisticThereWithId(s2.getCamp_id())){
            this.kvStore.put(s2.getCamp_id(),new AdCampaignStatistics(s2.getCamp_id()));
        }
        AdCampaignStatistics campaign = this.kvStore.get(s2.getCamp_id());
        if(s2.getIs_fake() == 1){
            campaign.addFakeClick();
        }

        if(s2.getIs_fake() == 0){
            campaign.addValidClick();
        }

        this.kvStore.put(s2.getCamp_id(),campaign);

        this.forwardCampaingToStream(campaign);
    }

    private void forwardCampaingToStream(AdCampaignStatistics campaign){
        this.context.forward(null,campaign);
        this.context.commit();
    }

    private boolean isAdCampaingStatisticThereWithId(String ID){
        AdCampaignStatistics campaign = this.kvStore.get(ID);
        if(campaign == null){
            return false;
        }
        return true;
    }

    @Override
    public void close() {

    }
}
