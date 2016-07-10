(ns infinitelives.utils.resources
  (:require [infinitelives.utils.string :as string]
            [infinitelives.utils.console :refer [log]]
            [cljs.core.async :refer [chan put! <! timeout close! take!]])
  (:require-macros [cljs.core.async.macros :refer [go]]))


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
  "loads each url in the passed in list. Updates the progress as it goes by
  calling a progress callback function."
  [urls progress-fn]
  (let [finished (chan) ;; make our channel to recieve finished loads
        num-urls (count urls) ;; how many urls
        ]
    (doall ;; start loading all the urls
     (map #(take!
            (load %)
            (fn [res] (put! finished res))) urls))
    (go
      (loop [i 1 coll {}]
        (let [
              ;; a new url has finished loading
              [url obj] (<! finished)

              ;; setup a pixi texture keyed by the tail of its filename
              new-coll (assoc coll url (register! url obj))]

          ;; callback progress-fn
          (when progress-fn (progress-fn i num-urls url obj))

          ;; more images?
          (if (< i num-urls)
            (recur (inc i) new-coll)
            new-coll))))))
