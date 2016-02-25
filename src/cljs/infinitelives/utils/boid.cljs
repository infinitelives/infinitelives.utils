(ns infinitelives.utils.boid
  (:require [infinitelives.utils.vec2 :as vec2]
            [infinitelives.utils.math :as math]
            [infinitelives.utils.console :refer [log]]))

(def example
  {:mass 10.0
   :pos (vec2/vec2 0 0)
   :vel (vec2/vec2 0 0)
   :max-force 1.0
   :max-speed 4.0})

(defn seek [{:keys [mass pos vel max-force max-speed] :as boid} target]
  (let [desired-vel
        (-> pos
            (vec2/direction target)
            (vec2/scale max-speed))
        steering (-> desired-vel
                     (vec2/sub vel)
                     (vec2/truncate max-force))
        accel (vec2/scale steering (/ 1 mass))
        new-vel (-> accel
                    (vec2/add vel)
                    (vec2/truncate max-speed))

        new-pos (vec2/add pos new-vel)]
    (assoc boid
           :pos new-pos
           :vel new-vel)))

(defn flee [{:keys [mass pos vel max-force max-speed] :as boid} target]
  (let [desired-vel
        (-> pos
            (vec2/direction target)
            (vec2/scale (- max-speed)))
        steering (-> desired-vel
                     (vec2/sub vel)
                     (vec2/truncate max-force))
        accel (vec2/scale steering (/ 1 mass))
        new-vel (-> accel
                    (vec2/add vel)
                    (vec2/truncate max-speed))

        new-pos (vec2/add pos new-vel)]
    (assoc boid
           :pos new-pos
           :vel new-vel)))

(defn pursue [{:keys [pos] :as boid} target target-vel]
  (seek boid (->> pos
                 (vec2/direction target)
                 vec2/magnitude
                 (* 4)                  ; 4 = c (T=Dc)
                 (vec2/scale target-vel)
                 (vec2/add target))))

(defn evade [{:keys [pos] :as boid} target target-vel]
  (flee boid (->> pos
                 (vec2/direction target)
                 vec2/magnitude
                 (* 4)                  ; 4 = c (T=Dc)
                 (vec2/scale target-vel)
                 (vec2/add target))))
