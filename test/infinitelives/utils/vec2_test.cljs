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

(deftest add
  (is (vec2/equals
       (vec2/vec2 1 1)
       (vec2/add
        (vec2/vec2 -1 -2) (vec2/vec2 2 3)))))

(deftest sub
  (is (vec2/equals
       (vec2/vec2 1 1)
       (vec2/sub (vec2/vec2 20 -10) (vec2/vec2 19 -11)))))

(deftest dot
  (is (= (vec2/dot
          (vec2/vec2 10 20)
          (vec2/vec2 5 2))
         90)))

(deftest scale
  (is (vec2/equals (vec2/scale (vec2/vec2 1 -1) 5)
                  (vec2/vec2 5 -5))))

(deftest scale-div
  (is (vec2/equals (vec2/scale-div (vec2/vec2 5 -5) 5)
                  (vec2/vec2 1 -1))))

(deftest unit
  (is (vec2/equals
       (vec2/unit (vec2/vec2 10 -10))
       (vec2/vec2 on-root-2 (- on-root-2)))))

(deftest abs
  (is (vec2/equals
       (vec2/abs (vec2/vec2 -2 -5))
       (vec2/vec2 2 5))))

(deftest direction
  (is (vec2/equals
       (vec2/direction (vec2/vec2 10 5) (vec2/vec2 10 -5))
       (vec2/vec2 0 -1))))

(deftest rotate
  (is (vec2/almost
       (vec2/rotate (vec2/vec2 1 0) pi-on-2)
       (vec2/vec2 0 1)))
  (is (vec2/almost
       (vec2/rotate (vec2/vec2 20 20) Math/PI)
       (vec2/vec2 -20 -20)))
  (is (vec2/almost
       (vec2/rotate (vec2/vec2 10 10) (- pi-on-2))
       (vec2/rotate (vec2/vec2 10 10) (* 3 pi-on-2))
       (vec2/vec2 10 -10))))

(deftest rotate-90
  (is (vec2/equals
       (vec2/rotate-90 (vec2/vec2 1 0))
       (vec2/vec2 0 1))))

(deftest random-unit
  (loop [tests 20]
    (let [v1 (vec2/random-unit)
          v2 (vec2/random-unit)]
      (is (<= 0.9999999 (vec2/magnitude v1) 1))
      (is (<= 0.9999999 (vec2/magnitude v2) 1))
      (is (not (vec2/almost v1 v2)))
      (when (pos? tests)
        (recur (dec tests))))))

(deftest random
  (loop [tests 20]
    (let [v1 (vec2/random)
          v2 (vec2/random)]
      (is (<= (vec2/magnitude v1) 1))
      (is (<= (vec2/magnitude v2) 1))
      (is (not (vec2/almost v1 v2)))
      (when (pos? tests)
        (recur (dec tests))))))

(deftest lerp
  (let [v0 (vec2/vec2 -10 -10)
        v1 (vec2/vec2 10 10)]
    (is (vec2/equals
         (vec2/lerp v0 v1 0)
         v0))
    (is (vec2/equals
         (vec2/lerp v0 v1 1)
         v1))
    (is (vec2/equals
         (vec2/lerp v0 v1 0.5)
         (vec2/zero)))
    (is (vec2/equals
         (vec2/lerp v0 v1 0.25)
         (vec2/vec2 -5 -5)))
    (is (vec2/equals
         (vec2/lerp v0 v1 0.75)
         (vec2/vec2 5 5)))
    (is (vec2/equals
         (vec2/lerp v0 v1 2)
         (vec2/vec2 30 30)))
    (is (vec2/equals
         (vec2/lerp v0 v1 -1)
         (vec2/vec2 -30 -30)))))

(deftest heading
  (is (= 0 (vec2/heading (vec2/vec2 1 0))))
  (is (= pi-on-2 (vec2/heading (vec2/vec2 0 1))))
  (is (= Math/PI (vec2/heading (vec2/vec2 -1 0))))
  (is (= (* 3 pi-on-2) (vec2/heading (vec2/vec2 0 -1))))
  (is (= (/ Math/PI 4) (vec2/heading (vec2/vec2 1 1))))
  (is (= (* 3 (/ Math/PI 4)) (vec2/heading (vec2/vec2 -1 1))))
  (is (= (* 5 (/ Math/PI 4)) (vec2/heading (vec2/vec2 -1 -1))))
  (is (= (* 7 (/ Math/PI 4)) (vec2/heading (vec2/vec2 1 -1)))))

(deftest angle-between
  (is (= 0 (vec2/angle-between (vec2/vec2 1 0) (vec2/vec2 1 0))))
  (is (= (/ Math/PI 2) (vec2/angle-between (vec2/vec2 1 0) (vec2/vec2 0 1))))
  (is (= (/ Math/PI 2) (vec2/angle-between (vec2/vec2 -1 0) (vec2/vec2 0 1))))
  (is (= Math/PI (vec2/angle-between (vec2/vec2 1 0) (vec2/vec2 -1 0)))))

(deftest rotated-pos?
  (is (vec2/rotated-pos? (vec2/vec2 0 1) (vec2/vec2 -1 1)))
  (is (vec2/rotated-pos? (vec2/vec2 0 1) (vec2/vec2 -1 0)))
  (is (vec2/rotated-pos? (vec2/vec2 0 1) (vec2/vec2 -1 -1)))
  (is (not (vec2/rotated-pos? (vec2/vec2 0 1) (vec2/vec2 1 1))))
  (is (not (vec2/rotated-pos? (vec2/vec2 0 1) (vec2/vec2 1 -1))))
  (is (not (vec2/rotated-pos? (vec2/vec2 0 1) (vec2/vec2 1 -1)))))
