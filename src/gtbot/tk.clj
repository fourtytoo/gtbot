(ns gtbot.tk
  (:require [org.httpkit.client :as http]))

(def tkk-url
  "http://translate.google.com/")

(def tkk-regex
  #"TKK=eval\('\(\(function\(\)\{var\s+a\\x3d(-?[0-9]+);var\s+b\\x3d(-?[0-9]+);return\s+([0-9]+)")

(defn- search-tkk
  "Search the TKK stuff in string S."
  [s]
  (let [[match? & vals] (re-find tkk-regex s)]
    (when match?
      (map #(-> % BigInteger. unchecked-int) vals))))

(defn- get-tkk-values []
  (-> (http/get tkk-url) deref :body search-tkk))

(defn- get-b-d1
  "Return b and d1 for `gen-tk'."
  []
  (let [[x y z] (get-tkk-values)]
    [z (unchecked-int (+ x y))]))

(defn- unsigned-bit-shift-right-int [x n]
  (clojure.lang.Numbers/unsignedShiftRightInt x n))

(defn- gen-rl [a b]
  (reduce (fn [a [accumulate shift x]]
            (->> (if (>= x 97) 87 48)
                 (- x)
                 (shift a)
                 (accumulate a)))
          a b))

(defn- add-int [x y]
  (unchecked-int (+ x y)))

(defn- unsigned-byte [x]
  (bit-and 0xFF x))

(defn- string->UTF8 [s]
  (map unsigned-byte (.getBytes s "UTF-8")))

(defn gen-tk [text & [b-d1]]
  (let [b-d1 (or b-d1 (get-b-d1))
        [b d1] b-d1
        sum add-int
        xor bit-xor
        lsh (comp unchecked-int bit-shift-left)
        rsh unsigned-bit-shift-right-int
        ub [[sum lsh 51]
            [xor rsh 98]
            [sum lsh 102]]
        vb [[sum lsh 97]
            [xor rsh 54]]
        abs31 (fn [x]
                (if (neg? x)
                  (bit-or (bit-and 0x7FFFFFFF x) 0x80000000)
                  x))
        a (-> (reduce (fn [a e]
                        (gen-rl (+ a e) vb))
                      b
                      (string->UTF8 text))
              (gen-rl ub)
              (bit-xor d1)
              abs31
              (rem 1e6)
              unchecked-int)]
    (let [a' (int (rem a 1e6))]
      (str a' "." (bit-xor a' b)))))
