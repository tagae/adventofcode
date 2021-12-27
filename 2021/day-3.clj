(ns adventofcode.2021)

(defn char-to-bit
  [c]
  (if (= c \0) 0 1))

(defn to-bit-seq
  [bit-string]
  (map char-to-bit bit-string))

(defn to-bit-seqs
  [bit-strings]
  (mapv (fn [bit-string] (to-bit-seq bit-string)) bit-strings))

(defn counts-to-bits
  [comparison total-entries bit-counts]
  (let [half (/ total-entries 2)]
    (map #(if (apply comparison [% half]) 1 0) bit-counts)))

(defn gamma-bits
  [{:keys [total-entries bit-counts]}]
  (counts-to-bits > total-entries bit-counts))

(defn epsilon-bits
  [{:keys [total-entries bit-counts]}]
  (counts-to-bits < total-entries bit-counts))

(defn bits-to-number
  [bits]
  (reduce (fn [result bit] (+ (* result 2) bit)) bits))

(defn read-bit-strings
  [file-name]
  (with-open [rdr (clojure.java.io/reader file-name)]
    (let [non-blank? (comp not clojure.string/blank?)
          bit-strings (filter non-blank? (line-seq rdr))]
      (into [] bit-strings))))

(defn count-bit-seqs
  [bit-seqs]
  (let [prefixed-bit-seqs (map #(cons 1 %) bit-seqs)
        sum-seqs (partial mapv +)
        [total-entries & bit-counts] (apply sum-seqs prefixed-bit-seqs)]
    {:total-entries total-entries :bit-counts bit-counts}))

(defn part1
  [file-name]
  (let [bit-strings (read-bit-strings file-name)
        bit-seqs (to-bit-seqs bit-strings)
        bit-counts (count-bit-seqs bit-seqs)
        gamma (bits-to-number (gamma-bits bit-counts))
        epsilon (bits-to-number (epsilon-bits bit-counts))]
    (* gamma epsilon)))


(part1 "day-3-input.txt")

(defn filter-by-bit-criterion
  [criterion bit-seqs]
  (loop [position 0 bit-seqs bit-seqs]
    (let [total-seqs (count bit-seqs)]
      (if (= total-seqs 1)
        (first bit-seqs)
        (let [count-at-position (reduce + (map #(nth % position) bit-seqs))
              bit-to-keep (if (criterion count-at-position (/ total-seqs 2)) 1 0)]
          (recur (inc position)
                 (filter #(= (nth % position) bit-to-keep) bit-seqs)))))))

(defn oxygen-generator-rating-bits
  [bit-seqs]
  (filter-by-bit-criterion >= bit-seqs))

(defn co2-scrubber-rating-bits
  [bit-seqs]
  (filter-by-bit-criterion < bit-seqs))

(defn part2
  [file-name]
  (let [bit-strings (read-bit-strings file-name)
        bit-seqs (to-bit-seqs bit-strings)]
    (* (bits-to-number (oxygen-generator-rating-bits bit-seqs))
       (bits-to-number (co2-scrubber-rating-bits bit-seqs)))))

(part2 "day-3-input.txt")
