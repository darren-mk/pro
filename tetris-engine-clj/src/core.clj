(ns core)

(defn pad [v height]
  (let [padding (take (- height (count v))
                      (repeat nil))]
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

(defn find-max-height [base shape]
  (apply max (map find-height-single base shape)))

#_#_#_
[:i 0]
[:i 4]
[:q 8]

(def t
  [[0 1]
   [1 1]
   [0 1]])

(def panel
  (atom (into [] (take 10 (iterate identity [])))))

(defn fill [base col height]
  (concat (pad base height) col))

(defn land! [shape col]
  (let [width (count shape)
        base (mapv @panel (range col (+ col width)))
        height (find-max-height base shape)]
    (doseq [i (range width)]
      (swap! panel update (+ col i) fill (get shape i) height))
    #_(swap! panel update fill (get shape 0) height)))

(land! t 2)

@panel
