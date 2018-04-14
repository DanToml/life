(ns life.collectors-test
  (:require [clojure.test :refer :all]
            [clojure.tools.logging :as log]
            [bond.james :as bond]
            [life.config :as cfg]
            [life.collectors :as collectors]
            [life.collectors.swarm :as c.swarm]))

(deftest collection-enabled-works
  (testing "when collection-disabled is set to true"
    (cfg/with-config {:collection-disabled true}
      (is (= false (collectors/collection-enabled?)))))
  (testing "when collection-disabled is set to false"
    (cfg/with-config {:collection-disabled false}
      (is (= true (collectors/collection-enabled?)))))
  (testing "when collection-disabled is not set"
    (cfg/with-config {}
      (is (= true (collectors/collection-enabled?))))))


(deftest start-works
  (testing "starts nothing when collection is disabled"
    (bond/with-stub [[collectors/collection-enabled? (constantly false)]
                     [log/log* (constantly nil)]]
      (is (= {} (collectors/start)))
      (is (= 1 (-> log/log* bond/calls count)))))
  (testing "attempts to start swarm when collection and swarm are enabled"
    (bond/with-stub [[collectors/collection-enabled? (constantly true)]
                     [c.swarm/enabled? (constantly true)]
                     [c.swarm/start (constantly {:test true})]]
      (is (= {:swarm {:test true}} (collectors/start)))
      (is (= 1 (-> c.swarm/start bond/calls count)))
      (is (= 1 (-> c.swarm/enabled? bond/calls count))))))

(deftest stop-works
  (testing "stops swarm when there is a :swarm key"
    (bond/with-stub [[c.swarm/stop (constantly nil)]]
      (collectors/stop {:swarm {:test true}})
      (is (= 1 (-> c.swarm/stop bond/calls count)))
      (is (= [{:test true}] (-> c.swarm/stop bond/calls first :args))))))
