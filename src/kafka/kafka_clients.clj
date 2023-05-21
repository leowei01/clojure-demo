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
                           "bootstrap.servers"  "localhost:9092"}}))

(defn build-producer
  [configs]
  (let [producer-config (@configs :producer-config)]
    (new KafkaProducer producer-config)))

(defn build-consumer
  [configs]
  (let [consumer-config (@configs :consumer-config)
        consumer        (new KafkaConsumer consumer-config)
        _               (.subscribe consumer ["demo"])]
    consumer))

(def clients
  (atom {:producer (build-producer configs)
         :consumer (build-consumer configs)}))

(defn send-msg!
  [clients topic value]
  (let [producer (@clients :producer)
        record   (new ProducerRecord topic value)]
    (.send producer record)))

(defn read-msg!
  [clients]
  (let [consumer (@clients :consumer)
        record-s (.poll consumer (Duration/ofMillis 100))
        value-s  (mapv (fn [record]
                         (.value record))
                       record-s)
        _        (.commitAsync consumer)]
    value-s))

(defn setup-clients-with-replacement-producer!
  [clients replacement-producer]
  (swap! clients assoc :producer replacement-producer))

(defn setup-clients-with-replacement-consumer!
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

  (build-producer configs)
  #_=> #_#object[org.apache.kafka.clients.producer.KafkaProducer
                 0x53c5e34f
                 "org.apache.kafka.clients.producer.KafkaProducer@53c5e34f"]

  (build-consumer configs)
  #_=> #_#object[org.apache.kafka.clients.consumer.KafkaConsumer
                 0x186c605f
                 "org.apache.kafka.clients.consumer.KafkaConsumer@186c605f"]

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

  ;; producer send record
  (send-msg! clients "demo" "Hello world!")
  #_=> #_#object[org.apache.kafka.clients.producer.internals.FutureRecordMetadata
                 0xde5318c
                 "org.apache.kafka.clients.producer.internals.FutureRecordMetadata@de5318c"]

  ;; consumer receive message
  (read-msg! clients)
  #_=> ["Hello world!"]

  ;; setup clients with new producer
  (setup-clients-with-replacement-producer! clients (build-producer configs))
  #_=> {:producer #object[org.apache.kafka.clients.producer.KafkaProducer
                          0x730fadc1
                          "org.apache.kafka.clients.producer.KafkaProducer@730fadc1"],
        :consumer #object[org.apache.kafka.clients.consumer.KafkaConsumer
                          0x35a3713e
                          "org.apache.kafka.clients.consumer.KafkaConsumer@35a3713e"]}

  ;; setup clients with new consumer
  (setup-clients-with-replacement-consumer! clients (build-consumer configs))
  #_=> {:producer #object[org.apache.kafka.clients.producer.KafkaProducer
                          0x4801c356
                          "org.apache.kafka.clients.producer.KafkaProducer@4801c356"],
        :consumer #object[org.apache.kafka.clients.consumer.KafkaConsumer
                          0x13aad972
                          "org.apache.kafka.clients.consumer.KafkaConsumer@13aad972"]}

  )



