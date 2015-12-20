(ns infinitelives.utils.file
  (:require [clojure.string]))

; convert resources list to urls list
(defn resources-to-urls [file-list]
  (into {} (for [[k v] (seq file-list)] [k (clojure.string/replace v "resources/public/" "")])))

