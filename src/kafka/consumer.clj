(ns kafka.consumer
  (:import (java.net InetAddress)
           (java.util Properties)
           (org.apache.kafka.clients.consumer KafkaConsumer)
           (java.time Duration)
           (org.apache.kafka.common.serialization StringDeserializer)))

(defn build-consumer
  [config topic]
  (let [consumer (new KafkaConsumer config)]
    (.subscribe consumer [topic])
    consumer))

(defn read-msg!
  [consumer]
  (while true
    (let [records (.poll consumer (Duration/ofMillis 100))]
      (doseq [record records]
        (println (str "Processed Value: " (.value record)))))
    (.commitAsync consumer)))

(comment

  ;; generate config
  (def consumer-config (new Properties))
  (.putAll consumer-config {"group.id"           "My-Group"
                            "key.deserializer"   StringDeserializer
                            "value.deserializer" StringDeserializer
                            "client.id"          (.getHostName (InetAddress/getLocalHost))
                            "bootstrap.servers"  "localhost:9092"})

  ;; create a customer client
  (def consumer (atom (build-consumer consumer-config "demo")))

  ;; while loop to receive message
  (swap! consumer read-msg!)
  )

