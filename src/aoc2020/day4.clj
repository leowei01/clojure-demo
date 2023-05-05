(ns aoc2020.day4
  (:require [aoc2020.util :refer [file->seq]]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(defn file->password-str-s [filename]
  (let [file           (-> filename
                           (io/resource)
                           (slurp))
        passport-str-s (str/split file #"\n\n")]
    (map (fn [passport-str] (str/replace passport-str #"\n" " "))
         passport-str-s)))

(defn parse-passport-str [passport-str]
  (let [field-value-s (->> (str/split passport-str #" ")
                           (map (fn [field-value-str]
                                  (str/split field-value-str #":"))))]
    (reduce (fn [output [field value]]
              (assoc output (keyword field) value))
            {}
            field-value-s)))

(defn passport-validate? [passport]
  (let [field-s             (->> (map key passport)
                                 (set))
        field-s-without-cid (disj field-s :cid)]
    (>= (count field-s-without-cid) 7)))

(defn count-valid-passport [filename]
  (let [passport-str-s (file->password-str-s filename)
        passport-s     (map parse-passport-str passport-str-s)]
    (->> (map passport-validate? passport-s)
         (filter (fn [passport] passport))
         (count))))





(defn valid-byr? [byr]
  )

(defn valid-iyr? [iyr]
  )

(defn valid-eyr? [eyr]
  )

(defn valid-hgt? [hgt]
  )

(defn valid-hcl? [hcl]
  )

(defn valid-ecl? [ecl]
  )

(defn valid-pid? [pid]
  )

(defn passport-validate? [passport]
  (let [field-s              (->> (map key passport)
                                  (set))
        field-s-without-cid  (disj field-s :cid)
        valid-field?         (>= (count field-s-without-cid) 7)
        field-valid-value?-m {:byr valid-byr?
                              :iyr valid-iyr?
                              :eyr valid-eyr?
                              :hgt valid-hgt?
                              :hcl valid-hcl?
                              :ecl valid-ecl?
                              :pid valid-pid?}
        valid-value?         (every? (fn [field]
                                       ((field-valid-value?-m field) (passport field)))
                                     field-s-without-cid)]
    (and valid-field? valid-value?)))



(comment

  ;; part 1

  ;; parse file to passport-str-s
  ;; parse passport-str to password
  ;; validate the password
  ;; filter password-s and count

  ;; parse file to passport-str-s
  (do (def sample-passport-str-s
        (file->password-str-s "aoc2020/day4/sample-input.txt"))
      sample-passport-str-s)
  #_=> ("ecl:gry pid:860033327 eyr:2020 hcl:#fffffd byr:1937 iyr:2017 cid:147 hgt:183cm"
         "iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884 hcl:#cfa07d byr:1929"
         "hcl:#ae17e1 iyr:2013 eyr:2024 ecl:brn pid:760753108 byr:1931 hgt:179cm"
         "hcl:#cfa07d eyr:2025 pid:166559648 iyr:2011 ecl:brn hgt:59in")

  ;; parse passport-str to password
  (do (def passport-s (map parse-passport-str sample-passport-str-s))
      passport-s)
  #_=> ({:ecl "gry", :pid "860033327", :eyr "2020", :hcl "#fffffd", :byr "1937", :iyr "2017", :cid "147", :hgt "183cm"}
        {:iyr "2013", :ecl "amb", :cid "350", :eyr "2023", :pid "028048884", :hcl "#cfa07d", :byr "1929"}
        {:hcl "#ae17e1", :iyr "2013", :eyr "2024", :ecl "brn", :pid "760753108", :byr "1931", :hgt "179cm"}
        {:hcl "#cfa07d", :eyr "2025", :pid "166559648", :iyr "2011", :ecl "brn", :hgt "59in"})

  ;; validate the password
  (passport-validate? (first passport-s))
  #_=> true

  (map passport-validate? passport-s)
  #_=> (true false true false)

  ;; filter valid passwords and count
  (count-valid-passport "aoc2020/day4/sample-input.txt")
  #_=> 2

  (count-valid-passport "aoc2020/day4/input.txt")
  #_=> 200


  ;; part 2

  ;; parse file to passport-str-s
  ;; parse passport-str to password
  ;; validate the fields
  ;; validate every field value
  ;; filter password-s and count

  )
