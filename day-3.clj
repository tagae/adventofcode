(ns adventofcode.2021)

(defn count-bits
  [file-name]
  (with-open [rdr (clojure.java.io/reader file-name)]
    (let [non-blank? (comp not clojure.string/blank?)
          bit-strings (filter non-blank? (line-seq rdr))
          char-to-bit #(if (= % \0) 0 1)
          bit-seqs (map (fn [bit-string] (map char-to-bit bit-string)) bit-strings)
          prefixed-bit-seqs (map #(cons 1 %) bit-seqs)
          sum-seqs (partial map +)
          [total-entries & bit-counts] (apply sum-seqs prefixed-bit-seqs)]
      {:total-entries total-entries :bit-counts bit-counts})))

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
  (reduce (fn [result bit] (bit-or (* result 2) bit)) bits))

(let [file-name "day-3-input.txt"
      bit-counts (count-bits file-name)
      gamma (bits-to-number (gamma-bits bit-counts))
      epsilon (bits-to-number (epsilon-bits bit-counts))]
  (* gamma epsilon))
