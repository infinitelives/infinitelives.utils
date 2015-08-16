(ns infinitelives.utils.console)

;;
;; No host methods (static or not) can be used with partial as-is because partial only works with Clojure functions.
;; What you really want is to use clojure.contrib.import-static to easily wrap those static methods:
;;
;; - Chas Emerick
;;
(defn log
  ([a1]
   (.log js/console a1))
  ([a1 a2]
   (.log js/console a1 a2))
  ([a1 a2 a3]
   (.log js/console a1 a2 a3))
  ([a1 a2 a3 a4]
   (.log js/console a1 a2 a3 a4))
  ([a1 a2 a3 a4 a5]
   (.log js/console a1 a2 a3 a4 a5))
  ([a1 a2 a3 a4 a5 a6]
   (.log js/console a1 a2 a3 a4 a5 a6))
  ([a1 a2 a3 a4 a5 a6 a7]
   (.log js/console a1 a2 a3 a4 a5 a6 a7))
  ([a1 a2 a3 a4 a5 a6 a7 a8]
   (.log js/console a1 a2 a3 a4 a5 a6 a7 a8)))
