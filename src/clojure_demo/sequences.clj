(ns clojure-demo.sequences
  (:gen-class))


;; Sequences
(def xs #{{:a 11 :b 1 :c 1 :d 4}
          {:a 2 :b 12 :c 2 :d 6}
          {:a 3 :b 3 :c 3 :d 8 :f 42}})
(def ys #{{:a 11 :b 11 :c 11 :e 5}
          {:a 12 :b 11 :c 12 :e 3}
          {:a 3 :b 3 :c 3 :e 7}})
(join xs ys)

;; want to modify all elements of list
(first [1 2 3])
(rest [1 2 3])
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

;; (if test true false)
(if true :true :flase)

;; add if condition to deal with empty list
(defn inc-first [nums]
  (if (first nums)
    (cons (inc (first nums))
          (rest nums))
    (list)))


;; Recursion
;; Some part of the problem which has a known solution
;; A relationship which connects one part of the problem to the next

;; (f(x), f(x), f(x)...)
(defn inc-all [nums]
  (if (first nums)
    (cons (inc (first nums))
          (inc-all (rest nums)))
    (list)))

;; generalize from inc
(defn transform-all [f nums]
  (if (first nums)
    (cons (f (first nums))
          (transform-all f (rest nums)))
    (list)))

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


;; (iterate f x) -> (f x), (f (f x)) etc
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

;; into
;; (into to from) can convert and concat any collections
(into [1 2 3] [4 5 6])

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
;; overlapping with step
(partition 2 1 [1 2 3 4 5])
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

;; map is a special reduce: (reduce f [] [1 2 3]), f takes 2 ele and use conj
(defn my-map [f coll]
  (reduce (fn [output element]
            (conj output (f element)))
          []
          coll))

;; take while
(defn my-take-while [f coll]
  (reduce (fn [output ele]
            (if (f ele)
              (conj output ele)
              (reduced output)))
          []
          coll))

;; (reduced out) will terminate reduce and return out
(doc reduced)
;; Almost any operation on a sequence can be expressed in terms of a reduce–though for various reasons






;; Most of Clojure’s sequence functions are lazy.
;; They don’t do anything until needed
(def infseq (map inc (iterate inc 0)))
(realized? infseq)                                          ;false

;; only defined, do nothing until needed
(take 10 infseq)
(realized? infseq)






;; Problem: sum of products of consecutive pairs of the first 1000 off integers
(doc partition)
;; (partition [n step coll]) use for get overlapping partitions

(reduce +
        (take 1000
              (map (fn [pair] (* (first pair) (second pair)))
                   (partition 2 1
                              (filter odd?
                                      (iterate inc 0))))))
;; The part that happens first appears deepest, last, in the expression.
;; In a chain of reasoning like this, it’d be nicer to write it in order.

;; ->> will take the parameter as the last of next expression
;; ->> 0 (iterate inc) == (iterate inc 0)
(->> 0
     (iterate inc)
     (filter odd?)
     (take 1000)
     (partition 2 1)
     (map (fn [pair]
            (* (first pair) (second pair))))
     (reduce +))


;; get from complex map
(def recipe
  {:title       "Chocolate chip cookies"
   :ingredients {"flour"           [(+ 2 1/4) :cup]
                 "baking soda"     [1 :teaspoon]
                 "salt"            [1 :teaspoon]
                 "butter"          [1 :cup]
                 "sugar"           [3/4 :cup]
                 "brown sugar"     [3/4 :cup]
                 "vanilla"         [1 :teaspoon]
                 "eggs"            2
                 "chocolate chips" [12 :ounce]}})




;; Rich Comment Blocks

(comment
  (inc-first [1 2 3 4])
  ;; [2 2 3 4]
  (inc-first (list))
  ;; []
  (inc-all [1 2 3 4])
  ;; [2 3 4 5]

  (transform-all dec [1 2 3 4])
  ;; [0 1 2 3]
  (transform-all keyword ["a" "b" "c"])
  ;; [:a :b :c]
  (transform-all list [1 2 3 4])
  ;; [(1) (2) (3) (4)]

  (expand inc 0 10)
  ;; (0 1 2 ... 9)

  (my-map inc [1 2 3 4])
  ;; [2 3 4 5]

  (my-take-while pos? [1 -1 2 -2])
  ;; [1]


  ;; Thread
  ;; macroexpand represent the thread form
  (macroexpand
    '(->> 1
          (* 2)
          (/ 3)
          (+ 4)
       ))

  ;; get from complex map
  (get (get (get recipe :ingredients) "flour") 1)

  ;; use thread to represent straightforward
  (-> recipe
      (get :ingredients)
      (get "flour")
      (second))

  (-> recipe
      :ingredients
      "flour"
      (get 1)
    )
  ;; str "flour" is not IFN --> error

  (let [my-map {:a 1
                :b {:c 2
                    :d {:e 3
                        "f" 4}}}]
    #_(-> my-map :b :d :e)
    (get-in my-map [:b :d "f"]))
  ;; if keys are keywords, use -> to get from complex map
  ;; or use get-in if key is str


  ;; do
  (do (defn f [] (+ 1 2)) (f))
  (defn f [] (+ 1 2))
  (f)
  (do (def a "123") a)


  )