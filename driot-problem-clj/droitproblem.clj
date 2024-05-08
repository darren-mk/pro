(ns droitproblem)

(def input
  [{:level 1 :num "a" :text "Right to life"}
   {:level 2 :num "i" :text "Right to life protected"}
   {:level 2 :num "ii" :text "Circumstances allowing …"}
   {:level 3 :num "1" :text "Force no more than …"}
   {:level 1 :num "b" :text "Prohibition on torture"}])

(def output
  [{:level 1 :num "a" :text "Right to life"
    :children [{:level 2 :num "i" :text "Right to life protected"}
               {:level 2 :num "ii" :text "Circumstances allowing …"
                :children [{:level 3
                            :num "1"
                            :text "Force no more than …"}]}]}
   {:level 1 :num "b" :text "Prohibition on torture"}])

(defn accumulate [acc m]
  (let [target-level (or (-> acc last :level) 0)]
    (cond (empty? acc) (conj acc m)
          (= target-level (:level m)) (conj acc m)
          (< target-level (:level m)) (update-in acc [(dec (count acc)) :children]
                                                 (fn [a m'] (into [] (accumulate a m'))) m))))

(defn solve [input]
  (reduce accumulate [] input))

(= output (solve input)) ;; => true

(solve input)
;; => [{:level 1,
;;      :num "a",
;;      :text "Right to life",
;;      :children
;;      [{:level 2, :num "i", :text "Right to life protected"}
;;       {:level 2,
;;        :num "ii",
;;        :text "Circumstances allowing …",
;;        :children [{:level 3, :num "1", :text "Force no more than …"}]}]}]
