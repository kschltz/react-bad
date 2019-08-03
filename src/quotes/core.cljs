(ns quotes.core
  (:require
    [reagent.core :as r]
    [cljs.core.async :as a :refer [>! <! go go-loop]]
    [cljs-http.client :as h]))


(def quote (r/atom :all))

(go-loop []
         (<! (a/timeout 15000))
         (let [q (<! (h/get "https://breaking-bad-quotes.herokuapp.com/v1/quotes"
                            {:with-credentials? false}))]
           (reset! quote (first (:body q)))
           (recur)))

;; -------------------------
;; Views

(defn quote-comp
  [q]
  (let [qt  @q]
    [:div [:p (:quote qt)]
     [:p "- " (:author qt)]]))


(defn home-page []
  [:div [:h2 "Breaking bad quotes"]
   [quote-comp  quote]])

;; -------------------------
;; Initialize app

(defn mount-root []
  (r/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))
