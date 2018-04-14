(defproject life
  (format "0.1.%s" (or (System/getenv "CIRCLE_BUILD_NUM")
                       "1-SNAPSHOT"))
  :description "Who knows?"
  :url "http://example.com"
  :license {:name "MIT"
            :url "https://mit-license.org/"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.logging "0.4.0"]
                 [cprop "0.1.10"]
                 [mount "0.1.11"]
                 [overtone/at-at "1.2.0"]
                 [prismatic/schema "1.1.6"]
                 [prismatic/schema-generators "0.1.0"]]
  :main ^:skip-aot life.core
  :target-path "target/%s"
  :test-paths ["test"]
  :resource-paths ["resources"]
  :profiles {:uberjar {:aot :all}
             :dev {:dependencies
                   [[circleci/bond "0.3.0" :exclusions [org.clojure/clojure
                                                        org.clojure/clojurescript]]]}}
  :plugins [[test2junit "1.3.3"]
            [jonase/eastwood "0.2.4"]])
