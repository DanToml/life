(ns life.collectors.swarm
  (:require [clojure.tools.logging :as log]
            [mount.core :as mount]
            [schema.core :as s]
            [overtone.at-at :as at]
            [life.postgres :as pg]
            [life.config :as cfg]))

(s/def Config
  {:access-token s/Str
   (s/optional-key :period) s/Int})

(s/defn ^:always-validate config :- (s/maybe Config)
  "Fetch the user config for the collector. Returns nil if not present or
  raises if invalid."
  []
  (cfg/get-in :collectors :swarm))

(defn enabled? []
  (some? (config)))

(def ^:private default-period
  "Default interval for collecting data from Swarm (15 minutes)"
  (* 15 60 1000))

(defn collect-data* [config db-conn])

(def collect-data (partial collect-data* #'config #'pg/*conn*))

(defn start
  ([]
   (start (at/mk-pool)))
  ([pool]
   (let [config (config)
         scheduling-period (or (:period config) default-period)]
     (at/every scheduling-period #'collect-data pool :desc "Swarm Collector"))))

(defn stop
  [swarm]
  (at/stop swarm))
