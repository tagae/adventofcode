(ns adventofcode.2021.day-11
  (:use clojure.test
        [clojure.string :only [join]]))

(def vicinity-deltas
  #{[1 0] [-1 0] [1 1] [-1 -1] [1 -1] [-1 1] [0 -1] [0 1]})

(defn within-bounds?
  [energy-levels j i]
  (let [grid-size (count energy-levels)]
    (and (<= 0 j) (< j grid-size) (<= 0 i) (< i grid-size))))

(defn increase-level-at
  [energy-levels j i]
  (if (within-bounds? energy-levels j i)
    (let [level ((energy-levels j) i)
          increased (assoc energy-levels j
                           (assoc (energy-levels j) i
                                  (inc level)))]
      (condp = level
        9 (reduce (fn [result [delta-j delta-i]]
                    (increase-level-at result (+ j delta-j) (+ i delta-i)))
                  increased
                  vicinity-deltas)
        increased))
    energy-levels))

(defn step
  [energy-levels]
  (let [grid-size (count energy-levels)]
    (loop [j 0
           i 0
           energy-levels energy-levels]
      (if (< j grid-size)
        (recur (+ j (quot (inc i) grid-size))
               (mod (inc i) grid-size)
               (increase-level-at energy-levels j i))
        (mapv (fn [row] (mapv #(if (> % 9) 0 %) row)) energy-levels)))))

(defn count-flashes
  [energy-levels]
  (reduce + (map #(count (filter #{0} %)) energy-levels)))

(defn part1
  [step-count energy-levels]
  (reduce +
          (take step-count
                (map count-flashes
                     (drop 1 (iterate step energy-levels))))))

(defn simultaneous-flash?
  [energy-levels]
  (every? true? (map (partial every? zero?) energy-levels)))

(defn part2
  [energy-levels]
  (first
   (keep-indexed #(if (simultaneous-flash? %2) %1)
                 (iterate step energy-levels))))

(defn parse-energy-levels
  [lines]
  (let [digit #(Character/digit % 10)]
    (mapv #(mapv digit %) lines)))

(defn with-line-seq
  [file-name func]
  (with-open [rdr (clojure.java.io/reader file-name)]
    (func (line-seq rdr))))

(def with-energy-levels
  #(doall (with-line-seq % parse-energy-levels)))

(defn print-levels
  [energy-levels]
  (run! println energy-levels)
  (println))

(deftest energy-level-parsing
  (is (= 6 (((with-energy-levels "day-11-example.txt") 9) 9))))

(deftest part1-small-example
  (is (= 9 (part1 2 (with-energy-levels "day-11-small-example.txt")))))

(deftest part1-example-step-10
  (is (= 204 (part1 10 (with-energy-levels "day-11-example.txt")))))

(deftest part1-example
  (is (= 1656 (part1 100 (with-energy-levels "day-11-example.txt")))))

(deftest part1-input
  (is (= 1747 (part1 100 (with-line-seq "day-11-input.txt" parse-energy-levels)))))

(deftest part2-example
  (is (= 195 (part2 (with-energy-levels "day-11-example.txt")))))

(deftest part2-input
  (is (= 505 (part2 (with-energy-levels "day-11-input.txt")))))

(run-tests 'adventofcode.2021.day-11)
