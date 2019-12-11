#!/usr/bin/env python
import threading, logging, time
import multiprocessing
import string
import json
from kafka import KafkaConsumer, KafkaProducer
import random
from datetime import datetime
import os
random.seed(20)

server_location = os.environ.get("KAFKA_CONNECT")

class Producer(threading.Thread):

    camp_ids = [f"camp_id_number{x}" for x in range(20)]

    def __init__(self):
        threading.Thread.__init__(self)
        self.stop_event = threading.Event()

    def stop(self):
        self.stop_event.set()

    def run(self):
        producer = KafkaProducer(bootstrap_servers=server_location)

        while not self.stop_event.is_set():
            analytics_click = self._generate_click()
            analytics_json = json.dumps(analytics_click)
            producer.send('ad_campaign', bytes(analytics_json, "utf-8"))
            time.sleep(1)

        producer.close()

    def _generate_cookie(self):
        posibilities = string.ascii_letters + string.digits
        return "".join(random.choice(posibilities) for x in range(10))

    def _is_fake_generator(self):
        return random.randint(0, 1)

    def _generate_click(self):
        return {
            "camp_id": random.choice(self.camp_ids),
            "cookie": self._generate_cookie(),
            "is_fake": self._is_fake_generator(),
            "time": datetime.now().isoformat()
        }


class Consumer(multiprocessing.Process):
    def __init__(self):
        multiprocessing.Process.__init__(self)
        self.stop_event = multiprocessing.Event()

    def stop(self):
        self.stop_event.set()

    def run(self):
        consumer = KafkaConsumer(bootstrap_servers=server_location,
                                 auto_offset_reset='earliest',
                                 consumer_timeout_ms=2000)
        consumer.subscribe(['ad_campaign_statistics'])

        while not self.stop_event.is_set():
            for message in consumer:
                print(message.value, flush=True)
                if self.stop_event.is_set():
                    break

        consumer.close()


def main():
    time.sleep(40)

    tasks = [
        Producer(),
        Consumer()
    ]


    for t in tasks:
        t.start()

    time.sleep(120)

    for task in tasks:
        task.stop()

    for task in tasks:
        task.join()


if __name__ == "__main__":
    logging.basicConfig(
        format='%(asctime)s.%(msecs)s:%(name)s:%(thread)d:%(levelname)s:%(process)d:%(message)s',
        level=logging.INFO
    )
    main()
