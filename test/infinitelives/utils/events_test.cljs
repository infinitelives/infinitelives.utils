(ns infinitelives.utils.events-test
  (:require [cljs.test :refer-macros [deftest is are]]
            [infinitelives.utils.events :as events]))

(deftest ascii
  (are [x y] (= x y)
       10 (events/ascii "\n")
       13 (events/ascii "\r")
       32 (events/ascii " ")
       48 (events/ascii "0")
       57 (events/ascii "9")
       65 (events/ascii "A")
       90 (events/ascii "Z")
       97 (events/ascii "a")
       122 (events/ascii "z")))

;; just test the keys are there. we assume the codes are correct.
(deftest key-codes
  (let [ks (set (keys events/key-codes))]
    (doseq [c "0123456789abcdefghijklmnopqrstuvwxyz,.;'[]-=`/\\"]
      (is (contains? ks c)))
    (doseq [c "0123456789abcdefghijklmnopqrstuvwxyz"]
      (is (contains? ks (keyword c))))
    (doseq [k [:backspace :tab :enter :shift :control :alt :pause :capslock :esc :space
               :pageup :pagedown :end :home :left :up :right :down :insert :delete :f1
               :f2 :f3 :f4 :f5 :f6 :f7 :f8 :f9 :f10 :f11 :f12 :numlock :scrolllock :comma
               :. :/ :backtick :squareleft :backslash :squareright :quote]]
      (is (contains? ks k)))))

(defn- make-event [t code]
  (let [ev (.createEvent js/document "Event")]
    (.initEvent ev t true true)
    (set! (.-keyCode ev) code)
    ev))

(defn- keydown
  [code]
  (.dispatchEvent js/document (make-event "keydown" code)))

(defn- keyup [code]

  (.dispatchEvent js/document (make-event "keyup" code)))

(deftest is-pressed?
  (events/install-key-handler!)
  (is (not (events/is-pressed? :backspace)))
  (keydown 8)
  (is (events/is-pressed? :backspace))
  (keyup 8)
  (is (not (events/is-pressed? :backspace)))
  (keydown 38) ;; up
  (keydown 65) ;; a
  (keydown 16) ;; shift
  (is (and
       (events/is-pressed? :shift)
       (events/is-pressed? "a")
       (events/is-pressed? :up)))
  (keyup 38)
  (keyup 65)
  (keyup 16)
  (is (not (or
            (events/is-pressed? :shift)
            (events/is-pressed? "a")
            (events/is-pressed? :up)))))
