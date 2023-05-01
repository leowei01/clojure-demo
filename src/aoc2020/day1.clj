(ns aoc2020.day1
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn file->seq
  [filename]
  (-> filename
      (io/resource)
      (slurp)
      (str/split-lines)))


(defn sum-2020->mutiply
  "using set to find the complement"
  [xs]
  (->>
    (let
      [xs          (map #(Integer/parseInt %) xs)
       s           (set xs)
       complement? #(contains? s (- 2020 %))]
      (filter complement? xs))
    (first)
    (#(* % (- 2020 %)))))


(defn sum-2020->mutiply
  "using map to find the complement,
  avoid the complement is itself"
  [xs]
  (->>
    (let
      [xs          (map #(Integer/parseInt %) xs)
       m           (frequencies xs)
       complement? #(and (contains? m (- 2020 %))
                         (or (not (= % 1010)) (> (get m %) 1)))]
      (filter complement? xs))
    (first)
    (#(* % (- 2020 %)))))






(comment

  ;; sample-input
  (do (def sample-entries (file->seq "aoc2020/day1/sample-input.txt"))
      sample-entries)
  #_=> ["1721" "979" "366" "299" "675" "1456"]

  ;; input
  (def sample-entries (file->seq "aoc2020/day1/day1-input.txt"))

  (sum-2020->mutiply sample-entries)
  #_=> 567171






  )