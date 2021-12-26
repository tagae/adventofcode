(ns adventofcode.2021.day-8
  (:use [clojure.string :only [split blank?]]
        [clojure.set :only [difference intersection map-invert]]
        clojure.test))

(defn with-line-seq
  [file-name func]
  (with-open [rdr (clojure.java.io/reader file-name)]
    (func (line-seq rdr))))

(defn parse-entries
  [str]
  (remove blank? (split str #"\s")))

(defn parse-note
  [note]
  (let [[signal-part output-part] (split note #"[|]")
        signal-patterns (parse-entries signal-part)
        output-value (parse-entries output-part)]
    [signal-patterns output-value]))

(defn easy-digits
  [output-value]
  (filter #(#{2 4 3 7} (count %)) output-value))

(defn part1
  [notes]
  (reduce + (map (comp count easy-digits second parse-note) notes)))

(def segment-counts
  {1 2
   4 4
   7 3
   8 7})

(defn pattern-signals
  [pattern]
  (reduce (fn [result char] (conj result (symbol (str char)))) #{} pattern))

(defn decode-patterns
  [patterns]
  (loop [signals (set (map pattern-signals patterns))
         decoded-numbers {}
         [number & remaining-numbers] '(1 4 7 8 3 2 5 6 9 0)]
    (letfn [(find-by [& comparisons]
              (first
               (reduce (fn [result [op key-numbers segment-count]]
                         (let [key-signals (map decoded-numbers key-numbers)]
                           (filter #(= (count (apply op % key-signals)) segment-count) result)))
                       signals
                       comparisons)))]
      (if number
        (let [signal (condp = number
                       1 (find-by [identity '() (segment-counts 1)])
                       4 (find-by [identity '() (segment-counts 4)])
                       7 (find-by [identity '() (segment-counts 7)])
                       8 (find-by [identity '() (segment-counts 8)])
                       3 (find-by [difference '(1) 3])
                       2 (find-by [difference '(3) 1] [intersection '(1) 1] [difference '(4) 3])
                       5 (find-by [intersection '(1) 1] [difference '(4) 2])
                       6 (find-by [intersection '(1) 1])
                       9 (find-by [intersection '(4) 4])
                       0 (find-by [intersection '(8) 6]))]
          (recur
           (disj signals signal)
           (assoc decoded-numbers number signal)
           remaining-numbers))
        decoded-numbers))))

(defn decode-output
  [note]
  (let [[patterns output-value] (parse-note note)
        encoding (map-invert (decode-patterns patterns))
        output-signals (map pattern-signals output-value)
        output-digits (map encoding output-signals)]
    (reduce (fn [result digit] (+ (* result 10) digit)) 0 output-digits)))

(defn part2
  [notes]
  (apply + (map decode-output notes)))

(deftest part1-example
  (is (= 26 (with-line-seq "day-8-example.txt" part1))))

(deftest part1-input
  (is (= 355 (with-line-seq "day-8-input.txt" part1))))

(deftest part2-simple-example
  (is (= 5353 (decode-output "acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf"))))

(deftest part2-example
  (is (= 61229 (with-line-seq "day-8-example.txt" part2))))

(deftest part2-input
  (is (= 983030 (with-line-seq "day-8-input.txt" part2))))

(run-tests 'adventofcode.2021.day-8)
