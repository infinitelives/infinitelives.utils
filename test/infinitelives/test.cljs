(ns infinitelives.test
  (:require [cljs.test :refer-macros [run-all-tests]]
            [infinitelives.utils.vec2-test]
            [infinitelives.utils.events-test]
            [infinitelives.utils.string-test]
            [infinitelives.utils.math-test]
            [infinitelives.utils.spatial-test]
            [infinitelives.utils.geom2-test]
            [infinitelives.utils.pathfind-test]
))

(enable-console-print!)

(defn ^:export run
  []
  (run-all-tests #"infinitelives.*-test"))
