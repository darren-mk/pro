(ns name-matching.core
  (:require
   [clojure.java.io :as io]
   [clojure.data.csv :as csv]
   [name-matching.domain :as nmf]))

(def files
  {:source 
   {:applicants "input/applicants.csv"
    :employees "input/employees.csv"}
   :result
   {:applicants-to-employees
    "resources/output/result-applicants-to-employees.csv"}})

(defn -main []
  (with-open
    [writer
     (io/writer
      (-> files :result :applicants-to-employees))]
    (csv/write-csv
     writer
     (nmf/map-coll->table 
      (nmf/match:applicants->employees
       (nmf/get-applicants files)
       (nmf/get-employees files)))))
  (println "Job is done. Check output file."))
