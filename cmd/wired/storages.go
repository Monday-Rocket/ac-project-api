package wired

import (
	"context"
	"log"

	"gorm.io/gorm"
	"gorm.io/driver/mysql"
	"github.com/google/wire"

	"ac-project/api/internal/config"
)

type Storages struct {
	DB *gorm.DB
}

var DbsSet = wire.NewSet(
	ConnectDb,
	wire.Struct(new(Storages), "*"),
)

func ConnectDb() *gorm.DB {
	DatasourceUrl := config.RuntimeConf.Datasource.Db.Url
	UserName := config.RuntimeConf.Datasource.Db.UserName
	Password := config.RuntimeConf.Datasource.Db.Password
	dsn := UserName + ":" + Password + "@" + DatasourceUrl
	
	db, err := gorm.Open(mysql.Open(dsn), &gorm.Config{})
	if err != nil {
	  panic("Db 연결에 실패하였습니다.")
	}
	return db
}