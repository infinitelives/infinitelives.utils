(ns infinitelives.utils.string-test
  (:require [cljs.test :refer-macros [deftest is]]
            [infinitelives.utils.string :as string]))


(deftest ends-with?
  (is (string/ends-with? "test string.ext" ".ext"))
  (is (string/ends-with? "inside.ext.outside.ext" ".ext"))
  (is (not (string/ends-with? "inside.ext.outside.not" ".ext"))))

(deftest starts-with?
  (is (string/starts-with? "test string.ext" "test"))
  (is (string/starts-with? "test string test.ext" "test"))
  (is (not (string/starts-with? "test string.ext" "est"))))
