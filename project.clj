(defproject infinitelives.utils "0.1.0-SNAPSHOT"
  :description "Support utilities for cljs games"
  :url "https://github.com/infinitelives"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "0.0-3308"]]

  :plugins [[lein-cljsbuild "1.0.6"]]

  :source-paths ["src/cljs"]
  :test-paths ["test"]

  :cljsbuild
  {
   :builds {:test
            {:source-paths ["src" "test"]
             :compiler {:output-to "resources/test/compiled.js"
                        :optimizations :whitespace
                        :pretty-print true}}}
   :test-commands {"test" ["phantomjs"
                           "resources/test/test.js"
                           "resources/test/test.html"]}})
