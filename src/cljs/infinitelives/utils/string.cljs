(ns infinitelives.utils.string)

(defn ends-with? [str end]
  (let [len (.-length str)
        end-len (.-length end)
        pos (.indexOf str end)]
    (= (+ pos end-len) len)))

(defn starts-with? [str start]
  (= 0 (indexOf str start)))
