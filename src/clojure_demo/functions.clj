(ns clojure-demo.functions
  (:gen-class))


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

(comment



  )