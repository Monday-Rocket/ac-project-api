package wired

import (
	"context"
	"log"

	firebase "firebase.google.com/go"
	"firebase.google.com/go/auth"
	"github.com/google/wire"
)

type APIs struct {
	FirebaseAuthClient *auth.Client
}

var APIsSet = wire.NewSet(
	newFirebaseClient,
	wire.Struct(new(APIs), "*"),
)

func newFirebaseClient() *auth.Client {
	ctx := context.Background()
	app, err := firebase.NewApp(ctx, nil)
	if err != nil {
		log.Fatalf("error initializing app: %v\n", err)
	}

	authClient, err := app.Auth(ctx)

	if err != nil {
		log.Fatalf("error initializing authClient: %v\n", err)
	}

	return authClient
}
