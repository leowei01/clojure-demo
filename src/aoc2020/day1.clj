(ns aoc2020.day1
  (:require [aoc2020.util :refer [file->seq]]))

(defn parse-s
  "parsing strings to integers"
  [s]
  (map (fn [str]
         (parse-long str))
       s))

(defn compl
  "calculate complement"
  [sum n]
  (- sum n))

(defn product-of-compl
  "multiply x and its complement"
  [sum n]
  (when n
    (* n (compl sum n))))

(defn two-sum
  "filter x for which both x and its complement are in the sequence"
  [sum s]
  (let [nums         (->> s
                          (parse-s)
                          (set))
        compl-in-entries? (fn [n]
                       (contains? nums (compl sum n)))
        res-num      (->> nums
                          (filter compl-in-entries?)
                          (first))]
    (product-of-compl sum res-num)))





(defn three-sum [sum s]
  (let [nums             (parse-s s)
        two-sum-res      (fn [n]
                           (two-sum (compl sum n) s))
        two-sum-res-s    (map two-sum-res nums)
        product-of-compl (map (fn [n compl]
                                (when compl (* n compl)))
                              nums
                              two-sum-res-s)]
    (->> product-of-compl
         (filter (fn [n] n))
         (first))))


(comment

  ;; sample-input
  (do (def sample-entries (file->seq "aoc2020/day1/sample-input.txt"))
      sample-entries)
  #_=> ["1721" "979" "366" "299" "675" "1456"]

  ;; input
  (def sample-entries (file->seq "aoc2020/day1/input.txt"))
  #_=> [1531
        1959
        1344
        1508
        1275
        1729,,,]


  ;; part 1

  ;; decode/parse

  (parse-s ["1721" "979" "366" "299" "675" "1456"])
  #_=> (1721 979 366 299 675 1456)

  ;; calculate complement

  (compl 2020 1721)
  #_=> 299

  (mapv (partial compl 2020)
        [1721 979 366 299 675 1456])
  #_=> [299 1041 1654 1721 1345 564]

  ;; filter whose complement in entries

  (let [entry-s      (set [299 1041 1654 1721 1345 564])
        compl-in-entries? (fn [n]
                       (contains? entry-s (compl 2020 n)))]
    (filter compl-in-entries? entry-s))
  #_=> (299 1721)

  (let [entry-s      (set [1 2 3 4])
        compl-in-entries? (fn [n]
                       (contains? entry-s (compl 10 n)))]
    (filter compl-in-entries? entry-s))
  #_=> ()

  ;; product of n and n's complement

  (product-of-compl 10 3)
  #_=> 21

  (mapv (partial product-of-compl 10)
        [1 2 3 4])
  #_=> [9 16 21 24]

  ;; two-sum

  (two-sum 2020 sample-entries)
  #_=> 567171


  ;; part 2

  ;; calculate product of complements using two-sum

  (let [s           ["1721" "979" "366" "299" "675" "1456"]
        two-sum-res (fn [n]
                      (two-sum (compl 2020 n) s))]
    (two-sum-res 979))
  #_=> 366 * 657 = 247050

  (let [s    ["1721" "979" "366" "299" "675" "1456"]
        nums (parse-s s)
        two-sum-res (fn [n]
                      (two-sum (compl 2020 n) s))]
    (map two-sum-res nums))
  #_=> (nil 247050 660825 nil 358314 nil)

  ;; calculate n * product of complements

  (map (fn [n compl]
         (when compl (* n compl)))
       [1721 979 366 299 675 1456]
       [nil 247050 660825 nil 358314 nil])
  #_=> (nil 241861950 241861950 nil 241861950 nil)

  ;; filter valid result

  (->> [nil 241861950 241861950 nil 241861950 nil]
       (filter (fn [n] n))
       (first))
  #_=> 241861950

  ;; three-sum

  (three-sum 2020 sample-entries)
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