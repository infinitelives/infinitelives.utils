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
