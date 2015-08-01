(ns
    ^{:doc "Browser events"}
  infinitelives.utils.events
  (:require
   [cljs.core.async :refer [put! chan <! >! alts! timeout close!]])
  (:require-macros
   [cljs.core.async.macros :refer [go]])
  )

;; a dynamic var that passes through ctrl+shift+key events
;; to the root event handler
(defonce ^:dynamic *devtools-passthrough* true)

;;
;; an atom which holds the present state of a key
;; true meaning pressed, and false or nil meaning raised
;;
(def key-state (atom {}))

(defn ascii
  "A clojurescript version of ascii value of. javascript doesn't have
  a char type, but uses a string of length 1 to represent"
  [c]
  (.charCodeAt c 0))

(def
  ^{:doc "A hashmap with a variety of keys (strings, keywords).
mapping to browser key codes. Use the is-pressed? function to test
keys directly.

Keycodes can be any string of length 1 representing a key (lowercase).
Keycodes can also be any of the following keywords

:backspace :tab :enter :shift :control :alt :pause :capslock :esc :space
:pageup :pagedown :end :home :left :up :right :down :insert :delete :f1
:f2 :f3 :f4 :f5 :f6 :f7 :f8 :f9 :f10 :f11 :f12 :numlock :scrolllock :comma
:. :/ :backtick :squareleft :backslash :squareright :quote

or any of the single alphanumeric lowercase characters as keywords

eg.
:w :a :s :d :1 :5 etc."}
  key-codes
  (merge
   {
    ;; lookup by code
    :backspace 8
    :tab 9
    :enter 13
    :shift 16
    :control 17
    :alt 18
    :pause 19
    :capslock 20
    :esc 27
    :space 32
    :pageup 33
    :pagedown 34
    :end 35
    :home 36
    :left 37
    :up 38
    :right 39
    :down 40
    :insert 45
    :delete 46
    :f1 112
    :f2 113
    :f3 114
    :f4 115
    :f5 116
    :f6 117
    :f7 118
    :f8 119
    :f9 120
    :f10 121
    :f11 122
    :f12 123
    :numlock 144
    :scrolllock 145
    :comma 188
    :. 190
    :/ 191
    :backtick 192
    :squareleft 219
    :backslash 220
    :squareright 221
    :quote 222
    }

   ;; lookup by keyword of char
   (for [c "0123456789abcdefghijklmnopqrstuvwxyz"]
     [(keyword (str c)) (- (ascii c) 32)])

   ;; lookup by char
   (for [c "0123456789abcdefghijklmnopqrstuvwxyz,.;'[]-=`/\\"]
     [c (- (ascii c) 32)])))

(defn handle-keydown-event
  "the base event handler for key down events. Takes the keycode
  and sets that key in the key-state dictionary to true"
  [ev]
  (swap! key-state (fn [old] (assoc old (.-keyCode ev) true)))

  ;; if debug, we should passthrough ctrl-shift keys for dev tools
  (if (and *devtools-passthrough* (.-ctrlKey ev) (.-shiftKey ev))
    ;; pass through keypress
    false

    ;; else prevent event propagation (cursor keys scroll body on mozilla)
    (.preventDefault ev)))

(defn handle-keyup-event
  "the basic event handler for key up events. Takes the keycode
  and removes it as a key from the key-state dictionary"
  [ev]
  (swap! key-state (fn [old] (dissoc old (.-keyCode ev))))

  ;; stop event propagation on mozilla
  (.preventDefault ev)

  true)

(defn install-key-handler!
  "install the keyup and keydown event handlers"
  []
  (.addEventListener js/window "keydown" handle-keydown-event)
  (.addEventListener js/window "keyup" handle-keyup-event))

(defn is-pressed?
  "returns true if the key is pressently down. code is a keyword,
  or a string of length 1. Examples:

  ```
  ;; test if keys are down by keyword
  (is-pressed? :backspace)
  (is-pressed? :f10)
  (is-pressed? :left)
  (is-pressed? :space)
  (is-pressed? :w)
  (is-pressed? :a)

  ;; test if keys are down by string
  (is-pressed? \" \")
  (is-pressed? \"w\")
  (is-pressed? \"a\")
  (is-pressed? \"d\")
  (is-pressed? \"s\")
  ```

  See key-codes for a list of keyword keys.
"
  [code]
  (@key-state (key-codes code)))
