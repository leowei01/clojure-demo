(ns aoc2020.day1
  (:require [aoc2020.util :refer [file->seq]]))

(defn parse-entry-str-s
  "parsing strings to integers"
  [entry-str-s]
  (mapv parse-long entry-str-s))

(defn complement
  "calculate complement"
  [sum entry]
  (- sum entry))

(defn product-of-complement
  "multiply x and its complement"
  [sum entry]
  (when entry
    (* entry (complement sum entry))))

(defn complement-in-entries? [sum entry entry-s]
  (-> (complement sum entry)
      ((partial contains? entry-s))))

(defn two-sum
  "filter x for which both x and its complement are in the sequence"
  [sum entry-s]
  (let [entry-s      (set entry-s)
        entry-result (->> entry-s
                          (filter (fn [entry]
                                    (complement-in-entries? sum entry entry-s)))
                          (first))]
    (product-of-complement sum entry-result)))





(defn three-sum [sum entry-s]
  (let [two-sum-result        (fn [entry]
                                (two-sum (complement sum entry) entry-s))
        two-sum-result-s      (map two-sum-result entry-s)
        product-of-complement (map (fn [entry complement]
                                     (when complement (* entry complement)))
                                   entry-s
                                   two-sum-result-s)]
    (->> product-of-complement
         (filter (fn [entry] entry))
         (first))))


(comment

  ;; sample-input
  (do (def sample-entries (->> (file->seq "aoc2020/day1/sample-input.txt")
                               (parse-entry-str-s)))
      sample-entries)
  #_=> ["1721" "979" "366" "299" "675" "1456"]

  ;; input
  (do (def entries (->> (file->seq "aoc2020/day1/input.txt")
                        (parse-entry-str-s)))
      entries)
  #_=> [1531
        1959
        1344
        1508
        1275
        1729,,,]


  ;; part 1

  ;; decode/parse
  ;; calculate complement
  ;; filter entry whose complement is in entries
  ;; calculate product of n and n's complement

  ;; decode/parse
  (parse-entry-str-s ["-1" "1" "2" "3" "10"])
  #_=> [-1 1 2 3 10]

  ;; calculate complement
  (complement 100 9)
  #_=> 91

  (mapv (partial complement 100)
        [-1 0 11 1000])
  #_=> [101 100 89 -900]


  ;; filter entry whose complement is in entries
  (complement-in-entries? 10 1 #{1 2 3 4})
  #_=> false

  (complement-in-entries? 10 1 #{1 2 3 9})
  #_=> true

  (filter (fn [entry]
            (complement-in-entries? 10 entry entry-s))
          #{1 2 3 9})
  #_=> (1 9)

  (filter (fn [entry]
            (complement-in-entries? 10 entry entry-s))
          #{1 2 3 4})
  #_=> ()

  ;; calculate product of n and n's complement
  (product-of-complement 10 3)
  #_=> 21

  (mapv (partial product-of-complement 10)
        [1 2 3 4])
  #_=> [9 16 21 24]

  ;; two-sum
  (two-sum 2020 sample-entries)
  #_=> 514579

  (two-sum 2020 entries)
  #_=> 567171


  ;; part 2

  ;; calculate product of complements using two-sum
  ;; calculate n * product of complements (ignore nil)
  ;; filter valid result

  ;; three-sum
  (three-sum 2020 sample-entries)
  #_=> 241861950

  (three-sum 2020 entries)
  #_=> 212428694



  ;(defn compl-multiply
  ;  "multiply complements using two-sum"
  ;  [n sum strings]
  ;  (let [compl         (int->compl n sum)
  ;        compl-product (two-sum compl strings)]
  ;    (when compl-product
  ;      (* n compl-product))))
  ;
  ;(defn three-sum [sum strings]
  ;  (let [nums (parse-entries strings)
  ;        compls (map (fn [n]
  ;                      (compl-multiply n sum strings))
  ;                    nums)]
  ;    (->> compls
  ;         (filter (fn [n] n))
  ;         (first))))
  ;
  ;(defn three-sum [sum strings]
  ;  (let [nums (parse-entries strings)
  ;        two-sum-res (->> nums
  ;                 (map (fn [n]
  ;                        [n (two-sum (- sum n) strings)])))
  ;        res-num (->> two-sum-res
  ;                     (filter (fn [n] (second n)))
  ;                     (first))]
  ;    (* (first res-num) (second res-num))))

  )