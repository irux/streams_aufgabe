from confluent_kafka.admin import AdminClient, NewTopic, NewPartitions, ConfigResource, ConfigSource

a = AdminClient({'bootstrap.servers': "127.0.0.1:9092"})

x = a.list_topics(timeout=10)

print(x)

#a.create_topics([NewTopic("test", num_partitions=3, replication_factor=1)])