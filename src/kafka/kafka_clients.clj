(ns kafka.kafka-clients
  (:import (java.net InetAddress)
           (org.apache.kafka.clients.producer KafkaProducer ProducerRecord)
           (org.apache.kafka.common.serialization StringSerializer StringDeserializer)
           (org.apache.kafka.clients.consumer KafkaConsumer)
           (java.time Duration)))

(defn build-producer-config-atom
  []
  (atom {"value.serializer"  StringSerializer
         "key.serializer"    StringSerializer
         "client.id"         (.getHostName (InetAddress/getLocalHost))
         "bootstrap.servers" "localhost:9092"
         "acks"              "all"}))

(defn build-consumer-config-atom
  []
  (atom {"group.id"           "My-Group"
         "key.deserializer"   StringDeserializer
         "value.deserializer" StringDeserializer
         "client.id"          (.getHostName (InetAddress/getLocalHost))
         "bootstrap.servers"  "localhost:9092"}))

(defn build-producer-atom
  [config]
  (let [config (deref config)]
    (atom (new KafkaProducer config))))

(defn build-consumer-atom
  [config]
  (let [config   (deref config)
        consumer (new KafkaConsumer config)]
    (.subscribe consumer ["demo"])
    (atom consumer)))

(defn send-msg!
  [producer record]
  (let [producer (deref producer)]
    (.send producer record)))

(defn read-msg!
  [consumer]
  (let [consumer (deref consumer)
        record-s (.poll consumer (Duration/ofMillis 100))]
    (.commitAsync consumer)
    (mapv (fn [record]
            (str "Processed Value: " (.value record)))
          record-s)))

(defn build-producer
  [config]
  (let [config (deref config)]
    (new KafkaProducer config)))

(defn build-consumer
  [config]
  (let [config   (deref config)
        consumer (new KafkaConsumer config)]
    (.subscribe consumer ["demo"])
    consumer))

(comment

  ;; build producer config
  (do (def producer-config (build-producer-config-atom))
      producer-config)
  #_=> #_#object[clojure.lang.Atom
                 0x2d4203fd
                 {:status :ready,
                  :val    {"value.serializer"  org.apache.kafka.common.serialization.StringSerializer,
                           "key.serializer"    org.apache.kafka.common.serialization.StringSerializer,
                           "client.id"         "m-w1kx3xw0nr",
                           "bootstrap.servers" "localhost:9092",
                           "acks"              "all"}}]

  ;; build consumer config
  (do (def consumer-config (build-consumer-config-atom))
      consumer-config)
  #_=> #_#object[clojure.lang.Atom
                 0xf91e958
                 {:status :ready,
                  :val    {"group.id"           "My-Group",
                           "key.deserializer"   org.apache.kafka.common.serialization.StringDeserializer,
                           "value.deserializer" org.apache.kafka.common.serialization.StringDeserializer,
                           "client.id"          "m-w1kx3xw0nr",
                           "bootstrap.servers"  "localhost:9092"}}]

  ;; build producer
  (do (def producer (build-producer-atom producer-config))
      producer)
  #_=> #_#object[clojure.lang.Atom
                 0x26014103
                 {:status :ready,
                  :val    #object[org.apache.kafka.clients.producer.KafkaProducer
                                  0x1325259b
                                  "org.apache.kafka.clients.producer.KafkaProducer@1325259b"]}]

  ;; build consumer and subscribe topics
  (do (def consumer (build-consumer-atom consumer-config))
      consumer)
  #_=> #_#object[clojure.lang.Atom
                 0x1c8b04b8
                 {:status :ready,
                  :val    #object[org.apache.kafka.clients.consumer.KafkaConsumer
                                  0x6213626d
                                  "org.apache.kafka.clients.consumer.KafkaConsumer@6213626d"]}]

  ;; generate record
  (do (def record (new ProducerRecord "demo" "Hello world!"))
      record)
  #_=> #_#object[org.apache.kafka.clients.producer.ProducerRecord
                 0x3fb54ba6
                 "ProducerRecord(topic=demo, partition=null, headers=RecordHeaders(headers = [], isReadOnly = false), key=null, value=Hello world!, timestamp=null)"]

  ;; producer send record
  (send-msg! producer record)
  #_=> #_#object[org.apache.kafka.clients.producer.internals.FutureRecordMetadata
                 0xde5318c
                 "org.apache.kafka.clients.producer.internals.FutureRecordMetadata@de5318c"]

  ;; consumer receive message
  (read-msg! consumer)
  #_=> ["Processed Value: Hello world!"]



  ;; if the producer/consumer died, we should rebuild them
  ;; if we want to change the config, we should rebuild the consumer and producer

  ;; modify producer config
  (swap! producer-config assoc "client.id" "id")
  #_=> {"value.serializer"  org.apache.kafka.common.serialization.StringSerializer,
        "key.serializer"    org.apache.kafka.common.serialization.StringSerializer,
        "client.id"         "id",
        "bootstrap.servers" "localhost:9092",
        "acks"              "all"}

  ;; modify consumer config
  (swap! consumer-config assoc "client.id" "id")
  #_=> {"group.id"           "My-Group",
        "key.deserializer"   org.apache.kafka.common.serialization.StringDeserializer,
        "value.deserializer" org.apache.kafka.common.serialization.StringDeserializer,
        "client.id"          "id",
        "bootstrap.servers"  "localhost:9092"}


  ;; modify producer with new config (this build-producer will return a Kafka producer instead of atom)
  (reset! producer (build-producer producer-config))
  #_=> #_#object[org.apache.kafka.clients.producer.KafkaProducer
                 0x576e0952
                 "org.apache.kafka.clients.producer.KafkaProducer@576e0952"]

  ;; modify consumer with new config (this build-consumer will return a Kafka producer instead of atom)
  (reset! consumer (build-consumer consumer-config))
  #_=> #_#object[org.apache.kafka.clients.consumer.KafkaConsumer
                 0x4eb78e47
                 "org.apache.kafka.clients.consumer.KafkaConsumer@4eb78e47"]

  ;; producer send record
  (send-msg! producer record)
  #_=> #_#object[org.apache.kafka.clients.producer.internals.FutureRecordMetadata
                 0xde5318c
                 "org.apache.kafka.clients.producer.internals.FutureRecordMetadata@de5318c"]

  ;; consumer receive message
  (read-msg! consumer)
  #_=> ["Processed Value: Hello world!"]

  )

