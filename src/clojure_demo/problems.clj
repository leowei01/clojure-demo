(ns clojure-demo.problems
  (:use [clojure.repl]))

;; Palindrome
;; write a function to find out if string is a palindrome
;(first string)
;(last string)
;(rest string)
;(take (- (count string) 1) string)

(defn palindrome [string]
  (if
    (or (= (count string) 0) (= (count string) 1))
    true
    (if
      (= (first string) (last string))
      (palindrome (rest
                    (take
                      (- (count string) 1)
                      string)))
      false)))


(defn palindrome [string]
  (= (seq string) (reverse string)))

(def palin-string (str "abcba"))
(def not-palin-string (str "acnjdson"))





;; find the number of 'c's in string
(def string "abccasc")
(defn find-c [string]
  (count
    (filter (fn [char]
              (= char \c))
            (seq string))))





;; my-filter
(defn my-filter [f, coll]
  (reduce (fn [output ele]
            (if (f ele)
              (conj output ele)
              output))
          []
          coll))

(defn find-c-my-filter [string]
  (count
    (my-filter (fn [char]
                 (= char \c))
               (seq string))))

;; we can not use map to implement my-filter
;; map can not change the size
;; the output variable is immutable
(defn my-filter [f coll]
  (let [output []]
    (map (fn [x]
           (if (f x)
             (conj output x)))
         coll)
    output))


;; find the first 100 prime number





;; Rich Comment Blocks
(comment

  ;; palindrome
  (palindrome palin-string)
  (palindrome not-palin-string)

  ;; count c
  (find-c string)

  ;; how to delete the outer brackets
  [(seq string)]

  ;; my-filter
  (find-c-my-filter string)

  (let [n 10]
    (not-any? f '(1 2 3)))


  )











