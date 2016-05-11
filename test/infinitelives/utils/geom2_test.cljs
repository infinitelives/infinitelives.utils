(ns infinitelives.utils.geom2-test
  (:require [cljs.test :refer-macros [deftest is]]
            [infinitelives.utils.geom2 :as geom2]
            [infinitelives.utils.vec2 :as vec2]))

(def on-root-2 (/ 1 (Math/sqrt 2)))
(def pi-on-2 (/ Math/PI 2))

(deftest angle
  (is (= pi-on-2
         (geom2/angle
          (vec2/vec2 100 100)
          (vec2/vec2 100 50)
          (vec2/vec2 400 50))))
  (is (> 1e-7 (geom2/angle
              (vec2/vec2 -10 10)
              (vec2/vec2 0 0)
              (vec2/vec2 -10 10))))
  (is (= Math/PI
         (geom2/angle
          (vec2/vec2 -10 0)
          (vec2/vec2 0 0)
          (vec2/vec2 10 0)))))

(deftest polygon-triplets
  (let [a (vec2/vec2 0 0)
        b (vec2/vec2 0 1)
        c (vec2/vec2 1 1)
        d (vec2/vec2 1 0)
        poly [a b c d]]
    (is (= (geom2/polygon-triplets poly)
           [[d a b] [a b c] [b c d] [c d a]]))))

(deftest outside-angle
  (is (= (- (/ Math/PI 2))
         (geom2/outside-angle (vec2/vec2 0 0)
                              (vec2/vec2 0 1)
                              (vec2/vec2 1 1))))
  (is (= (/ Math/PI 2)
         (geom2/outside-angle (vec2/vec2 0 0)
                              (vec2/vec2 0 1)
                              (vec2/vec2 -1 1))))
  (is (< -1e-10
         (- (- (/ Math/PI 4))
            (geom2/outside-angle (vec2/vec2 0 0)
                                 (vec2/vec2 0 1)
                                 (vec2/vec2 1 2)))
         1e-10))
  (is (< -1e-10
         (-  (/ Math/PI 4)
             (geom2/outside-angle (vec2/vec2 0 0)
                                  (vec2/vec2 0 1)
                                  (vec2/vec2 -1 2)))
         1e-10)))

(deftest total-outside-angle
  (let [poly [(vec2/vec2 0 0)
              (vec2/vec2 0 1)
              (vec2/vec2 1 2)
              (vec2/vec2 1 3)
              (vec2/vec2 0 4)
              (vec2/vec2 1 5)
              (vec2/vec2 2 5)
              (vec2/vec2 3 4)
              (vec2/vec2 2 3)
              (vec2/vec2 2 2)
              (vec2/vec2 3 2)
              (vec2/vec2 3 1)
              (vec2/vec2 2 1)
              (vec2/vec2 1 0)]]
    (is (=
         (* -2 Math/PI)
         (geom2/total-outside-angle poly)))
    (is (=
         (* 2 Math/PI)
         (geom2/total-outside-angle (reverse poly))))))

(deftest positive-poly?:negative-poly?
  (let [poly [(vec2/vec2 0 0)
              (vec2/vec2 0 1)
              (vec2/vec2 1 2)
              (vec2/vec2 1 3)
              (vec2/vec2 0 4)
              (vec2/vec2 1 5)
              (vec2/vec2 2 5)
              (vec2/vec2 3 4)
              (vec2/vec2 2 3)
              (vec2/vec2 2 2)
              (vec2/vec2 3 2)
              (vec2/vec2 3 1)
              (vec2/vec2 2 1)
              (vec2/vec2 1 0)]]
    (is (geom2/positive-poly? (reverse poly)))
    (is (not (geom2/positive-poly? poly)))
    (is (geom2/negative-poly? poly))
    (is (not (geom2/negative-poly? (reverse poly))))))

(deftest triangulate
  (let [a (vec2/vec2 1 0)
        b (vec2/vec2 1 1)
        c (vec2/vec2 0 1)
        d (vec2/vec2 0 2)
        e (vec2/vec2 1 2)
        f (vec2/vec2 2 1)
        g (vec2/vec2 2 0)
        poly [a b c d e f g]]
    (is (= (geom2/triangulate poly)
           [[g a b] [f g b] [e f b] [e b c] [e c d]]))))

#_ (deftest tri
  (let [a (vec2/vec2 0 2)
        b (vec2/vec2 4 3)
        c (vec2/vec2 3 2)
        d (vec2/vec2 4 1)
        poly [a b c d]]
    (println (geom2/triangulate poly))))
