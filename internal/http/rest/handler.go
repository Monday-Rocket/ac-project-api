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

type UpdateUserRequest struct {
	Nickname   string
	JobGroupId uint `json:"job_group_id"`
}

func updateUser(s user.Service) func(w http.ResponseWriter, r *http.Request, _ httprouter.Params) {
	return func(w http.ResponseWriter, r *http.Request, _ httprouter.Params) {
		var jwtToken = r.Header["X-Auth-Token"][0]
		var request UpdateUserRequest

		// Try to decode the request body into the struct. If there is an error,
		// respond to the client with the error message and a 400 status code.
		err := json.NewDecoder(r.Body).Decode(&request)
		if err != nil {
			http.Error(w, err.Error(), http.StatusBadRequest)
			return
		}

		var res = s.UpdateUser(jwtToken, request.Nickname, request.JobGroupId)
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(res)
	}
}

// func getJobGroups(s user.Service) func(w http.ResponseWriter, r *http.Request, _ httprouter.Params) {
// 	return func(w http.ResponseWriter, r *http.Request, _ httprouter.Params) {
// 		var
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
