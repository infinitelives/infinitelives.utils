(ns infinitelives.utils.string
  (:require [clojure.string :as s])
  )

(defn ends-with? [str end]
  (let [len (.-length str)
        end-len (.-length end)]
    (not= -1 (.indexOf str end (- len end-len)))))

(defn starts-with? [str start]
  (= 0 (.indexOf str start)))

(defn url-remove-params [url]
  (-> url
      (.split "?")
      first))

(defn url-keyword [url]
  (-> url
      url-remove-params
      (.split "/")
      last
      (.split ".")
      first
      keyword))

(defn get-extension [path]
  (s/lower-case (last (.split path "."))))

(defn get-url-extenstion [url]
  (-> url
      url-remove-params
      get-extension))
