(ns infinitelives.utils.console)

(defn log [& args] (js/console.log.apply js/console (clj->js (into [] args))))
