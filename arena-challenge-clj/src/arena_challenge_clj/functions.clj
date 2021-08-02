(ns arena-challenge-clj.functions
  (:require [clojure.java.io :as io]
            [clojure.string :as cstr]))

;;;; all functions in this namespace is pure functions ;;;;

(defn get-source-string
  "using the filename in string,
  source in the given file
  is captured in string."
  [source-file]
  (-> source-file 
      io/resource
      io/file
      slurp))

(defn get-lines
  "using the given source string,
  lines are parsed as collection of line items.
  each line has multiple names."
  [source-string]
  (as-> source-string v
    (cstr/split v #"\n")
    (map #(cstr/split % #",") v)))

(defn extract-candid-names
  "in order to be a part of more-than-fifty pairs,
  individual name must also appear at least fifty times.
  therefore, this function extracts all individual names
  that appear more than fifty times by itself.
  for xflexibility, the threshold for fifty is given
  as an argument."
  [lines threshold]
  (->> lines
       (apply concat)
       frequencies
       (filter #(<= threshold (val %)))
       keys
       set))

(defn generate-pairs
  "using all names each of which appears more than
  threshold time, it generates unique pairs."
  [names]
  (distinct (for [a names b names
                  :when (not= a b)]
              #{a b})))

(defn narrow-pairs
  "using the candidate pairs, lines, and threshold,
  this function detects how many lines
  each candidate pair appears in.
  per each line, all possible names within the line
  gets filtered and then counted by frequencies function.
  the result is map by frequencies function,
  which gets filtered for threshold later.
  finally, only keys are picked
  so the outcome is just set of pairs."
  [names lines threshold]
  (->> lines 
       (mapcat (fn [line]
                 (->> line
                      (filter names)
                      generate-pairs)))
       frequencies       
       (filter #(<= threshold (val %)))
       keys))

(defn process
  "this is a complete process cycle
  containing all functions above.
  using let statement, it leaves clues of
  what process is made at which stage."
  [source-file threshold]
  (let [source-string (get-source-string source-file)
        lines (get-lines source-string)
        names (extract-candid-names lines threshold)
        extracts (narrow-pairs names lines threshold)]
    extracts))
