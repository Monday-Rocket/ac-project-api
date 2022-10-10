package rest

import (
	"net/http"
	"encoding/json"

	"ac-project/api/internal/service/user"
	"github.com/julienschmidt/httprouter"
)

func Handler(a user.Service) http.Handler {
	router := httprouter.New()

	router.POST("/users", createUser(a))
	router.PATCH("/users", updateUser(a))
	// router.GET("/users", getUser(a))

	// router.POST("/job-groups", getJobGroups(a))
	// router.POST("/topics", getTopics(a))

	return router
}

func createUser(s user.Service) func(w http.ResponseWriter, r *http.Request, _ httprouter.Params) {
	return func(w http.ResponseWriter, r *http.Request, _ httprouter.Params) {
		var jwtToken = r.Header["X-Auth-Token"][0]

		var res = s.AddUser(jwtToken)
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(res)
	}
}

func updateUser(s user.Service) func(w http.ResponseWriter, r *http.Request, _ httprouter.Params) {
	return func(w http.ResponseWriter, r *http.Request, _ httprouter.Params) {
		var jwtToken = r.Header["X-Auth-Token"][0]
		decoder := json.NewDecoder(r.Body)
		var updatingUser updatingUser
		err := decoder.Decode(&updatingUser)
		if err != nil {
			http.Error(w, err.Error(), http.StatusBadRequest)
			return
		}

		var res = s.UpdateUser(jwtToken, updatingUser.nickname, updatingUser.job_group_id)
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(res)
	}
}

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
