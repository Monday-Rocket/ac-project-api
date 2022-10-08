package rest

import (
	"encoding/json"
	"net/http"

	"ac-project/api/internal/service/user"

	"github.com/julienschmidt/httprouter"
)

func Handler(a user.Service) http.Handler {
	router := httprouter.New()

	router.POST("/users", createUser(a))
	// router.PATCH("/users/:id", updateUser(a))
	// router.GET("/users", getUser(a))

	// router.POST("/job-groups", getJobGroups(a))
	// router.POST("/topics", getTopics(a))

	return router
}

func createUser(s user.Service) func(w http.ResponseWriter, r *http.Request, _ httprouter.Params) {
	return func(w http.ResponseWriter, r *http.Request, _ httprouter.Params) {
		decoder := json.NewDecoder(r.Header)

		var token = r.Header["x-auth-token"]

		user = s.AddUser(token)
		// error handling omitted for simplicity

		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(user)
	}
}

// func updateUser(a user.Service) func(w http.ResponseWriter, r *http.Request, _ httprouter.Params) {
// 	return func(w http.ResponseWriter, r *http.Request, _ httprouter.Params) {
// 		decoder := json.NewDecoder(r.Body)

// 		var newUser a.AddUser
// 		err := decoder.Decode(&newUser)
// 		if err != nil {
// 			http.Error(w, err.Error(), http.StatusBadRequest)
// 			return
// 		}

// 		s.AddBeer(newBeer)
// 		// error handling omitted for simplicity

// 		w.Header().Set("Content-Type", "application/json")
// 		json.NewEncoder(w).Encode("New user added.")
// 	}
// }

// func getUser(a user.Service) func(w http.ResponseWriter, r *http.Request, _ httprouter.Params) {
// 	return func(w http.ResponseWriter, r *http.Request, _ httprouter.Params) {
// 		decoder := json.NewDecoder(r.Body)

// 		var newBeer adding.Beer
// 		err := decoder.Decode(&newBeer)
// 		if err != nil {
// 			http.Error(w, err.Error(), http.StatusBadRequest)
// 			return
// 		}

// 		s.AddBeer(newBeer)
// 		// error handling omitted for simplicity

// 		w.Header().Set("Content-Type", "application/json")
// 		json.NewEncoder(w).Encode("New beer added.")
// 	}
// }
