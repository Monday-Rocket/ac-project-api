package firebase

import (
	"context"
	"log"

	"firebase.google.com/go/auth"
)

type AuthHandler struct {
	AuthClient *auth.Client
}



func (a AuthHandler) GetUIDByEmail(
	context context.Context,
	authHandler AuthHandler,
	email string,
) string {
	user, err := authHandler.AuthClient.GetUserByEmail(context, email)
	if err != nil {
		log.Fatal(err)
	}
	return user.UID
}
