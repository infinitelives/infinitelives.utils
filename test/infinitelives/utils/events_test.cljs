(ns infinitelives.utils.events-test
  (:require [cljs.test :refer-macros [deftest is]]
            [infinitelives.utils.events :as events]))

(deftest ascii
  (is (= 10 (events/ascii "\n")))
  (is (= 13 (events/ascii "\r")))
  (is (= 32 (events/ascii " ")))
  (is (= 48 (events/ascii "0")))
  (is (= 57 (events/ascii "9")))
  (is (= 65 (events/ascii "A")))
  (is (= 90 (events/ascii "Z")))
  (is (= 97 (events/ascii "a")))
  (is (= 122 (events/ascii "z"))))

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
