import faust
import time


def create_app():
    return faust.App(id="streams_test",
                broker="kafka://kafka:9092")


app = create_app()


