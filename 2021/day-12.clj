(ns adventofcode.2021.day-12
  (:use clojure.test
        [clojure.set :only [map-invert]]))

(defn small-cave?
  [cave]
  (Character/isLowerCase (first (name cave))))

(defn find-caves-part1
  [adjacent-caves cave path]
  (let [adjacent (get adjacent-caves cave #{})
        visited-small-caves (filter small-cave? path)]
    (reduce disj adjacent visited-small-caves)))

(defn find-caves-part2
  [adjacent-caves cave path]
  (let [adjacent (disj (get adjacent-caves cave #{}) 'start)
        visited-small-caves (filter small-cave? path)
        small-cave-visited-twice? (some #{2} (vals (frequencies visited-small-caves)))]
    (if small-cave-visited-twice?
      (reduce disj adjacent (filter small-cave? path))
      adjacent)))

(defn cave-paths
  [find-caves adjacent-caves]
  (loop [pending-paths [['start]]
         finished-paths '()]
    (if (empty? pending-paths)
      finished-paths
      (let [path (peek pending-paths)
            cave (peek path)
            next-caves (find-caves adjacent-caves cave path)
            remaining-paths (pop pending-paths)]
        (if (= cave 'end)
          (recur remaining-paths
                 (cons path finished-paths))
          (recur (into remaining-paths (map (partial conj path) next-caves))
                 finished-paths))))))

(defn cave-graph
  "Builds an adjacency list representation of a cave map, with symmetric edges"
  [cave-map]
  (reduce (fn [graph [a b]] (assoc graph a (conj (get graph a #{}) b)))
          {}
          (mapcat (fn [[a b]] [[a b] [b a]]) cave-map)))

(defn parse-cave-map
  [lines]
  (for [line lines]
    (map symbol (re-seq #"\w+" line))))

(def part1
  (comp count
        (partial cave-paths find-caves-part1)
        cave-graph
        parse-cave-map))

(def part2
  (comp count
        (partial cave-paths find-caves-part2)
        cave-graph
        parse-cave-map))

(defn with-line-seq
  [file-name func]
  (with-open [rdr (clojure.java.io/reader file-name)]
    (func (line-seq rdr))))

(deftest parse-example
  (is (= '(start A)
         (first (with-line-seq "day-12-example.txt" parse-cave-map)))))

(deftest example-cave-graph
  (is (= {'start '#{A b}
          'A '#{start c b end}
          'b '#{start A d end}
          'c '#{A}
          'd '#{b}
          'end '#{A b}}
         (with-line-seq "day-12-example.txt"
           (comp cave-graph parse-cave-map)))))

(deftest part1-example
  (is (= 10 (with-line-seq "day-12-example.txt" part1))))

(deftest part1-larger-example
  (is (= 19 (with-line-seq "day-12-larger-example.txt" part1))))

(deftest part1-even-larger-example
  (is (= 226 (with-line-seq "day-12-even-larger-example.txt" part1))))

(deftest part1-input
  (is (= 4186 (with-line-seq "day-12-input.txt" part1))))

(deftest part2-example
  (is (= 36 (with-line-seq "day-12-example.txt" part2))))

(deftest part2-input
  (is (= 92111 (with-line-seq "day-12-input.txt" part2))))

(run-tests 'adventofcode.2021.day-12)
