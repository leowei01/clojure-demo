(ns clojure-demo.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

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

;; represent fractions exactly, we can use the ratio type
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





;; Functions

;; let [symbol value] use symbol to do anything
;; apply only within let expression
(let [num 5] (str "I have " num " dogs"))
(let [+ -] (+ 2 3))
(let [num 5
      name "leo"]
  (str "I want to give " name " " num " apples"))

;; functions
;; the nature of the function: an expression with unbound symbols
(let [x] (+ x 1))                                           ;x is unbound
(fn [x] (+ x 1))
((fn [x] (+ x 1)) 2)
#(+ %1 %2 1)
(#(+ %1 %2 1) 2 3)
(let [func #(* % 2)]
  (+ (func 1)
     (func 2)))

;; var def
(def nums 5)
(str "I have " nums " apples")

;; define will make you lose something, redefine carefully
;; Redefining names in this way changes the meaning of expressions everywhere in a program, without warning.
;; Expressions which relied on the value of a Var could suddenly take on new, possibly incorrect, meanings.
;; It’s a powerful tool for experimenting at the REPL, and for updating a running program, but it can have unexpected consequences.
;; Good Clojurists use def to set up a program initially, and only change those definitions with careful thought.

;; define a function
(def plus (fn [x] (+ x 1)))
(defn half [x] (/ x 2))
(half 5)
;; multiple arities
(defn half
  ([] 1/2)
  ([x] (/ x 2)))
(half)
(half 5)
;; multiple arguments
(defn add
  [x y]
  (+ x y))
(add 5 4)
;; any arguments, & more-args to slurp all remaining arguments as a list
(defn vargs
  [x y & more]
  {:x x
   :y y
   :more more})
(vargs 3)
(vargs 3 4)
(vargs 3 4 1 2 3 4 5)                                       ;(1 2 3 4 5) is token as a list
;; docstring
(defn launch
  "assumptions, context, purpose, input, output"
  [args1 args2]
  "wen don't know how to do it")

;; doc tells us the full name of the function, the arguments it accepts, and its docstring
(doc launch)
;; source
(source type)
(source +)



;; function (fn [x] (+ x 1)), #(+ % 1)
;; let: define within let expression, (let [] ())
;; define var: (def a 1)
;; define function: (def func (fn [x] (+ x 1))), (defn func [x] (+ x 1))
;; multiple arities, multiple arguments, any arguments, docstring




;; Sequences


;; Rich Comment Blocks
(comment
  (def xs #{{:a 11 :b 1 :c 1 :d 4}
            {:a 2 :b 12 :c 2 :d 6}
            {:a 3 :b 3 :c 3 :d 8 :f 42}})

  (def ys #{{:a 11 :b 11 :c 11 :e 5}
            {:a 12 :b 11 :c 12 :e 3}
            {:a 3 :b 3 :c 3 :e 7}})

  (join xs ys))

;; want to modify all elements of list
(first [1 2 3])
(rest [1 2 3])
(cons 1 [2])
(cons 1 [2 3 4])

;; conj vs cons
;; (conj coll x & xs) --> add x & xs into the end of coll
(conj [1 2] 3 4)                                            ;[1 2 3 4]
;; (cons x seq) --> add x at the beginning of seq
(cons [1 2] [4 5 6])                                        ;([1 2] 4 5 6)

;; define a function inc the first element
(defn inc-first [nums]
  (cons (inc (first nums))
        (rest nums)))

(inc-first [1 2 3 4])

;; (if test true false)
(if true :true :flase)

;; add if condition to deal with empty list
(defn inc-first [nums]
  (if (first nums)
    (cons (inc (first nums))
          (rest nums))
    (list)))
(inc-first (list))

;; recursion
;; Some part of the problem which has a known solution
;; A relationship which connects one part of the problem to the next

;; (f(x), f(x), f(x)...)
(defn inc-first [nums]
  (if (first nums)
    (cons (inc (first nums))
          (inc-first (rest nums)))
    (list)))
(inc-first [1 2 3 4])

;; generalize from inc
(defn transform-all [f nums]
  (if (first nums)
    (cons (f (first nums))
          (transform-all f (rest nums)))
    (list)))
(transform-all dec [1 2 3 4])
(transform-all keyword ["a" "b" "c"])
(transform-all list [1 2 3 4])

;; map
(map inc [1 2 3 4])
;; maps: sparse, and the relationships between keys and values may be arbitrarily complex
;; map: expresses the same type of relationship, applied to a series of elements in fixed order.





;; Build sequence

;; (x, f(x), f(f(x))...)
(defn expand [f x count]
  (if (pos? count)
    (cons x (expand f (f x) (dec count)))
    (list)))

;; (when test & body) If logical true, evaluates body in an implicit do.
(defn expand [f x count]
  (when (pos? count)
    (cons x (expand f (f x) (dec count)))))
(expand inc 0 10)

;; (iterate f x)
;; Returns a infinite sequence of x, (f x), (f (f x)) etc
;; (take n coll) first n element of coll
(take 10 (iterate inc 0))
(take 5 (iterate (fn [x] (str x "o")) "y"))

;; (repeat x) --> infinite x sequence
;; (repeat n x) n times x
(take 5 (repeat 3))
(repeat 5 :hi)
(repeat 3 [1 2 3])

;; rand: return random number
(rand)

;; range(n m): n to m-1
;; range(n m step)
(doc range)
(range 5)
(range 5 10)
(range 1 10 2)

;; cycle: repeating forever
(doc cycle)
(take 9 (cycle [1 2 3]))






;; Transform sequence

;; map multiple sequences
(map (fn [n vehicle] (str "I have got " n " " vehicle "s"))
     [0 200 9]
     ["car" "train" "kiteboard"])

(map +
     [1 2 3]
     [4 5 6]
     [7 8 9])
;; map stops at the end of the smaller one
(map (fn [index element] (str index ". " element))
     (iterate inc 0)
     [:a :b :c])
;; (iterate) is infinite, [a b c] is finite, map will stop at 2


;; map-indexed
(doc map-indexed)
(map-indexed (fn [index element] (str index ". " element))
             ["erlang" "ruby" "haskell"])


;; concat: tack sequence onto the end of another
(doc concat)
(concat [1 2 3] [:a :b :c] '(4 5 6))

;; interleave
(doc interleave)
(interleave [1 2 3] [:a :b :c])

;; interpose: add separator
(interpose "l" [1 2 3])

;; reverse
(reverse [1 2 3])
(reverse "woolf")
;; Strings are sequences too! Each element of a string is a character, written \f
(apply str (reverse "woolf"))
(doc apply)

;; (apply f [1 2 3]) --> (f 1 2 3)
;; Applies f to the argument list formed by prepending intervening arguments to args.
(max [1 2 3])                                               ;[1 2 3]
(apply max [1 2 3])                                         ;3
;; In this case, 'max' has received one vector argument
;; If you would like to find the largest item **within** the vector, you would need to use `apply`

;; break string into chars
(seq "abcd")

;; shuffle sequence
(shuffle [1 2 3 4 5 6])




;; Subsequence

;; drop first n
(drop 3 (range 10))

;; take-last, drop-last
(take-last 2 [1 2 3])

;; take-while, drop-while
(take-while pos? [1 -1 2 -2])

;; split-at
(split-at 4 (range 10))
;; split-with: split when the f return false
(split-with number? [1 2 3 "a" :a 4 5 6])

;; filter
(filter pos? [1 2 -1 -2])
(filter number? [1 2 3 "a" :a 4 5 6])

;; remove
(remove pos? [1 2 -1 -2])
(remove number? [1 2 3 "a" :a 4 5 6])

;; partition
(partition 2 [1 2 3 4 5])
(partition-all 2 [1 2 3 4 5])
;; partition-by
(partition-by neg? [1 2 3 2 1 -1 -2 -3 -2 -1 1 2])
(doc partition-all)




;; Collapsing sequence

(frequencies [:meow :mrrrow :meow :meow])

;; (group-by f coll) group by the result of applying f to each element
(group-by pos? [1 2 -1 -2])
(group-by :first [{:first "Li" :last "Zhou"}
                  {:first "Sarah" :last "Lee"}
                  {:first "Sarah" :last "Dunn"}
                  {:first "Li" :last "O'Toole"}])
;; :first keyword (act as a function!) to look up those first names

;; reduce
(reduce + [1 2 3 4])
;; f(1,2) = 3, f(3,3)=6, f(6,6)=12
;; f(f(f(1,2),3),4)=12
;; reductions to see a sequence of all the intermediate states.
(reductions + [1 2 3 4])
;; start with default state
(reduce conj #{} [1 2 3 4])

;; convert list to map
(into {} [[:a 1] [:b 2]])
(reduce conj [] [1 2 3 4])


;; map is a special reduce: (reduce f [] [1 2 3]), f takes 2 ele and use conj
(defn my-map [f coll]
  (reduce (fn [output element]
            (conj output (f element)))
          []
          coll))
(my-map inc [1 2 3 4])

;; take while
(defn my-take-while [f coll]
  (reduce (fn [output ele]
            (if (f ele)
              (conj output ele)
              (reduced output)))
          []
          coll))
(my-take-while pos? [1 -1 2 -2])



(comment)






