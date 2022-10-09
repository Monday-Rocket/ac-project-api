package mysql

import (
	"log"
	"ac-project/api/internal/service/user"
	"gorm.io/gorm"
)

type UserRepository struct {
	Db *gorm.DB
}

func (r UserRepository) CreateUser(
	User user.User,
) uint {
	var userRecord = UserRecord {
		Nickname : User.Nickname,
		Job : User.Job,
		UID : User.UID,
	}

	result := r.Db.Create(&userRecord)
	if result.Error != nil {
		log.Fatal(result.Error)
	}
	return userRecord.ID
}

func (r UserRepository) GetUserByNickname(
	Nickname string,
) user.User {
	var userRecord UserRecord
	r.Db.Where("nickname = ?", Nickname).First(&userRecord)
	return user.User {
		Nickname: "",
		UID: "",
		Job: 3,
	}
}
