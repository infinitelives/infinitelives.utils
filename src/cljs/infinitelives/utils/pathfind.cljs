(ns infinitelives.utils.pathfind)

(defrecord state [closed-set open-set came-from g-score f-score])

(def diagonal-cost 14)
(def orthogonal-cost 10)

(defn manhattan [dx dy] (+ (Math/abs dx) (Math/abs dy)))

(defn chebyshev [dx dy] (max (Math/abs dx) (Math/abs dy)))

(defn euclid [dx dy] (.sqrt js/Math (+ (* dx dx) (* dy dy))))

(defn distance-between [func [x1 y1] [x2 y2]]
  (func
   (Math/abs (- x1 x2))
   (Math/abs (- y1 y2))))

(defn state-add-neighbour [{:keys [closed-set open-set came-from]
                            :as  state} current neighbour]
  (assoc state
         :open-set (conj open-set neighbour)
         :came-from (assoc came-from neighbour current)))

(defn state-add-open [state cell]
  (update state :open-set conj cell))

(defn state-open-to-closed [state cell]
  (-> state
      (update :open-set disj cell)
      (update :closed-set conj cell)))

(defn reduce-state-over-neighbours [state current neighbours]
  (reduce
   (fn [acc item] (state-add-neighbour acc current item))
   state
   neighbours))

(defn g-dist [[x1 y1] [x2 y2]]
  (let [dx (Math/abs (- x1 x2))
        dy (Math/abs (- y1 y2))]
    (cond
      (and (= 1 dx) (= 1 dy))
      diagonal-cost

      (or (= 1 dx) (= 1 dy))
      orthogonal-cost)))

(defn calculate-open-fscore [{:keys [f-score g-score open-set] :as state}
                             parent destination]
  (let [to-calc (reduce disj open-set (keys f-score))
        new-g-score (into g-score
                          (for [cell to-calc]
                            [cell (+ (g-dist cell parent)
                                     (get g-score parent))]))
        new-f-score (into f-score
                          (for [cell to-calc]
                            [cell (+ (new-g-score cell)
                                     (* orthogonal-cost (distance-between manhattan cell destination)))]))]
    (assoc state :f-score new-f-score :g-score new-g-score)))

(defn neighbors-for [[x y]]
  (for [dx [-1 0 1] dy [-1 0 1]
        :when (not (and (zero? dx) (zero? dy)))]
    [(+ x dx) (+ y dy)]))

(defn lowest-f-score-open-cell [{:keys [f-score open-set]}]
  (->> f-score
       (sort-by second)
       (filter (fn [[k v]] (open-set k)))
       first
       first))

(defn backtrack [{:keys [came-from]} end start]
  (loop [pos end path '()]
    (if (not= pos start)
      (recur (came-from pos) (conj path pos))
      (conj path start))))

(defn A*-step [state passable? current end corner-cut?]
  (let [
        neighbors (apply disj (into #{} (neighbors-for current)) (:closed-set state))
        [xp yp] current
        passable-neighbors
        (->> neighbors
             (filter passable?)
             (filter (fn [[x y]]
                       (cond
                         corner-cut? true
                         (and (= x (dec xp)) (not (passable? [(dec xp) yp]))) false
                         (and (= x (inc xp)) (not (passable? [(inc xp) yp]))) false
                         (and (= y (dec yp)) (not (passable? [xp (dec yp)]))) false
                         (and (= y (inc yp)) (not (passable? [xp (inc yp)]))) false
                         :default true))))
        state (-> state
                  (reduce-state-over-neighbours current passable-neighbors)
                  (calculate-open-fscore current end)
                  (state-open-to-closed current))
        next-cell (lowest-f-score-open-cell state)]
    [state next-cell]))

(defn A* [passable? start end & arguments]
  (let [args (into #{} arguments)
        corner-cut? (:corner-cut args)
        state (-> (->state #{} #{start}
                              {} {start 0}
                              {start (distance-between manhattan start end)}))]
    (loop [[{:keys [open-set] :as state} next-cell]
           (A*-step state passable? start end corner-cut?)]
      (cond
        (empty? open-set)
        nil

        (not= next-cell end)
        (recur (A*-step state passable? next-cell end corner-cut?))

        ;; backtrack
        :default
        (backtrack state end start)))))
