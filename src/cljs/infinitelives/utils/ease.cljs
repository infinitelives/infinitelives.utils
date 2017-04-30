(ns infinitelives.utils.ease)

;; Easing functions

;; arguments:
;; t - time: starts at 0 and goes to duration d. you repeatedly pass in differnt
;;           values of this, from 0 -> d
;; d - duration: how long the movement goes fo
;; b - initial value
;; c - change (final value - initial value)

(defn linear [t b c d]
  (+ b (* c (/ t d))))

(defn quad-in
  "quadtratic curve, starting easy to start, then
  speeding up to the destination"
  [t b c d]
  (let [rt (/ t d)]
    (+ b (* c (* rt rt)))))

(defn quad-out
  "quadratic curve, starting fast, and then slowing
  down to a stop at the destination."
  [t b c d]
  (let [rt (- (/ t d) 1)]
    (+ b (* c (- 1 (* rt rt))))))

(defn quad-in-out
  "two quadratic curves: starting easy to start, then
  speeding up to the halfway point, then continuing
  fast through the halfway point and then slowing down
  to stop at the destination"
  [t b c d]
  (let [hd (/ d 2)
        hc (/ c 2)]
    (if (< t hd)
      (quad-in t b hc hd)
      (quad-out (- t hd) (+ b hc) hc hd))))
