(ns infinitelives.utils.intersect-test
  (:require [cljs.test :refer-macros [deftest is]]
            [infinitelives.utils.intersect :as intersect]))

(deftest aabb-intersect-point
  (is (nil? (intersect/aabb-intersect-point [10 10 1 1] [5 3])))
  (is (nil? (intersect/aabb-intersect-point [10 10 1 1] [9 9])))
  (is (= {:delta [0 0], :normal [0 0], :pos [10 10]}
         (intersect/aabb-intersect-point [10 10 1 1] [10 10])))
  (is (= {:delta [-0.1999999999999993 0], :normal [-1 0], :pos [9 9.6]}
         (intersect/aabb-intersect-point [10 10 1 1] [9.2 9.6]))))
