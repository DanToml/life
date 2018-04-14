(ns life.core
  (:require [clojure.tools.logging :as log]
            [mount.core :as mount]
            life.collectors)
  (:gen-class))

(defn -main
  [& args]
  (log/info "Starting life...")
  (mount/start))
