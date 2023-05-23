(ns kafka.kafka-clients
  (:import (org.apache.kafka.clients.producer KafkaProducer ProducerRecord)
           (org.apache.kafka.common.serialization StringSerializer StringDeserializer)
           (org.apache.kafka.clients.consumer KafkaConsumer)
           (java.time Duration)))

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
  (atom {:producer nil
         :consumer nil}))

(defn build-producer
  [producer-config]
  (new KafkaProducer producer-config))

(defn build-consumer
  [consumer-config]
  (let [consumer (new KafkaConsumer consumer-config)
        topic    (consumer-config "topic")
        _        (.subscribe consumer [topic])]
    consumer))

(defn update-producer-client!
  [clients replacement-producer]
  (swap! clients assoc :producer replacement-producer))

(defn update-consumer-client!
  [clients replacement-consumer]
  (swap! clients assoc :consumer replacement-consumer))

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



(comment

  @configs
  #_=> {:producer-config {"value.serializer"  org.apache.kafka.common.serialization.StringSerializer,
                          "key.serializer"    org.apache.kafka.common.serialization.StringSerializer,
                          "client.id"         "m-w1kx3xw0nr",
                          "bootstrap.servers" "localhost:9092",
                          "acks"              "all"},
        :consumer-config {"group.id"           "My-Group",
                          "key.deserializer"   org.apache.kafka.common.serialization.StringDeserializer,
                          "value.deserializer" org.apache.kafka.common.serialization.StringDeserializer,
                          "client.id"          "m-w1kx3xw0nr",
                          "bootstrap.servers"  "localhost:9092",
                          "topic"              "demo"}}

  @clients
  #_=> {:producer nil, :consumer nil}

  (do (def producer (build-producer (@configs :producer-config)))
      producer)
  #_=> #_#object[org.apache.kafka.clients.producer.KafkaProducer
                 0x53c5e34f
                 "org.apache.kafka.clients.producer.KafkaProducer@53c5e34f"]

  (do (def consumer (build-consumer (@configs :consumer-config)))
      consumer)
  #_=> #_#object[org.apache.kafka.clients.consumer.KafkaConsumer
                 0x186c605f
                 "org.apache.kafka.clients.consumer.KafkaConsumer@186c605f"]

  ;; setup clients with new producer
  (update-producer-client! clients producer)
  #_=> {:producer #object[org.apache.kafka.clients.producer.KafkaProducer
                          0x4e126a30
                          "org.apache.kafka.clients.producer.KafkaProducer@4e126a30"],
        :consumer nil}

  ;; setup clients with new consumer
  (update-consumer-client! clients consumer)
  #_=> {:producer #object[org.apache.kafka.clients.producer.KafkaProducer
                          0x4e126a30
                          "org.apache.kafka.clients.producer.KafkaProducer@4e126a30"],
        :consumer #object[org.apache.kafka.clients.consumer.KafkaConsumer
                          0x3ffcf28e
                          "org.apache.kafka.clients.consumer.KafkaConsumer@3ffcf28e"]}

  ;; producer send record
  (send-msg! (@clients :producer) "demo" "Hello world!")
  #_=> #_#object[org.apache.kafka.clients.producer.internals.FutureRecordMetadata
                 0xde5318c
                 "org.apache.kafka.clients.producer.internals.FutureRecordMetadata@de5318c"]

  ;; consumer receive message
  (read-msg! (@clients :consumer))
  #_=> ["Hello world!"]

  )



