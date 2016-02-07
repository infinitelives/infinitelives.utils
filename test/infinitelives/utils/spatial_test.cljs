(ns infinitelives.utils.spatial-test
  (:require [cljs.test :refer-macros [deftest is]]
            [infinitelives.utils.spatial :as s]))

(deftest hash-position
  (is (= (s/hash-position 10 [123 435 32.4]) [12 43 3]))
  (is (= (s/hash-position 10 [3.2 -2.4 -4.6]) [0 -1 -1]))
  (is (= (s/hash-position 10 [7.2 -5.4 -8.6]) [0 -1 -1]))
  (is (= (s/hash-position 10 [-9.9 -9.9 9.9 9.9]) [-1 -1 0 0]))
  (is (= (s/hash-position 10 [17.2 -25.4]) [1 -3]))
  (is (= (s/hash-position 10 [0 0]) [0 0]))
  (is (= (s/hash-position 5 [-9.9 -9.9 9.9 9.9]) [-2 -2 1 1]))
)

(deftest add-to-hash
  (is (=
       (s/add-to-hash {:hash {} :divider 10} [:my :entity] [124 -220.5])
       {:divider 10 :hash {[12 -23] {[:my :entity] [124 -220.5]}}}
       )))

(deftest remove-from-hash
  (is (=
       (s/remove-from-hash
        {:divider 10 :hash {[12 -23] {[:my :entity] [124 -220.5]}}}
        [:my :entity] [124 -220.5])
       {:divider 10 :hash {}})))

(deftest move-in-hash
  (is (=
       (s/move-in-hash
        {:divider 10 :hash {[12 -23] {[:my :entity] [124 -220.5]}}}
        [:my :entity] [124 -220.5] [125 -223.4])
       {:divider 10 :hash {[12 -23] {[:my :entity] [125 -223.4]}}}))

  (is (=
       (s/move-in-hash
        {:divider 10 :hash {[12 -23] {[:my :entity] [124 -220.5]}}}
        [:my :entity] [124 -220.5] [115 23.4])
       {:divider 10 :hash {[11 2] {[:my :entity] [115 23.4]}}}))
)
