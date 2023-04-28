(ns clojure-demo.naming-conventions
  (:gen-class))

(def sample-person {:awards [1 2 3]})

(defn person->award [person]
  (person :awards))




(comment
  (->> sample-person
       (person->award))
  )


