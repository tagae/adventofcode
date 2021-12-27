(ns adventofcode.2021.day-7
  (:use [clojure.string :only [split]]
        clojure.test))

(defn with-line-seq
  [file-name func]
  (with-open [rdr (clojure.java.io/reader file-name)]
    (func (line-seq rdr))))

(defn fuel-to
  [target positions]
  (apply + (map (fn [position]
                  (if (<= position target)
                    (- target position)
                    (- position target))) positions)))

(defn find-best
  [measure-distance positions]
  (let [farthest-position (apply max positions)]
    (reduce (fn [[best-position best-fuel] target]
              (let [target-fuel (measure-distance target positions)]
                (if (< target-fuel best-fuel)
                  [target target-fuel]
                  [best-position best-fuel])))
            [farthest-position (measure-distance farthest-position positions)]
            (range (apply min positions) farthest-position))))

(defn part1
  [positions]
  (find-best fuel-to positions))

(defn gauss-sum
  [n]
  (/ (* n (inc n)) 2))

(defn incremental-fuel-to
  [target positions]
  (apply + (map (fn [position]
                  (gauss-sum (if (<= position target)
                               (- target position)
                               (- position target)))) positions)))

(defn part2
  [positions]
  (find-best incremental-fuel-to positions))

(defn parse-positions
  [[position-line & ignored]]
  (map #(Integer. %) (split position-line #",")))

(deftest part1-example
  (is (= [2 37] (with-line-seq "day-7-example.txt" (comp part1 parse-positions)))))

(deftest part1-input
  (is (= [343 340987] (with-line-seq "day-7-input.txt" (comp part1 parse-positions)))))

(deftest part2-example
  (is (= [5 168] (with-line-seq "day-7-example.txt" (comp part2 parse-positions)))))

(deftest part2-example-prev
  (is (= 206 (with-line-seq "day-7-example.txt"
               (comp (partial incremental-fuel-to 2) parse-positions)))))

(deftest part2-input
  (is (= [478 96987874] (with-line-seq "day-7-input.txt" (comp part2 parse-positions)))))

(run-tests 'adventofcode.2021.day-7)
