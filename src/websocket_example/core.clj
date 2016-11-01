(ns websocket-example.core
   (:require [org.httpkit.server :as hkit]))

(defn handler [request]
  (hkit/with-channel request channel
    (swap! clients conj channel)
    (hkit/on-close channel (fn [status] (println "channel closed: " status)))
    (hkit/on-receive channel (fn [data] ;; echo it back
                               (prn "log on server" data)
                          (doseq [aclios @clients] (hkit/send! aclios (str ">>" data)))))))

(defn -main [& args]
  ;(def stopfce (hkit/run-server app {:port 8020}))
  (defonce clients (atom #{})) 
  (def stopfce (hkit/run-server handler {:port 8020}))
  (stopfce))

