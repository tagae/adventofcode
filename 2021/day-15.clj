(ns adventofcode.2021.day-15
  (:use clojure.test))

(defn parse-risk-levels
  [lines]
  (vec
   (for [line lines]
     (mapv #(Character/digit % 10) line))))

(defn up [[j i]] [(dec j) i])
(defn down [[j i]] [(inc j) i])
(defn left [[j i]] [j (dec i)])
(defn right [[j i]] [j (inc i)])

(defn row-count [risk-levels] (count risk-levels))
(defn column-count [risk-levels] (count (first risk-levels)))

(defn safest-path
  [risk-levels]
  (letfn [(within-bounds? [[j i]]
            (and (<= 0 j) (< j (row-count risk-levels))
                 (<= 0 i) (< i (column-count risk-levels))))
          (adjacent-to [vertex]
            (filter within-bounds? ((juxt up down left right) vertex)))]
    (loop [unvisited #{[0 0]}
           visited #{}
           costs {[0 0] 0}]
      (if (empty? unvisited)
        costs
        (let [current-position (first (sort-by costs unvisited))
              adjacent-positions (remove visited (adjacent-to current-position))
              current-cost (costs current-position)]
          (recur (into (disj unvisited current-position) adjacent-positions)
                 (conj visited current-position)
                 (reduce (fn [costs adjacent-position]
                           (let [[j i] adjacent-position
                                 adjacent-cost (+ current-cost ((risk-levels j) i))]
                             (if (< adjacent-cost (get costs adjacent-position ##Inf))
                               (assoc costs adjacent-position adjacent-cost)
                               costs)))
                         costs
                         adjacent-positions)))))))

(defn part1
  [risk-levels]
  (let [bottom (dec (row-count risk-levels))
        right (dec (column-count risk-levels))]
    ((safest-path risk-levels) [bottom right])))

(defn expanded-board
  [risk-levels]
  (reduce into []
          (for [j (range 0 5)]
            (mapv
             (fn [row]
               (reduce into
                       []
                       (for [i (range 0 5)]
                         (map #(inc (mod (dec (+ j i %)) 9)) row))))
             risk-levels))))

(defn with-line-seq
  [file-name func]
  (with-open [rdr (clojure.java.io/reader file-name)]
    (func (line-seq rdr))))

(deftest part1-example
  (is (= 40 (with-line-seq "day-15-example.txt"
              (comp part1 parse-risk-levels)))))

(deftest part1-input
  (is (= 553 (with-line-seq "day-15-input.txt"
               (comp part1 parse-risk-levels)))))

(deftest part2-example
  (is (= 315 (with-line-seq "day-15-example.txt"
               (comp part1 expanded-board parse-risk-levels)))))

(deftest part2-input
  (is (= 2858 (with-line-seq "day-15-input.txt"
                (comp part1 expanded-board parse-risk-levels)))))

(run-tests 'adventofcode.2021.day-15)
