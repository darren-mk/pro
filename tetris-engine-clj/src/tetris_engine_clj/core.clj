(ns tetris-engine-clj.core
  (:require
   [clojure.string :as str])
  (:gen-class))

(def shapes
  "this map is aligned by keyword of
  single alphabets that represent shapes of tetris elements.
  each of the values is vectors within a vector.
  subvectors represent columns, and they start from left.
  number 1 within subvectors are painted part of the shape,
  and number 0 empty part outside the shape."
  {:q [[1 1] [1 1]]
   :z [[0 1] [1 1] [1]]
   :s [[1] [1 1] [0 1]]
   :t [[0 1] [1 1] [0 1]]
   :i [[1] [1] [1] [1]]
   :l [[1 1 1] [1]]
   :j [[1] [1 1 1]]})

(defn init-panel
  "panel represents the tetris board that consists
  of 10 columns. initially, each col has an empty vector."
  []
  (vec (take 10 (iterate identity []))))

(defn cushion
  "cushion means to added zeros to the existing
  column in the panel so that the shape can be
  added by index."
  [col-in-shape height-of-range]
  (let [padding (take (- height-of-range (count col-in-shape))
                      (repeat 0))]
    (-> col-in-shape (concat padding) vec)))

(defn count-zero-pieces
  "counts how many zeroes a sequence
  contains on its left side."
  ([col]
   (count-zero-pieces col 0))
  ([col cnt]
   (if (= 1 (first col))
     cnt
     (count-zero-pieces (rest col) (inc cnt)))))

(defn find-height-single
  "figures out at what height a column of shape can
  land in the panel by comparing column in the panel
  and column in the shape to be added."
  [panel-col shape-col]
  (let [beg-zeros (count-zero-pieces shape-col)
        computed (- (count panel-col) beg-zeros)]
    (if (neg-int? computed) 0 computed)))

(defn find-overall-height
  "checks required height for each column in the shape
  against the selected columns of the panel, and gets
  the maximum of heights. it means the bottom line of
  the shape will be placed at that height."
  [base shape]
  (apply max (map find-height-single base shape)))

(defn fill
  "combines panel column and shape column at the
  given overall height."
  [panel-col shape-col height]
  (let [f (fn [b item]
            (let [v (val item)]
              (if (zero? v) b
                  (assoc b (+ height (key item))
                         (val item)))))]
    (if (empty? shape-col)
      panel-col
      (vec (reduce f
            (cushion panel-col (+ height (count shape-col)))
            (zipmap (range) shape-col))))))

(defn delete-by-index
  "removes item in vector by given index"
  [v index]
  (vec (concat (take index v)
               (drop (inc index) v))))

(defn pad
  "adds empty vectors around the shape in order to
  ease the landing process. results in ten vectors
  in total afterwards. the argument place refers to
  column number of the panel structure."
  [shape place]
  (vec (concat
        (take place (repeat []))
        shape
        (take (- 10 place (count shape)) (repeat [])))))

(defn land
  "adds shape to panel by given column place of the panel."
  [panel shape place]
  (let [width (count shape)
        base (mapv panel (range place (+ place width)))
        height (find-overall-height base shape)
        padded-shape (pad shape place)]
    (mapv #(fill %1 %2 height) panel padded-shape)))

(defn disappear
  "removes a whole row when it is filled up."
  ([panel]
   (disappear panel 0))
  ([panel row-num]
   (let [current-row (map #(get % row-num) panel)
         ended? (every? nil? current-row)
         made? (every? #(= 1 %) current-row)]
     (cond ended? (vec panel)
           made? (disappear (map #(delete-by-index % row-num) panel) row-num)
           :else (disappear panel (inc row-num))))))

(defn interpret
  "converts string data of single item into
  the internal data structure for processing."
  [s]
  (as-> s $
    (str/split $ #",")
    (map #(-> % char-array seq) $)
    (map (fn [[letter num]]
           {:shape (-> letter str str/lower-case keyword)
            :place (-> num str Integer/parseInt )}) $)
    (vec $)))

(defn process
  "processes landing and disappearance for each item
  in the given sequence from interpreted data."
  [ms]
  (let [f (fn [aggr {:keys [shape place]}]
            (-> aggr
                (land (shape shapes) place)
                (disappear)))]
    (reduce f (init-panel) ms)))

(defn verdict
  "determines the maximum height
  based the processed result."
  [panel]
  (apply max (map count panel)))

(defn -main [& arg]
  (let [input (or (first arg) "input.txt")
        output (or (second arg) "output.txt")
        data (-> input str slurp (str/split #"\n"))
        results (map (comp verdict process interpret) data)
        printable (str/join "\n" results)]
    (spit output (str printable "\n"))
    (println printable)))

(comment
  (-main))
