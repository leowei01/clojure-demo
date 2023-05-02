(ns aoc2020.day1
  (:require [aoc2020.util :refer [file->seq]]))


;; part 1

(defn strs->ints [strs]
  "parsing strings to integers"
  (let [str->int (fn [str]
                   (Integer/parseInt str))]
    (map str->int strs)))

(defn int->compl [n sum]
  "calculate complement"
  (- sum n))

(defn compl-in-set? [n sum s]
  "detect whether the complement is in the sequence (using set)"
  (let [compl (int->compl n sum)]                           ; calculate complement
    (contains? s compl)))                                   ; check if exist in set

(defn int->int*compl [n sum]
  "multiply x and its complement"
  (if n                                                     ; if n is not nil
    (let [compl (int->compl n sum)]                         ; calculate complement
      (* n compl))                                          ; multiply num and its complement
    nil))

(defn two-sum [sum strings]
  "filter x for which both x and its complement are in the sequence
  using set: complement-in-set?
  using map: complement-in-map?"
  (let [nums (strs->ints strings)                           ; parse strings
        s (set nums)]                                       ; covert to set
    (-> (->> nums
             (filter (fn [n]
                       (compl-in-set? n sum s)))            ; filter whose complement in the set
             (first))                                       ; just select one (maybe nil)
        (int->int*compl sum))))                             ; multiply num and its complement

;(defn compl-in-map? [n sum m]
;  "detect whether the complement is in the sequence (using map)"
;  (let [compl (- sum n)]
;    (and (contains? m compl)
;         (or (-> sum (/ 2) (= n) (not))
;             (-> n m (> 1))))))

;(defn filter-compl-in-seq [sum seq]
;  "filter x for which both x and its complement are in the sequence
;  using set: complement-in-set?
;  using map: complement-in-map?"
;  (let [s (set seq)]
;    (filter (fn [n]
;              (compl-in-set? n sum s))
;       seq)))





;; part 2

(defn compl-multiply [n sum strings]
  "multiply complements using two-sum"
  (let [compl (int->compl n sum)                            ; calculate complement
        compl-product (two-sum compl strings)]              ; calculate complement product
    (if compl-product                                       ; if has complement product
      (* n compl-product)                                   ; multiply n and complement product
      nil)))                                                ; otherwise return nil

(defn three-sum [sum strings]
  (let [nums (strs->ints strings)]                          ; parse strings
    (->> (map (fn [n]
                (compl-multiply n sum strings))
              nums)                                         ; calculate complement product
         (filter (fn [n] n))                                ; filter the non-nil result
         (first))))                                         ; just select one

;(defn compl-multiply-s [sum strings]
;  (let [ints (strs->ints strings)]
;    (map (fn [n]
;           (let [compl (- sum n)]
;             (two-sum compl strings)))
;         ints)))
;
;(defn compl? [n compl-multiply]
;  (if compl-multiply
;    (* n compl-multiply)
;    nil))
;
;(defn three-sum [sum strings]
;  (let [ints (strings->integers strings)
;        compl-multiply-s (compl-multiply-s sum strings)]
;    (->> (map compl?
;              ints
;              compl-multiply-array)
;         (filter (fn [n] n))
;         (first))))


(comment

  ;; sample-input
  (do (def sample-entries (file->seq "aoc2020/day1/sample-input.txt"))
      sample-entries)
  #_=> ["1721" "979" "366" "299" "675" "1456"]

  ;; input
  (def sample-entries (file->seq "aoc2020/day1/day1-input.txt"))
  #_=> [1531
        1959
        1344
        1508
        1275
        1729,,,]

  ;; part 1

  ;; decode/parse
  ;; calculate complement
  ;; filter whose complement in set
  ;; multiply n and n's complement

  (two-sum 2020 sample-entries)
  #_=> 567171



  ;; part 2

  ;; calculate complement --> the sum of two-sum
  ;; multiply complements using two-sum
  ;; filter valid result

  (three-sum 2020 sample-entries)
  #_=> 212428694






  )