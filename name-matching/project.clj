(defproject name-matching "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.12.0"]
                 [org.clojure/data.csv "1.0.0"]
                 [clojure.java-time "0.3.2"]]
  :main name-matching.core
  :repl-options {:init-ns name-matching.core})
