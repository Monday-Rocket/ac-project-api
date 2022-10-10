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
	Id uint,
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
) string {
	var jobGroup JobGroupRecord

	var userRecord = UserRecord {
		Nickname : User.Nickname,
		JobGroup : jobGroup,
		UID : User.UID,
	}

	result := r.Db.Create(&userRecord)
	if result.Error != nil {
		log.Fatal(result.Error)
	}
	return userRecord.UID
}

func (r UserRepository) UpdateUser(
	User user.User,
) user.User {
	var userRecord = toRecord(User)

	r.Db.Model(&userRecord).Updates(userRecord)

	return toEntity(userRecord)
}

func toRecord(user user.User) UserRecord {
	return UserRecord{
		UID: user.UID,
		Nickname: user.Nickname,
		JobGroup: JobGroupRecord {
			ID: user.JobGroup.ID,
			name: user.JobGroup.Name,
		},
	}
}

func toEntity(userRecord UserRecord) user.User {
	return user.User{
		UID : userRecord.UID,
		Nickname : userRecord.Nickname,
		JobGroup : &user.JobGroup{
			ID: userRecord.JobGroup.ID,
			Name: userRecord.JobGroup.name,
		},
	}
}

// func (r UserRepository) GetUserByNickname(
// 	Nickname string,
// ) user.User {
// 	var userRecord UserRecord
// 	r.Db.Where("nickname = ?", Nickname).First(&userRecord)
// 	return nil
// }
