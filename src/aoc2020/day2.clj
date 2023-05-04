(ns aoc2020.day2
  (:require [aoc2020.util :refer [file->seq]]
            [clojure.string :as str]))

(defn parse-password-str-with-space [password-str]
  (str/split password-str #" "))

(defn parse-policy-with-dash [policy]
  (->> (str/split policy #"-")
       (mapv parse-long)))

(defn valid-password-with-freq? [password-str]
  (let [[policy ch pw] (parse-password-str-with-space password-str)
        [lo hi] (parse-policy-with-dash policy)
        ch      (first ch)
        freqs   (frequencies (seq pw))
        ch-freq (if (contains? freqs ch)
                  (get freqs ch)
                  0)]
    (<= lo ch-freq hi)))

(defn count-of-valid-password-with-freq [password-str-s]
  (->> password-str-s
       (filter valid-password-with-freq?)
       (count)))



(defn ch-at-pos-equals? [ch pos password]
  (-> (- pos 1)
      ((partial get password))
      (= ch)))

(defn exactly-one? [pos1? pos2?]
  (let [both?   (and pos1? pos2?)
        either? (or pos1? pos2?)]
    (and (not both?) either?)))

(defn valid-password-with-pos? [password-str]
  (let [[policy ch pw] (parse-password-str-with-space password-str)
        [pos1 pos2] (parse-policy-with-dash policy)
        ch    (first ch)
        pos1? (ch-at-pos-equals? ch pos1 pw)
        pos2? (ch-at-pos-equals? ch pos2 pw)]
    (exactly-one? pos1? pos2?)))

(defn count-of-valid-password-with-pos [password-str-s]
  (->> password-str-s
       (filter valid-password-with-pos?)
       (count)))





(comment

  (do (def sample-password-str-s (file->seq "aoc2020/day2/sample-input.txt"))
      sample-password-str-s)
  #_=> ["1-3 a: abcde"
        "1-3 b: cdefg"
        "2-9 c: ccccccccc"]

  (do (def password-str-s (file->seq "aoc2020/day2/input.txt"))
      password-str-s)
  #_=> [" 1-8 n: dpwpmhknmnlglhjtrbpx"
        "11-12 n: frpknnndpntnncnnnnn"
        "4-8 t: tmttdtnttkr"]

  ;; Part 1

  ;; split the entries with space to get [policy ch password]
  (parse-password-str-with-space (first sample-password-str-s))
  #_=> ["1-3" "a:" "abcde"]

  ;; split the policy entry with dash "-" and convert string into integer to get [lowest highest]
  (parse-policy-with-dash "1-3")
  #_=> [1 3]

  (parse-policy-with-dash "1-12")
  #_=> [1 12]

  ;; calculate the frequencies of characters in password
  (frequencies "abcde")
  #_=> {\a 1, \b 1, \c 1, \d 1, \e 1}

  (frequencies "abcda")
  #_=> {\a 2, \b 1, \c 1, \d 1}

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
  (let [lo   1
        freq 1
        hi   3]
    (<= lo freq hi))
  #_=> true

  (let [lo   1
        freq 4
        hi   3]
    (<= lo freq hi))
  #_=> false

  ;; filter the valid passwords and count
  (->> sample-password-str-s
       (filter valid-password-with-freq?)
       (count))
  #_=> 2

  ;; count of valid passwords
  (count-of-valid-password-with-freq sample-password-str-s)
  #_=> 2

  (count-of-valid-password-with-freq password-str-s)
  #_=> 445



  ;; Part 2

  ;; parse passwords with space to get [policy ch password]
  (parse-password-str-with-space (first sample-password-str-s))
  #_=> ["1-3" "a:" "abcde"]

  ;; parse policy with dash "-" and convert string into int to get [position1 position2]
  (parse-policy-with-dash "1-3")
  #_=> [1 3]

  ;; check if the position1 and position2 character is equal to ch
  (let [pos      1
        ch       \a
        password "abcde"]
    (ch-at-pos-equals? ch pos password))
  #_=> true

  (let [pos      3
        ch       \a
        password "abcde"]
    (ch-at-pos-equals? ch pos password))
  #_=> false

  ;; check if there is exactly one of these positions is ch
  (let [pos1? true
        pos2? false]
    (exactly-one? pos1? pos2?))
  #_=> true

  (let [pos1? true
        pos2? true]
    (exactly-one? pos1? pos2?))
  #_=> false

  (let [pos1? false
        pos2? false]
    (exactly-one? pos1? pos2?))
  #_=> false

  ;; filter the true entries and count
  (->> sample-password-str-s
       (filter valid-password-with-pos?)
       (count))
  #_=> 1

  ;; count-of-valid-password2
  (count-of-valid-password-with-pos sample-password-str-s)
  #_=> 1

  (count-of-valid-password-with-pos password-str-s)
  #_=> 491

  )
