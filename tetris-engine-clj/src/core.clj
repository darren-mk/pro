(ns core
  (:require
   [clojure.string :as str]))

(def shapes
  {:q [[1 1] [1 1]]
   :z [[0 1] [1 1] [1]]
   :s [[1] [1 1] [0 1]]
   :t [[0 1] [1 1] [0 1]]
   :i [[1] [1] [1] [1]]
   :l [[1 1 1] [1]]
   :j [[1] [1 1 1]]})

(defn cushion [v height]
  (let [padding (take (- height (count v))
                      (repeat 0))]
    (-> v (concat padding) vec)))

(defn count-zero-pieces
  ([col]
   (count-zero-pieces col 0))
  ([col cnt]
   (if (= 1 (first col))
     cnt
     (count-zero-pieces (rest col) (inc cnt)))))

(defn find-height-single [base col]
  (let [beg-zeros (count-zero-pieces col)
        computed (- (count base) beg-zeros)]
    (if (neg-int? computed) 0 computed)))

(defn find-required-height [base shape]
  (apply max (map find-height-single base shape)))

(defn init-panel []
  (vec (take 10 (iterate identity []))))

(defn fill [base col height]
  (if (empty? col)
    base
    (vec (reduce
          (fn [b item] (let [v (val item)]
                         (if (zero? v) b
                             (assoc b (+ height (key item)) (val item)))))
          (cushion base (+ height (count col)))
          (zipmap (range) col)))))

(defn delete-by-index [v row-num]
  (vec (concat (take row-num v)
               (drop (inc row-num) v))))

(defn pad [shape place]
  (vec (concat
        (take place (repeat []))
        shape
        (take (- 10 place (count shape)) (repeat [])))))

(defn land [panel shape place]
  (let [width (count shape)
        base (mapv panel (range place (+ place width)))
        height (find-required-height base shape)
        padded-shape (pad shape place)]
    (mapv #(fill %1 %2 height) panel padded-shape)))

(defn disappear
  ([panel]
   (disappear panel 0))
  ([panel row-num]
   (let [current-row (map #(get % row-num) panel)
         ended? (every? nil? current-row)
         made? (every? #(= 1 %) current-row)]
     (cond ended? (vec panel)
           made? (disappear (map #(delete-by-index % row-num) panel) row-num)
           :else (disappear panel (inc row-num))))))

(defn interpret [s]
  (as-> s $
    (str/split $ #",")
    (map #(-> % char-array seq) $)
    (map (fn [[letter num]]
           {:shape (-> letter str str/lower-case keyword)
            :place (-> num str Integer/parseInt )}) $)
    (vec $)))

(defn process [ms]
  (let [f (fn [aggr {:keys [shape place]}]
            (-> aggr
                (land (shape shapes) place)
                (disappear)))]
    (reduce f (init-panel) ms)))

(defn verdict [panel]
  (apply max (map count panel)))

(defn run [arg]
  (let [input (or (get arg 'input-file) "input.txt")
        data (-> input str slurp (str/split #"\n"))
        results (map (comp verdict process interpret) data)]
    (doseq [result results] (println result))))

(comment
  (run {}))
