(ns chatgpt_sa.core
  (:gen-class))

(defn euclidean-distance [[x1 y1] [x2 y2]]
  (Math/sqrt (+ (Math/pow (- x2 x1) 2) (Math/pow (- y2 y1) 2))))

(defn total-distance [path cities]
  (->> (map #(get cities %) path)
       (partition 2 1)
       (map #(apply euclidean-distance %))
       (reduce +)))

(defn random-swap [path]
  (let [idx1 (rand-int (count path))
        idx2 (rand-int (count path))]
    (assoc path idx1 (path idx2) idx2 (path idx1))))

(defn acceptance-probability [energy new-energy temp]
  (if (< new-energy energy)
    1.0
    (Math/exp (/ (- energy new-energy) temp))))

(defn simulated-annealing [cities initial-path initial-temp cooling-rate num-iterations]
  (loop [current-path initial-path
         best-path initial-path
         temp initial-temp]
    (if (<= temp 1)
      best-path
      (let [new-path (random-swap current-path)
            current-energy (total-distance current-path cities)
            new-energy (total-distance new-path cities)]
        (if (< new-energy current-energy)
          (recur new-path new-path (* temp cooling-rate))
          (if (< (rand) (acceptance-probability current-energy new-energy temp))
            (recur new-path best-path (* temp cooling-rate))
            (recur current-path best-path (* temp cooling-rate))))))))

(def cities {0 [0 0] 1 [1 1] 2 [2 2] 3 [3 3] 4 [4 4]})
(def initial-path (shuffle (keys cities)))
(def initial-temp 10000.0)
(def cooling-rate 0.995)
(def num-iterations 10000)

(defn -main
  [& args]
  (def optimized-path (simulated-annealing cities initial-path initial-temp cooling-rate num-iterations))
  (println "Initial path:" initial-path "Distance:" (total-distance initial-path cities))
  (println "Optimized path:" optimized-path "Distance:" (total-distance optimized-path cities)))