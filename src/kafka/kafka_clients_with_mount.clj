(ns kafka.kafka-clients-with-mount
  (:require [mount.core :as mount :refer [defstate]])
  (:import (org.apache.kafka.clients.producer KafkaProducer ProducerRecord)
           (org.apache.kafka.common.serialization StringSerializer StringDeserializer)
           (org.apache.kafka.clients.consumer KafkaConsumer)
           (java.time Duration)))

(declare build-producer build-consumer)

(defstate configs :start {:producer-config {"value.serializer"  StringSerializer
                                            "key.serializer"    StringSerializer
                                            "client.id"         "m-w1kx3xw0nr"
                                            "bootstrap.servers" "localhost:9092"
                                            "acks"              "all"}
                          :consumer-config {"group.id"           "My-Group"
                                            "key.deserializer"   StringDeserializer
                                            "value.deserializer" StringDeserializer
                                            "client.id"          "m-w1kx3xw0nr"
                                            "bootstrap.servers"  "localhost:9092"
                                            "topic"              "demo"}})

(defstate clients :start {:producer (build-producer (configs :producer-config))
                          :consumer (build-consumer (configs :consumer-config))})

(defn build-producer
  [producer-config]
  (new KafkaProducer producer-config))

(defn build-consumer
  [consumer-config]
  (let [consumer (new KafkaConsumer consumer-config)
        topic    (consumer-config "topic")
        _        (.subscribe consumer [topic])]
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

(mount/start)



(comment

  configs
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

  clients
  #_=> {:producer #object[org.apache.kafka.clients.producer.KafkaProducer
                          0x19848ca7
                          "org.apache.kafka.clients.producer.KafkaProducer@19848ca7"],
        :consumer #object[org.apache.kafka.clients.consumer.KafkaConsumer
                          0x5e8a9ca7
                          "org.apache.kafka.clients.consumer.KafkaConsumer@5e8a9ca7"]}


  (do (def producer (build-producer (configs :producer-config)))
      producer)
  #_=> #_#object[org.apache.kafka.clients.producer.KafkaProducer
                 0x53c5e34f
                 "org.apache.kafka.clients.producer.KafkaProducer@53c5e34f"]

  (do (def consumer (build-consumer (configs :consumer-config)))
      consumer)
  #_=> #_#object[org.apache.kafka.clients.consumer.KafkaConsumer
                 0x186c605f
                 "org.apache.kafka.clients.consumer.KafkaConsumer@186c605f"]

  ;; producer send record
  (send-msg! (clients :producer) "demo" "Hello world!")
  #_=> #_#object[org.apache.kafka.clients.producer.internals.FutureRecordMetadata
                 0xde5318c
                 "org.apache.kafka.clients.producer.internals.FutureRecordMetadata@de5318c"]

  ;; consumer receive message
  (read-msg! (clients :consumer))
  #_=> ["Hello world!"]

  )



