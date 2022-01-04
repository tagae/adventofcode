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

(defn dot-matrix
  [dots]
  (reduce (fn [result [x y]]
            (if (< y (count result))
              (let [row (result y)]
                (assoc result y (conj (into row (repeat (- x (count row)) '.)) 'O)))
              (conj result (conj (vec (repeat (dec x) '.)) 'O))))
          []
          (sort-by (fn [[x y]] [y x]) dots)))

(defn print-matrix
  [matrix]
  (println)
  (doseq [y (range 0 (count matrix))]
    (println (matrix y)))
  matrix)

(defn part2
  [{:keys [dots foldings]}]
  (print-matrix (dot-matrix (fold-paper dots foldings))))

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

(deftest part2-example
  (is (= '[[O O O O O]
           [O . . . O]
           [O . . . O]
           [O . . . O]
           [O O O O O]]
         (with-line-seq "day-13-example.txt"
           (comp part2 parse-input)))))

(deftest part2-input
  (is (= '[[O . . O . O . . O . O . . O . . . O O . . O O . . . O O . . . . O O . O O O O]
           [O . . O . O . O . . O . . O . . . . O . O . . O . O . . O . . . . O . . . . O]
           [O O O O . O O . . . O . . O . . . . O . O . . . . O . . O . . . . O . . . O]
           [O . . O . O . O . . O . . O . . . . O . O . O O . O O O O . . . . O . . O]
           [O . . O . O . O . . O . . O . O . . O . O . . O . O . . O . O . . O . O]
           [O . . O . O . . O . . O O . . . O O . . . O O O . O . . O . . O O . . O O O O]]
         (with-line-seq "day-13-input.txt"
           (comp part2 parse-input)))))

(run-tests 'adventofcode.2021.day-13)
