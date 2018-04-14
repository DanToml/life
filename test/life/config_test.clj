(ns life.config-test
  (:require [clojure.test :refer :all]
            [life.config :as config]))

(deftest with-config-works
  (config/with-config {:test-random "1234"}
    (is (= "1234" (config/get-in :test-random)))))
