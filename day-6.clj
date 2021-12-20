(ns adventofcode.2021.day-6
  (:use [clojure.string :only [split]]
        clojure.set clojure.test))

(defn with-line-seq
  [file-name func]
  (with-open [rdr (clojure.java.io/reader file-name)]
    (func (line-seq rdr))))

(defn count-offspring
  ([days-until-spawning days-left]
   (count-offspring (- days-left days-until-spawning)))
  ([days-left]
   (if (> days-left 0)
     (+ (count-offspring (- days-left 7))
        (count-offspring (- days-left 9)))
     1)))

(defn part1
  [days-left initial-states]
  (apply + (map #(count-offspring % days-left) initial-states)))

(defn parse-states
  [[state-line & ignored]]
  (map #(Integer. %) (split state-line #",")))

(deftest part1-base-cases
  (is (= 2 (part1 1 '(0))))
  (is (= 2 (part1 4 '(3)))))

(deftest part1-example
  (is (= 26 (part1 18 '(3 4 3 1 2)))))

(deftest part1-example
    (is (= 5934 (with-line-seq "day-6-example.txt"
                  (comp (partial part1 80) parse-states)))))

(run-tests 'adventofcode.2021.day-6)
