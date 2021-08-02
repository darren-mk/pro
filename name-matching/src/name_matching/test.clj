(ns name-matching.test
  (:require
   [clojure.java.io :as io]
   [clojure.data.csv :as csv]
   [clojure.string :refer [lower-case includes?]]
   [clojure.set :refer [intersection]]
   [java-time :refer [local-date]]
   [name-matching.function :refer :all]))

(compare-two-names "robert" "robert") ;; => 1.0
(compare-two-names "scott" "deborah") ;; => 0.3371428608894348
(compare-two-names "kathy" "katherine") ;; => 0.697777795791626
(compare-two-names "gonzales" "carter gonzales") ;; => 0.7666666507720947

 (let [applicant {:first-name "robert" :last-name "scott"}]
    (as-> employees v
         (map
          (fn [employee]
            (hash-map
             :record employee
             :score (+ (compare-two-names
                        (:first-name applicant)
                        (:first-name employee))
                       (compare-two-names
                        (:last-name applicant)
                        (:last-name employee)))))
          v)
         (sort-by (juxt :score) v)
         (last v)
         (:record v)
         (assoc
          v
          :applicant-id (:id applicant)
          :applicant-first-name (:first-name applicant)
          :applicant-last-name (:last-name applicant))))
