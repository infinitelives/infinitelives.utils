(ns infinitelives.utils.resources
  (:require [infinitelives.utils.string :as string]
            [cljs.core.async :refer [chan put! <! timeout close!]])
  )


(defmulti load
  (fn [url] (string/get-extension url)))

(defmethod load "png" [url]
  (let [c (chan)
        i (js/Image.)]
    (set! (.-onload i) #(put! c [url i]))
    (set! (.-onerror i) #(put! c [url nil]))
    (set! (.-onabort i) #(js/alert "abort"))
    (set! (.-src i) url)
    c))

(defmulti register
  (fn [url obj] (string/get-extension url)))

(defmethod register "png" [url img]
  (register-texture! url img))

(defn load-url
  [url]
  (go
    (apply register (<! (load url)))))

(defn load-urls
  "loads each url in the passed in list. Updates the progress as it goes with
  a percentage. Once complete, displays all the images fullsize."
  [urls progress-fn]
  (let [finished (chan)                         ;; make our channel to
        num-urls (count urls)                   ;; how many urls
        images (doall                           ;; start loading all the urls
                (map #(put! finished (load %)) urls))]
    (go
      (loop [i 1]
        (let [[url obj] (<! finished)] ;; a new image has finished loading
          ;; setup a pixi texture keyed by the tail of its filename
          (register url obj)

          ;; callback progress-fn
          (when progress-fn (progress-fn i num-urls url obj))

          ;; more images?
          (when (< i num-urls)
            (recur (inc i))))))))
