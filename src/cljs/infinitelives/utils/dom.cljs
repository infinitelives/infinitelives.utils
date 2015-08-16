(ns infinitelives.utils.dom)

(defn as-str
  "Coerces strings and keywords to strings, while preserving namespace of
   namespaced keywords"
  [s]
  (if (keyword? s)
    (str (some-> (namespace s) (str "/")) (name s))
    s))

(defn set-style!
  "Set the style of `elem` using key-value pairs:
      (set-style! elem :display \"block\" :color \"red\")"
  [elem & kvs]
  (assert (even? (count kvs)))
  (let [style (.-style elem)]
    (doseq [[k v] (partition 2 kvs)]
      (.setProperty style (as-str k) v))
    elem))

(defn append!
  "Append `child` to `parent`"
  ([parent child]
     (doto parent
       (.appendChild child))))
