(ns
    ^{:doc "Vectors with 2 Dimensions. Utilises underlying google vector code."}
  infinitelives.utils.vec2
  (:require
   [goog.vec.Vec2 :as Vec2]))

(def ^{:dynamic true
       :doc "The fuzzy zone size for the almost comparitor"}
  *almost-delta* 1e-14)

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

(defn almost
  "Returns true if all the vectors passed in are so close they
  are almost equal. This is for dealing with precision problems
  in comparison."
  ([v0 v1]
   (and
    (< (Math/abs (- (aget v0 0) (aget v1 0))) *almost-delta*)
    (< (Math/abs (- (aget v0 1) (aget v1 1))) *almost-delta*)))

  ([v0 v1 & args]
   (let [vecs (concat [v0 v1] args)
         xs (for [v vecs] (aget v 0))
         ys (for [v vecs] (aget v 1))
         min-x (reduce min xs)
         max-x (reduce max xs)
         min-y (reduce min ys)
         max-y (reduce max ys)]
     (and (< (- max-x min-x) *almost-delta*)
          (< (- max-y min-y) *almost-delta*)))))

(defn add
  "Returns a new vec2 which is v0 + v1"
  [v0 v1]
  (Vec2/add v0 v1 (make)))

(defn sub
  "Passed one arg, returns a vector that is equal and
  opposite to v, 180 degrees around. Passed two args,
  returns a vector that is equal to v0 - v1"
  ([v]
   (Vec2/negate v (make)))
  ([v0 v1]
   (Vec2/subtract v0 v1 (make))))
