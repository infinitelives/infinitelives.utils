(ns infinitelives.utils.ease-test
  (:require [cljs.test :refer-macros [deftest is]]
            [infinitelives.utils.ease :as ease]))

(deftest linear
  (is (= (ease/linear 0 0 10 10) 0))
  (is (= (ease/linear 5 0 10 10) 5))
  (is (= (ease/linear 0.5 0 10 1) 5)))

(deftest quads
  (is (= (ease/quad-in 0 0 1 1) 0))
  (is (= (ease/quad-in 1 0 1 1) 1))
  (is (= (ease/quad-in 0.5 0 1 1) 0.25))

  (is (= (ease/quad-out 0 0 1 1) 0))
  (is (= (ease/quad-out 1 0 1 1) 1))
  (is (= (ease/quad-out 0.5 0 1 1) 0.75))

  (is (= (ease/quad-in-out 0 0 1 1) 0))
  (is (= (ease/quad-in-out 1 0 1 1) 1))
  (is (= (ease/quad-in-out 0.5 0 1 1) 0.5))
  (is (= (ease/quad-in-out 0.25 0 1 1) 0.125))
  (is (= (ease/quad-in-out 0.75 0 1 1) 0.875)))
