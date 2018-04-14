(ns life.collectors.swarm-test
  (:require [clojure.test :refer :all]
            [bond.james :as bond]
            [overtone.at-at :as at]
            [life.config :as cfg]
            [life.collectors.swarm :as swarm]))

(deftest config-works
  (testing "pulls config correctly"
    (cfg/with-config {:collectors {:swarm {:access-token "1234"
                                           :period 1000}}}
      (is (= {:access-token "1234" :period 1000} (swarm/config)))))
  (testing "returns nil when there is no config"
    (cfg/with-config {:collectors {}}
      (is (nil? (swarm/config)))))
  (testing "throws when config is invalid"
    (cfg/with-config {:collectors {:swarm {:invalid-key "invalid-value"}}}
      (is (thrown? clojure.lang.ExceptionInfo (swarm/config))))))

(deftest enabled?-works
  (testing "returns false when config is nil"
    (bond/with-stub [[swarm/config (constantly nil)]]
      (is (false? (swarm/enabled?)))))
  (testing "returns true when config is not nil"
    (bond/with-stub [[swarm/config (constantly {:some-map true})]]
      (is (true? (swarm/enabled?)))))
  (testing "does not swallow config parsing exceptions"
    (bond/with-stub [[swarm/config (fn [] (throw (Exception. "Test")))]]
      (is (thrown? java.lang.Exception (swarm/enabled?))))))

(deftest stop-works
  (testing "does not throw when passed nil"
    (is (false? (swarm/stop nil))))
  (testing "calls at/stop with the passed arg"
    (bond/with-stub [[at/stop (constantly nil)]]
      (swarm/stop {:test true})
      (is (= [{:test true}] (-> at/stop bond/calls first :args))))))

(deftest start-works
  (testing "starts a scheduled function with the config period if provided"
    (bond/with-stub [[swarm/config (constantly {:period 1500})]
                     [at/every (constantly nil)]]
      (swarm/start)
      (is (= 1500 (-> at/every bond/calls first :args first))))))
