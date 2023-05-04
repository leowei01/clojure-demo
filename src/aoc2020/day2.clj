(ns aoc2020.day2
  (:require [aoc2020.util :refer [file->seq]]
            [clojure.string :as str]))

(defn parse-policy-password-str-with-space [password-str]
  (str/split password-str #" "))

(defn parse-policy-with-dash [policy]
  (->> (str/split policy #"-")
       (mapv parse-long)))

(defn valid-password-with-freq? [policy-password-str]
  (let [[policy letter pwd] (parse-policy-password-str-with-space policy-password-str)
        [lo hi] (parse-policy-with-dash policy)
        letter (first letter)
        freqs  (frequencies (seq pwd))
        {letter-freq letter :or {letter-freq 0}} freqs]
    (<= lo letter-freq hi)))

(defn count-of-valid-password-with-freq [policy-password-str-s]
  (->> policy-password-str-s
       (filter valid-password-with-freq?)
       (count)))



(defn letter-at-pos-equals? [{:keys [letter pos password]}]
  (let [pos-letter (get password (- pos 1))]
    (= letter pos-letter)))
∏
(defn exactly-one? [pos1? pos2?]
  (let [both?   (and pos1? pos2?)
        either? (or pos1? pos2?)]
    (and (not both?) either?)))

(defn valid-policy-password-with-pos? [policy-password-str]
  (let [[policy letter pwd] (parse-policy-password-str-with-space policy-password-str)
        [pos1 pos2] (parse-policy-with-dash policy)
        letter (first letter)
        pos1?  (letter-at-pos-equals? {:letter   letter
                                       :pos      pos1
                                       :password pwd})
        pos2?  (letter-at-pos-equals? {:letter   letter
                                       :pos      pos2
                                       :password pwd})]
    (exactly-one? pos1? pos2?)))

(defn count-of-valid-password-with-pos [policy-password-str-s]
  (->> policy-password-str-s
       (filter valid-policy-password-with-pos?)
       (count)))





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

  ;; split the entries with space to get [policy ch password]
  (parse-policy-password-str-with-space (first sample-policy-password-str-s))
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
  (let [freqs (frequencies "abcde")
        {letter-freq \a :or {letter-freq 0}} freqs]
    letter-freq)
  #_=> 1

  (let [freqs (frequencies "abcde")
        {letter-freq \z :or {letter-freq 0}} freqs]
    letter-freq)
  #_=> 0

  ;; check if lowest <= frequency <= highest
  (let [[lo freq hi] [1 1 3]]
    (<= lo freq hi))
  #_=> true

  (let [[lo freq hi] [1 4 3]]
    (<= lo freq hi))
  #_=> false

  ;; filter the valid passwords and count
  (->> sample-policy-password-str-s
       (filter valid-policy-password-with-freq?)
       (count))
  #_=> 2

  ;; count of valid passwords
  (count-of-valid-password-with-freq sample-policy-password-str-s)
  #_=> 2

  (count-of-valid-password-with-freq policy-password-str-s)
  #_=> 445



  ;; Part 2

  ;; parse passwords with space to get [policy ch password]
  (parse-policy-password-str-with-space (first sample-policy-password-str-s))
  #_=> ["1-3" "a:" "abcde"]

  ;; parse policy with dash "-" and convert string into int to get [position1 position2]
  (parse-policy-with-dash "1-3")
  #_=> [1 3]

  ;; check if the position1 and position2 character is equal to ch
  (letter-at-pos-equals? {:letter   \a
                          :pos      1
                          :password "abcde"})
  #_=> true

  (letter-at-pos-equals? {:letter   \a
                          :pos      3
                          :password "abcde"})
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
  (->> sample-policy-password-str-s
       (filter valid-policy-password-with-pos?)
       (count))
  #_=> 1

  ;; count-of-valid-password2
  (count-of-valid-password-with-pos sample-policy-password-str-s)
  #_=> 1

  (count-of-valid-password-with-pos policy-password-str-s)
  #_=> 491

  )
