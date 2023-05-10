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

(defn two-sum
  "filter x for which both x and its complement are in the sequence"
  [sum entry-str-s]
  (let [entry-s      (->> entry-str-s
                          (parse-entry-str-s)
                          (set))
        complement-s (->> entry-s
                          (map (partial complement sum)))
        entry-result (->> complement-s
                          (filter (fn [complement]
                                    (contains? entry-s complement)))
                          (first))]
    (when entry-result
      (* entry-result
         (complement sum entry-result)))))





(defn three-sum
  [sum entry-str-s]
  (let [entry-s               (->> entry-str-s
                                   (parse-entry-str-s)
                                   (set))
        two-sum-result-s      (->> entry-s
                                   (map (partial complement sum))
                                   (map (fn [complement]
                                          (two-sum complement entry-str-s))))
        product-of-complement (map (fn [entry complement]
                                     (when complement
                                       (* entry complement)))
                                   entry-s
                                   two-sum-result-s)]
    (->> product-of-complement
         (filter (fn [entry] entry))
         (first))))


(comment

  ;; sample-input
  (do (def sample-entry-str-s (->> (file->seq "aoc2020/day1/sample-input.txt")
                                   (parse-entry-str-s)))
      sample-entry-str-s)
  #_=> [1721 979 366 299 675 1456]

  ;; input
  (do (def entry-str-s (->> (file->seq "aoc2020/day1/input.txt")
                            (parse-entry-str-s)))
      entry-str-s)
  #_=> [1531
        1959
        1344
        1508
        1275
        1729,,,]


  ;; part 1

  ;; decode/parse
  ;; calculate complement
  ;; filter entry whose complement is in entries and calculate product

  ;; decode/parse
  (parse-entry-str-s ["-1" "1" "2" "3" "10"])
  #_=> [-1 1 2 3 10]

  ;; calculate complement
  (complement 100 9)
  #_=> 91

  (mapv (partial complement 100)
        [-1 0 11 1000])
  #_=> [101 100 89 -900]

  ;; filter entries and calculate product
  (two-sum 10 ["1" "9" "2" "3"])
  #_=> 9

  (two-sum 2020 sample-entry-str-s)
  #_=> 514579

  (two-sum 2020 entry-str-s)
  #_=> 567171


  ;; part 2

  ;; calculate product of complements using two-sum
  ;; calculate product and filter non-nil result

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


  ;
  ;(defn complement-in-entries?
  ;  [sum entry entry-s]
  ;  (-> (complement sum entry)
  ;      ((partial contains? entry-s))))
  ;
  ;(defn two-sum
  ;  "filter x for which both x and its complement are in the sequence"
  ;  [sum entry-s]
  ;  (let [entry-s      (set entry-s)
  ;        entry-result (->> entry-s
  ;                          (filter (fn [entry]
  ;                                    (complement-in-entries? sum entry entry-s)))
  ;                          (first))]
  ;    (when entry-result
  ;      (-> entry-result
  ;          (partial complement sum)
  ;          (* entry-result)))))

  )