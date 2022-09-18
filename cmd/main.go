package main

import (
	"context"
	"fmt"
	"log"
	"net/http"

	"github.com/gin-gonic/gin"

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

	r := gin.Default()
	r.GET("/ping", func(c *gin.Context) {
		c.JSON(http.StatusOK, gin.H{
			"message": "pong",
		})
	})
	r.Run() // listen and serve on 0.0.0.0:8080 (for windows "localhost:8080")
}
