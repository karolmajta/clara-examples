(ns clara.examples.queries
  (:require-macros [clara.macros :refer [defquery]])
  (:require [clara.rules.accumulators :as acc]
            [clara.examples.facts :refer [Promotion Discount]]))

(def max-discount
  "Accumulator that returns the highest percentage discount."
  (acc/max :percent :returns-fact true))

(defquery get-promotions
          "Query to find promotions for the purchase."
          []
          [?promotion <- Promotion])


(defquery get-best-discount
          "Query to find the best discount that can be applied"
          []
          [?discount <- max-discount :from [Discount]])
