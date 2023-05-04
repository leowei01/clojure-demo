(ns aoc2020.day3
  (:require [aoc2020.util :refer [file->seq]]))

(defn tree-at-pos? [grid row col]
  (let [location (-> row
                     grid
                     (get col))]
    (= \# location)))

(defn generate-row-s [{:keys [slope-dist row-number]}]
  (->> (iterate (fn [row]
                  (+ row (slope-dist :down)))
                0)
       (take-while (fn [row] (< row row-number)))))

(defn generate-col-s [{:keys [slope-dist length-of-row-s col-number]}]
  (->> (iterate (fn [col] (-> col
                              (+ (slope-dist :right))
                              (mod col-number)))
                0)
       (take length-of-row-s)))

(defn tree-count [grid slope-dist]
  (let [row-number (count grid)
        col-number (count (first grid))
        row-s      (generate-row-s {:slope-dist slope-dist
                                    :row-number row-number})
        col-s      (generate-col-s {:slope-dist      slope-dist
                                    :length-of-row-s (count row-s)
                                    :col-number      col-number})]
    (->> (map (partial tree-at-pos? grid)
              row-s
              col-s)
         (filter (fn [tree?] tree?))
         (count))))



(defn product-of-tree-count [grid slope-dist-s]
  (->> slope-dist-s
       (map (fn [slope-dist]
              (tree-count grid slope-dist)))
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

  ;; generate the row sequence, row plus 1 every time until reach the bottom
  (generate-row-s {:slope-dist slope-dist
                   :row-number 10})
  #_=> (0 1 2 3 4 5 6 7 8 9)

  ;; generate the column sequence, column plus 3 every time and iterate cycle
  (
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
  (tree-count sample-grid {:right 1 :down 1})
  #_=> 7

  (tree-count grid {:right 1 :down 1})
  #_=> 242


  ;; part 2
  (def slope-dist-s [{:right 1 :down 1}
                     {:right 3 :down 1}
                     {:right 5 :down 1}
                     {:right 7 :down 1}
                     {:right 1 :down 2}])

  (product-of-tree-count grid slope-dist-s)
  )