(ns infinitelives.utils.line-test
  (:require [cljs.test :refer-macros [deftest is]]
            [infinitelives.utils.line :as line]))

(deftest bresenham
  (is (= (line/bresenham 0 0 0 1) [[0 0] [0 1]])))
