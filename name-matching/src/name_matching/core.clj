(ns name-matching.core
  (:require
   [clojure.java.io :as io]
   [clojure.data.csv :as csv]
   [clojure.string :refer [lower-case includes?]]
   [clojure.set :refer [intersection]]
   [java-time :refer [local-date]]
   [name-matching.function :refer :all]))

(def files
  {:source 
   {:applicants "applicants.csv"
    :employees "employees.csv"}
   :result
   {:applicants-to-employees "outcome/result-applicants-to-employees.csv"
    :employees-to-applicants "outcome/result-employees-to-applicants.csv"}})

(defn -main []
  (do
    (with-open
      [writer
       (io/writer
        (-> files :result :applicants-to-employees))]
      (csv/write-csv
       writer
       (map-coll->table 
        (match:applicants->employees
         (get-applicants files)
         (get-employees files)))))
    (with-open
      [writer
       (io/writer
        (-> files :result :employees-to-applicants))]
      (csv/write-csv
       writer
       (map-coll->table 
        (match:employees->applicants
         (get-employees files)
         (get-applicants files)))))))
