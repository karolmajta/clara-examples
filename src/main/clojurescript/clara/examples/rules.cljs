(ns clara.examples.rules
  (:require-macros [clara.macros :refer [defrule defquery defsession]])
  (:require [clara.rules :refer [insert!]]
            [clara.rules.accumulators :as acc]
            [clara.examples.facts :refer [Order
                                          Customer
                                          Purchase
                                          ->Discount
                                          Total ->Total
                                          ->Promotion]]))

(defrule total-purchases
         [?total <- (acc/sum :cost) :from [Purchase]]
         =>
         (insert! (->Total ?total)))

;;; Discounts.
(defrule summer-special
         "Place an order in the summer and get 20% off!"
         [Order (#{:june :july :august} month)]
         =>
         (insert! (->Discount :summer-special 20)))

(defrule vip-discount
         "VIPs get a discount on purchases over $100. Cannot be combined with any other discount."
         [Customer (= status :vip)]
         [Total (> total 100)]
         =>
         (insert! (->Discount :vip 10)))

;;; Promotions.
(defrule free-widget-month
         "All purchases over $200 in August get a free widget."
         [Order (= :august month)]
         [Total (> total 200)]
         =>
         (insert! (->Promotion :free-widget-month :widget)))

(defrule free-lunch-with-gizmo
         "Anyone who purchases a gizmo gets a free lunch."
         [Purchase (= item :gizmo)]
         =>
         (insert! (->Promotion :free-lunch-with-gizmo :lunch)))