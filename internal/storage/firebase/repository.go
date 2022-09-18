package firebase

import (
	"context"
	"fmt"
	"log"

	firebase "firebase.google.com/go"
)

func main() {
	ctx := context.Background()
	app, err := firebase.NewApp(ctx, nil)
	if err != nil {
		log.Fatalf("error initializing app: %v\n", err)
	}
	auth, err := app.Auth(ctx)
	if err != nil {
		log.Fatal(err)
	}
	user, err := auth.GetUserByEmail(ctx, "ts4840644804@gmail.com")
	if err != nil {
		log.Fatal(err)
	}

	fmt.Println(user.UID)
}
