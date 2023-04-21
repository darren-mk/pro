(defproject tetris-engine-clj "0.1.0-SNAPSHOT"
  :description "Coding test assignment by Darren Kim for DRW"
  :dependencies [[org.clojure/clojure "1.11.1"]]
  :main tetris-engine-clj.core
  :aot [tetris-engine-clj.core]
  :repl-options {:init-ns tetris-engine-clj.core}
  :profiles {:uberjar {:aot :all}})
