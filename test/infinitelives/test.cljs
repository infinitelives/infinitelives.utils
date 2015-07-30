(ns infinitelives.test
  (:require [cljs.test :refer-macros [run-all-tests]]
            [infinitelives.utils.vec2-test]
            ))

(enable-console-print!)

(defn ^:export run
  []
  (run-all-tests #"infinitelives.*-test"))
