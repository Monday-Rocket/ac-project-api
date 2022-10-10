package mysql

import (
	"gorm.io/gorm"
)

type UserRecord struct {
	gorm.Model
	UID        string
	Nickname   *string
	JobGroup   *JobGroupRecord
}

type JobGroupRecord struct {
	gorm.Model
	name string
}