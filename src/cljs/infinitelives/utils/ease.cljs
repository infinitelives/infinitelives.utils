(ns infinitelives.utils.ease)

;; Easing functions

;; arguments:
;; t - time: starts at 0 and goes to duration d. you repeatedly pass in differnt
;;           values of this, from 0 -> d
;; d - duration: how long the movement goes fo
;; b - initial value
;; c - change (final value - initial value)

;; *-f functions take t in range 0 to 1
;;     and return value v in range 0 to 1

(def linear-f identity)

(defn- quad-in-f [t]
  (* t t))

(defn- quad-out-f
  "v = -(x-1)^2 + 1
  =>   v = -x^2 + 2x
  =>   v = x * (2-x)"

  [t]
  (* t (- 2 t)))

(defn- quad-in-out-f [t]
  (if (< t 0.5)
    (* 2 t t)
    (+ -1 (* t (- 4 (* 2 t))))))

(defn- make-ease-fn [func]
  (fn [t b c d]
    (+ b (* c (func (/ t d))))))

(defn linear [t b c d]
  (+ b (* c (/ t d))))


(def
  ^{:arglist '([t b c d])
    :doc "quadtratic curve, starting easy to start, then
  speeding up to the destination"}

  quad-in (make-ease-fn quad-in-f))

(def
  ^{:arglist '([t b c d])
    :doc "quadratic curve, starting fast, and then slowing
  down to a stop at the destination."}
  quad-out (make-ease-fn quad-out-f))

(def
  ^{:arglist '([t b c d])
    :doc "two quadratic curves: starting easy to start, then
  speeding up to the halfway point, then continuing
  fast through the halfway point and then slowing down
  to stop at the destination"}
  quad-in-out (make-ease-fn quad-in-out-f))
