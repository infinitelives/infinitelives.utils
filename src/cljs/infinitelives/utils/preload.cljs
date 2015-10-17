(ns infinitelives.utils.preload
  (:require [cljs.core.async :refer [put! chan <! close!]]))

(defn make-image-result-callback [finished !remaining num-urls img]
                                 (fn [ev]
                                   (swap! !remaining dec)
                                   ; returns:
                                   ; progress-radix
                                   ; number-of-urls-requested
                                   ; number-of-urls-remaining
                                   ; js/Image-object
                                   ; js/Event-object
                                   (put! finished [(/ (- num-urls @!remaining) num-urls) num-urls @!remaining img ev])
                                   (when (= @!remaining 0) (close! finished))))

; refactored this out of infinitelives.pixi.resources
(defn load-urls
  "Takes a list of URLs and starts loading each one into a js/Image.
  Returns a chan. Each time a URL finishes (or errors) the result is pushed down the chan.
  Result: [progress-radix number-of-urls number-of-urls-remaining image-object event-object]
  Check image.src for the URL of what was loaded.
  Check event.type for 'load' or 'error' or 'abort'."
  [urls]
  (let [finished (chan)                         ;; make our channel to push results down
        num-urls (count urls)                   ;; how many urls there are at start
        !remaining (atom num-urls)]               ;; how many urls are left to load
    ;; start loading all the urls
    (doall 
      (map (fn [src]
             (let [i (js/Image.)]
               (set! (.-onload i) (make-image-result-callback finished !remaining num-urls i))
               (set! (.-onerror i) (make-image-result-callback finished !remaining num-urls i))
               (set! (.-onabort i) (make-image-result-callback finished !remaining num-urls i))
               (set! (.-src i) src)
               i))
           urls))
    finished))

(comment ; how to use the code above to load a bunch of URLs, including bail-on-error-condition
  (let [c (load-urls ["http://1.bp.blogspot.com/-fmKkNLWpV5I/UZiu-4OR7fI/AAAAAAAAALw/cb-ijoz6Mkw/s400/240px-Sonic1.png"
                      "https://thiswaytotheegress.files.wordpress.com/2010/04/gpw-200702-61-nasa-iss007-e-5379-night-terminator-day-moonset-20030511-russian-federation-large.jpg"
                      "http://40.media.tumblr.com/8aa5337ee4a90e635dba4617afb7b71f/tumblr_mnlx4fLprC1r05x4jo1_1280.jpg"
                      "https://upload.wikimedia.org/wikipedia/en/c/ca/Black_Flag_-_Scream_snippet.ogg"
                      "https://upload.wikimedia.org/wikipedia/en/8/81/Megadrive_another_world.png"
                      "http://bogus.site.otherewrerewre.com/"
                      "https://upload.wikimedia.org/wikipedia/en/e/ed/Bubble_bobble.jpg"])]
    (go (loop []
          (let [image-result (<! c)]
            (when (not (nil? image-result))
              (let [[progress num-urls remaining img ev] image-result]
                (if (not (= (.-type ev) "load"))
                  (print "There was an error loading one of the resources")
                  (do
                    (log progress num-urls remaining (.-src img) (.-type ev))
                    (recur)))))))
        (print "Finished loading images."))))

