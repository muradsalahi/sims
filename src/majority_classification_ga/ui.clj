(ns sims.ui
  (:import (javax.swing JApplet JFrame JPanel)
           (java.awt Graphics Color Dimension)))

(defn make-applet ^JApplet [panel]
  (doto (new JApplet)
    (.setContentPane panel)
    (.setVisible true)))

(defn make-panel [width height on-render]
  (doto (proxy [JPanel] []
          (paint [graphics] (render graphics width height on-render)))
    (.setPreferredSize (new Dimension width height))))

(defn make-frame ^JFrame [^JApplet applet ^String name]
  (doto (JFrame. name)
    (.add (.getContentPane applet))
    (.pack)
    (.setLocationByPlatform true)
    (.setDefaultCloseOperation JFrame/EXIT_ON_CLOSE)
    (.setVisible true)))