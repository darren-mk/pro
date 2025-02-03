(ns name-matching.domain
  (:require
   [clojure.java.io :as io]
   [clojure.data.csv :as csv]
   [clojure.string :refer [lower-case includes?]]
   [clojure.set :refer [intersection]]
   [java-time :refer [local-date time-between]]))

(defn ->applicant [[id-str date-str fname-str lname-str]]
  (let [applicant-id (Integer/parseInt id-str)
        application-date (local-date "yyyy-MM-dd" date-str)
        first-name (lower-case fname-str)
        last-name (lower-case lname-str)
        full-name (str first-name " " last-name)]
    {:applicant-id applicant-id
     :application-date application-date
     :applicant-first-name first-name
     :applicant-last-name last-name
     :applicant-full-name full-name}))

(defn get-applicants
  "Takes a map containing file names
  within :source and :applicants.
  The files must be saved in resources folder
  in the project folder."
  [files]
  (->> files :source :applicants 
       io/resource io/reader 
       csv/read-csv rest 
       (map ->applicant)))

(defn ->employee [[id-str hire-date-str fname-str lname-str term-date-str]]
  (let [employee-id (Integer/parseInt id-str)
        first-name (lower-case fname-str)
        last-name (lower-case lname-str)
        full-name (str first-name " " last-name)
        hire-date (local-date "yyyy-MM-dd" (or hire-date-str "2000-12-31"))
        term-date (local-date "yyyy-MM-dd" (or term-date-str "2021-03-31"))
        tenure (float (/ (time-between hire-date term-date :months) 12))]
    {:employee-id employee-id
     :employee-first-name first-name
     :employee-last-name last-name
     :employee-full-name full-name
     :hire-date hire-date
     :term-date term-date
     :tenure-years tenure}))

(defn get-employees [files]
  (->> files :source :employees 
       io/resource io/reader csv/read-csv rest
       (map ->employee)))

(defn avg [& nums]
  (/ (apply + nums) (count nums)))

(defn compare-two-names
  "This function simply takes two strings as arguments,
  which represents two names. e.g. kathy, katherine. "
  [a b]
  (let [a-chars-vec (-> a char-array vec)
        b-chars-vec (-> b char-array vec)
        a-chars-count (count a-chars-vec)
        b-chars-count (count b-chars-vec)
        a-chars-set (set a-chars-vec)
        b-chars-set (set b-chars-vec)
        common-chars (set (intersection a-chars-set b-chars-set))
        common-chars-count (count common-chars)
        common-chars-in-a (filter common-chars a-chars-vec)
        common-chars-in-b (filter common-chars b-chars-vec)
        common-chars-count-in-a (count common-chars-in-a)
        common-chars-count-in-b (count common-chars-in-b)
        common-portion-on-a (/ common-chars-count-in-a a-chars-count)
        common-portion-on-b (/ common-chars-count-in-b b-chars-count)
        portion-score (float (avg common-portion-on-a common-portion-on-b))
        a-in-common (apply str (filter common-chars a-chars-vec))
        b-in-common (apply str (filter common-chars b-chars-vec))
        inclusion-a-to-b (includes? a-in-common b-in-common)
        inclusion-b-to-a (includes? b-in-common a-in-common)
        order-score (if (< 2 common-chars-count)
                      (reduce (fn [score p] (+ score (if p 0.5 0)))
                              0 [inclusion-a-to-b inclusion-b-to-a])
                      0)
        weights {:portion 0.8 :order 0.2}]
    (+ (* portion-score (weights :portion))
       (* order-score (weights :order)))))

(defn compare-names-two-ways
  [a-fname a-lname b-fname b-lname]
  (max (avg (compare-two-names a-fname b-fname)
            (compare-two-names a-lname b-lname))
       (avg (compare-two-names a-fname b-lname)
            (compare-two-names a-lname b-fname))))

(defn match:applicants->employees [applicants employees]
  (for [applicant applicants]
    (let [direction "applicant->employee"
          employee-cands-direct
          (filter
           #(= (:applicant-full-name applicant)
               (:employee-full-name %))
           employees)]
      (cond (<= 1 (count employee-cands-direct))
            (merge {:direction direction
                    :matching-score 1.0}
                   applicant
                   (first employee-cands-direct))
            :else
            (as-> (->> employees
                       (map
                        (fn [employee]
                          (hash-map
                           :record employee
                           :matching-score
                           (compare-names-two-ways
                            (:applicant-first-name applicant)
                            (:applicant-last-name applicant)
                            (:employee-first-name employee)
                            (:employee-last-name employee)))))
                       (sort-by (juxt :matching-score))
                       last) employee-cand-eval-w-score
              (merge {:direction direction 
                      :matching-score (:matching-score employee-cand-eval-w-score)}
                     applicant
                     (:record employee-cand-eval-w-score)))))))

(defn map-coll->table [map-coll]
  (cons
   (vec (map name (keys (first map-coll))))
   (vec (for [m map-coll]
          (vec (vals m))))))
