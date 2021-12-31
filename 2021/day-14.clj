(ns adventofcode.2021.day-14
  (:use clojure.test))

(defn parse-input
  [lines]
  {:polymer
   (map (comp symbol str) (first lines))
   :rules
   (reduce (fn [rules line]
             (let [[[_ a b r & tail] & none] (re-seq #"(\w)(\w) -> (\w)" line)]
               (assoc rules [(symbol a) (symbol b)] (symbol r))))
           {}
           (drop 2 lines))})


(defn polymer-pairs
  [polymer]
  (let [pairs (map vector polymer (drop 1 polymer))]
    (cons ['start (first polymer)]
          (cons [(last polymer) 'end]
                pairs))))

(defn rule-deltas
  [rules pair-counts]
  (mapcat (fn [[pair count]]
            (if-let [element (rules pair)]
              (list
               [pair (- count)]
               [[(first pair) element] count]
               [[element (second pair)] count])
              '()))
          pair-counts))

(defn apply-deltas
  [pair-counts deltas]
  (reduce (fn [result [pair delta]]
            (let [count (or (result pair) 0)
                  new-count (+ count delta)]
              (if (zero? new-count)
                (dissoc result pair)
                (assoc result pair new-count))))
          pair-counts
          deltas))

(defn apply-rules
  [rules pair-counts]
  (apply-deltas pair-counts (rule-deltas rules pair-counts)))

(defn polymer-pair-counts
  [input]
  (let [pairs (polymer-pairs (input :polymer))
        pair-counts (frequencies pairs)]
    (iterate (partial apply-rules (input :rules)) pair-counts)))

(defn count-pair-elements
  [pair-counts]
  (reduce (fn [element-counts [[a b] count]]
            (update (update element-counts a (fnil + 0) count)
                    b (fnil + 0) count))
          {}
          pair-counts))

(defn element-quantities
  [pair-counts]
  (map (fn [[element count]] (/ count 2))
       (dissoc (count-pair-elements pair-counts) 'start 'end)))

(defn max-min-diff
  [step-count input]
  (let [nth-step (nth (polymer-pair-counts input) step-count)
        quantities (element-quantities nth-step)]
    (- (apply max quantities) (apply min quantities))))

(def part1 (partial max-min-diff 10))

(def part2 (partial max-min-diff 40))

(defn with-line-seq
  [file-name func]
  (with-open [rdr (clojure.java.io/reader file-name)]
    (func (line-seq rdr))))

(deftest parsing
  (let [parsed (with-line-seq "day-14-example.txt" (comp parse-input doall))]
    (is (= '(N N C B) (parsed :polymer)))
    (is (= 'B (get (parsed :rules) '[C H])))))

(deftest part1-example
  (is (= 1588
         (with-line-seq "day-14-example.txt"
           (comp part1 parse-input)))))

(deftest part1-input
  (is (= 2988
         (with-line-seq "day-14-input.txt"
           (comp part1 parse-input)))))

(deftest part2-example
  (is (= 2188189693529
         (with-line-seq "day-14-example.txt"
           (comp part2 parse-input)))))

(deftest part2-input
  (is (= 3572761917024
         (with-line-seq "day-14-input.txt"
           (comp part2 parse-input)))))

(run-tests 'adventofcode.2021.day-14)
