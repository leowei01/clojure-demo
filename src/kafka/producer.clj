(ns kafka.producer
  (:import (java.net InetAddress)
           (java.util Properties)
           (org.apache.kafka.clients.producer KafkaProducer ProducerRecord)
           (org.apache.kafka.common.serialization StringSerializer)))

(defn build-producer
  [config]
  (new KafkaProducer config))

(defn send-msg!
  [producer record]
  (.send producer record))

(comment

  ;; generate config
  (def producer-config (new Properties))
  (.putAll producer-config
           {"value.serializer"  StringSerializer
            "key.serializer"    StringSerializer
            "client.id"         (.getHostName (InetAddress/getLocalHost))
            "bootstrap.servers" "localhost:9092"
            "acks"              "all"})

  ;; create a producer client
  (def producer (atom (build-producer producer-config)))

  ;; generate and send a record
  (def record (new ProducerRecord "demo" "Hello world!"))
  (swap! producer send-msg! record)

  )
