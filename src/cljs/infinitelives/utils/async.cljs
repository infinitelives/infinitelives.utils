(ns infinitelives.utils.async
  (:require [cljs.core.async :refer [<! >! timeout]])
  (:require-macros [infinitelives.utils.async :refer
                    [continue-while
                     go-while]]))
