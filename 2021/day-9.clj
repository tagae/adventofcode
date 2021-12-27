(ns adventofcode.2021.day-9
  (:use clojure.test))

(defn heightmap-at
  [heightmap x y]
  (nth (nth heightmap y []) x ##Inf))

(defn low-point?
  [heightmap x y]
  (let [height ((heightmap y) x)
        left (heightmap-at heightmap (dec x) y)
        right (heightmap-at heightmap (inc x) y)
        up (heightmap-at heightmap x (dec y))
        down (heightmap-at heightmap x (inc y))]
    (and (< height left)
         (< height right)
         (< height up)
         (< height down))))

(defn at-low-points
  [heightmap func]
  (let [width (count (first heightmap))
        height (count heightmap)]
    (for [j (range 0 height)
          i (range 0 width)
          :when (low-point? heightmap i j)]
      (func heightmap i j))))

(defn risk-levels
  [heightmap]
  (at-low-points heightmap (fn [heightmap i j] (inc ((heightmap j) i)))))

(defn part1
  [heightmap]
  (apply + (risk-levels heightmap)))

(defn mark-at
  [heightmap x y area]
  (assoc heightmap y (assoc (heightmap y) x area)))

(defn mark-basin-at
  [heightmap x y]
  (let [current (heightmap-at heightmap x y)]
    (if (or (= current 9) (= current ##Inf) (symbol? current))
      heightmap
      (let [current (mark-at heightmap x y 'b)
            up (mark-basin-at current x (inc y))
            down (mark-basin-at up x (dec y))
            right (mark-basin-at down (inc x) y)
            left (mark-basin-at right (dec x) y)
            result left]
        result))))

(defn basin-size
  [heightmap]
  (count (filter #{'b} (flatten heightmap))))

(defn basin-size-at
  [heightmap i j]
  (basin-size (mark-basin-at heightmap i j)))

(defn basin-sizes
  [heightmap]
  (at-low-points heightmap (fn [heightmap i j] (basin-size-at heightmap i j))))

(defn part2
  [heightmap]
  (apply * (take 3 (sort > (basin-sizes heightmap)))))

(defn parse-heightmap
  [lines]
  (vec
   (for [line lines]
     (mapv #(Character/digit % 10) line))))

(defn with-line-seq
  [file-name func]
  (with-open [rdr (clojure.java.io/reader file-name)]
    (func (line-seq rdr))))

(deftest risk-levels-example
  (is (= '(2 1 6 6)
         (with-line-seq "day-9-example.txt"
           (comp risk-levels parse-heightmap)))))

(deftest part-1-example
  (is (= 15 (with-line-seq "day-9-example.txt"
              (comp part1 parse-heightmap)))))

(deftest part-1-input
  (is (= 522 (with-line-seq "day-9-input.txt"
               (comp part1 parse-heightmap)))))

(deftest part2-example-top-left
  (is (= 3 (with-line-seq "day-9-example.txt"
             (comp basin-size #(mark-basin-at % 0 0) parse-heightmap)))))

(deftest part2-example-top-right
  (is (= 9 (with-line-seq "day-9-example.txt"
             (comp basin-size #(mark-basin-at % 9 0) parse-heightmap)))))

(deftest part2-example-basin-sizes
  (is (= '(3 9 14 9) (with-line-seq "day-9-example.txt"
              (comp basin-sizes parse-heightmap)))))

(deftest part2-example
  (is (= 1134 (with-line-seq "day-9-example.txt"
                (comp part2 parse-heightmap)))))

(deftest part2-input
  (is (= 916688 (with-line-seq "day-9-input.txt"
                  (comp part2 parse-heightmap)))))

(run-tests 'adventofcode.2021.day-9)
