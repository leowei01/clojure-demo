(ns aoc2020.day3
  (:require [aoc2020.util :refer [file->seq]]))

(defn col-slope [col-number col]
  (-> col
      (+ 3)
      (mod col-number)))

(defn tree-at-pos? [grid row col]
  (let [location (-> row
                     grid
                     (get col))]
    (= \# location)))

(defn count-tree [grid]
  (let [row-number (count grid)
        col-number (count (first grid))
        row-s      (->> (iterate inc 0)
                        (take row-number))
        col-s      (->> (iterate (partial col-slope col-number) 0)
                        (take row-number))]
    (->> (map (partial tree-at-pos? grid)
              row-s
              col-s)
         (filter (fn [tree?] tree?))
         (count))))


(comment
  (do (def sample-grid (file->seq "aoc2020/day3/sample-input.txt")) sample-grid)
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

  (do (def grid (file->seq "aoc2020/day3/input.txt")) grid)
  #_=> ["...........#..#.#.###....#....."
        "...#..#...........#.#...#......"
        "#.....#..#........#...#..##...."
        "..#...##.#.....#.............#."
        "#.#..#......#.....#....#......."
        ".....#......#..#....#.....#...."
        ".......#.#..............#......"
        ".....#...#..........##...#.....",,,]

  ;; part 1

  ;; generate the row sequence, row plus 1 every time until reach the bottom
  (take 10 (iterate inc 0))
  #_=> (0 1 2 3 4 5 6 7 8 9)

  ;; generate the column sequence, column plus 3 every time and iterate cycle
  (col-slope 5 0)
  #_=> 3

  (col-slope 5 3)
  #_=> 1

  ;; check the position (x y) in the grid is a tree
  (tree-at-pos? sample-grid 0 0)
  #_=> false

  (tree-at-pos? sample-grid 1 0)
  #_=> true

  (map (partial tree-at-pos? sample-grid)
       [0 1 2]
       [0 3 6])
  #_=> (false false true)

  ;; filter and count the true result (tree number)
  (->> (filter (fn [tree?] tree?)
               [false false true])
       (count))

  ;; count the tree
  (count-tree sample-grid)
  #_=> 7

  (count-tree grid)
  #_=> 242


  ;; part 2

  (count-tree grid slope-row slope-col)
  (reduce * number-of-trees-s)
  )