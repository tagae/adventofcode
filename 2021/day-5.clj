(ns adventofcode.2021.day-5
  (:use clojure.set clojure.test))

(defn cardinal-direction
  [[x1 y1 x2 y2]]
  (cond
    (= x1 x2) :south
    (< y1 y2) :south-east
    (= y1 y2) :east
    (> y1 y2) :north-east))

(defn clip-at
  [& vents]
  (fn [[x y]]
    (loop [[next & remainder] vents]
      (if next
        (let [[x1 y1 x2 y2] next]
          (if (and
               (<= x1 x x2)
               (or (<= y1 y y2) (<= y2 y y1)))
            (recur remainder)))
        (list [x y])))))

(defn intersection-points
  [vent1 vent2]
  (let [[x11 y11 x12 y12] vent1
        [x21 y21 x22 y22] vent2
        clip (clip-at vent1 vent2)]
    (condp = (list (cardinal-direction vent1) (cardinal-direction vent2))
      '(:south :south)
      (if (= x11 x21)
        (map #(vector x11 %) (range y21 (inc (min y12 y22)))))
      '(:south :south-east)
      (clip [x11 (+ (- x11 x21) y21)])
      '(:south :east)
      (clip [x11 y21])
      '(:south :north-east)
      (clip [x11 (+ (- x21 x11) y21)])
      '(:south-east :south-east)
      (if (= (- x11 y11) (- x21 y21))
        (for [x (range x21 (inc (min x12 x22)))
              :let [y (+ (- x x21) y21)]]
          [x y]))
      '(:south-east :east)
      (clip [(+ (- y21 y11) x11) y21])
      '(:south-east :north-east)
      (let [x (/ (- (+ y21 x21 x11) y11) 2)]
        (if (int? x)
          (clip [x (+ (- x x11) y11)])))
      '(:east :east)
      (if (= y11 y21)
        (map #(vector % y11) (range x21 (inc (min x12 x22)))))
      '(:east :north-east)
      (clip [(- (+ x21 y21) y11) y11])
      '(:north-east :north-east)
      (if (= (+ x11 y11) (+ x21 y21))
        (for [x (range x21 (inc (min x12 x22)))
              :let [y (+ (- x21 x) y21)]]
          [x y])))))

(defn pairwise
  [op coll]
  (loop [[next & remainder] coll
         result '()]
    (if (empty? remainder)
      result
      (recur remainder (concat result (mapcat #(op next %) remainder))))))

(defn orient-south-eastward
  [[x1 y1 x2 y2]]
  (if (or (> x1 x2) (and (= x1 x2) (> y1 y2)))
    [x2 y2 x1 y1]
    [x1 y1 x2 y2]))

(def cardinal-direction-order
  {:south 0 :south-east 1 :east 2 :north-east 3})

(defn vent-order
  [coords]
  [(cardinal-direction-order (cardinal-direction coords))
   (first coords)
   (second coords)])

(defn parse-vents
  [vent-lines]
  (letfn [(parse-vent [vent-line]
            (mapv #(Integer/parseInt %) (re-seq #"\d+" vent-line)))]
    (map parse-vent vent-lines)))

(defn read-vents
  [vent-lines]
  (sort-by vent-order
           (map orient-south-eastward
                (parse-vents vent-lines))))

(defn part1
  [vent-lines]
  (count (set (pairwise intersection-points
                        (filter #(#{:south :east} (cardinal-direction %))
                                (read-vents vent-lines))))))

(defn part2
  [vent-lines]
  (count (set (pairwise intersection-points (read-vents vent-lines)))))

(defn with-line-seq
  [file-name func]
  (with-open [rdr (clojure.java.io/reader file-name)]
    (func (line-seq rdr))))

(deftest part1-example
  (is (= 5 (with-line-seq "day-5-example.txt" part1))))

(deftest part1-input
  (is (= 7674 (with-line-seq "day-5-input.txt" part1))))

(deftest part2-example
  (is (= 12 (with-line-seq "day-5-example.txt" part2))))

(deftest part2-input
  (is (= 20898 (with-line-seq "day-5-input.txt" part2))))

(run-tests 'adventofcode.2021.day-5)
