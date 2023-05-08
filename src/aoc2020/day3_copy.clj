(ns aoc2020.day3-copy
  (:require [aoc2020.util :refer [file->seq]]))

(defn tree-at-pos?
  [grid position]
  (let [row      (first position)
        col      (second position)
        location (-> row
                     grid
                     (get col))]
    (= \# location)))

(defn position-s
  [grid slope-dist]
  (let [row-number (count grid)
        col-number (count (first grid))
        row-s      (->> (iterate (fn [row]
                                   (+ row (slope-dist :down)))
                                 0)
                        (take-while (fn [row] (< row row-number))))
        col-s      (->> (iterate (fn [col]
                                   (-> col
                                       (+ (slope-dist :right))
                                       (mod col-number)))
                                 0)
                        (take (count row-s)))]
    (map (fn [row col] [row col])
         row-s
         col-s)))

(defn tree-count
  [grid slope-dist]
  (let [position-s (position-s grid slope-dist)]
    (->> (filter (partial tree-at-pos? grid)
                 position-s)
         (count))))



(defn product-of-tree-count
  [grid slope-dist-s]
  (->> (map (fn [slope-dist]
              (tree-count grid slope-dist))
            slope-dist-s)
       (reduce *)))


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

  (def slope-dist {:right 3 :down 1})

  ;; generate the position[row col] sequence
  (position-s sample-grid slope-dist)
  #_=> ([0 0] [1 3] [2 6] [3 9] [4 1] [5 4] [6 7] [7 10] [8 2] [9 5] [10 8])

  ;; check the position[row col] in the grid is a tree
  (tree-at-pos? sample-grid [0 0])
  #_=> false

  (tree-at-pos? sample-grid [1 0])
  #_=> true

  (filter (partial tree-at-pos? sample-grid)
          [[0 0]
           [1 3]
           [2 6]])
  #_=> ([2 6])

  ;; filter and count the true result (tree number)
  (tree-count sample-grid slope-dist)
  #_=> 7

  (tree-count grid slope-dist)
  #_=> 242


  ;; part 2
  (def slope-dist-s [{:right 1 :down 1}
                     {:right 3 :down 1}
                     {:right 5 :down 1}
                     {:right 7 :down 1}
                     {:right 1 :down 2}])

  ;; calculate result of every slope and multiply
  (product-of-tree-count grid slope-dist-s)
  #_=> 2265549792

  )
