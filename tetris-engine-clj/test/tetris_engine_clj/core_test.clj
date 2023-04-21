(ns tetris-engine-clj.core-test
  (:require
   [clojure.java.io :as io]
   [clojure.test :as t]
   [tetris-engine-clj.core :as src]))

(def samples
  (-> "samples.edn" io/resource slurp read-string))

(t/deftest cushion-test
  (t/testing "cushion is added"
    (t/is (= [1 2 3 0] (src/cushion [1 2 3] 4)))
    (t/is (= [1 2 3 0 0 0 0] (src/cushion [1 2 3] 7))))
  (t/testing "cushion is not needed"
    (t/is (= [1 2 3] (src/cushion [1 2 3] 3)))
    (t/is (= [1 2 3] (src/cushion [1 2 3] 0)))))

(t/deftest count-zero-pieces-test
  (t/testing "sequence has no zero at the beginning"
    (t/is (= 0 (src/count-zero-pieces [1 0 1]))))
  (t/testing "sequence has zeroes at the beginning"
    (t/is (= 1 (src/count-zero-pieces [0 1 1])))
    (t/is (= 2 (src/count-zero-pieces [0 0 1])))))

(t/deftest find-height-single-test
  (t/testing "column in shape has no emptiness at beginning"
    (t/is (= 5 (src/find-height-single [1 1 0 nil 1] [1 1 1])))
    (t/is (= 3 (src/find-height-single [1 1 1] [1 1 1]))))
  (t/testing "column in shape has emptiness at beginning"
    (t/are [expt result] (= expt result)
      1 (src/find-height-single [1 1 1] [0 0 1])
      0 (src/find-height-single [1] [0 0 1])
      0 (src/find-height-single [] [0 1]))))

(t/deftest find-overall-height-test
  (t/testing "panel is empty"
    (t/is (= 0 (src/find-overall-height [[] [] []] [[0 1] [1 1] [0 1]]))))
  (t/testing "blocks are touched by a single point"
    (t/is (= 1 (src/find-overall-height [[] [1] []] [[0 1] [1 1] [0 1]])))
    (t/is (= 2 (src/find-overall-height [[1 1] [1 1] [1]] [[0 1] [1 1] [0 1]]))))
  (t/testing "blocks are touched by multiple points at different heights"
    (t/is (= 1 (src/find-overall-height [[1 1] [1 1] [1]] [[0 1] [0 1] [0 1]])))))

(t/deftest fill-test
  (t/testing "shape column is filled with gap"
    (t/is (= [1 1 1 0 0 0 0 1 1] (src/fill [1 1 1] [1 1] 7)))
    (t/is (= [1 0 0 0 0 0 0 1 1 1] (src/fill [1] [1 1 1] 7))))
  (t/testing "shape column is filled without gap"
    (t/is (= [1 1 1 1 1] (src/fill [1 1] [1 1 1] 2))))
  (t/testing "shape column is empty"
    (t/is (= [1] (src/fill [1] [] 7)))))

(t/deftest delete-by-index-test
  (t/testing "remove item in the middle"
    (t/is (= [1 2 4 5] (src/delete-by-index [1 2 3 4 5] 2))))
  (t/testing "remove item at the beginning"
    (t/is (= [2 3 4 5] (src/delete-by-index [1 2 3 4 5] 0)))))

(t/deftest pad-test
  (t/testing "pad shape placed in the middle"
    (t/is (= [[] [] [] [] [] [1 1] [1 1] [] [] []] (src/pad [[1 1] [1 1]] 5))))
  (t/testing "pad shape placed at left end"
    (t/is (= [[1 1] [1 1] [] [] [] [] [] [] [] []] (src/pad [[1 1] [1 1]] 0))))
  (t/testing "pad shape placed at right end"
    (t/is (= [[] [] [] [] [] [] [] [] [1 1] [1 1]] (src/pad [[1 1] [1 1]] 8)))))

(t/deftest land-test
  (t/testing "adds shape to empty panel"
    (t/is (= [[] [0 1] [1 1] [0 1] [] [] [] [] [] []]
             (src/land [[] [] [] [] [] [] [] [] [] []] [[0 1] [1 1] [0 1]] 1))))
  (t/testing "adds shape to panel with existing blocks"
    (t/are [expt result] (= expt result)
      [[] [0 1] [1 1] [0 1 1] [0 1 1 1] [0 1 0 1] [0 0 0 1] [0 0 0 1] [] []]
      (src/land [[] [0 1] [1 1] [0 1 1] [0 1 1] [0 1] [] [] [] []] [[1] [1] [1] [1]] 4)
      [[] [0 1] [1 1] [0 1 1] [0 1 1] [0 1] [] [] [] []]
      (src/land [[] [0 1] [1 1] [0 1] [] [] [] [] [] []] [[0 1] [1 1] [1]] 3)
      [[] [] [] [] [] [] [] [1] [1 1 1 1] [0 0 1 1]]
      (src/land [[] [] [] [] [] [] [] [1] [1 1] []] [[1 1] [1 1]] 8)
      [[] [] [0 0 0 0 1 1] [1 1 1 1 1 1] [] [] [] [] [] []]
      (src/land [[] [] [] [1 1 1 1] [] [] [] [] [] []] [[1 1] [1 1]] 2))))

(t/deftest disppear-test
  (t/testing "all rows are filled up"
    (t/is (= [[] [] []] (src/disappear [[1 1 1] [1 1 1] [1 1 1]]))))
  (t/testing "some rows are filled up"
    (t/is (= [[0 1] [0] [1]] (src/disappear [[0 1 1 1] [0 1 1] [1 1 1]])))))

(t/deftest interpret-test
  (t/testing "interpretation of the given examples"
    (t/are [expt result] (= expt result)
      [{:shape :q :place 0} {:shape :i :place 2}]
      (src/interpret "Q0,I2")
      [{:shape :t, :place 1} {:shape :z, :place 3} {:shape :i, :place 4}]
      (src/interpret "T1,Z3,I4")
      [{:shape :t, :place 1} {:shape :z, :place 3} {:shape :i, :place 4}]
      (src/interpret "T1,Z3,I4"))))

(t/deftest process-test
  (t/testing "sample testing"
    (doseq [sample samples]
      (t/is (= (:render sample) (-> sample :input src/interpret src/process))))))

(t/deftest verdict-test
  (t/testing "sample testing"
    (doseq [sample samples]
      (t/is (= (:result sample) (src/verdict (:render sample)))))))
