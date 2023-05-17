(ns kafka.kafka-clients
  (:import (java.net InetAddress)
           (java.util Properties)
           (org.apache.kafka.clients.producer KafkaProducer ProducerRecord)
           (org.apache.kafka.common.serialization StringSerializer StringDeserializer)
           (org.apache.kafka.clients.consumer KafkaConsumer)
           (java.time Duration)))

(defn build-producer-config
  []
  (let [producer-config (new Properties)]
    (.putAll producer-config {"value.serializer"  StringSerializer
                              "key.serializer"    StringSerializer
                              "client.id"         (.getHostName (InetAddress/getLocalHost))
                              "bootstrap.servers" "localhost:9092"
                              "acks"              "all"})
    producer-config))

(defn build-consumer-config
  []
  (let [consumer-config (new Properties)]
    (.putAll consumer-config {"group.id"           "My-Group"
                              "key.deserializer"   StringDeserializer
                              "value.deserializer" StringDeserializer
                              "client.id"          (.getHostName (InetAddress/getLocalHost))
                              "bootstrap.servers"  "localhost:9092"})
    consumer-config))

(defn build-producer
  [config]
  (new KafkaProducer config))

(defn build-consumer
  [config]
  (new KafkaConsumer config))

(defn send-msg!
  [producer record]
  (.send producer record))

(defn subscribe-topic-s!
  [consumer topic-s]
  (.subscribe consumer topic-s))

(defn read-msg!
  [consumer]
  (let [record-s (.poll consumer (Duration/ofMillis 100))]
    (.commitAsync consumer)
    (mapv (fn [record]
            (str "Processed Value: " (.value record)))
          record-s)))

(comment

  ;; build producer config
  (do (def producer-config (build-producer-config))
      producer-config)
  #_=> {"value.serializer"  org.apache.kafka.common.serialization.StringSerializer,
        "acks"              "all",
        "bootstrap.servers" "localhost:9092",
        "key.serializer"    org.apache.kafka.common.serialization.StringSerializer,
        "client.id"         "m-w1kx3xw0nr"}

  ;; build consumer config
  (do (def consumer-config (build-consumer-config))
      consumer-config)
  #_=> {"key.deserializer"   org.apache.kafka.common.serialization.StringDeserializer,
        "value.deserializer" org.apache.kafka.common.serialization.StringDeserializer,
        "group.id"           "My-Group",
        "bootstrap.servers"  "localhost:9092",
        "client.id"          "m-w1kx3xw0nr"}

  ;; build producer
  (do (def producer (build-producer producer-config)))
  #_=> #'kafka.kafka-clients/producer

  ;; build consumer
  (do (def consumer (build-consumer consumer-config)))
  #_=> #'kafka.kafka-clients/consumer

  ;; consumer subscribe topics
  (subscribe-topic-s! consumer ["demo"])
  #_=> nil

  ;; generate record
  (do (def record1 (new ProducerRecord "demo" "Hello world!"))
      record1)
  #_=> object [org.apache.kafka.clients.producer.ProducerRecord
               0x3fb54ba6
               "ProducerRecord(topic=demo, partition=null, headers=RecordHeaders(headers = [], isReadOnly = false), key=null, value=Hello world!, timestamp=null)"]

  ;; producer send record
  (send-msg! producer record1)

  ;; consumer receive message
  (read-msg! consumer)
  #_=> ["Processed Value: Hello world!"]

  )
