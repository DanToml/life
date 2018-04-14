(ns life.collectors
  (:require [clojure.tools.logging :as log]
            [mount.core :as mount]
            [life.collectors.swarm :as c.swarm]
            [life.config :as cfg]))

(defn collection-enabled?
  "This function exists to enable different execution modes of the life application.
   e.g for running collections in a different process to the web application, or
       disabling collection in development."
  []
  (not (or (cfg/get-in :collection-disabled) false)))

(defn start
  []
  (if (collection-enabled?)
    (do
     (log/info "Starting collectors")
     (merge
      (when (c.swarm/enabled?)
        {:swarm (c.swarm/start)})))
    (do
     (log/info "Collection is disabled")
     {})))

(defn stop
  [collectors]
  (log/info "Shutting down collectors")
  (when-let [swarm (:swarm collectors)]
    (c.swarm/stop swarm)))

(mount/defstate collectors
  :start (start)
  :stop (stop collectors))

