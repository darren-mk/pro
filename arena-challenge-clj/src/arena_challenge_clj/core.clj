(ns arena-challenge-clj.core
  (:require
   [clojure.java.io :as io]
   [clojure.data.csv :as csv]
   [arena-challenge-clj.functions :refer :all]))

(def source-file "Artist_lists_small.txt")
(def result-file "output/result.csv")
(def threshold 50)

(defn -main
  "main function is supposed to run by lein run command in cli.
  it triggers process function in other file
  by providing source-file name and threshold number.
  and with the resturned result, it creates csv file in output folder."
  []
  (with-open [writer (io/writer result-file)]
    (csv/write-csv writer (process source-file threshold))))
