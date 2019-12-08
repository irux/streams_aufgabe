import faust
import time

app = faust.App(id="streams_test",
                broker="kafka://kafka:9092")
