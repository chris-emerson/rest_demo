(ns rest-demo.core
  (:require [org.httpkit.server :as server]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer :all]
            [clojure.pprint :as pp]
            [clojure.string :as str]
            [clojure.data.json :as json])
  (:gen-class))

; (defn -main
;   "I don't do a whole lot ... yet."
;   [& args]
;   (println "Hello, World!")
;

; Simple Body Page
(defn simple-body-page [req] ;(3)
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "Hello World"})
;
; request-example
(defn request-example [req]
     {:status  200
      :headers {"Content-Type" "text/html"}
      :body    (->
                (pp/pprint req)
                (str "Request Object: " req))})

; Hello-name handler
(defn hello-name [req]
     {:status  200
      :headers {"Content-Type" "text/html"}
      :body    (->
                (pp/pprint req)
                (str "Hello " (:name (:params req))))})


; my people-collection mutable collection vector
(def people-collection (atom []))

;Collection Helper functions to add a new person
(defn addperson [firstname surname]
  (swap! people-collection conj {:firstname (str/capitalize firstname)
                                     :surname (str/capitalize surname)}))

; Example JSON objects
(addperson "Functional" "Human")
(addperson "Micky" "Mouse")






;(concat people-collection (addperson "Functional" "Human"))

; Return List of People
(defn people-handler [req]
        {:status  200
         :headers {"Content-Type" "text/json"}
         :body    (str (json/write-str @people-collection))})

; Partial function to get the parameter specified by pname
(defn getparameter [req pname] (get (:params req) pname))

; Add a new person into the people-collection
(defn addperson-handler [req]
        {:status  200
         :headers {"Content-Type" "text/json"}
         :body    (-> (let [p (partial getparameter req)]
                        (str (json/write-str (addperson (p :firstname) (p :surname))))))})

; Our main routes
(defroutes app-routes
  (GET "/" [] simple-body-page)
  (GET "/request" [] request-example)
  (GET "/hello" [] hello-name)
  (GET "/people" [] people-handler)
  (GET "/people/add" [] addperson-handler)
  (route/not-found "Error, page not found!"))



  ; (GET "/pageb" [] pageb)
  ; (wrap-defaults hello-world-str site-defaults)
  ; (GET "/json" [] mail-handler)
;  (POST "/postoffice" [] mail-handler)
;  (ANY "/anything-goes" [] general-handler)



;
(defn -main
  "This is our main entry point"
  [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "3000"))]
    ; Run the server with Ring.defaults middleware
    (server/run-server (wrap-defaults #'app-routes site-defaults) {:port port})
    ; Run the server without ring defaults
    ;(server/run-server #'app-routes {:port port})
    (println (str "Running webserver at http:/127.0.0.1:" port "/"))))
