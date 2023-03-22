;; (ns sims.core
;;   (:gen-class))

;; (def size 200)

;; (def neighborhood-size 7)

;; (def population-size 100)

;; (defn spawn-lattice
;;   [size]
;;   "Returns a binary 1d array of size `size`."
;;   (apply str (repeatedly size #(rand-int 2))))

;; (defn int-to-binary-string
;;   [string-length int]
;;   (let [fmt (str "%" string-length "s")]
;;     (clojure.string/replace (format fmt (Integer/toBinaryString int)) " " "0")))

;; (defn spawn-policy
;;   [neighborhood-size]
;;   (let [keys (map (partial int-to-binary-string neighborhood-size)
;;                   (range 0 (Math/pow 2 neighborhood-size)))]
;;     (zipmap keys (repeatedly (Math/pow 2 neighborhood-size) #(rand-int 2)))))

;; (defn apply-policy
;;   [policy lattice neighborhood-size]
;;   (let
;;     [padding-size (/ (dec neighborhood-size) 2)
;;      left-pad     (take-last padding-size lattice)
;;      right-pad    (take padding-size lattice)
;;      padded       (concat left-pad lattice right-pad)]
;;     (clojure.string/join "" (map (fn [i]
;;            (let [neighborhood (apply str (take neighborhood-size (drop i padded)))]
;;             ;;  (println "neighborhood:" neighborhood)
;;              (get policy neighborhood)))
;;          (range 0 (- (count padded) (dec neighborhood-size)))))))
  

;; (defn -main
;;   "I don't do a whole lot ... yet."
;;   [& args]
;;   ;; (println (spawn-lattice size))
;;   ;; (println (spawn-policy 3))
;;   (let [lattice (spawn-lattice size)
;;         policy (spawn-policy neighborhood-size)]
;;     (loop [state lattice
;;            t 0]
;;       (println "t:" t)
;;       (if (<= t 200)
;;         (do
;;           (println state)
;;           (recur (apply-policy policy state neighborhood-size) (inc t)))))))
(ns majority_classification_ga.core
  (:gen-class)
  (:import [javax.swing JFrame JPanel Timer]
           [java.awt Color Dimension Graphics]
           [java.awt.event ActionListener]))

(def grid (atom
  [[1 0 1 0]
   [0 1 0 1]
   [1 0 1 0]
   [0 1 0 1]]))

(defn cell-color [cell-value]
  (if (= cell-value 1)
    Color/BLACK
    Color/WHITE))

(defn- draw-grid [g grid cell-size]
  (dotimes [i (count grid)]
    (dotimes [j (count (get grid 0))]
      (.setColor g (cell-color (get-in grid [i j])))
      (.fillRect g
                 (* j cell-size)
                 (* i cell-size)
                 cell-size
                 cell-size))))

(defn create-panel [grid cell-size]
  (proxy [JPanel] []
    (getPreferredSize []
      (Dimension. (* (count (get @grid 0)) cell-size)
                  (* (count @grid) cell-size)))
    (paintComponent [^Graphics g]
      (proxy-super paintComponent g)
      (draw-grid g @grid cell-size))))

(defn create-frame [title grid cell-size]
  (let [frame (JFrame. title)
        panel (create-panel grid cell-size)]
    (.add frame panel)
    (.pack frame)
    (.setDefaultCloseOperation frame JFrame/EXIT_ON_CLOSE)
    frame))

(defn update-grid [grid]
  (mapv (fn [row] (mapv (fn [cell] (if (= cell 1) 0 1)) row)) grid))

(defn start-animation [panel interval]
  (let [timer (Timer. interval
                      (proxy [ActionListener] []
                        (actionPerformed [e]
                          (do
                            (reset! grid (update-grid @grid))
                            (.repaint panel)))))]

    (.start timer)))

(defn -main [& args]
  (let [frame (create-frame "Black and White Grid" grid 50)
        panel (first (.getComponents frame))]
    (.setVisible frame true)
    (start-animation panel 500)))