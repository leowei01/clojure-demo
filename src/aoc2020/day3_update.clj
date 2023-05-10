(ns aoc2020.day3-update
  (:require [aoc2020.util :refer [file->seq]]))

(defn grid-template->grid
  [grid-template]
  (mapv (fn [row] (cycle (seq row)))
        grid-template))

(defn position-s
  [{slope-right :right
    slope-down  :down}]
  (pmap (fn [x]
          [(* slope-down x) (* slope-right x)])
        (range)))

(defn value-at-pos
  [grid [row col]]
  (let [row-s (take (+ 1 col) (grid row))]
    (nth row-s col)))

(defn tree-count
  [grid-template slope-dist]
  (let [row-number (count grid-template)
        grid       (grid-template->grid grid-template)]
    (->> slope-dist
         (position-s)
         (take-while (fn [pos] (< (first pos) row-number)))
         (map (partial value-at-pos grid))
         (filter (fn [value] (= \# value)))
         (count))))



(defn product-of-tree-count
  [grid-template slope-dist-s]
  (->> (map (fn [slope-dist]
              (tree-count grid-template slope-dist))
            slope-dist-s)
       (reduce *)))


(comment
  (do (def sample-grid-template (file->seq "aoc2020/day3/sample-input.txt")) sample-grid-template)
  #_=> ["..##......."
        "#...#...#.."
        ".#....#..#."
        "..#.#...#.#"
        ".#...##..#."
        "..#.##....."
        ".#.#.#....#"
        ".#........#"
        "#.##...#..."
        "#...##....#"
        ".#..#...#.#"]

  (do (def grid-template (file->seq "aoc2020/day3/input.txt")) grid-template)
  #_=> ["...........#..#.#.###....#....."
        "...#..#...........#.#...#......"
        "#.....#..#........#...#..##...."
        "..#...##.#.....#.............#."
        "#.#..#......#.....#....#......."
        ".....#......#..#....#.....#...."
        ".......#.#..............#......"
        ".....#...#..........##...#.....",,,]

  ;; part 1

  (def slope-dist {:right 3 :down 1})

  ;; generate infinite grid
  (def sample-grid (grid-template->grid sample-grid-template))

  ;; generate the position[row col] infinite sequence
  (position-s slope-dist)
  #_=> ([0 0] [1 3] [2 6] [3 9] [4 12] ...)

  ;; get the value of the position[row col]
  (value-at-pos sample-grid [0 0])
  #_=> \.

  (value-at-pos sample-grid [1 0])
  #_=> \#

  ;; filter and count the result (tree number)
  (tree-count sample-grid-template slope-dist)
  #_=> 7

  (tree-count grid-template slope-dist)
  #_=> 242


  ;; part 2
  (def slope-dist-s [{:right 1 :down 1}
                     {:right 3 :down 1}
                     {:right 5 :down 1}
                     {:right 7 :down 1}
                     {:right 1 :down 2}])

  ;; calculate result of every slope and multiply
  (product-of-tree-count grid-template slope-dist-s)
  #_=> 2265549792

  )
