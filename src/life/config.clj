(ns life.config
  (:refer-clojure :exclude [get-in])
  (:require [clojure.java.io :as io]
            [cprop.source :as cp.source]
            [cprop.tools :as cp.tools]))

(def ^:dynamic ^:private *config*
  nil)

(defmacro with-config
  [config-map & body]
  `(binding [*config* ~config-map]
     ~@body))

(defn- load-config-from-file
  [path-in-resources]
  (some-> path-in-resources io/resource cp.source/from-stream))

(def ^:private load-config
  (memoize
   (fn []
     (cp.tools/merge-maps (load-config-from-file "config.edn")
                          (cp.source/from-env)))))

(defn- config
  []
  (if *config*
    *config*
    (load-config)))

(defn get-in
  [& args]
  (clojure.core/get-in (config) args))
