package rest

import (
	"encoding/json"
	"net/http"
	"strings"

	"ac-project/api/internal/service/user"

	"github.com/julienschmidt/httprouter"
)

func Handler(a user.Service) http.Handler {
	router := httprouter.New()

	router.POST("/users", createUser(a))
	router.PATCH("/users", updateUser(a))
	router.GET("/users/:id", getUser(a))

	router.GET("/job-groups", getJobGroups(a))
	// router.POST("/topics", getTopics(a))

	return router
}

type DefaultResponse struct {
	Status int         `json:"status"`
	Data   interface{} `json:"data"`
}

func createUser(s user.Service) func(w http.ResponseWriter, r *http.Request, _ httprouter.Params) {
	return func(w http.ResponseWriter, r *http.Request, _ httprouter.Params) {
		var jwtToken = r.Header["X-Auth-Token"][0]

		var res = DefaultResponse{Status: 0, Data: s.AddUser(jwtToken)}
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

		var res = DefaultResponse{Status: 0, Data: s.UpdateUser(jwtToken, request.Nickname, request.JobGroupId)}
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(res)
	}
}

func getJobGroups(s user.Service) func(w http.ResponseWriter, r *http.Request, _ httprouter.Params) {
	return func(w http.ResponseWriter, r *http.Request, _ httprouter.Params) {
		var res = DefaultResponse{Status: 0, Data: s.FindAllJobGroup()}
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(res)
	}
}

func getUser(s user.Service) func(w http.ResponseWriter, r *http.Request, _ httprouter.Params) {
	return func(w http.ResponseWriter, r *http.Request, _ httprouter.Params) {
		id := strings.TrimPrefix(r.URL.Path, "/users/")
		var res = DefaultResponse{Status: 0, Data: s.FindUserById(id)}
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(res)
	}
}
