package mysql

import (
	"gorm.io/gorm"
)

type UserRecord struct {
	gorm.Model
	Nickname string
	Job      int
	UID      string
}

type JobGroup struct {
	gorm.Model
	name string
}
