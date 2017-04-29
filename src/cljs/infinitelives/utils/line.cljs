(ns infinitelives.utils.line
  (:require [infinitelives.utils.console :refer [log]]
            [infinitelives.utils.vec2 :as vec2]
            [cljs.core.match :refer-macros [match]]
))

(defn octant-of
  "return the octant number that a vector from
  start to end, or from [x0 y0] to [x1 y1].

 Octants:
  \\|1/
  3\\/0
 ---+---
  4/|\\7
  /5|6\\

  "
  ([start end]
   (let [[x0 y0] (vec2/as-vector start)
         [x1 y1] (vec2/as-vector end)]
     (octant-of x0 y0 x1 y1)))
  ([x0 y0 x1 y1]
   (cond
     (and (< x0 x1)
          (< y0 y1)
          (> (- x1 x0) (- y1 y0)))
     0

     (and (< x0 x1)
          (< y0 y1)
          (<= (- x1 x0) (- y1 y0)))
     1

     (and (<= x1 x0)
          (< y0 y1)
          (< (- x0 x1) (- y1 y0)))
     2

     (and (<= x1 x0)
          (< y0 y1)
          (>= (- x0 x1) (- y1 y0)))
     3

     (and (<= x1 x0)
          (<= y1 y0)
          (> (- x0 x1) (- y0 y1)))
     4

     (and (<= x1 x0)
          (<= y1 y0)
          (<= (- x0 x1) (- y0 y1)))
     5

     (and (< x0 x1)
          (<= y1 y0)
          (< (- x1 x0) (- y0 y1)))
     6

     (and (< x0 x1)
          (<= y1 y0)
          (>= (- x1 x0) (- y0 y1)))
     7)))

(defn to-zero-octant
  "return a projection of [x y] into the zero octant"
  [octant x y]
  (case octant
    0 [x y]
    1 [y x]
    2 [y (- x)]
    3 [(- x) y]
    4 [(- x) (- y)]
    5 [(- y) (- x)]
    6 [(- y) x]
    7 [x (- y)]))

(defn from-zero-octant
  "project [x y] from the zero octand to the passed in octant"
  [octant x y]
  (case octant
    0 [x y]
    1 [y x]
    2 [(- y) x]
    3 [(- x) y]
    4 [(- x) (- y)]
    5 [(- y) (- x)]
    6 [y (- x)]
    7 [x (- y)]))

(defn bresenham
  "given a start and end point, return a lazy sequence
  of all the squares that are crossed in the drawing of the line
  via bresenham's algorithm

  pass in two vec2s, start and end, or four values x0, y0, x1, y1

  https://en.wikipedia.org/wiki/Bresenham%27s_line_algorithm
  "
  ([start end]
   (let [[x0 y0] (vec2/as-vector start)
         [x1 y1] (vec2/as-vector end)]
     (bresenham x0 y0 x1 y1)))
  ([x0 y0 x1 y1]
   (let [octant (octant-of x0 y0 x1 y1)
         [x0 y0] (to-zero-octant octant x0 y0)
         [x1 y1] (to-zero-octant octant x1 y1)]
     (for [x (range x0 (inc x1))]
       (from-zero-octant
        octant x (+ y0
                    (* (- x x0)
                       (/ (- y1 y0)
                          (- x1 x0)))))))))
