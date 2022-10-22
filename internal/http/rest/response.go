package rest

type DefaultResponse struct {
	Status int         `json:"status"`
	Data   interface{} `json:"data"`
}

type ErrorResponse struct {
	Status int         `json:"status"`
	Error  Error 		`json:"error"`
}

type Error struct {
	Message string         `json:"message"`
}