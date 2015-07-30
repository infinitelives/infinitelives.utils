(ns infinitelives.utils.vec2-test
  (:require [cljs.test :refer-macros [deftest is]]
            [infinitelives.utils.vec2 :as vec2]))

(def on-root-2 (/ 1 (Math/sqrt 2)))
(def pi-on-2 (/ Math/PI 2))

(deftest create-vec2
  (let [v2 (vec2/vec2 -1 1)]
    (is (and
         (= (aget v2 0) -1)
         (= (aget v2 1) 1))))
  (let [v3 (vec2/make)]
    (is (and
         (= (aget v3 0) 0)
         (= (aget v3 1) 0))))
  (let [v3 (vec2/zero)]
    (is (and
         (= (aget v3 0) 0)
         (= (aget v3 1) 0)))))

(deftest magnitude
  (let [v (vec2/vec2 on-root-2 on-root-2)]
    (is (< 0.9999999 (vec2/magnitude v) 1.0000001))
    (is (< 0.9999999 (vec2/magnitude v) 1.0000001)))
  (let [v (vec2/vec2 10 -10)]
    (is (= (Math/sqrt 200) (vec2/magnitude v)))
    (is (= 200 (vec2/magnitude-squared v)))))

(deftest distance
  (let [v0 (vec2/vec2 20 10)
        v1 (vec2/vec2 -20 10)]
    (is (= 40 (vec2/distance v0 v1)))
    (is (= 1600 (vec2/distance-squared v0 v1)))))

(deftest equals
  (is (vec2/equals
       (vec2/vec2 100 -200)
       (vec2/vec2 100 -200))))

(deftest almost
  (binding [vec2/*almost-delta* 1e-5]
    (is (vec2/almost
         (vec2/vec2 20 20)
         (vec2/vec2 20 20)))
    (is (vec2/almost
         (vec2/vec2 0 0)
         (vec2/vec2 1e-6 1e-6)))
    (is (not (vec2/almost
              (vec2/vec2 0 0)
              (vec2/vec2 0 0)
              (vec2/vec2 0 0)
              (vec2/vec2 0 0)
              (vec2/vec2 0 0)
              (vec2/vec2 0 0)
              (vec2/vec2 0 0.0001))))))
