package main

import (
	"fmt"

	"ac-project/api/internal/config"
	"ac-project/api/internal/http/rest"
	"os"

	"github.com/spf13/viper"
	// "time"
)

func main() {
	// ctx := context.Background()
	// app, err := firebase.NewApp(ctx, nil)
	// if err != nil {
	// 	log.Fatalf("error initializing app: %v\n", err)
	// }
	// authHandler := wired.InitializeAuthHandler(ctx)
	// authHandler.Start()token

	// userRepository := wired.InitializeUserRepository(ctx)
	// userRepository.Start()
	// auth, err := app.Auth(ctx)
	// if err != nil {
	// 	log.Fatal(err)
	// }
	// userByEmail, err := auth.GetUserByEmail(ctx, "ts4840644804@gmail.com")
	// if err != nil {
	// 	log.Fatal(err)
	// }

	// fmt.Println(userByEmail.UID)

	// set up the HTTP server
	var userService = InitalizeUserService
	router := rest.Handler(userService)
}

// main문이 실행되기전에 먼저 실행
func init() {
	profile := initProfile()
	setRuntimeConfig(profile)
}

// PROFILE을 기반으로 config파일을 읽고 전역변수로 언마실링
func setRuntimeConfig(profile string) {
	viper.AddConfigPath("../")
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
