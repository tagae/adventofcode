(ns adventofcode.2021.day-10
  (:use clojure.test))

(def matching-closings
  {\( \)
   \[ \]
   \{ \}
   \< \>})

(def valid-openings
  (set (keys matching-closings)))

(defn first-illegal-closing
  [line]
  (loop [chunks line
         openings '()]
    (let [[head & tail] chunks
          [last-opening & remaining-openings] openings]
      (if (valid-openings head)
        (recur tail (cons head openings))
        (if (= head (matching-closings last-opening))
          (recur tail remaining-openings)
          head)))))

(defn find-illegal-closings
  [lines]
  (for [line lines
        :let [illegal-closing (first-illegal-closing line)]
        :when illegal-closing]
    illegal-closing))

(def illegal-closing-points
  {\) 3
   \] 57
   \} 1197
   \> 25137})

(defn part1
  [lines]
  (apply + (map illegal-closing-points (find-illegal-closings lines))))

(defn with-line-seq
  [file-name func]
  (with-open [rdr (clojure.java.io/reader file-name)]
    (func (line-seq rdr))))

(deftest part1-example
  (is (= 26397 (with-line-seq "day-10-example.txt" part1))))

(deftest part1-input
  (is (= 392043 (with-line-seq "day-10-input.txt" part1))))

(run-tests 'adventofcode.2021.day-10)
