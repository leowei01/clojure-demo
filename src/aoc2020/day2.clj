(ns aoc2020.day2
  (:require [aoc2020.util :refer [file->seq]]
            [clojure.string :as str]))

(defn split-with-space [string]
  (-> string
      (str/split #" ")))

(defn split-with-dash [string]
  (->> (-> string
           (str/split #"-"))
       (mapv (fn [s] (parse-long s)))))

(defn valid-password [string]
  (let [[policy ch pw] (split-with-space string)
        [lo hi] (split-with-dash policy)
        ch      (first ch)
        freqs   (frequencies (seq pw))
        ch-freq (if (contains? freqs ch)
                  (get freqs ch)
                  0)]
    (<= lo ch-freq hi)))

(defn count-of-valid-password [entries]
  (->> entries
       (filter valid-password)
       (count)))



(defn valid-password2 [string]
  (let [[policy ch pw] (split-with-space string)
        [pos1 pos2] (split-with-dash policy)
        ch      (first ch)
        pos1?   (= ch (get pw (- pos1 1)))
        pos2?   (= ch (get pw (- pos2 1)))]
    (and (not (and pos1? pos2?))
         (or pos1? pos2?))))

(defn count-of-valid-password2 [entries]
  (->> entries
       (filter valid-password2)
       (count)))





(comment

  (do (def sample-entries (file->seq "aoc2020/day2/sample-input.txt")) sample-entries)
  #_=> ["1-3 a: abcde"
        "1-3 b: cdefg"
        "2-9 c: ccccccccc"]

  (def entries (file->seq "aoc2020/day2/input.txt"))

  ;; Part 1

  ;; split the entries with space to get [policy ch password]
  (split-with-space (first sample-entries))
  #_=> ["1-3" "a:" "abcde"]

  ;; split the policy entry with dash "-" and convert string into int to get [lowest highest]
  (split-with-dash "1-3")
  #_=> [1 3]

  ;; calculate the frequencies of characters in password
  (frequencies "abcde")
  #_=> {\a 1, \b 1, \c 1, \d 1, \e 1}

  ;; get the frequency of ch (if there is no ch in password, frequency = 0)
  (let [freqs (frequencies "abcde")]
    (if (contains? freqs \a)
      (get freqs \a)
      0))
  #_=> 1

  (let [freqs (frequencies "abcde")]
    (if (contains? freqs \z)
      (get freqs \z)
      0))
  #_=> 0

  ;; check if lowest <= frequency <= highest
  (<= 1 1 3)
  #_=> true

  (<= 1 4 3)
  #_=> false

  ;; filter the true entries and count
  (->> sample-entries
       (filter valid-password)
       (count))
  #_=> 2

  ;; count-of-valid-password
  (count-of-valid-password sample-entries)
  #_=> 2

  (count-of-valid-password entries)
  #_=> 445



  ;; Part 2

  ;; split the entries with space to get [policy ch password]
  (split-with-space (first sample-entries))
  #_=> ["1-3" "a:" "abcde"]

  ;; split the policy entry with dash "-" and convert string into int to get [position1 position2]
  (split-with-dash "1-3")
  #_=> [1 3]

  ;; check if the position1 and position2 character is equal to ch
  (let [pos 1]
    (= \a (get "abcde" (- pos 1))))
  #_=> true

  (let [pos 3]
    (= \a (get "abcde" (- pos 1))))
  #_=> false

  ;; check if there is exactly one of these positions contains ch
  (let [pos1? true
        pos2? false]
    (and (not (and pos1? pos2?))
         (or pos1? pos2?)))
  #_=> true

  (let [pos1? true
        pos2? true]
    (and (not (and pos1? pos2?))
         (or pos1? pos2?)))
  #_=> false

  (let [pos1? false
        pos2? false]
    (and (not (and pos1? pos2?))
         (or pos1? pos2?)))
  #_=> false

  ;; filter the true entries and count
  (->> sample-entries
       (filter valid-password2)
       (count))
  #_=> 1

  ;; count-of-valid-password2
  (count-of-valid-password2 sample-entries)
  #_=> 1

  (count-of-valid-password2 sample-entries)
  #_=> 491


  )
