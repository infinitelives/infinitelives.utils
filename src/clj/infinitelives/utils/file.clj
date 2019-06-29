(ns infinitelives.utils.file
  (:require [clojure.string]))

; load in a set of files from a directory that match a particular extension at compile time
; https://www.refheap.com/18583
(defmacro get-file-list
  [dir ext]
  (->> (for [file (file-seq (clojure.java.io/file dir))
             :when (and (.isFile file) (.endsWith (str file) ext))]
         [(keyword (clojure.string/replace (last (clojure.string/split (str file) #"/")) ext "")) (str file)]) (into {})))
