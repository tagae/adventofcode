(ns adventofcode.2021.day-13
  (:use clojure.test
        [clojure.string :only [blank?]]))

(defn parse-input
  [lines]
  (let [[dot-lines instruction-lines] (split-with (complement blank?) lines)]
    {:dots
     (for [line dot-lines :while (not (blank? line))]
       (mapv #(Integer/parseInt % 10) (re-seq #"\d+" line)))
     :foldings
     (for [line (rest instruction-lines)]
       (let [[_ axis coord] (first (re-seq #"([xy])=(\d+)" line))]
         [(symbol axis) (Integer/parseInt coord 10)]))}))

(defn shift-coords
  [dots [dx dy]]
  (map (fn [[x y]] [(+ x dx) (+ y dy)]) dots))

(defn fold-delta
  [axis coord]
  (condp = axis
    'x [(- coord) 0]
    'y [0 (- coord)]))

(defn abs
  [n]
  (if (< n 0) (- n) n))

(defn reflect
  [w coord]
  (- coord (abs (- w coord))))

(defn fold-paper
  [dots foldings]
  (reduce (fn [result [axis coord]]
            (set (map (fn [[x y]]
                        (condp = axis
                          'x [(reflect x coord) y]
                          'y [x (reflect y coord)]))
                      result)))
          dots
          foldings))

(defn part1
  [{:keys [dots foldings]}]
  (fold-paper dots (take 1 foldings)))

(defn with-line-seq
  [file-name func]
  (with-open [rdr (clojure.java.io/reader file-name)]
    (func (doall (line-seq rdr)))))

(deftest parsing
  (let [parsed (with-line-seq "day-13-example.txt" parse-input)]
    (is (= 18 (count (parsed :dots))))
    (is (= '([y 7] [x 5]) (parsed :foldings)))))

(deftest part1-example
  (is (= 17 (with-line-seq "day-13-example.txt"
              (comp count part1 parse-input)))))

(deftest part1-example
  (is (= 621 (with-line-seq "day-13-input.txt"
               (comp count part1 parse-input)))))

(run-tests 'adventofcode.2021.day-13)
