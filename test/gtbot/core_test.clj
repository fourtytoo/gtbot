(ns gtbot.core-test
  (:require [clojure.test :refer :all]
            [gtbot.core :refer :all]))

(deftest a-test
  (-> (translate "de" "en" "Kartoffel")
      :translation
      (= "potato")
      is))
