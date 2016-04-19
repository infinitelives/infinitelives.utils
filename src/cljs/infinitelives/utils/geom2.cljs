(ns
    ^{:doc "Geometry functionality for 2D operations."}
  infinitelives.utils.geom2
  (:require [infinitelives.utils.vec2 :as vec2]))

(defn angle
  "given three points in R2 space (represented as vec2's p1, p2 and
  p3) calculate the inner angle formed around p2 by the vectors p2->p1
  and p2->p3"
  [p1 p2 p3]
  (vec2/angle-between
   (vec2/sub p1 p2)
   (vec2/sub p3 p2)))

(defn polygon-triplets
  "take the vec2 points of a polygon, and return triplets of
  consecutive points representing each angle in turn. So for a
  polygon [a b c d e] return [ [e a b] [a b c] [b c d] [c d e] [d e
  a]], That is, the corner a, then the corner b ... and so on"
  [poly]
  (map vector
       (conj (butlast poly) (last poly))
       poly
       (concat (rest poly) (take 1 poly))))
