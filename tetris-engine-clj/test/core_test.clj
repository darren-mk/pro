(ns core-test
  (:require
   [core :as src]
   [clojure.test :as t]))

(def samples
  [{:input "I0,I4,Q8",
    :render [[] [] [] [] [] [] [] [] [1] [1]],
    :result 1}
   {:input "T1,Z3,I4",
    :render
    [[]
     [0 1]
     [1 1]
     [0 1 1]
     [0 1 1 1]
     [0 1 0 1]
     [0 0 0 1]
     [0 0 0 1]
     []
     []],
    :result 4}
   {:input "Q0,I2,I6,I0,I6,I6,Q2,Q4",
    :render [[1] [1] [0 1 1] [0 1 1] [1] [1] [1] [1] [1] [1]],
    :result 3}
   {:input "Q0",
    :render [[1 1] [1 1] [] [] [] [] [] [] [] []],
    :result 2}
   {:input "Q0,Q1",
    :render [[1 1] [1 1 1 1] [0 0 1 1] [] [] [] [] [] [] []],
    :result 4}
   {:input "Q0,Q2,Q4,Q6,Q8",
    :render [[] [] [] [] [] [] [] [] [] []],
    :result 0}
   {:input "Q0,Q2,Q4,Q6,Q8,Q1",
    :render [[] [1 1] [1 1] [] [] [] [] [] [] []],
    :result 2}
   {:input "Q0,Q2,Q4,Q6,Q8,Q1,Q1",
    :render [[] [1 1 1 1] [1 1 1 1] [] [] [] [] [] [] []],
    :result 4}
   {:input "I0,I4,Q8",
    :render [[] [] [] [] [] [] [] [] [1] [1]],
    :result 1}
   {:input "I0,I4,Q8,I0,I4",
    :render [[] [] [] [] [] [] [] [] [] []],
    :result 0}
   {:input "L0,J2,L4,J6,Q8",
    :render [[1 1] [] [] [1 1] [1 1] [] [] [1 1] [1] [1]],
    :result 2}
   {:input "L0,Z1,Z3,Z5,Z7",
    :render [[1 1] [1] [1] [1] [1] [1] [1] [1] [1] []],
    :result 2}
   {:input "T0,T3",
    :render [[0 1] [1 1] [0 1] [0 1] [1 1] [0 1] [] [] [] []],
    :result 2}
   {:input "T0,T3,I6,I6",
    :render [[0] [1] [0] [0] [1] [0] [1] [1] [1] [1]],
    :result 1}
   {:input "I0,I6,S4",
    :render [[] [] [] [] [] [1] [1] [] [] []],
    :result 1}
   {:input "T1,Z3,I4",
    :render
    [[]
     [0 1]
     [1 1]
     [0 1 1]
     [0 1 1 1]
     [0 1 0 1]
     [0 0 0 1]
     [0 0 0 1]
     []
     []],
    :result 4}
   {:input "L0,J3,L5,J8,T1",
    :render
    [[1 1 1] [1 1] [1 1] [1 1] [1 1 1] [1 1 1] [1] [] [1] [1 1 1]],
    :result 3}
   {:input "L0,J3,L5,J8,T1,T6",
    :render [[1] [] [] [] [1] [1] [] [] [] [1]],
    :result 1}
   {:input "L0,J3,L5,J8,T1,T6,J2,L6,T0,T7",
    :render [[1] [1] [1] [1 1] [] [] [1 1] [1] [1] [1]],
    :result 2}
   {:input "L0,J3,L5,J8,T1,T6,J2,L6,T0,T7,Q4",
    :render [[] [] [] [1] [1] [1] [1] [] [] []],
    :result 1}
   {:input "S0,S2,S4,S6",
    :render
    [[1]
     [1 1]
     [0 1 1]
     [0 0 1 1]
     [0 0 0 1 1]
     [0 0 0 0 1 1]
     [0 0 0 0 0 1 1]
     [0 0 0 0 0 0 1 1]
     [0 0 0 0 0 0 0 1]
     []],
    :result 8}
   {:input "S0,S2,S4,S5,Q8,Q8,Q8,Q8,T1,Q1,I0,Q4",
    :render
    [[1 0 0 0 0 0 0]
     [1 1 0 0 1 1 1]
     [0 1 1 1 1 1 1]
     [0 0 1 1 1 0 0]
     [0 0 0 1 1 0 0 1]
     [0 0 0 0 1 1 1 1]
     [0 0 0 0 0 1 1]
     [0 0 0 0 0 0 0]
     [1 1 1 1 1 1 1]
     [1 1 1 1 1 1 1]],
    :result 8}
   {:input "L0,J3,L5,J8,T1,T6,S2,Z5,T0,T7",
    :render [[] [] [] [] [] [] [] [] [] []],
    :result 0}
   {:input "Q0,I2,I6,I0,I6,I6,Q2,Q4",
    :render [[1] [1] [0 1 1] [0 1 1] [1] [1] [1] [1] [1] [1]],
    :result 3}])

(t/deftest cushion-test
  (t/is (= [1 2 3 0 0 0 0] (src/cushion [1 2 3] 7))))

(t/deftest count-zero-pieces-test
  (t/is (= 0 (src/count-zero-pieces [1 0 1])))
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
  (t/is (= [[0 1] [0] [1]] (src/disappear [[0 1 1 1] [0 1 1] [1 1 1]]))))

(t/deftest interpret-test
  (t/is (= [{:shape :q :place 0} {:shape :i :place 2}]
           (src/interpret "Q0,I2")))
  (t/is (= [{:shape :t, :place 1} {:shape :z, :place 3} {:shape :i, :place 4}]
           (src/interpret "T1,Z3,I4")))
  (t/is (= [{:shape :t, :place 1} {:shape :z, :place 3} {:shape :i, :place 4}]
           (src/interpret "T1,Z3,I4"))))

(t/deftest process-test
  (t/testing "sample testing"
    (doseq [sample samples]
      (t/is (= (:render sample) (-> sample :input src/interpret src/process))))))

(t/deftest verdict-test
  (t/testing "sample testing"
    (doseq [sample samples]
      (t/is (= (:result sample) (src/verdict (:render sample)))))))
