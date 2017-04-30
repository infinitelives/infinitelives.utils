(ns infinitelives.utils.coordinates
  (:require [infinitelives.utils.vec2 :as vec2]))

(defn get-window-size
  "Returns the size of the window as [width height]"
  []
  [(.-innerWidth js/window)
   (.-innerHeight js/window)])

(defn origin-top-left->center
  "Converts coordinates with an origin in the top left
   to coordinates with the origin in the center"
  ([x y]
    (let [[width height] (get-window-size)]
    [(- x (/ width 2))
     (- y (/ height 2))]))
  ([vec-pos]
    (apply origin-top-left->center (vec2/get-xy vec-pos))))

