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
  (assert (>= (count poly) 3)
          "Polygon passed to polygon-triplets has less than three vertices")
  (map vector
       (conj (butlast poly) (last poly))
       poly
       (concat (rest poly) (take 1 poly))))

(defn outside-angle
  "given three vec2 points representing an angle, calculate the
  'outside' angle formed on the central point, b. By outside we mean
  'in the positive polar direction'. That is, on a computer display
  co-ordinate system, in a clockwise direction (from vector a->b) and
  in a maths co-ordinate system, a counter-clockwise direction. If the
  angle is in the negative polar direction, returns a negative
  value"
  [a b c]
  (let [ab (vec2/sub b a)
        bc (vec2/sub c b)
        pos? (vec2/rotated-pos? ab bc)
        theta (vec2/angle-between ab bc)]
    (if pos? theta (- theta))))

(defn total-outside-angle
  "Sum all the outside angles on a polygon. Should return 2*pi or
  -2*pi"
  [poly]
  (apply
   + (map
      #(apply outside-angle %)
      (polygon-triplets poly))))

(def positive-poly?
  ^{:doc "is the polygon points laid out in a positive radial
  direction (clockwise on screen, anti-clockwise on math)?"}
  (comp pos? total-outside-angle))

(def negative-poly? (comp not positive-poly?))

(defn triangulate
  ([poly]
   (triangulate poly []))
  ([poly tris]
   (if (< (count poly) 3)
     tris
     (let [corners (polygon-triplets poly)

           ;; assuming negative polygon layout
           angles (map #(+ Math/PI (apply outside-angle %)) corners)

           smallest-index (second (first (sort (map vector angles (range)))))

           ;; new poly without that vertex
           new-poly-head (take smallest-index poly)
           new-poly-tail (drop (inc smallest-index) poly)
           new-poly (concat new-poly-head new-poly-tail)

           ;; triangle to add
           new-tri (nth corners smallest-index)]
       (recur new-poly (conj tris new-tri))))))

(defn- sign [p1 p2 p3]
  (-
   (*
    (- (vec2/get-x p1) (vec2/get-x p3))
    (- (vec2/get-y p2) (vec2/get-y p3)))
   (*
    (- (vec2/get-x p2) (vec2/get-x p3))
    (- (vec2/get-y p1) (vec2/get-y p3)))))

(defn point-in-triangle?
  [[v1 v2 v3] point]
  (let [b1 (neg? (sign point v1 v2))
        b2 (neg? (sign point v2 v3))
        b3 (neg? (sign point v3 v1))]
    (and (= b1 b2) (= b2 b3))))
