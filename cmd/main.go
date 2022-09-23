package main

import (
	"fmt"
	"os"
	"gorm.io/driver/mysql"
	"github.com/spf13/viper"
	"gorm.io/gorm"
	"ac-project/api/internal/config"
)

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
	err = viper.Unmarshal(&configuration.RuntimeConf)
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

func main() {
  DatasourceUrl := configuration.RuntimeConf.Datasource.Db.Url
  UserName := configuration.RuntimeConf.Datasource.Db.UserName
  Password := configuration.RuntimeConf.Datasource.Db.Password
  dsn := UserName + ":" + Password + "@" + DatasourceUrl
  fmt.Println(dsn)
  db, err := gorm.Open(mysql.Open(dsn), &gorm.Config{})
  if err != nil {
    panic("Db 연결에 실패하였습니다.")
  } else {
	fmt.Println("Db 연결에 성공하였습니다.")
	fmt.Println(db)
  }
}