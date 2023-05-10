(ns aoc2020.day3-copy
  (:require [aoc2020.util :refer [file->seq]]))

(defn position-s
  [slope-dist]
  (let [row-s (take-nth (slope-dist :down) (range))
        col-s (take-nth (slope-dist :right) (range))]
    (map (fn [row col] [row col])
         row-s
         col-s)))

(defn tree-at-pos?
  [grid-template position]
  (let [col-number (-> grid-template
                       (first)
                       (count))
        row        (first position)
        col        (mod (second position) col-number)
        location   (-> row
                       grid-template
                       (get col))]
    (= \# location)))

(defn tree-count
  [grid-template slope-dist]
  (let [row-number (count grid-template)]
    (->> slope-dist
         (position-s)
         (take-while (fn [pos] (< (first pos) row-number)))
         (filter (partial tree-at-pos? grid-template))
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

  ;; generate the position[row col] infinite sequence
  (position-s slope-dist)
  #_=> ([0 0] [1 3] [2 6] [3 9] [4 12] ...)

  ;; check the position[row col] in the grid-template is a tree
  (tree-at-pos? sample-grid-template [0 0])
  #_=> false

  (tree-at-pos? sample-grid-template [1 0])
  #_=> true

  (filter (partial tree-at-pos? sample-grid-template)
          [[0 0]
           [1 3]
           [2 6]])
  #_=> ([2 6])

  ;; filter and count the true result (tree number)
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
