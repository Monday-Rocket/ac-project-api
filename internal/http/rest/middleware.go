package rest

import (
	"fmt"
	"net/http"
	"context"
	"encoding/json"

	"ac-project/api/internal/storage/firebase"
)

type AuthMiddleware struct {
	AuthHandler *firebase.AuthHandler
}

func (a AuthMiddleware) AuthUser(handler http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		fmt.Println("middleware-start")
		ctx := r.Context()

		if (len(r.Header["X-Auth-Token"]) == 0) {
			var res = ErrorResponse{Status: 1000, Error: Error{Message: "회원 인증에 실패했습니다."}}
			w.Header().Set("Content-Type", "application/json")
			json.NewEncoder(w).Encode(res)
			return;
		}
		jwtToken := r.Header["X-Auth-Token"][0]
		verified, err := a.AuthHandler.VerifyToken(ctx, jwtToken)
		if (err != nil) {
			var res = ErrorResponse{Status: 1000, Error: Error{Message: "회원 인증에 실패했습니다."}}
			w.Header().Set("Content-Type", "application/json")
			json.NewEncoder(w).Encode(res)
			return;
		}
		fmt.Println("middleware-token: " + jwtToken)
		ctxWithValue := context.WithValue(ctx, "token", verified)
		r = r.WithContext(ctxWithValue)

		handler.ServeHTTP(w, r)
	})
}
