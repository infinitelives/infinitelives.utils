(ns infinitelives.utils.resources
  (:require [infinitelives.utils.string :as string]
            [cljs.core.async :refer [chan put! <! timeout close!]]))


(defmulti load
  "a multimethod to load a resource by url and
  return a channel, down which will be sent [url object]
  apon successful load and [url nil] on an error.
  "
  (fn [url] (string/get-extension url)))

(defmulti register!
  "a multimethod that will register a loaded
  resource to the subsystem that handles it.
  It will be passed two arguments, the url
  and the object. register! returns immediately"
  (fn [url obj] (string/get-extension url)))

(defn load-url
  "load-url takes a url and returns a channel
  that after load and registration will "
  [url]
  (go
    (apply register! (<! (load url)))))

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
