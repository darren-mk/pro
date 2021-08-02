(ns name-matching.research
  (:require
   [clojure.java.io :as io]
   [clojure.data.csv :as csv]
   [clojure.string :refer [lower-case includes?]]
   [clojure.set :refer [intersection]]
   [java-time :refer [local-date]]
   [name-matching.function :refer :all]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;; WHAT CHARACTERS ARE USED IN NAMES? ;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def files
  {:source 
   {:applicants "applicants.csv"
    :employees "employees.csv"}
   :result "outcome/result.csv"})

(def raw-names
  (apply concat 
         (for [section [:applicants :employees]
               n [2 3]]
           (->> files
                :source
                section
                io/resource
                io/reader
                csv/read-csv
                rest
                (map #(nth % n))))))

 (->> raw-names
       (mapcat #(set (char-array %)))
       set)
;; => #{\space \A \a \B \b \C \c \D \d \E \e \F \f \G \g \H \h \I \i \J \j
;;      \K \k \L \l \- \M \m \N \n \O \o \P \p \R \r \S \s \T \t \U \u \V
;;      \v \W \w \X \x \Y \y \Z \z}

;;;; Conclusion: English alphabets, space, and hyphen



;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;; SIZES OF DATASETS? ;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def applicants (get-applicants files))
(def employees (get-employees files))

(count applicants) ;; => 900
(count (set (map :id applicants))) ;; => 900
(count employees) ;; => 1100
(count (set (map :id employees))) ;; => 800

;;;; Conclusion:
;;;; No duplicates in applicatns
;;;; 300 of duplicates in employees


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;; DO DATASETS HAVE PREFIXES? ;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(def prefixes
  ["dr." "jr." "sr." "sir"
   "ms." "mr." "," "."])

;; there is no prefix words in first-names of applicants 
(for [prefix prefixes]
  (filter
   #(clojure.string/includes? % prefix)
   (map :first-name applicants)))
;; => (() () () () () () () ())

;; there is no prefix words in last-names of applicants 
(for [prefix prefixes]
  (filter
   #(clojure.string/includes? % prefix)
   (map :last-name applicants)))
;; => (() () () () () () () ())


;; there is no prefix words in first-names of applicants 
(for [prefix prefixes]
  (filter
   #(clojure.string/includes? % prefix)
   (map :first-name employees)))
;; => (() () () () () () () ())

;; there is no prefix words in last-names of applicants 
(for [prefix prefixes]
  (filter
   #(clojure.string/includes? % prefix)
   (map :last-name employees)))
;; => (() () () () () () () ())

;;;; Conclusion:
;;;; No prefix is used in datasets
