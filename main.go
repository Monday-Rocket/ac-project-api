package main

import(
	"log"
	"net/http"
)

func main() {
	http.HandleFunc("/hello", func(rw http.ResponseWriter, r *http.Request) {
		rw.Write([]byte("Hello"))
	})

	log.Fatal(http.ListenAndServe(":5100", nil))
}