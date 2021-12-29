(ns adventofcode.2021.day-10
  (:use clojure.test
        [clojure.string :only [join]]))

(def matching-closings
  {\( \)
   \[ \]
   \{ \}
   \< \>})

(def valid-openings
  (set (keys matching-closings)))

(defn check-chunks
  "Returns the first non-matching closing character if any,
   otherwise the list of openings that lack a closing."
  [chunks]
  (loop [[head & tail] chunks
         pending-openings '()]
    (if head
      (if (valid-openings head)
        (recur tail (cons head pending-openings))
        (let [[last-opening & remaining-openings] pending-openings]
          (if (= head (matching-closings last-opening))
            (recur tail remaining-openings)
            head)))
      pending-openings)))

(def illegal-closing-points
  {\) 3
   \] 57
   \} 1197
   \> 25137})

(defn illegal-closing-chars
  [lines]
  (filter char? (map check-chunks lines)))

(defn part1
  [lines]
  (reduce + (map illegal-closing-points (illegal-closing-chars lines))))

(defn closing-characters
  [openings]
  (map matching-closings openings))

(def legal-closing-points
  {\) 1
   \] 2
   \} 3
   \> 4})

(defn closing-characters-score
  [closing-chars]
  (reduce
   (fn [total char] (+ (* total 5) (legal-closing-points char)))
   0 closing-chars))

(defn median
  [scores]
  (let [sorted (sort scores)
        halfway (quot (count sorted) 2)]
    (nth sorted halfway)))

(defn pending-openings
  [lines]
  (filter seq? (map check-chunks lines)))

(defn part2
  [lines]
  (median
   (mapv (comp closing-characters-score closing-characters)
         (pending-openings lines))))

(defn with-line-seq
  [file-name func]
  (with-open [rdr (clojure.java.io/reader file-name)]
    (func (line-seq rdr))))

(deftest part1-example
  (is (= 26397 (with-line-seq "day-10-example.txt" part1))))

(deftest part1-input
  (is (= 392043 (with-line-seq "day-10-input.txt" part1))))

(deftest part2-example-5-closing
  (is (= "])}>" (join (closing-characters
                       (check-chunks "<{([{{}}[<[[[<>{}]]]>[]]"))))))

(deftest part2-example
  (is (= 288957 (with-line-seq "day-10-example.txt" part2))))

(deftest part2-input
  (is (= 1605968119 (with-line-seq "day-10-input.txt" part2))))

(run-tests 'adventofcode.2021.day-10)
