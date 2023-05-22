(ns kafka.kafka-clients
  (:import (org.apache.kafka.clients.producer KafkaProducer ProducerRecord)
           (org.apache.kafka.common.serialization StringSerializer StringDeserializer)
           (org.apache.kafka.clients.consumer KafkaConsumer)
           (java.time Duration)))

(declare build-producer build-consumer)

(def configs
  (atom {:producer-config {"value.serializer"  StringSerializer
                           "key.serializer"    StringSerializer
                           "client.id"         "m-w1kx3xw0nr"
                           "bootstrap.servers" "localhost:9092"
                           "acks"              "all"}
         :consumer-config {"group.id"           "My-Group"
                           "key.deserializer"   StringDeserializer
                           "value.deserializer" StringDeserializer
                           "client.id"          "m-w1kx3xw0nr"
                           "bootstrap.servers"  "localhost:9092"
                           "topic"              "demo"}}))

(def clients
  (atom {:producer (build-producer (@configs :producer-config))
         :consumer (build-consumer (@configs :consumer-config))}))

(defn build-producer
  [producer-config]
  (new KafkaProducer producer-config))

(defn build-consumer
  [consumer-config]
  (let [consumer (new KafkaConsumer consumer-config)
        topic    (consumer-config "topic")
        _        (.subscribe consumer topic)]
    consumer))

(defn send-msg!
  [producer topic value]
  (let [record (new ProducerRecord topic value)]
    (.send producer record)))

(defn read-msg!
  [consumer]
  (let [record-s (.poll consumer (Duration/ofMillis 100))
        value-s  (mapv (fn [record]
                         (.value record))
                       record-s)
        _        (.commitAsync consumer)]
    value-s))

(defn update-producer-client!
  [clients replacement-producer]
  (swap! clients assoc :producer replacement-producer))

(defn update-consumer-client!
  [clients replacement-consumer]
  (swap! clients assoc :consumer replacement-consumer))





(comment

  configs
  #_=> #_#object[clojure.lang.Atom
                 0x4f73a5d5
                 {:status :ready,
                  :val    {:producer-config {"value.serializer"  org.apache.kafka.common.serialization.StringSerializer,
                                             "key.serializer"    org.apache.kafka.common.serialization.StringSerializer,
                                             "client.id"         "m-w1kx3xw0nr",
                                             "bootstrap.servers" "localhost:9092",
                                             "acks"              "all"},
                           :consumer-config {"group.id"           "My-Group",
                                             "key.deserializer"   org.apache.kafka.common.serialization.StringDeserializer,
                                             "value.deserializer" org.apache.kafka.common.serialization.StringDeserializer,
                                             "client.id"          "m-w1kx3xw0nr",
                                             "bootstrap.servers"  "localhost:9092"}}}]

  clients
  #_=> #_#object[clojure.lang.Atom
                 0x675887e5
                 {:status :ready,
                  :val    {:producer #object[org.apache.kafka.clients.producer.KafkaProducer
                                             0x63003cec
                                             "org.apache.kafka.clients.producer.KafkaProducer@63003cec"],
                           :consumer #object[org.apache.kafka.clients.consumer.KafkaConsumer
                                             0x33adbd3d
                                             "org.apache.kafka.clients.consumer.KafkaConsumer@33adbd3d"]}}]

  (build-producer (@configs :producer-config))
  #_=> #_#object[org.apache.kafka.clients.producer.KafkaProducer
                 0x53c5e34f
                 "org.apache.kafka.clients.producer.KafkaProducer@53c5e34f"]

  (build-consumer (@configs :consumer-config))
  #_=> #_#object[org.apache.kafka.clients.consumer.KafkaConsumer
                 0x186c605f
                 "org.apache.kafka.clients.consumer.KafkaConsumer@186c605f"]

  ;; producer send record
  (send-msg! (@clients :producer) "demo" "Hello world!")
  #_=> #_#object[org.apache.kafka.clients.producer.internals.FutureRecordMetadata
                 0xde5318c
                 "org.apache.kafka.clients.producer.internals.FutureRecordMetadata@de5318c"]

  ;; consumer receive message
  (read-msg! (@clients :consumer))
  #_=> ["Hello world!"]

  ;; setup clients with new producer
  (update-producer-client! clients (build-producer (@configs :producer-config)))
  #_=> {:producer #object[org.apache.kafka.clients.producer.KafkaProducer
                          0x730fadc1
                          "org.apache.kafka.clients.producer.KafkaProducer@730fadc1"],
        :consumer #object[org.apache.kafka.clients.consumer.KafkaConsumer
                          0x35a3713e
                          "org.apache.kafka.clients.consumer.KafkaConsumer@35a3713e"]}

  ;; setup clients with new consumer
  (update-consumer-client! clients (build-consumer (@configs :consumer-config)))
  #_=> {:producer #object[org.apache.kafka.clients.producer.KafkaProducer
                          0x4801c356
                          "org.apache.kafka.clients.producer.KafkaProducer@4801c356"],
        :consumer #object[org.apache.kafka.clients.consumer.KafkaConsumer
                          0x13aad972
                          "org.apache.kafka.clients.consumer.KafkaConsumer@13aad972"]}

  )



