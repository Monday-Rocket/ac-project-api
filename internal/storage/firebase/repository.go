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
	email string,
) string {
	user, err := a.AuthClient.GetUserByEmail(context, email)
	if err != nil {
		log.Fatal(err)
	}
	return user.UID
}

func (a AuthHandler) VerifyToken(token string) (*auth.Token, error) {
	verified, err := a.AuthClient.VerifyIDToken(nil, token)
	return verified, err

}