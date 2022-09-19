package cmd

import (
	"ac-project/api/cmd/wired"
	"context"
	"log"
)

func execute() {
	// context that can be canceled
	ctx := context.Background()
	// initialize the app context
	appCtx, cleanFunc, err := wired.NewContext(
		ctx,
	)
	if err != nil {
		log.Fatal(err)
	}
	defer cleanFunc()

	// start the servers
	log.Println(appCtx)
}
