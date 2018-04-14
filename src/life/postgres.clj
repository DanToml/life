(ns life.postgres
  (:require [mount.core :as mount]
            [schema.core :as s]
            [life.config :as cfg]))

(s/def Config
  {})

(s/defn ^:always-validate config :- Config
  []
  (cfg/get-in :postgres))

(mount/defstate *conn*
  :start (constantly {}))
