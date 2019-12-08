from confluent_kafka import Consumer, KafkaException

conf = {'bootstrap.servers': "127.0.0.1:9094", "group.id": 1}

c = Consumer(conf)
c.subscribe(["ad_campaign_statistics"])

while True:
    msg = c.poll(timeout=2.0)
    if msg is None:
        print("Es Nulo")
        continue

    print(msg.value())

