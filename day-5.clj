(ns adventofcode.2021.day-5
  (:use clojure.test))

(defn with-line-seq
  [file-name func]
  (with-open [rdr (clojure.java.io/reader file-name)]
    (func (line-seq rdr))))

(defn parse-vents
  [vent-lines]
  (letfn [(parse-vent [vent-line]
            (mapv #(Integer/parseInt %) (re-seq #"\d+" vent-line)))]
    (map parse-vent vent-lines)))

(defn from-to
  [a b]
  (if (>= a b)
    (take (inc (- a b)) (iterate dec a))
    (take (inc (- b a)) (iterate inc a))))

(defn bounds
  [[x1 y1 x2 y2]]
  (let [left (min x1 x2)
        right (max x1 x2)
        top (min y1 y2)
        bottom (max y1 y2)]
    [left top right bottom]))

(defn is-vertical
  [[x1 y1 x2 y2]]
  (= x1 x2))

(defn is-horizontal
  [[x1 y1 x2 y2]]
  (= y1 y2))

(defn parallel-intersection-points
  [f c11 c12 c21 c22]
  (let [min1 (min c11 c12) max1 (max c11 c12)
        min2 (min c21 c22) max2 (max c21 c22)]
    (if (or (> min1 max2) (< max1 min2))
      '()
      (map f (range (max min1 min2) (inc (min max1 max2)))))))

(defn perpendicular-intersection-points
  [[x11 y11 x12 y12] [x21 y21 x22 y22]]
  (let [min-x1 (min x11 x12)
        min-y1 (min y11 y12)
        min-x2 (min x21 x22)
        min-y2 (min y21 y22)
        max-x1 (max x11 x12)
        max-y1 (max y11 y12)
        max-x2 (max x21 x22)
        max-y2 (max y21 y22)]
    (cond
      (> min-x1 max-x2) '()
      (< max-x1 min-x2) '()
      (> min-y1 max-y2) '()
      (< max-y1 min-y2) '()
      (and (= min-x1 max-x1) (= min-y2 max-y2)) (list [min-x1 min-y2])
      (and (= min-x2 max-x2) (= min-y1 max-y1)) (list [min-x2 min-y1])
      :else (throw (AssertionError. "unexpected case")))))

(defn intersection-points
  [[x11 y11 x12 y12] [x21 y21 x22 y22]]
  (cond
    (= x11 x12 x21 x22) (parallel-intersection-points (fn [y] [x11 y]) y11 y12 y21 y22)
    (= y11 y12 y21 y22) (parallel-intersection-points (fn [x] [x y11]) x11 x12 x21 x22)
    (and (= x11 x12) (= x21 x22)) '() ; parallel but on different axis
    (and (= y11 y12) (= y21 y22)) '() ; parallel but on different axis
    :else (perpendicular-intersection-points [x11 y11 x12 y12] [x21 y21 x22 y22])))

(deftest vertical-intersection
  (is (= '([1 2] [1 3]) (intersection-points [1 0 1 3] [1 2 1 5])))
  (is (= '() (intersection-points [1 0 1 3] [1 4 1 6]))))

(deftest horizontal-intersection
  (is (= '([2 1] [3 1]) (intersection-points [0 1 3 1] [2 1 5 1])))
  (is (= '() (intersection-points [0 1 3 1] [4 1 6 1]))))

(deftest perpendicular-intersection
  (is (= '([7 4]) (intersection-points [7 0 7 4] [9 4 3 4]))))

(comment
  (for [x1 (from-to x11 x12)
        y1 (from-to y11 y12)
        x2 (from-to x21 x22)
        y2 (from-to y21 y22)
        :when (and (= x1 x2) (= y1 y2))]
    [x1 y1]))

(defn all-intersection-points
  [vents]
  (loop [[vent & remainder] vents
         found-points '()]
    (if (empty? remainder)
      (set found-points)
      (recur remainder (concat found-points (mapcat #(intersection-points vent %) remainder))))))

(deftest parallel-vent-intersection
  (is (= '([0 9] [1 9] [2 9])
         (intersection-points [0 9 5 9] [0 9 2 9]))))

(defn part1
  [vent-lines]
  (count
   (all-intersection-points
    (filter #(or (is-vertical %) (is-horizontal %))
            (parse-vents vent-lines)))))

(deftest part1-example
  (is (= 5 (with-line-seq "day-5-example.txt" part1))))

(deftest part1-example
  (is (= 7674 (with-line-seq "day-5-input.txt" part1))))

(run-tests 'adventofcode.2021.day-5)
