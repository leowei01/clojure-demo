(ns clojure-demo.basics
  (:gen-class))


;; LISP
;; the first must be function
;; from left to right
;; from inner to outer

;; int
(type 3)
Long/MAX_VALUE
(inc Long/MAX_VALUE)

;; bigint
(inc (bigint Long/MAX_VALUE))
(type 5N)
(type (int 0))
(type (short 0))
(type (byte 0))
Integer/MAX_VALUE

;; Floats use 32 bits, and Doubles use 64.
;; Doubles are the default in Clojure.
(type 1.23)
(type (float 1.23))

;; floats and doubles are approximations
0.99999999999999999999

;; ratio
(type 1/3)

;; Mathematical operations
(type (+ 1 2.0))                                            ;int + double = double

;; Since floats are approximations, = considers them different from integers.
;; == also compares things, but a little more loosely: it considers integers equivalent to their floating-point representations.
(= 3 3.0)
(== 3 3.0)
(type =)
(= 2 2 2)
(= 2 2 3)

;; divide will return a ratio type
(/ 1 2)
(* 3 1.5)

;; multiple numbers
(+ 1 2 3)
(* 2 3 1/5)
(- 5 1 1 1)
(/ 24 2 3)

;; single number
(+ 2)

;; no number
;; we’ll see later that these generalizations make it easier to reason about higher-level numeric operations.
(+)
(*)

;; compare
(<= 1 2 3)
(<= 1 3 2)

;; inc and dec
(inc 3)
(dec 3)

;; Character
(type \a)

;; String
(type "cat")

;; make anything into string
(str "cat")
(str "cat")
(str 1)
(str true)
(str '(1 2 3))
(str nil)

(type nil)

;; str combine thins together
(str "meow " 3 " times")

;; #"..."  regular expression in Clojure
;; re-find and re-matches look for occurrences of a regular expression in a string
(re-find #"cat" "mystic cat mouse")
(re-find #"cat" "only dogs here")
;; re-match
(rest (re-matches #"(.+):(.+)" "mouse:treat"))


;; Booleans
(boolean true)
(boolean false)
(boolean nil)

;; the only negative values are false and nil
;; Every other value in Clojure is positive.
(boolean nil)
(boolean 0)
(boolean 1)
(boolean "dsa")
(boolean (str "cat"))

;; int bigint short str boolean are functions converting data to corresponding type
(type boolean)

;; and return first false or last true
(and true false true)
(and true true true)
(and 1 2 3)

;; or return first true
(or false 2 3)
(or false nil)

;; not
(not 2)
(not false)
(not nil)


;; Symbol
;; The job of symbols is to refer to things(function/variable), to point to other values.
;; When evaluating a program, symbols are looked up and replaced by their corresponding values.
(class 'str)

;; either short or full names
;; Symbol names are separated with a /
(= str clojure.core/str)
(name 'clojure.core/str)
(type 'Integer/MAX_VALUE)


;; Keywords
;; use as labels or identifiers
;; aren’t labels in the sense of symbols: keywords aren’t replaced by any other value.
;; They’re just names, by themselves.
;; useful in map, looking up values
(type :cat)
(str :cat)
(name :cat)
(keyword "oop")
(:a {:a 1 :b 2})



;; List (linked list)
;; quote lists with a ' to prevent them from being evaluated.
;; without quote ', any () will be evaluated

;; initialize
'(1 2 3)
(type '(1 2 3))
(list 1 2 3)
;; comparable
(= (list 1 2) (list 1 2))
;; add element to head(linked list offers immediate access to the first element.)
(conj '(1 2 3) 4)
(conj '(1 2 3) '(4 5))                                      ;conj will add single element even if it's a list
;; get
(first (list 1 2 3))
(second (list 1 2 3))
(nth (list 1 2 3) 2)
;; can not use get
(get (list 1 2 3) 2)
;; well-suited for small collections
;; slow when you want to get arbitrary elements from later in the list.



;; Vector(things like ArrayList)
;; For fast access to every element, we use a vector.
;; intended for looking up elements by index

;; initialize
[1 2 3]
(type [1 2 3])
(vector 1 2 3)
;; convert list to vec
(vec (list 1 2 3))
;; add to tail
(conj [1 2 3] 4)
;; get nth is fast
;; binary tree structure
(first [1 2 3])
(second [1 2 3])
(nth [1 2 3] 1)
([:a :b :c] 1)
(get [1 2 3] 0)
;; get remaining elements
(rest [1 2 3])
(next [1 2 3])
;; differences between rest and next
(rest [1])
(next [1])
;; get last
(last [1 2 3])
;; size
(count [1 2 3])
;; vectors and lists containing the same elements are considered equal
(= '(1 2 3) [1 2 3])
;; In almost all contexts, you can consider vectors, lists, and other sequences as interchangeable.
;; They only differ in their performance characteristics, and in a few data-structure-specific operations.
;; modify using accos, but index < count
(assoc [1 2 3] 0 10)


;; Set
;; unordered
#{:a :b :c}
;; convert into list or vector
;; vec set and into
(vec #{:a :b :c})
(into [] '(1 2 3 4))
(into #{} [1 2 3 4])
(into '() #{:a :b :c})
(set [1 2 3])
;; sort set
(sort #{:a :b :c})
;; add element
(conj #{:a :b :c} :d)
;; remove element
(disj #{:a :b} :b)
;; contain
(contains? #{1 2 3} 3)
(#{1 2 3} 2)
(#{1 2 3} 4)
;; union
(union #{1} #{2})



;; Map
;; keyword --> key
;; any type
{:name "mittens" :weight 9 :color "black"}
;; get
(get {"cat" "meow" "dog" "woof"} "cat")
;; getOrDefault
(get {:glinda :good} :wicked :not-here)
;; directly get
({"cat" "meow" "dog" "woof"} "cat")
(:raccoon {:weasel "queen" :raccoon "king"})
;; put
(assoc {:bolts 1088} :camshafts 3)
(assoc nil 5 2)                                             ; create a new map
;; merge
(merge {:a 1 :b 2} {:b 3 :c 4})
;; remove
(dissoc {:potatoes 5 :mushrooms 2} :mushrooms)
;; get
(def m {:a  1
        :b  2
        "c" 3})
(:a m)
(m :a)
(m "c")
(get m "c")

;; int bigint short boolean str symbol keywords

;list
;conj
;disj
;nth
;
;vector
;conj
;disj
;assoc
;get/nth/last
;
;set
;conj
;disj
;get/contains?
;
;map
;assoc
;dissoc
;assoc
;get

(comment


  (defn student [id, m]
    (get m id))
  ;; #_ can comment out an expression




  )