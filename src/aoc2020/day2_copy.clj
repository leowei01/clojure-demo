(ns aoc2020.day2-copy
  (:require [aoc2020.util :refer [file->seq]]
            [clojure.string :as str]))

(defn parse-policy-password-str
  [policy-password-str]
  (let [[policy letter pwd] (str/split policy-password-str #" ")
        [arg1 arg2] (->> (str/split policy #"-")
                         (mapv parse-long))
        letter (first letter)]
    {:policy {:arg1   arg1
              :arg2   arg2
              :letter letter}
     :pwd    pwd}))

(defn valid-password?
  [policy-password]
  (let [{:keys [policy pwd]} policy-password
        {lo     :arg1
         hi     :arg2
         letter :letter} policy
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


(defn valid-password?
  [policy-password]
  (let [{:keys [policy pwd]} policy-password
        {pos1   :arg1
         pos2   :arg2
         letter :letter} policy
        [pos1-equal? pos2-equal?] (mapv (fn [pos] (= letter
                                                     (get pwd (- pos 1))))
                                        [pos1 pos2])
        both?   (and pos1-equal? pos2-equal?)
        either? (or pos1-equal? pos2-equal?)]
    (and (not both?) either?)))





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
  #_=> {:policy {:arg1   1,
                 :arg2   3,
                 :letter \a},
        :pwd    "abcde"}

  ;; validate password based on frequency policy
  (valid-password? {:policy {:arg1   1
                             :arg2   3
                             :letter \a}
                    :pwd    "abcde"})
  #_=> true

  (valid-password? {:policy {:arg1   1
                             :arg2   3
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

  ;; validate password based on position policy
  (valid-password? {:policy {:arg1   1
                             :arg2   3
                             :letter \a}
                    :pwd    "abcde"})
  #_=> true

  (valid-password? {:policy {:arg1   1
                             :arg2   3
                             :letter \a}
                    :pwd    "abade"})
  #_=> false

  ;; filter and count the valid passwords
  (count-valid-password sample-policy-password-str-s)
  #_=> 1

  (count-valid-password policy-password-str-s)
  #_=> 491

  )
