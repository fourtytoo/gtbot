(ns gtbot.core
  (:require [gtbot.tk :refer [gen-tk]]
            [org.httpkit.client :as http]
            [clojure.data.json :as json]
            [clojure.string :as string]))

(def base-url
  "http://translate.google.com/translate_a/single")

(defn- make-query-params [source-language target-language text]
  {"client"  "t"
   "ie"      "UTF-8"
   "oe"      "UTF-8"
   "sl"      source-language
   "tl"      target-language
   "q"       text
   "dt"      ["bd" "ex" "ld" "md" "qc" "rw" "rm" "ss" "t" "at"]
   "pc"      "1"
   "otf"     "1"
   "srcrom"  "1"
   "ssel"    "0"
   "tsel"    "0"
   "tk"      (gen-tk text)})

(defn- remove-newlines [text]
  (string/replace text #"\n+" " "))

(defn- normalise-answer [json]
  (let [main (first json)]
    {:detailed (second json)
     :detailed-definition (nth json 12 nil)
     :suggestion (nth json 7 nil)
     :phonetic (->> main
                    (map #(nth % 2 ""))
                    string/join)
     :translation (->> main
                       (map first)
                       string/join)
     :text-phonetic (->> main
                         (map #(nth % 3 ""))
                         string/join)}))

(defn translate [from to text]
  (->> (remove-newlines text)
       (make-query-params from to)
       (assoc {} :query-params)
       (http/get base-url)
       deref
       :body
       json/read-json
       normalise-answer))
