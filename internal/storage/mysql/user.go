package mysql

import (
	"time"
	"gorm.io/gorm"
)

type User struct {
  	gorm.Model
	Nickname     string
	Job          string
	UID			 string
}

type JobGroup struct {
	gorm.Model
	name		 string
}

