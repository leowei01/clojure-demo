(ns aoc2020.day2
  (:require [aoc2020.util :refer [file->seq]]
            [clojure.string :as str]))

(defn parse-policy-password-str
  [policy-password-str]
  (let [[policy letter pwd] (str/split policy-password-str #" ")
        [lo hi] (->> (str/split policy #"-")
                     (mapv parse-long))
        letter (first letter)]
    {:policy {:lo     lo
              :hi     hi
              :letter letter}
     :pwd    pwd}))

(defn valid-password?
  [policy-password]
  (let [{:keys [policy pwd]} policy-password
        {:keys [lo hi letter]} policy
        freqs (frequencies (seq pwd))
        {letter-freq letter :or {letter-freq 0}} freqs]
    (<= lo letter-freq hi)))

(defn count-valid-password
  [policy-password-str-s]
  (let [policy-password-s (->> policy-password-str-s
                               (map parse-policy-password-str))]
    (->> policy-password-s
         (filter valid-password?)
         (count))))



(defn parse-policy-password-str
  [policy-password-str]
  (let [[policy letter pwd] (str/split policy-password-str #" ")
        [pos1 pos2] (->> (str/split policy #"-")
                         (mapv parse-long))
        letter (first letter)]
    {:policy {:pos1   pos1
              :pos2   pos2
              :letter letter}
     :pwd    pwd}))

(defn valid-password?
  [policy-password]
  (let [{:keys [policy pwd]} policy-password
        {:keys [pos1 pos2 letter]} policy
        letter-pos1 (get pwd (- pos1 1))
        letter-pos2 (get pwd (- pos2 1))
        pos1?       (= letter letter-pos1)
        pos2?       (= letter letter-pos2)
        both?       (and pos1? pos2?)
        either?     (or pos1? pos2?)]
    (and (not both?) either?)))

(defn count-valid-password
  [policy-password-str-s]
  (let [policy-password-s (->> policy-password-str-s
                               (map parse-policy-password-str))]
    (->> policy-password-s
         (filter valid-password?)
         (count))))





(comment

  (do (def sample-policy-password-str-s (file->seq "aoc2020/day2/sample-input.txt"))
      sample-policy-password-str-s)
  #_=> ["1-3 a: abcde"
        "1-3 b: cdefg"
        "2-9 c: ccccccccc"]

  (do (def policy-password-str-s (file->seq "aoc2020/day2/input.txt"))
      policy-password-str-s)
  #_=> [" 1-8 n: dpwpmhknmnlglhjtrbpx"
        "11-12 n: frpknnndpntnncnnnnn"
        "4-8 t: tmttdtnttkr"]

  ;; Part 1

  ;; parse strings into policies and passwords
  ;; validate password based on frequency policy
  ;; filter and count the valid passwords


  ;; parse strings into policies and passwords
  (parse-policy-password-str (first sample-policy-password-str-s))
  #_=> {:policy {:lo     1,
                 :hi     3,
                 :letter \a},
        :pwd    "abcde"}

  ;; validate password based on frequency policy
  (valid-password? {:policy {:lo     1
                             :hi     3
                             :letter \a}
                    :pwd    "abcde"})
  #_=> true

  (valid-password? {:policy {:lo     1
                             :hi     3
                             :letter \z}
                    :pwd    "abcde"})
  #_=> false

  ;; filter and count the valid passwords
  (count-valid-password sample-policy-password-str-s)
  #_=> 2

  (count-valid-password policy-password-str-s)
  #_=> 445



  ;; Part 2

  ;; parse strings into policies and passwords
  ;; validate password based on position policy
  ;; filter and count the valid passwords

  ;; parse strings into policies and passwords
  (parse-policy-password-str (first sample-policy-password-str-s))
  #_=> {:policy {:pos1   1,
                 :pos2   3,
                 :letter \a},
        :pwd    "abcde"}

  ;; validate password based on position policy
  (valid-password? {:policy {:pos1   1
                             :pos2   3
                             :letter \a}
                    :pwd    "abcde"})
  #_=> true

  (valid-password? {:policy {:pos1   1
                             :pos2   3
                             :letter \a}
                    :pwd    "abade"})
  #_=> false

  ;; filter and count the valid passwords
  (count-valid-password sample-policy-password-str-s)
  #_=> 1

  (count-valid-password policy-password-str-s)
  #_=> 491

  )
