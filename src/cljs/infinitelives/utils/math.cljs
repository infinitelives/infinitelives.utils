(ns infinitelives.utils.math)

(defn rand-between
  "return a random integer between low and high inclusive"
  [low high]
  (let [size (Math/abs (- high low))]
    (int (+ low (* size (rand))))))

(defn limit
  "limit the number num between low and high. If its less than low, return low
  if its more than high, return hi. else return num."
  [num low high]
  (cond
   (< num low) low
   (> num high) high
   :default num))
