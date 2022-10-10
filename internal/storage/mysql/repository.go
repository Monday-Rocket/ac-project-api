package mysql

import (
	"log"
	"ac-project/api/internal/service/user"
	"gorm.io/gorm"
)

type UserRepository struct {
	Db *gorm.DB
}

func (r UserRepository) FindJobGroupById(
	Id string,
) user.JobGroup {
	var jobGroupRecord JobGroupRecord
	r.Db.Where("id = ?", Id).First(&jobGroupRecord)
	return user.JobGroup {
		ID: jobGroupRecord.ID,
		Name: jobGroupRecord.name,
	}
}

func (r UserRepository) CreateUser(
	User user.User,
) uint {
	var jobGroup JobGroupRecord

	var userRecord = UserRecord {
		Nickname : User.Nickname,
		JobGroup : &jobGroup,
		UID : User.UID,
	}

	result := r.Db.Create(&userRecord)
	if result.Error != nil {
		log.Fatal(result.Error)
	}
	return userRecord.ID
}

// func (r UserRepository) GetUserByNickname(
// 	Nickname string,
// ) user.User {
// 	var userRecord UserRecord
// 	r.Db.Where("nickname = ?", Nickname).First(&userRecord)
// 	return nil
// }
