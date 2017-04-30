(ns infinitelives.utils.line-test
  (:require [cljs.test :refer-macros [deftest is]]
            [infinitelives.utils.line :as line]))

(deftest bresenham
  ;; moving 1 square
  (is (= (line/bresenham 0 0 0 1) [[0 0] [0 1]]))
  (is (= (line/bresenham 0 0 1 1) [[0 0] [1 1]]))
  (is (= (line/bresenham 0 0 1 0) [[0 0] [1 0]]))
  (is (= (line/bresenham 0 0 1 -1) [[0 0] [1 -1]]))
  (is (= (line/bresenham 0 0 0 -1) [[0 0] [0 -1]]))
  (is (= (line/bresenham 0 0 -1 -1) [[0 0] [-1 -1]]))
  (is (= (line/bresenham 0 0 -1 0) [[0 0] [-1 0]]))
  (is (= (line/bresenham 0 0 -1 1) [[0 0] [-1 1]]))

  ;; start and end the same
  (is (= (line/bresenham 1 -3 1 -3) [[1 -3]]))
  (is (= (line/bresenham 0 0 0 0) [[0 0]]))

  ;; longer trace
  (is (= (line/bresenham -5 -2 3 9)
         [[-5 -2]
          [-4 -1]
          [-4 0]
          [-3 1]
          [-2 2]
          [-1 3]
          [-1 4]
          [0 5]
          [1 6]
          [2 7]
          [2 8]
          [3 9]])))
