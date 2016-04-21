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
         1e-10))

  )

(comment
  (deftest polygon-angles
    (let [a (vec2/vec2 0 0)
          b (vec2/vec2 0 4)
          c (vec2/vec2 3 3)
          d (vec2/vec2 4 1)
          e (vec2/vec2 3 0)
          poly [a b c d e]]
      (println
       (sort (map vector
                  (map #(apply geom2/angle %) (geom2/polygon-triplets poly))
                  (range)))))))
