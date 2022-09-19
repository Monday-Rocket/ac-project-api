package firebase

import (
	"context"
	"log"

	"firebase.google.com/go/auth"
)

type AuthHandler struct {
	authClient *auth.Client
}

func NewAuthHandler(
	authClient *auth.Client,
) AuthHandler {
	return AuthHandler{authClient: authClient}
}

func GetUIDByEmail(
	context context.Context,
	authHandler AuthHandler,
	email string,
) string {
	user, err := authHandler.authClient.GetUserByEmail(context, email)
	if err != nil {
		log.Fatal(err)
	}
	return user.UID
}
