(ns
    ^{:doc "Vectors with 2 Dimensions. Utilises underlying google vector code."}
  infinitelives.utils.vec2
  (:require
   [goog.vec.Vec2 :as Vec2]))

(def
  ^{:arglist '([x y])
    :doc "Create a two dimensional vector #<x,y>"}
  vec2 Vec2/createFloat64FromValues)

(def
  ^{:arglist '([])
   :doc "Return the zero two dimensional vector #<0,0>"}
  make Vec2/createFloat64)

(def
  ^{:arglist '([])
   :doc "Return the zero two dimensional vector #<0,0>"}
  zero Vec2/createFloat64)

(def
  ^{:arglist '([v])
    :doc "Return the length of vector v"}
  magnitude Vec2/magnitude)

(def
  ^{:arglist '([v])
    :doc "Return the length squared of vector v"}
  magnitude-squared Vec2/magnitudeSquared)

(def
  ^{:arglist '([v0 v1])
    :doc "Return the distance between the tip of v0 and the tip of v1"}
  distance Vec2/distance)

(def
  ^{:arglist '([v0 v1])
    :doc "Return the distance squared between the tip of v0 and the tip of v1"
    }
  distance-squared Vec2/distanceSquared)

(def
  ^{:arglist '([v0 v1])
    :doc "Returns true is both v0 and v1 point in the same direction
     and are of the same length"}
  equals Vec2/equals)
