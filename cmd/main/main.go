package main

import (
	"fmt"
	"log"
	"net/http"
	"context"

	"ac-project/api/internal/config"
	"ac-project/api/internal/http/rest"
	"ac-project/api/cmd/wired"
	"os"

	"github.com/spf13/viper"
	
	firebase "firebase.google.com/go"
	"google.golang.org/api/option"

)

func main() {

	opt := option.WithCredentialsFile("./serviceAccountKey.json")
	app, err := firebase.NewApp(context.Background(), nil, opt)
	if err != nil {
		fmt.Errorf("error initializing app: %v", err)
	}

	fmt.Println(app)

	// set up the HTTP server
	var userService, error = wired.InitalizeUserService()
	if (error != nil) {
		fmt.Println(error)
	}
	router := rest.Handler(userService)
	fmt.Println("The beer server is on tap now: http://localhost:8080")
	log.Fatal(http.ListenAndServe(":8080", router))
}

// main문이 실행되기전에 먼저 실행
func init() {
	profile := initProfile()
	setRuntimeConfig(profile)
}

// PROFILE을 기반으로 config파일을 읽고 전역변수로 언마실링
func setRuntimeConfig(profile string) {
	viper.AddConfigPath("./")
	// 환경변수에서 읽어온 profile이름의 yaml파일을 configPath로 설정합니다.
	viper.SetConfigName(profile)
	viper.SetConfigType("yaml")
	err := viper.ReadInConfig()
	if err != nil {
		panic(err)
	}
	// viper는 읽어온 설정파일의 정보를 가지고있으니, 전역변수에 언마샬링해
	// 애플리케이션의 원하는곳에서 사용하도록 합니다.
	err = viper.Unmarshal(&config.RuntimeConf)
	if err != nil {
		panic(err)
	}
}

// 환경변수는 PROFILE을 확인하기 위해 하나만 설정합니다.
func initProfile() string {
	var profile string
	profile = os.Getenv("GO_PROFILE")
	if len(profile) <= 0 {
		profile = "local"
	}
	fmt.Println("GOLANG_PROFILE: " + profile)
	return profile
}
