(ns core-test
  (:require
   [core :as src]
   [clojure.test :as t]))

(t/deftest pad-test
  (t/is (= [1 2 3 nil nil nil nil]
           (src/pad [1 2 3] 7))))

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
  (t/is (= 0 (src/find-max-height [[] [] []] [[0 1] [1 1] [0 1]])))
  (t/is (= 1 (src/find-max-height [[] [1] []] [[0 1] [1 1] [0 1]])))
  (t/is (= 1 (src/find-max-height [[1 1] [1 1] [1]] [[0 1] [0 1] [0 1]])))
  (t/is (= 2 (src/find-max-height [[1 1] [1 1] [1]] [[0 1] [1 1] [0 1]]))))

(t/deftest fill-test
  (t/is (= [1 2 3 nil nil nil nil 8 9] (src/fill [1 2 3] [8 9] 7)))

  )

(mapv ["a" "b" "c"] (range 1 3))

2
0 0
1 1 
1 1 1
