(ns simulated_annealing.core
  (:gen-class))

(defn energy_function [x] (Math/pow x 2))

(defn -main
  "comment"
  [& args]
  (println "hello"))