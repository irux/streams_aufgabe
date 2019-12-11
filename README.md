## Instructions
The project already contains a docker-compose.yml file that init the faust app, the kafka-zookeeper and the producer-consumer app. Everything in its own container.

The producer-consumer app produce the following object and send it to the **ad_campaign** topic:

Example:

```
{
        "camp_id": "IDCampaign",
        "cookie": "fWEFGETHWGTeqgreeqrgqer34t",
        "is_fake": 0,
        "time":"2019-12-11T10:31:31.222144"
}
```
The counter-Agent transform the mention object to the following structure:

```
{"camp_id": "IDCampaign",
 "fake_clicks": 0,
 "valid_clicks": 0,
 "total": 0,
 "fraud_percent": 0.0,
 "valid_percent": 0.0
}
```

and the agent send the transformation over the **ad_campaign_statistics** topic.


you only need to do:

```
docker-compose up --build
```

Kafka is listening to the container interface in the port 9092 and to localhost in 9094.

If you want to connect to the kafka container outside the docker network, you need to use localhost:9094.

PD:

if you want to restart everything, you need to use ``docker-compose down `` before you restart the environment.