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
