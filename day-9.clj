(ns adventofcode.2021.day-9
  (:use clojure.test))

(defn low-point?
  [heightmap x y]
  (let [x-bound (dec (count (first heightmap)))
        y-bound (dec (count heightmap))
        height ((heightmap y) x)
        left (if (> x 0) ((heightmap y) (dec x)) ##Inf)
        right (if (< x x-bound) ((heightmap y) (inc x)) ##Inf)
        up (if (> y 0) ((heightmap (dec y)) x) ##Inf)
        down (if (< y y-bound) ((heightmap (inc y)) x) ##Inf)]
    (and (< height left)
         (< height right)
         (< height up)
         (< height down))))

(defn risk-levels
  [heightmap]
  (let [width (count (first heightmap))
        height (count heightmap)]
    (for [j (range 0 height)
          i (range 0 width)
          :when (low-point? heightmap i j)]
      (inc ((heightmap j) i)))))

(defn part1
  [heightmap]
  (apply + (risk-levels heightmap)))

(defn parse-heightmap
  [lines]
  (vec
   (for [line lines]
     (mapv #(Character/digit % 10) line))))

(defn with-line-seq
  [file-name func]
  (with-open [rdr (clojure.java.io/reader file-name)]
    (func (line-seq rdr))))

(deftest risk-levels-example
  (is (= '(2 1 6 6)
         (with-line-seq "day-9-example.txt"
           (comp risk-levels parse-heightmap)))))

(deftest part-1-example
  (is (= 15 (with-line-seq "day-9-example.txt"
              (comp part1 parse-heightmap)))))

(deftest part-1-input
  (is (= 522 (with-line-seq "day-9-input.txt"
               (comp part1 parse-heightmap)))))

(run-tests 'adventofcode.2021.day-9)
