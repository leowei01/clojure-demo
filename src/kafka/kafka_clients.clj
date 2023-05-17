(ns kafka.kafka-clients
  (:import (java.net InetAddress)
           (java.util Properties)
           (org.apache.kafka.clients.producer KafkaProducer ProducerRecord)
           (org.apache.kafka.common.serialization StringSerializer StringDeserializer)
           (org.apache.kafka.clients.consumer KafkaConsumer)
           (java.time Duration)))

(def producer-config (new Properties))
(.putAll producer-config {"value.serializer"  StringSerializer
                          "key.serializer"    StringSerializer
                          "client.id"         (.getHostName (InetAddress/getLocalHost))
                          "bootstrap.servers" "localhost:9092"
                          "acks"              "all"})

(def consumer-config (new Properties))
(.putAll consumer-config {"group.id"           "My-Group"
                          "key.deserializer"   StringDeserializer
                          "value.deserializer" StringDeserializer
                          "client.id"          (.getHostName (InetAddress/getLocalHost))
                          "bootstrap.servers"  "localhost:9092"})

(defn build-producer
  [config]
  (new KafkaProducer config))

(defn build-consumer
  [config topic]
  (let [consumer (new KafkaConsumer config)]
    (.subscribe consumer [topic])
    consumer))

(def producer (build-producer producer-config))

(def consumer (build-consumer consumer-config "demo"))

(defn send-msg!
  [producer record]
  (.send producer record))

(defn read-msg!
  [consumer]
  (while true
    (let [records (.poll consumer (Duration/ofMillis 100))]
      (doseq [record records]
        (println (str "Processed Value: " (.value record)))))
    (.commitAsync consumer)))

(comment

  ;; generate record and send using producer
  (def record (new ProducerRecord "demo" "Hello world!"))
  (send-msg! producer record)

  ;; consumer while loop to receive message
  (read-msg! consumer)

  )
