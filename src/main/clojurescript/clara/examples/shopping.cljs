(ns clara.examples.shopping
  "Shopping example adapted to ClojureScript, updating the DOM to display results.
   Real examples would actually have styling."
  (:require-macros [clara.macros :refer [defsession]]
                   [dommy.core :refer [sel sel1]])
  (:require [dommy.core :as dommy]
            [hipo.core :as hipo]
            [clara.rules.engine]
            [clara.rules :refer [insert fire-rules query]]
            [clara.examples.facts :refer [->Customer
                                          ->Order
                                          ->Discount
                                          ->Purchase]]
            [clara.examples.queries :refer [get-best-discount get-promotions]]
            [clara.examples.rules]))

(defn show-discounts!
  "Print the discounts from the given session."
  [session]

  ;; Destructure and print each discount.
  (doseq [{{reason :reason percent :percent} :?discount} (query session get-best-discount)]
    (dommy/append! (sel1 :#shopping) (hipo/create [:.discount (str percent "% " reason " discount")])) )

  session)

(defn show-promotions!
  "Prints promotions from the given session"
  [session]

  (doseq [{{reason :reason type :type} :?promotion} (query session get-promotions)]
    (dommy/append! (sel1 :#shopping) (hipo/create [:.promotion  (str "Free " type " for promotion " reason)])) )

  session)

(defsession example-session
  'clara.examples.rules
  'clara.examples.queries)

(defn run-examples
  "Function to run the above example."
  []

  (dommy/append! (sel1 :#shopping) (hipo/create [:.overview "VIP Shopping Example:"]))

  ;; prints "10 % :vip discount"
  (-> example-session
      (insert (->Customer :vip)
              (->Order 2013 :march 20)
              (->Purchase 20 :gizmo)
              (->Purchase 120 :widget)) ; Insert some facts.
      (fire-rules)
      (show-discounts!))

  (dommy/append! (sel1 :#shopping) (hipo/create [:.overview  "Summer special and widget promotion example:"]))

  ;; prints: "20 % :summer-special discount"
  ;;         "Free :lunch for promotion :free-lunch-for-gizmo"
  ;;         "Free :widget for promotion :free-widget-month"
  (-> example-session
      (insert (->Customer :vip)
              (->Order 2013 :august 20)
              (->Purchase 20 :gizmo)
              (->Purchase 120 :widget)
              (->Purchase 90 :widget)) ; Insert some facts.
      (fire-rules)
      (show-discounts!)
      (show-promotions!))

  nil)
