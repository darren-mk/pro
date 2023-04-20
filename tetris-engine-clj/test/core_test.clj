(ns core-test
  (:require
   [core :as src]
   [clojure.test :as t]))

(t/deftest cushion-test
  (t/is (= [1 2 3 0 0 0 0]
           (src/cushion [1 2 3] 7))))

(t/deftest count-zero-piece
  (t/is (= 0 (src/count-zero-pieces [1 0 1 ])))
  (t/is (= 1 (src/count-zero-pieces [0 1 1])))
  (t/is (= 2 (src/count-zero-pieces [0 0 1]))))

(t/deftest find-height-single-test
  (t/is (= 5 (src/find-height-single [1 1 0 nil 1] [1 1 1])))
  (t/is (= 3 (src/find-height-single [1 1 1] [1 1 1])))
  (t/is (= 1 (src/find-height-single [1 1 1] [0 0 1])))
  (t/is (= 0 (src/find-height-single [1] [0 0 1])))
  (t/is (= 0 (src/find-height-single [] [0 1]))))

(t/deftest find-max-height-test
  (t/is (= 0 (src/find-required-height [[] [] []] [[0 1] [1 1] [0 1]])))
  (t/is (= 1 (src/find-required-height [[] [1] []] [[0 1] [1 1] [0 1]])))
  (t/is (= 1 (src/find-required-height [[1 1] [1 1] [1]] [[0 1] [0 1] [0 1]])))
  (t/is (= 2 (src/find-required-height [[1 1] [1 1] [1]] [[0 1] [1 1] [0 1]]))))

(t/deftest fill-test
  (t/is (= [1 2 3 0 0 0 0 8 9] (src/fill [1 2 3] [8 9] 7)))
  (t/is (= [1 0 0 0 0 0 0 1 1 1] (src/fill [1] [1 1 1] 7)))
  (t/is (= [1] (src/fill [1] [] 7))))

(t/deftest delete-by-index-test
  (t/is (= [1 2 4 5] (src/delete-by-index [1 2 3 4 5] 2))))

(t/deftest pad-test
  (t/is (= [[] [] [] [] [] [1 1] [1 1] [] [] []]
           (src/pad [[1 1] [1 1]] 5))))

(t/deftest land-test
  (t/is (= [[] [0 1] [1 1] [0 1] [] [] [] [] [] []]
           (src/land [[] [] [] [] [] [] [] [] [] []] [[0 1] [1 1] [0 1]] 1)))
  (t/is (= [[] [0 1] [1 1] [0 1 1] [0 1 1 1] [0 1 0 1] [0 0 0 1] [0 0 0 1] [] []] 
           (src/land [[] [0 1] [1 1] [0 1 1] [0 1 1] [0 1] [] [] [] []] [[1] [1] [1] [1]] 4)))
  (t/is (= [[] [0 1] [1 1] [0 1 1] [0 1 1] [0 1] [] [] [] []]
           (src/land [[] [0 1] [1 1] [0 1] [] [] [] [] [] []] [[0 1] [1 1] [1]] 3)))
  (t/is (= [[] [] [] [] [] [] [] [1] [1 1 1 1] [0 0 1 1]]
           (src/land [[] [] [] [] [] [] [] [1] [1 1] []] [[1 1] [1 1]] 8)))
  (t/is (= [[] [] [0 0 0 0 1 1] [1 1 1 1 1 1] [] [] [] [] [] []]
           (src/land [[] [] [] [1 1 1 1] [] [] [] [] [] []] [[1 1] [1 1]] 2))))

(t/deftest disppear-test
  (t/is (= [[] [] []] (src/disappear [[1 1 1] [1 1 1] [1 1 1]])))
  (t/is (= [[0 1] [] [1]] (src/disappear [[0 1 1 1] [0 1 1] [1 1 1]]))))
