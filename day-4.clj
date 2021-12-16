(ns adventofcode.2021
  (:use [clojure.string :only [split blank?]]))

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

(defn part1
  [lines]
  (let [{:keys [draw-numbers boards]} (parse-lines lines)]
    (loop [[called-number & remaining-numbers] draw-numbers
           candidates (map winning-sets boards)]
      (let [updated-candidates (map #(after-number-call called-number %) candidates)
            winner (first (filter is-winner updated-candidates))]
        (if winner
          (calculate-score called-number winner)
          (recur remaining-numbers updated-candidates))))))

(defn with-line-seq
  [file-name func]
  (with-open [rdr (clojure.java.io/reader file-name)]
    (func (line-seq rdr))))

(with-line-seq "day-4-example.txt" part1)
(with-line-seq "day-4-input.txt" part1)
