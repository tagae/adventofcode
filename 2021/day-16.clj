(ns adventofcode.2021.day-16
  (:use clojure.test
        [clojure.string :only [trim-newline]]))

(def hex-bits
  '{\0 [0 0 0 0]
    \1 [0 0 0 1]
    \2 [0 0 1 0]
    \3 [0 0 1 1]
    \4 [0 1 0 0]
    \5 [0 1 0 1]
    \6 [0 1 1 0]
    \7 [0 1 1 1]
    \8 [1 0 0 0]
    \9 [1 0 0 1]
    \A [1 0 1 0]
    \B [1 0 1 1]
    \C [1 1 0 0]
    \D [1 1 0 1]
    \E [1 1 1 0]
    \F [1 1 1 1]})

(defn parse-packet
  [input]
  (reduce into [] (map hex-bits input)))

(defn to-number
  ([number bits]
   (reduce (fn [result bit] (+ (* result 2) bit)) number bits))
  ([bits]
   (to-number 0 bits)))

(defn decode-literal
  [bits]
  (loop [groups (subvec bits 6)
         group-count 0
         number 0]
    (let [group-bits (subvec groups 1 5)
          remaining-bits (subvec groups 5)
          updated-number (to-number number group-bits)
          updated-group-count (inc group-count)
          last-group? (zero? (first groups))]
      (if last-group?
        {:number updated-number
         :bit-length (+ 6 (* updated-group-count 5))}
        (recur remaining-bits updated-group-count updated-number)))))

(declare decode-bits)

(defn decode-type-0-operator
  [bits]
  (let [contained-bit-count (to-number (subvec bits 7 22))]
    (loop [remaining-bit-count contained-bit-count
           remaining-bits (subvec bits 22 (+ 22 contained-bit-count))
           sub-packets []]
      (if (> remaining-bit-count 0)
        (let [sub-packet (decode-bits remaining-bits)
              sub-packet-length (sub-packet :bit-length)]
          (recur
           (- remaining-bit-count sub-packet-length)
           (subvec remaining-bits sub-packet-length)
           (conj sub-packets sub-packet)))
        {:sub-packets sub-packets
         :bit-length (+ 22 contained-bit-count)}))))

(defn decode-type-1-operator
  [bits]
  (loop [sub-packet-count (to-number (subvec bits 7 18))
         remaining-bits (subvec bits 18)
         sub-packets []]
    (if (> sub-packet-count 0)
      (let [sub-packet (decode-bits remaining-bits)]
        (recur (dec sub-packet-count)
               (subvec remaining-bits (sub-packet :bit-length))
               (conj sub-packets sub-packet)))
      {:sub-packets sub-packets
       :bit-length (reduce + 18 (map :bit-length sub-packets))})))

(defn decode-operator
  [bits]
  (let [length-type (nth bits 6)]
    (condp = length-type
      0 (decode-type-0-operator bits)
      1 (decode-type-1-operator bits))))

(defn decode-bits
  [bits]
  (let [version (to-number (subvec bits 0 3))
        type (to-number (subvec bits 3 6))]
    (assoc ((if (= type 4) decode-literal decode-operator) bits)
           :version version
           :type type)))

(defn sum-version-numbers
  [packet]
  (let [version (packet :version)]
    (if (= (packet :type) 4)
      version
      (reduce + version (map sum-version-numbers (packet :sub-packets))))))

(def part1
  (comp sum-version-numbers decode-bits parse-packet))

(deftest example-1-parsing
  (is (= '[1 1 0 1
           0 0 1 0
           1 1 1 1
           1 1 1 0
           0 0 1 0
           1 0 0 0]
         (parse-packet "D2FE28"))))

(deftest example-1-decoding
  (is (= '{:type 4
           :version 6
           :number 2021
           :bit-length 21}
         (decode-bits (parse-packet "D2FE28")))))

(deftest example-2-decoding
  (is (= '{:type 6
           :version 1
           :sub-packets
           [{:type 4
             :version 6
             :number 10
             :bit-length 11}
            {:type 4
             :version 2
             :number 20
             :bit-length 16}]
           :bit-length 49}
         (decode-bits (parse-packet "38006F45291200")))))

(deftest example-3-decoding
  (is (= '{:version 7
           :type 3
           :sub-packets
           [{:type 4
             :version 2
             :number 1
             :bit-length 11}
            {:type 4
             :version 4
             :number 2
             :bit-length 11}
            {:type 4
             :version 1
             :number 3,
             :bit-length 11}]
           :bit-length 51}
         (decode-bits (parse-packet "EE00D40C823060")))))

(deftest example-4-versions
  (is (= 16 (part1 "8A004A801A8002F478"))))

(deftest example-5-versions
  (is (= 12 (part1 "620080001611562C8802118E34"))))

(deftest example-6-versions
  (is (= 23 (part1 "C0015000016115A2E0802F182340"))))

(deftest example-7-versions
  (is (= 31 (part1 "A0016C880162017C3686B18A3D4780"))))

(deftest part-1-input
  (is (= 1014 (part1 (trim-newline (slurp "day-16-input.txt"))))))

(run-tests 'adventofcode.2021.day-16)
