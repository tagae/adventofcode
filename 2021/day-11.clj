(ns adventofcode.2021.day-11
  (:use clojure.test
        [clojure.string :only [join]]))

(def vicinity-coords
  #{[1 0] [-1 0] [1 1] [-1 -1] [1 -1] [-1 1] [0 -1] [0 1]})

(defn with-energy-level-at
  [energy-levels j i func]
  (let [grid-bound (dec (count energy-levels))]
    (if (and (<= 0 j grid-bound) (<= 0 i grid-bound))
      (let [level ((energy-levels j) i)]
        (if (number? level)
          (assoc energy-levels j (assoc (energy-levels j) i (func level)))
          energy-levels))
      energy-levels)))

(defn increase-level-at
  [energy-levels j i]
  (with-energy-level-at energy-levels j i inc))

(defn flash-at
  [energy-levels j i]
  (with-energy-level-at energy-levels j i (constantly 'f)))

(defn should-flash?
  [energy-level]
  (and (number? energy-level) (> energy-level 9)))

(defn propagate-flashes
  [energy-levels]
  (let [grid-size (count energy-levels)]
    (loop [j 0
           i 0
           energy-levels energy-levels]
      (if (< j grid-size)
        (if (should-flash? ((energy-levels j) i))
          (let [flashed (flash-at energy-levels j i)]
            (recur (max 0 (dec j))
                   (max 0 (dec i))
                   (reduce (fn [result [delta-j delta-i]]
                             (increase-level-at result (+ j delta-j) (+ i delta-i)))
                           flashed
                           vicinity-coords)))
          (recur (+ j (quot (inc i) grid-size))
                 (mod (inc i) grid-size)
                 energy-levels))
        energy-levels))))

(defn count-flashes
  [energy-levels]
  (reduce + (map #(count (filter #{'f} %)) energy-levels)))

(defn reset-flashes
  [energy-levels]
  (mapv #(replace {'f 0} %) energy-levels))

(defn steps
  [count energy-levels]
  (loop [energy-levels energy-levels
         total-flashes 0
         remaining-steps count]
    (if (> remaining-steps 0)
      (let [increased-levels (mapv #(mapv inc %) energy-levels)
            propagated-flashes (propagate-flashes increased-levels)]
        (recur (reset-flashes propagated-flashes)
               (+ total-flashes (count-flashes propagated-flashes))
               (dec remaining-steps)))
      total-flashes)))

(defn parse-energy-levels
  [lines]
  (let [digit #(Character/digit % 10)]
    (mapv #(mapv digit %) lines)))

(defn with-line-seq
  [file-name func]
  (with-open [rdr (clojure.java.io/reader file-name)]
    (func (line-seq rdr))))

(deftest energy-level-parsing
  (is (= 6 (((with-line-seq "day-11-example.txt" parse-energy-levels) 9) 9))))

(deftest part1-example
  (is (= 1656 (with-line-seq "day-11-example.txt"
                #(steps 100 (parse-energy-levels %))))))

(deftest part1-input
  (is (= 1747 (with-line-seq "day-11-input.txt"
                #(steps 100 (parse-energy-levels %))))))

(run-tests 'adventofcode.2021.day-11)
