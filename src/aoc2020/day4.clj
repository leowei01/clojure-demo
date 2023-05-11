(ns aoc2020.day4
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(defn file->passport-str-s
  [filename]
  (let [file (-> filename
                 (io/resource)
                 (slurp))]
    (->> (str/split file #"\n\n")
         (map (fn [passport-str] (str/replace passport-str #"\n" " "))))))




(defn parse-passport-str
  [passport-str]
  (let [field-value-s (->> (str/split passport-str #" ")
                           (map (fn [field-value-str]
                                  (str/split field-value-str #":"))))]
    (reduce (fn [output [field value]]
              (assoc output (keyword field) value))
            {}
            field-value-s)))

(defn valid-passport?
  [passport]
  (let [field-s             (->> (map key passport)
                                 (set))
        field-s-without-cid (disj field-s :cid)]
    (>= (count field-s-without-cid) 7)))

(defn count-valid-passport
  [passport-str-s]
  (->> passport-str-s
       (map parse-passport-str)
       (filter valid-passport?)
       (count)))





(defmulti valid-value?
          (fn [field _value] field))

(defmethod valid-value? :byr [_field value]
  (let [digits (parse-long value)]
    (<= 1920 digits 2002)))

(defmethod valid-value? :iyr [_field value]
  (let [digits (parse-long value)]
    (<= 2010 digits 2020)))

(defmethod valid-value? :eyr [_field value]
  (let [digits (parse-long value)]
    (<= 2020 digits 2030)))

(defmethod valid-value? :hgt [_field value]
  (let [unit-of-measure (->> (take-last 2 (seq value))
                             (apply str))
        value           (->> (drop-last 2 (seq value))
                             (apply str)
                             (parse-long))]
    (cond
      (= unit-of-measure "cm") (<= 150 value 193)
      (= unit-of-measure "in") (<= 59 value 76)
      :else false)))

(defmethod valid-value? :hcl [_field value]
  (let [rest-chars     (apply str (rest value))
        filtered-chars (str/replace rest-chars #"[^a-f0-9]" "")]
    (and (= \# (first value))
         (= 6 (count filtered-chars)))))
∏
(defmethod valid-value? :ecl [_field value]
  (let [scope #{"amb" "blu" "brn" "gry" "grn" "hzl" "oth"}]
    (contains? scope value)))

(defmethod valid-value? :pid [_field value]
  (let [pid (str/replace value #"[^0-9]" "")]
    (= (count pid) 9)))

(defn valid-passport?
  [passport]
  (let [field-s             (->> (map key passport)
                                 (set))
        field-s-without-cid (disj field-s :cid)
        valid-field?        (>= (count field-s-without-cid) 7)
        valid-value?        (every? (fn [field]
                                      (valid-value? field (passport field)))
                                    field-s-without-cid)]
    (and valid-field? valid-value?)))



(comment

  (do (def sample-passport-str-s
        (file->passport-str-s "aoc2020/day4/sample-input.txt"))
      sample-passport-str-s)
  #_=> ["ecl:gry pid:860033327 eyr:2020 hcl:#fffffd byr:1937 iyr:2017 cid:147 hgt:183cm"
        "iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884 hcl:#cfa07d byr:1929"
        "hcl:#ae17e1 iyr:2013 eyr:2024 ecl:brn pid:760753108 byr:1931 hgt:179cm"
        "hcl:#cfa07d eyr:2025 pid:166559648 iyr:2011 ecl:brn hgt:59in"]

  (do (def passport-str-s
        (file->passport-str-s "aoc2020/day4/input.txt"))
      passport-str-s)

  (do (def valid-passport-str-s
        (file->passport-str-s "aoc2020/day4/valid-input.txt"))
      valid-passport-str-s)

  (do (def invalid-passport-str-s
        (file->passport-str-s "aoc2020/day4/invalid-input.txt"))
      invalid-passport-str-s)




  ;; part 1

  ;; parse passport-str to password
  ;; validate the password
  ;; filter password-s and count

  ;; parse passport-str to password
  (parse-passport-str (first sample-passport-str-s))
  #_=> {:ecl "gry", :pid "860033327", :eyr "2020", :hcl "#fffffd", :byr "1937", :iyr "2017", :cid "147", :hgt "183cm"}

  (do (def sample-passport-s (map parse-passport-str sample-passport-str-s))
      sample-passport-s)
  #_=> ({:ecl "gry", :pid "860033327", :eyr "2020", :hcl "#fffffd", :byr "1937", :iyr "2017", :cid "147", :hgt "183cm"}
        {:iyr "2013", :ecl "amb", :cid "350", :eyr "2023", :pid "028048884", :hcl "#cfa07d", :byr "1929"}
        {:hcl "#ae17e1", :iyr "2013", :eyr "2024", :ecl "brn", :pid "760753108", :byr "1931", :hgt "179cm"}
        {:hcl "#cfa07d", :eyr "2025", :pid "166559648", :iyr "2011", :ecl "brn", :hgt "59in"})

  ;; validate the password
  (valid-passport? (first sample-passport-s))
  #_=> true

  (map valid-passport? sample-passport-s)
  #_=> (true false true false)

  ;; filter valid passwords and count
  (count-valid-passport sample-passport-str-s)
  #_=> 2

  (count-valid-passport passport-str-s)
  #_=> 200


  ;; part 2

  ;; parse passport-str to password
  ;; validate the field and field value
  ;; filter password-s and count

  ;; parse passport-str to password
  (do (def valid-passport-s (map parse-passport-str valid-passport-str-s))
      valid-passport-s)
  #_=> ({:pid "087499704", :hgt "74in", :ecl "grn", :iyr "2012", :eyr "2030", :byr "1980", :hcl "#623a2f"}
        {:eyr "2029", :ecl "blu", :cid "129", :byr "1989", :iyr "2014", :pid "896056539", :hcl "#a97842", :hgt "165cm"}
        {:hcl "#888785", :hgt "164cm", :byr "2001", :iyr "2015", :cid "88", :pid "545766238", :ecl "hzl", :eyr "2022"}
        {:iyr "2010", :hgt "158cm", :hcl "#b6652a", :ecl "blu", :byr "1944", :eyr "2021", :pid "093154719"})

  (do (def invalid-passport-s (map parse-passport-str invalid-passport-str-s))
      invalid-passport-s)
  #_=> ({:eyr "1972", :cid "100", :hcl "#18171d", :ecl "amb", :hgt "170", :pid "186cm", :iyr "2018", :byr "1926"}
        {:iyr "2019", :hcl "#602927", :eyr "1967", :hgt "170cm", :ecl "grn", :pid "012533040", :byr "1946"}
        {:hcl "dab227", :iyr "2012", :ecl "brn", :hgt "182cm", :pid "021572410", :eyr "2020", :byr "1992", :cid "277"}
        {:hgt "59cm", :ecl "zzz", :eyr "2038", :hcl "74454a", :iyr "2023", :pid "3556412378", :byr "2007"})

  ;; check if the value is valid
  ;; valid byr
  (valid-value? :byr "2002")
  #_=> true
  (valid-value? :byr "2003")
  #_=> false

  ;; valid hgt
  (valid-value? :hgt "60in")
  #_=> true
  (valid-value? :hgt "190cm")
  #_=> true
  (valid-value? :hgt "190in")
  #_=> false
  (valid-value? :hgt "190")
  #_=> false

  ;; valid hcl
  (valid-value? :hcl "#123abc")
  #_=> true
  (valid-value? :hcl "#123abz")
  #_=> false
  (valid-value? :hcl "123abc")
  #_=> false

  ;; valid ecl
  (valid-value? :ecl "brn")
  #_=> true
  (valid-value? :ecl "wat")
  #_=> false

  ;; valid pid
  (valid-value? :pid "000000001")
  #_=> true
  (valid-value? :pid "0123456789")
  #_=> false

  ;; validate the password (check if the field and all values are valid)
  (valid-passport? (first valid-passport-s))
  #_=> true

  (valid-passport? (first invalid-passport-s))
  #_=> false

  (map valid-passport? valid-passport-s)
  #_=> (true true true true)

  (map valid-passport? invalid-passport-s)
  #_=> (false false false false)

  ;; filter valid passwords and count
  (count-valid-passport sample-passport-str-s)
  #_=> 2

  (count-valid-passport passport-str-s)
  #_=> 116








  ;; use function map

  ;(defn valid-byr?
  ;  [byr]
  ;  (let [digits (parse-long byr)]
  ;    (<= 1920 digits 2002)))
  ;
  ;(defn valid-iyr?
  ;  [iyr]
  ;  (let [digits (parse-long iyr)]
  ;    (<= 2010 digits 2020)))
  ;
  ;(defn valid-eyr?
  ;  [eyr]
  ;  (let [digits (parse-long eyr)]
  ;    (<= 2020 digits 2030)))
  ;
  ;(defn valid-hgt?
  ;  [hgt]
  ;  (let [unit-of-measure (->> (take-last 2 (seq hgt))
  ;                             (apply str))
  ;        value           (->> (drop-last 2 (seq hgt))
  ;                             (apply str)
  ;                             (parse-long))]
  ;    (cond
  ;      (= unit-of-measure "cm") (<= 150 value 193)
  ;      (= unit-of-measure "in") (<= 59 value 76)
  ;      :else false)))
  ;
  ;(defn valid-hcl?
  ;  [hcl]
  ;  (let [rest-chars     (apply str (rest hcl))
  ;        filtered-chars (str/replace rest-chars #"[^a-f0-9]" "")]
  ;    (and (= \# (first hcl))
  ;         (= 6 (count filtered-chars)))))
  ;
  ;(defn valid-ecl?
  ;  [ecl]
  ;  (let [scope #{"amb" "blu" "brn" "gry" "grn" "hzl" "oth"}]
  ;    (contains? scope ecl)))
  ;
  ;(defn valid-pid?
  ;  [pid]
  ;  (let [pid (str/replace pid #"[^0-9]" "")]
  ;    (= (count pid) 9)))
  ;
  ;(defn valid-passport?
  ;  [passport]
  ;  (let [field-s              (->> (map key passport)
  ;                                  (set))
  ;        field-s-without-cid  (disj field-s :cid)
  ;        valid-field?         (>= (count field-s-without-cid) 7)
  ;        field-valid-value?-m {:byr valid-byr?
  ;                              :iyr valid-iyr?
  ;                              :eyr valid-eyr?
  ;                              :hgt valid-hgt?
  ;                              :hcl valid-hcl?
  ;                              :ecl valid-ecl?
  ;                              :pid valid-pid?}
  ;        valid-value?         (every? (fn [field]
  ;                                       ((field-valid-value?-m field) (passport field)))
  ;                                     field-s-without-cid)]
  ;    (and valid-field? valid-value?)))
  ;

  )
