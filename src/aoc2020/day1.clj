(ns aoc2020.day1
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn file->seq
  [filename]
  (-> filename
      (io/resource)
      (slurp)
      (str/split-lines)))


(defn strings->integers [strings]
  "parsing strings to integers"
  (let
    [string->integer (fn [x] (Integer/parseInt x))]
    (map string->integer strings)))

(defn seq->set [seq]
  "convert sequence to set"
  (set seq))

(defn seq->map [seq]
  "convert sequence to map"
  (frequencies seq))

(defn complement [x sum]
  "calculate complement"
  (- sum x))

(defn complement-in-set? [x sum seq]
  "detect whether the complement is in the sequence (using set)"
  (let
    [complement (complement x sum)
     s (seq->set seq)]
    (contains? s complement)))

(defn complement-in-map? [x sum seq]
  "detect whether the complement is in the sequence (using map)"
  (let
    [complement (complement x sum)
     m (seq->map seq)]
    (and (contains? m complement)
         (or (-> sum (/ 2) (= x) (not))
             (-> x m (> 1))))))

(defn filter-complement [sum strings]
  "filter x for which both x and its complement are in the sequence"
  (let
    [ints (strings->integers strings)]
    (->> ints
         (filter (fn [x] (complement-in-set? x sum ints))))))

(defn int-complement-multiply [x sum]
  "multiply x and its complement"
  (->> (-> x
           (complement sum))
       (* x)))

(defn sum-2020->mutiply
  [strings sum]
  (-> (->> strings
           (filter-complement sum)
           (first))
    (int-complement-multiply sum)))






(comment

  ;; sample-input
  (do (def sample-entries (file->seq "aoc2020/day1/sample-input.txt"))
      sample-entries)
  #_=> ["1721" "979" "366" "299" "675" "1456"]

  ;; input
  (def sample-entries (file->seq "aoc2020/day1/day1-input.txt"))

  (sum-2020->mutiply sample-entries 2020)
  #_=> 567171






  )