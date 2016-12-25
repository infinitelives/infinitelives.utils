(ns infinitelives.utils.pathfind-test
  (:require [cljs.test :refer-macros [deftest is]]
            [infinitelives.utils.pathfind :as pf]))

(deftest manhattan-test
  (is (= (pf/manhattan 0 0) 0))
  (is (= (pf/manhattan 1 4) 5))
  (is (= (pf/manhattan 1 -4) 5))
  (is (= (pf/manhattan -1 4) 5))
  (is (= (pf/manhattan -1 -4) 5)))

(deftest chebyshev-test
  (is (= (pf/chebyshev 0 0) 0))
  (is (= (pf/chebyshev 1 4) 4))
  (is (= (pf/chebyshev 1 -4) 4))
  (is (= (pf/chebyshev -1 4) 4))
  (is (= (pf/chebyshev -1 -4) 4)))

(deftest euclid-test
  (is (= (pf/euclid 0 0) 0))
  (is (= (pf/euclid 3 4) 5))
  (is (= (pf/euclid 3 -4) 5))
  (is (= (pf/euclid -3 4) 5))
  (is (= (pf/euclid -3 -4) 5)))

(deftest distance-between-test
  (is (= (pf/distance-between pf/manhattan [0 0] [-3 4]) 7))
  (is (= (pf/distance-between pf/chebyshev [0 0] [-3 4]) 4))
  (is (= (pf/distance-between pf/euclid    [0 0] [-3 4]) 5)))

(deftest state-add-neighbour-test
  (is (=
       (pf/state-add-neighbour
        (pf/->state #{} #{} {} {} {})
        [0 0]
        [1 1])
       (pf/->state #{} #{[1 1]} {[1 1] [0 0]} {} {}))))

(deftest state-add-open-test
  (is (=
       (pf/state-add-open
        (pf/->state #{} #{} {} {} {})
        [0 0])
       (pf/->state #{} #{[0 0]} {} {} {}))))

(deftest state-open-to-closed-test
  (is (=
       (pf/state-open-to-closed
        (pf/->state #{} #{[0 0]} {} {} {})
        [0 0])
       (pf/->state #{[0 0]} #{} {} {} {}))))

(deftest reduce-state-over-neighbours
  (is (=
       (pf/reduce-state-over-neighbours
        (pf/->state #{} #{[0 0]} {} {} {})
        [0 0]
        [[0 1] [0 -1] [1 0] [-1 0] [1 1] [-1 1] [1 -1] [-1 -1]])
       (pf/->state
        #{}
        #{[0 0] [0 1] [0 -1] [1 0] [-1 0] [1 1] [-1 1] [1 -1] [-1 -1]}
        {[0 1] [0 0]
         [0 -1] [0 0]
         [1 0] [0 0]
         [-1 0] [0 0]
         [1 1] [0 0]
         [-1 1] [0 0]
         [1 -1] [0 0]
         [-1 -1] [0 0]
         }
        {} {}))))

(deftest calculate-open-fscore-test
  (let [{:keys [g-score f-score]}
        (pf/calculate-open-fscore
         (pf/->state
          #{} #{[0 1] [0 0] [-1 1] [1 1] [1 -1] [1 0] [-1 0] [-1 -1] [0 -1]}
          {[0 1] [0 0], [0 -1] [0 0], [1 0] [0 0], [-1 0] [0 0], [1 1] [0 0], [-1 1] [0 0], [1 -1] [0 0], [-1 -1] [0 0]}
          {}, {})
         [0 0] [10 10])]
    (is (= g-score
           {[0 1] 10
            [0 0] 0
            [-1 1] 14
            [1 1] 14
            [1 -1] 14
            [1 0] 10
            [-1 0] 10
            [-1 -1] 14
            [0 -1] 10}))
    (is (= f-score
           {[0 1] 200
            [0 0] 200
            [-1 1] 214
            [1 1] 194
            [1 -1] 214
            [1 0] 200
            [-1 0] 220
            [-1 -1] 234
            [0 -1] 220}))))

(deftest lowest-f-score-open-cell-test
  (is
   (#{[0 1] [1 1]}
    (->
     (pf/->state
      #{} #{[0 1] [0 0] [-1 1] [1 1] [1 -1] [1 0] [-1 0] [-1 -1] [0 -1]}
      {[0 1] [0 0], [0 -1] [0 0], [1 0] [0 0], [-1 0] [0 0], [1 1] [0 0], [-1 1] [0 0], [1 -1] [0 0], [-1 -1] [0 0]}
      {}, {})
     (pf/calculate-open-fscore [0 1] [10 10])
     (pf/lowest-f-score-open-cell)))))

(deftest A*-step-test
  (let [start [0 0]
        end [10 10]
        [{:keys [open-set came-from g-score] :as state} next-cell]
        (-> (pf/->state #{} #{start} {} {start 0} {start 10})
            (pf/A*-step (constantly true) start end false))]
    (is (= open-set #{[-1 -1] [0 -1] [1 -1] [-1 0] [1 0] [-1 1] [0 1] [1 1]}))
    (is (= came-from
           {
            [-1 -1] [0 0]
            [0 -1] [0 0]
            [1 -1] [0 0]
            [-1 0] [0 0]
            [1 0] [0 0]
            [-1 1] [0 0]
            [0 1] [0 0]
            [1 1] [0 0]
            }))
    (is (= g-score
           {[0 -1] 10
            [-1 0] 10
            [0 1] 10
            [1 0] 10
            [-1 -1] 14
            [-1 1] 14
            [1 -1] 14
            [1 1] 14
            [0 0] 0
            }))))

(deftest A*-test
  (is (= (pf/A* (constantly true) [0 0] [10 5])
         '([0 0] [1 1] [2 2] [3 3] [4 4] [5 5] [6 5] [7 5] [8 5] [9 5] [10 5]))))

(deftest A*-obstacle-test
  (let [passable? (fn [pos]
                    (-> pos #{[3 3] [3 4] [4 4] [4 3]} boolean not))]
    (is (= (pf/A* passable? [0 0] [10 5] :corner-cut)
           '([0 0] [1 1] [2 2]
             [3 2] [4 2] [5 3]
             [6 4] [7 5] [8 5]
             [9 5] [10 5])))))

(deftest A*-obstacle-no-diags-test
  (let [passable? (fn [pos]
                    (-> pos #{[3 3] [3 4] [4 4] [4 3]} boolean not))]
    (is (= (pf/A* passable? [0 0] [10 5])
           '([0 0] [1 1] [2 2]
             [3 2] [4 2] [5 2]
             [6 3] [7 4] [8 5]
             [9 5] [10 5])))))

(deftest A*-cant-reach
  (let [passable? (fn [[x y]]
                    (not
                     (or (= x -5)
                         (= x 5)
                         (= y -5)
                         (= y 5))))]
    (is (nil? (pf/A* passable? [0 0] [10 10])))))

(deftest A*-direct-path
  (let [passable? (fn [[x y]]
                    (or
                     (and (<= 2 x 7)
                          (<= 1 y 6))
                     (and (<= 8 x 13)
                          (= 4 y))
                     (and (= 13 x)
                          (<= 4 y 9))
                     (and (<= 6 x 14)
                          (<= 10 y 12))))]
    (println (pf/A* passable? [6 5] [6 12]))))
