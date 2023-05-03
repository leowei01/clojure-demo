(ns aoc2020.day2
  (:require [aoc2020.util :refer [file->seq]]
            [clojure.string :as str]))


(defn split-with-space [string]
  (-> string
      (str/split #" ")))

(defn split-with-dash [string]
  (->> (-> string
           (str/split #"-"))
       (mapv (fn [s] (Integer/parseInt s)))))

(defn valid-password [string]
  (let [[policy ch pw] (split-with-space string)
        [lo hi] (split-with-dash policy)
        ch      (first ch)
        freqs   (frequencies (seq pw))
        ch-freq (if (contains? freqs ch)
                  (get freqs ch)
                  0)]
    (<= lo ch-freq hi)))

(defn valid-password2 [string]
  (let [[policy ch pw] (split-with-space string)
        [pos1 pos2] (split-with-dash policy)
        ch      (first ch)
        pos1?   (= ch (get pw (- pos1 1)))
        pos2?   (= ch (get pw (- pos2 1)))]
    (and (not (and pos1? pos2?))
         (or pos1? pos2?))))





(comment
  (do (def sample-entries (file->seq "aoc2020/day2/sample-input.txt")) sample-entries)
  (def sample-entries (file->seq "aoc2020/day2/input.txt"))

  ;; Part 1
  (->> sample-entries
       (filter valid-password)
       (count))

  ;; Part 2
  (->> sample-entries
       (filter valid-password2)
       (count))

  )
