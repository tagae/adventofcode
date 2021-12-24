(ns adventofcode.2021.day-8
  (:use [clojure.string :only [split blank?]]
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

(deftest part1-example
  (is (= 26 (with-line-seq "day-8-example.txt" part1))))

(deftest part1-input
  (is (= 355 (with-line-seq "day-8-input.txt" part1))))

(run-tests 'adventofcode.2021.day-8)
