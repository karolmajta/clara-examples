(ns clara.examples
  (:require [clara.examples.shopping :as shopping]))

;; Run the shopping examples.
(defn ^:export -main
  []
  (enable-console-print!)
  (shopping/run-examples))
