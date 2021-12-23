(ns adventofcode.2021.day-6
  (:use [clojure.string :only [split]]
        clojure.set clojure.test))

(defn with-line-seq
  [file-name func]
  (with-open [rdr (clojure.java.io/reader file-name)]
    (func (line-seq rdr))))

(def count-memoised
  (memoize (fn [days-left]
             (cond
               (<= 9 days-left) (+ (count-memoised (- days-left 7))
                                   (count-memoised (- days-left 9)))
               (= days-left 8) 3
               (<= 1 days-left 7) 2
               (= days-left 0) 1))))

(defn count-offspring
  [days-until-spawning days-left]
  (count-memoised (- days-left days-until-spawning)))

(defn part1
  [days-left initial-states]
  (apply + (map #(count-offspring % days-left) initial-states)))

(defn parse-states
  [[state-line & ignored]]
  (map #(Integer. %) (split state-line #",")))

(deftest part1-base-cases
  (is (= 2 (part1 1 '(0))))
  (is (= 2 (part1 4 '(3)))))

(deftest part1-simple-example
  (is (= 26 (part1 18 '(3 4 3 1 2)))))

(deftest part1-example
    (is (= 5934 (with-line-seq "day-6-example.txt"
                  (comp (partial part1 80) parse-states)))))

(deftest part1-input
  (is (= 355386 (with-line-seq "day-6-input.txt"
                  (comp (partial part1 80) parse-states)))))

(deftest part2-simple-example
  (is (= 26984457539 (part1 256 '(3 4 3 1 2)))))

(deftest part2-input
  (is (= 1613415325809 (with-line-seq "day-6-input.txt"
                         (comp (partial part1 256) parse-states)))))

(run-tests 'adventofcode.2021.day-6)
