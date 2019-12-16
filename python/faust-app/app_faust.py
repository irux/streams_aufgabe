import faust
import time
import os


def create_app():
    return faust.App(id="streams_test",
                broker=f"kafka://{os.environ.get('KAFKA_CONNECT')}")


app = create_app()


