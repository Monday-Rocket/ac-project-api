package mysql

import (
	"time"
	"gorm.io/gorm"
)

type User struct {
  	gorm.Model
	Name         string
	Email        string
	Nickname     string
	Birth        *time.Time
	Sex 		 string
}

