(ns name-matching.domain-test
  (:require 
   [clojure.math :as math]
   [clojure.test :as t]
   [name-matching.domain :as sut]))

(t/deftest ->applicant-test
  (t/is 
   (= {:applicant-id 37,
       :applicant-first-name "greg",
       :applicant-last-name "brown alexander",
       :applicant-full-name "greg brown alexander"}
      (dissoc
       (sut/->applicant
        ["37" "2013-03-06" "Greg" "Brown Alexander"])
       :application-date))))

(t/deftest ->employee-test
  (t/is
   (= {:employee-id 1330,
       :employee-first-name "jonathan",
       :employee-last-name "moore",
       :employee-full-name "jonathan moore"}
      (-> (sut/->employee
           ["1330" "2014-11-02" "Jonathan" "Moore" "2015-04-02"])
          (dissoc :hire-date)
          (dissoc :term-date)
          (dissoc :tenure-years)))))

(t/deftest compare-two-names-test
  (t/are [a b exp] 
         (= (-> (sut/compare-two-names a b)
                (* 1000)
                math/round)
            exp)
    "robert" "robert" 1000
    "scott" "deborah" 137
    "kathy" "katherine" 698
    "gonzales" "carter gonzales" 767))
