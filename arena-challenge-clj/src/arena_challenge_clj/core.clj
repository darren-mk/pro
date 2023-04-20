(ns arena-challenge-clj.core
  (:require
   [clojure.java.io :as io]
   [clojure.data.csv :as csv]))

(def filename "202303261933.csv")

(def reader (io/reader (str "~/Work/pro/arena-challenge-clj/resources/" filename)))
