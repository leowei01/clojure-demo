(ns clojure-demo.naming-conventions
  (:use [clojure.repl]))

(def sample-person {:awards [1 2 3]})

(defn person->award [person]
  (person :awards))

;f1 f2 i1 x1 r2
;function1
;element1
;f1


(comment
  (->> sample-person
       (person->award))

  ;; Quiz
  (nth '(1 2 3) 0)
  (first '(1 2 3))
  (rest '(1 2 3 4 5))
  (take-last 2 '(1 2 3 4 5))
  (drop 3 '(1 2 3 4 5))
  (count '(1 1 1 1 1))

  ;; first and last?
  (- (count '(1 2 3)) 1)
  (-> '(1 2 3)
      (count)
      (- 1))

  (get [1 2 3 4 5] 10 "Not found")
  (count [9 8 7 6])
  (into [1 2 3 4] [5 6 7])

  (get #{1 2 3} 3)
  (disj #{1 2 3} 1)


  ;; find awards by name
  (def sample-person
    {:name   "Amelia Earhart"
     :birth  1897
     :death  1939
     :awards {"US"    #{"Distinguished Flying Cross" "National Women's Hall of Fame"}
              "World" #{"Altitude record for Autogyro" "First to cross Atlantic twice"}}})

  (defn person->awards [name]
    (if (= (get sample-person :name) name)
      (get sample-person :awards)
      nil))

  (defn find [name]
    (-> name
        (= (get sample-person :name))
        (if (get sample-person :awards) nil)
        ))

  (find "Amelia Earhart")




  (person->awards "op")
  (person->awards "Amelia Earhart")



  )


