(ns adventofcode.2021.day-4
  (:use [clojure.string :only [split blank?]]
        clojure.test))

(defn parse-draw-numbers
  [draw-line]
  (map #(Integer. %) (split draw-line #",")))

(defn parse-boards
  [input]
  (letfn [(not-blank [line] (not (blank? line)))
          (parse-row [line] (mapv #(Integer/parseInt %) (re-seq #"\d+" line)))]
    (if-let [next-board-lines (seq (take-while not-blank input))]
      (cons
       (mapv parse-row next-board-lines)
       (parse-boards (drop (inc (count next-board-lines)) input))))))

(defn parse-lines
  [lines]
  (let [[draw-line ignored-line & board-lines] lines]
    {:draw-numbers (parse-draw-numbers draw-line)
     :boards (parse-boards board-lines)}))

(defn nth-column [board index]
  (mapv #(nth % index) board))

(defn transpose [board]
  (mapv #(nth-column board %) (range (count (first board)))))

(defn winning-sets
  [board]
  (concat (map set board) (map set (transpose board))))

(defn after-number-call
  [called-number winning-sets]
  (map #(disj % called-number) winning-sets))

(defn is-winner
  [winning-sets]
  (some empty? winning-sets))

(defn calculate-score
  [called-number winning-sets]
  (* called-number
     (/ (reduce (partial apply +) 0 winning-sets) 2)))

(defn make-winner
  [called-number winning-sets]
  {:winning-sets winning-sets :last-number called-number})

(defn find-winners
  [draw-numbers boards]
  (loop [[called-number & remaining-numbers] draw-numbers
         candidates (map winning-sets boards)
         found-winners '()]
    (if called-number
      (let [updated-candidates (map #(after-number-call called-number %) candidates)
            grouped-candidates (group-by is-winner updated-candidates)
            new-winners (map #(make-winner called-number %) (grouped-candidates true))
            remaining-candidates (grouped-candidates nil)
            updated-winners (concat found-winners new-winners)]
        (recur remaining-numbers remaining-candidates updated-winners))
      found-winners)))

(defn find-winner
  [pick lines]
  (let [{:keys [draw-numbers boards]} (parse-lines lines)
        {:keys [winning-sets last-number]} (pick (find-winners draw-numbers boards))]
    (calculate-score last-number winning-sets)))

(defn part1
  [lines]
  (find-winner first lines))

(defn part2
  [lines]
  (find-winner last lines))

(defn with-line-seq
  [file-name func]
  (with-open [rdr (clojure.java.io/reader file-name)]
    (func (line-seq rdr))))

(deftest part1-with-example
  (is (= 4512 (with-line-seq "day-4-example.txt" part1))))

(deftest part1-with-input
  (is (= 22680 (with-line-seq "day-4-input.txt" part1))))

(deftest part2-with-example
  (is (= 1924 (with-line-seq "day-4-example.txt" part2))))

(deftest part2-with-input
  (is (= 16168 (with-line-seq "day-4-input.txt" part2))))

(run-tests 'adventofcode.2021.day-4)
