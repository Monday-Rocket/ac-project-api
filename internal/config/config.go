package config

import (
   "gorm.io/gorm"
	"gorm.io/driver/mysql"
)

var RuntimeConf = RuntimeConfig{}

type RuntimeConfig struct {
   Datasource Datasource `yaml:"datasource"`
   Server     Server     `yaml:"server"`
}

type Datasource struct {
	Db      Db `yaml:"db"`
	Redis Redis `yaml:"redis"`
 }

type Db struct {
   Url      string `yaml:"url"`
   UserName string `yaml:"userName"`
   Password string `yaml:"password"`
}

type Redis struct {
	Url      string `yaml:"url"`
}

type Server struct {
   Port int `yaml:"port"`
}

func ConnectDb() *gorm.DB {
	DatasourceUrl := RuntimeConf.Datasource.Db.Url
	UserName := RuntimeConf.Datasource.Db.UserName
	Password := RuntimeConf.Datasource.Db.Password
	dsn := UserName + ":" + Password + "@" + DatasourceUrl
	
	db, err := gorm.Open(mysql.Open(dsn), &gorm.Config{})
	if err != nil {
	  panic("Db 연결에 실패하였습니다.")
	}
	return db
}