(ns aoc2020.day3-update
  (:require [aoc2020.util :refer [file->seq]]))

(defn grid-template->grid
  [grid-template]
  (mapv (fn [row] (cycle (seq row)))
        grid-template))

(defn position-s
  [{:keys [slope-right slope-down]}]
  (pmap (fn [x]
          [(* slope-down x) (* slope-right x)])
        (range)))

(defn value-at-pos
  [grid [row col]]
  (-> row
      (grid)
      (nth col)))

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
  ;; https://adventofcode.com/2020/day/3

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

  (def slope-dist {:slope-right 3 :slope-down 1})

  ;; generate infinite grid
  (def sample-grid (grid-template->grid sample-grid-template))

  ;; generate the position[row col] infinite sequence
  (position-s slope-dist)
  #_=> ([0 0] [1 3] [2 6] [3 9] [4 12],,,,)

  (do (def pos-s (take 10 (position-s slope-dist)))
      pos-s)
  #_=> ([0 0] [1 3] [2 6] [3 9] [4 12] [5 15] [6 18] [7 21] [8 24] [9 27])

  ;; get the value of the position[row col]
  (value-at-pos sample-grid [0 0])
  #_=> \.

  (value-at-pos sample-grid [1 0])
  #_=> \#

  (map (partial value-at-pos sample-grid) pos-s)
  #_=> (\. \. \# \. \# \# \. \# \# \#)

  ;; filter and count the tree (tree-count is the main function)
  (tree-count sample-grid-template slope-dist)
  #_=> 7

  (tree-count grid-template slope-dist)
  #_=> 242


  ;; part 2
  (def slope-dist-s [{:slope-right 1 :slope-down 1}
                     {:slope-right 3 :slope-down 1}
                     {:slope-right 5 :slope-down 1}
                     {:slope-right 7 :slope-down 1}
                     {:slope-right 1 :slope-down 2}])

  ;; calculate result of every slope and multiply
  (product-of-tree-count grid-template slope-dist-s)
  #_=> 2265549792

  ;; use infinite map and pos-s instead of finite map and mod
  ;; infinite sequence must be lazy-seq, if use map for lazy-seq, it will immediately evaluate, use pmap
  ;; destructure parameter
  ;; Build functions horizontally:
  ;; (->> a
  ;     b
  ;     c)
  ;
  ;instead of
  ;
  ;(defn a [] (...))
  ;(defn b [] (a ....))
  ;(defn c [] (b ...))

  )
