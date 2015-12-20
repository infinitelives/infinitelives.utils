(ns infinitelives.utils.math-test
  (:require [cljs.test :refer-macros [deftest is]]
            [infinitelives.utils.math :as math]))

(deftest rand-between
  (dotimes [n 1000]
    (let [v (math/rand-between 1 10)]
      (is (<= 1 v 10))))

  (dotimes [n 1000]
    (let [v (math/rand-between -20 -10)]
      (is (<= -20 v -10)))))
