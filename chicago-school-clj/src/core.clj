(ns core
  (:require
   [clj-http.client :as client]
   [clojure.data.json :as json]))

(def url "https://data.cityofchicago.org/resource/2m8w-izji.json")

(def data (client/get url))

(def body (-> data :body (json/read-str :key-fn keyword)))

(->> body
     (filter :cps_performance_policy_level)
     (map :cps_performance_policy_level)
     (filter #(= "LEVEL 3" %))
     count)
