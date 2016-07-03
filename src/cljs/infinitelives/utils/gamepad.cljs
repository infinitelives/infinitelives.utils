(ns infinitelives.utils.gamepad
  (:require [infinitelives.utils.events :as events]))

(defn axis
  ([gamepad gamepad-num axis-num]
   (aget (.-axes gamepad) axis-num))
  ([gamepad-num axis-num]
   (axis (aget (events/get-gamepads) gamepad-num) gamepad-num axis-num))
  ([axis-num]
   (axis 0 axis-num)))

(def stick->axis
  {[:left :x] 0
   [:left :y] 1
   [:right :x] 2
   [:right :y] 3})

(defn stick
  [gamepad-num [stick-name axis-name]]
  (axis gamepad-num (stick->axis [stick-name axis-name])))

(def ->buttons
  {
   :a 0 :A 0
   :b 1 :B 1
   :x 2 :X 2
   :y 3 :Y 3
   :left-bumper 4 :bumper-left 4 :lb 4 :bl 4
   :right-bumper 5 :bumper-right 5 :rb 5 :br 5
   :left-trigger 6 :trigger-left 6 :lt 6 :tl 6
   :right-trigger 7 :trigger-right 7 :rt 7 :tr 7
   :back 8
   :start 9
   :dpad-up 12 :up-dpad 12 :dup 12 :up 12
   :dpad-down 13 :down-dpad 13 :ddown 13 :down 13
   :dpad-left 14 :left-dpad 14 :dleft 14 :left 14
   :dpad-right 15 :right-dpad 15 :dright 15 :right 15
   :xbox 16
   })

(defn button
  ([gamepad gamepad-num button-num]
   (aget (.-buttons gamepad)
         (get ->buttons button-num button-num)))
  ([gamepad-num button-num]
   (button (aget (events/get-gamepads) gamepad-num) gamepad-num button-num))
  ([button-num]
   (button 0 button-num)))

(defn pressed? [val]
  (> val 0.1))

(def button-pressed? (comp pressed? button))
