(ns kafka.kafka-clients
  (:import (java.net InetAddress)
           (org.apache.kafka.clients.producer KafkaProducer ProducerRecord)
           (org.apache.kafka.common.serialization StringSerializer StringDeserializer)
           (org.apache.kafka.clients.consumer KafkaConsumer)
           (java.time Duration)))

(def producer-config-atom
  (atom {"value.serializer"  StringSerializer
         "key.serializer"    StringSerializer
         "client.id"         (.getHostName (InetAddress/getLocalHost))
         "bootstrap.servers" "localhost:9092"
         "acks"              "all"}))

(def consumer-config-atom
  (atom {"group.id"           "My-Group"
         "key.deserializer"   StringDeserializer
         "value.deserializer" StringDeserializer
         "client.id"          (.getHostName (InetAddress/getLocalHost))
         "bootstrap.servers"  "localhost:9092"}))

(defn build-producer
  []
  (new KafkaProducer @producer-config-atom))

(defn build-consumer
  []
  (let [consumer (new KafkaConsumer @consumer-config-atom)
        _        (.subscribe consumer ["demo"])]
    consumer))

(def producer-atom (atom (build-producer)))

(def consumer-atom (atom (build-consumer)))

(defn setup-producer-atom-with-new-producer!
  [new-producer]
  (reset! producer-atom new-producer))

(defn setup-consumer-atom-with-new-consumer!
  [new-consumer]
  (reset! consumer-atom new-consumer))

(defn send-msg!
  [record]
  (.send @producer-atom record))

(defn read-msg!
  []
  (let [consumer @consumer-atom
        record-s (.poll consumer (Duration/ofMillis 100))
        value-s  (mapv (fn [record]
                         (str "Processed Value: " (.value record)))
                       record-s)
        _        (.commitAsync consumer)]
    value-s))

(comment

  producer-config-atom
  #_=> #_#object[clojure.lang.Atom
                 0x31cebdce
                 {:status :ready,
                  :val    {"value.serializer"  org.apache.kafka.common.serialization.StringSerializer,
                           "key.serializer"    org.apache.kafka.common.serialization.StringSerializer,
                           "client.id"         "m-w1kx3xw0nr",
                           "bootstrap.servers" "localhost:9092",
                           "acks"              "all"}}]

  consumer-config-atom
  #_=> #_#object[clojure.lang.Atom
                 0x49058ea5
                 {:status :ready,
                  :val    {"group.id"           "My-Group",
                           "key.deserializer"   org.apache.kafka.common.serialization.StringDeserializer,
                           "value.deserializer" org.apache.kafka.common.serialization.StringDeserializer,
                           "client.id"          "m-w1kx3xw0nr",
                           "bootstrap.servers"  "localhost:9092"}}]


  (build-producer)
  #_=> #_#object[org.apache.kafka.clients.producer.KafkaProducer
                 0x53c5e34f
                 "org.apache.kafka.clients.producer.KafkaProducer@53c5e34f"]

  (build-consumer)
  #_=> #_#object[org.apache.kafka.clients.consumer.KafkaConsumer
                 0x186c605f
                 "org.apache.kafka.clients.consumer.KafkaConsumer@186c605f"]

  producer-atom
  #_=> #_#object[clojure.lang.Atom
                 0x2eba3b87
                 {:status :ready,
                  :val    #object[org.apache.kafka.clients.producer.KafkaProducer
                                  0x56863721
                                  "org.apache.kafka.clients.producer.KafkaProducer@56863721"]}]

  consumer-atom
  #_=> #_#object[clojure.lang.Atom
                 0x67822252
                 {:status :ready,
                  :val    #object[org.apache.kafka.clients.consumer.KafkaConsumer
                                  0x1a8ffcb1
                                  "org.apache.kafka.clients.consumer.KafkaConsumer@1a8ffcb1"]}]

  ;; generate record
  (do (def record (new ProducerRecord "demo" "Hello world!"))
      record)
  #_=> #_#object[org.apache.kafka.clients.producer.ProducerRecord
                 0x3fb54ba6
                 "ProducerRecord(topic=demo, partition=null, headers=RecordHeaders(headers = [], isReadOnly = false), key=null, value=Hello world!, timestamp=null)"]

  ;; producer send record
  (send-msg! record)
  #_=> #_#object[org.apache.kafka.clients.producer.internals.FutureRecordMetadata
                 0xde5318c
                 "org.apache.kafka.clients.producer.internals.FutureRecordMetadata@de5318c"]

  ;; consumer receive message
  (read-msg!)
  #_=> ["Processed Value: Hello world!"]

  ;; build new producer
  (do (def new-producer (build-producer))
      new-producer)
  #_=> #_#object[org.apache.kafka.clients.producer.KafkaProducer
                 0x56863721
                 "org.apache.kafka.clients.producer.KafkaProducer@56863721"]

  ;; setup producer atom with new producer client
  (setup-producer-atom-with-new-producer! new-producer)
  #_=> #_#object[org.apache.kafka.clients.producer.KafkaProducer
                 0x56863721
                 "org.apache.kafka.clients.producer.KafkaProducer@56863721"]

  ;; build new client
  (do (def new-consumer (build-consumer))
      new-consumer)
  #_=> #_#object[org.apache.kafka.clients.consumer.KafkaConsumer
                 0x1129a6bf
                 "org.apache.kafka.clients.consumer.KafkaConsumer@1129a6bf"]

  ;; setup consumer atom with new consumer client
  (setup-consumer-atom-with-new-consumer! new-consumer)
  #_=> #_#object[org.apache.kafka.clients.consumer.KafkaConsumer
                 0x1129a6bf
                 "org.apache.kafka.clients.consumer.KafkaConsumer@1129a6bf"])

