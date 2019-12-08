
from confluent_kafka import Producer
import json


conf = {'bootstrap.servers': "127.0.0.1:9094"}

# Create Producer instance
p = Producer(**conf)

p.poll(0)

object_to_send = {
    "camp_id": "test3",
    "cookie": "test",
    "is_fake": 0
}

p.produce("ad_campaign", json.dumps(object_to_send))

p.flush(timeout=3)






