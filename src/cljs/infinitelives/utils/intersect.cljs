(ns infinitelives.utils.intersect
  (:require [infinitelives.utils.vec2 :as vec2]))

(defrecord rect [pos half])

(defn clamp [v mn mx]
  (max
   mn
   (min v mx)))

(defn sign [n]
  (cond
    (pos? n) 1
    (neg? n) -1
    :else 0))

(defn aabb-intersect-point [[cx cy hx hy] [x y]]
  (let [dx (- x cx)
        px (- hx (Math/abs dx))]
    (when (pos? px)
      (let [dy (- y cy)
            py (- hy (Math/abs dy))]
        (when (pos? py)
          (if (< px py)
            (let [sx (sign dx)]
              {:delta [(* px sx) 0]
               :normal [sx 0]
               :pos [(+ cx (* sx hx)) y]})
            (let [sy (sign dy)]
              {:delta [0 (* py sy)]
               :normal [0 sy]
               :pos [x (+ cy (* sy hy))]})))))))

(defn intersect-segment [[bx by hx hy] [x y] [dx dy] [px py]]
  ;; when dx==0 or dy==0 we have problems

  (let [scale-x (/ 1.0 dx)
        scale-y (/ 1.0 dy)
        sign-x (Math/sign scale-x)
        sign-y (Math/sign scale-y)

        near-x (* scale-x
                  (- bx
                     (* sign-x (+ hx px))
                     x))
        near-y (* scale-y
                  (- by
                     (* sign-y (+ hy py))
                     y))
        far-x (* scale-x
                 (- (+ bx
                       (* sign-x (+ hx px)))
                    x))
        far-y (* scale-y
                 (- (+ by
                       (* sign-y (+ hy py)))
                    y))
        ]
    ;; not colliding at all, return nil
    (when-not (or (> near-x far-y) (> near-y far-x))
      (let [near (max near-x near-y)
            far (min far-x far-y)]
        (when near
          (js/console.log "near is" near-x near-y near)
          (when-not (or (>= near 1) (<= far 0))
            ;; collision
            (let [time (clamp near 0 1)
                  hdx (* near dx)
                  hdy (* near dy)]
              {:near near
               :far far
               :time time
               :normal (if (< near-y near-x)
                         [(- sign-x) 0]
                         [0 (- sign-y)])
               :delta [hdx hdy]
               :pos [(+ x hdx) (+ y hdy)]})))))))

(defn constrain-rect [rect old-pos new-pos]
  (let [hit (intersect-segment
             rect
             (vec2/as-vector old-pos)
             (vec2/as-vector (vec2/sub new-pos old-pos))
             [0 0])]
    (if hit
      ;; collided with box... reject
      (do
        (js/console.log "hit!" rect old-pos new-pos hit)
        (vec2/from-vector (:pos hit)))

      ;; no collision
      new-pos
      )
    )
  )

(defn constrain-rects [rects old-pos new-pos]
  (if (vec2/equals old-pos new-pos)
    new-pos
    (loop [[r & remain] rects
           new-pos new-pos]
      (if r
        (recur remain (constrain-rect r old-pos new-pos))
        new-pos))))


(defn circle-vector-intersection [[h k r] [x0 y0] [x1 y1]]
  (let [x1-x0 (- x1 x0)
        y1-y0 (- y1 y0)
        x0-h (- x0 h)
        y0-k (- y0 k)
        a (+ (* x1-x0 x1-x0) (* y1-y0 y1-y0))
        b (+ (* 2 x1-x0 x0-h) (* 2 y1-y0 y0-k))
        c (- (+ (* x0-h x0-h) (* y0-k y0-k))
             (* r r))
        desc (- (* b b) (* 4 a c))]
    ;(js/console.log a b c desc)
    (cond
      (neg? desc)
      ;; no answers
      [nil nil]

      (zero? desc)
      ;; one answer (glances circle)
      [(/ (- b) 2 a) nil]

      :default
      (let [sqr (Math/sqrt desc)
            t1 (/ (+ (- b) sqr) 2 a)
            t2 (/ (- (- b) sqr) 2 a)
            ]
        ;; two answers (through circle)
        [(min t1 t2) (max t1 t2)]))))

(defn both-inside?
  "is start and end both inside the circle?"
  [t1 t2]
  (and
   (neg? t1)
   (> t2 1)))

(defn outside->inside? [t1 t2]
  (and
   (< 0 t1 1)
   (> t2 1)))

(defn inside->outside? [t1 t2]
  (and
   (< t1 0)
   (< 0 t2 1)))

(defn outside->outside? [t1 t2]
  (and
   (< 0 t1 1)
   (< 0 t2 2)))

(defn circle-point-extract [[cx cy r] [x y]]
  (let [rx (- x cx)
        ry (- y cy)
        ex (+ x rx)
        ey (+ y ry)
        [t1 t2] (circle-vector-intersection [cx cy r] [x y] [ex ey])]
    (cond
      (< 0 t1) (vec2/lerp (vec2/vec2 x y) (vec2/vec2 ex ey) t1)
      (< 0 t2) (vec2/lerp (vec2/vec2 x y) (vec2/vec2 ex ey) t2)
      :default (vec2/vec2 x y))
    )
  )

(defn constrain-circle [circle old-pos new-pos]
  (if (vec2/equals old-pos new-pos)

    ;; not moving. extract from circle along shortest route
    (circle-point-extract circle (vec2/as-vector new-pos))

    ;; moving. collide with vector
    (let [[t1 t2] (circle-vector-intersection circle (vec2/as-vector old-pos) (vec2/as-vector new-pos))]
      (cond
        (nil? t1) new-pos
        (nil? t2) new-pos
        (both-inside? t1 t2) (circle-point-extract circle (vec2/as-vector old-pos))
        (outside->inside? t1 t2) (vec2/lerp old-pos new-pos t1)
        (inside->outside? t1 t2) new-pos
        (outside->outside? t1 t2) (vec2/lerp old-pos new-pos t1)
        :default new-pos))))

(defn test-constrain-rect []
  (js/console.log
   (constrain-rect [150 150 10 10] (vec2/vec2 152 140) (vec2/vec2 154 140))))

;(test-constrain-rect)
