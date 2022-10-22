package mysql

import (
	"fmt"
	"log"
	"gorm.io/gorm"
	"gorm.io/gorm/clause"

	"ac-project/api/internal/service/user"
	"ac-project/api/internal/errors"
)

type UserRepository struct {
	Db *gorm.DB
}

func (r UserRepository) FindJobGroupById(
	Id uint,
) user.JobGroup {
	var jobGroupRecord JobGroupRecord
	r.Db.Where("id = ?", Id).First(&jobGroupRecord)
	return user.JobGroup{
		ID:   jobGroupRecord.ID,
		Name: jobGroupRecord.Name,
	}
}

func (r UserRepository) CreateUser(
	User user.User,
) string {
	var jobGroup JobGroupRecord

	var userRecord = UserRecord{
		Nickname: User.Nickname,
		JobGroup: jobGroup,
		UID:      User.UID,
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

	r.Db.Model(&userRecord).Clauses(clause.Returning{}).Updates(userRecord)

	return toEntity(userRecord)
}

func (r UserRepository) FindAllJobGroup() []user.JobGroup {
	var records []JobGroupRecord
	r.Db.Find(&records)
	var entities []user.JobGroup
	for _, record := range records {
		entities = append(entities, user.JobGroup{ID: record.ID, Name: record.Name})
	}
	return entities
}

func (r UserRepository) FindUserById(UID string) (user.User, error) {
	var record UserRecord
	result := r.Db.Preload("JobGroup").Where("UID = ?", UID).First(&record)
	if (result.RowsAffected == int64(0)) {
		return user.User{}, &errors.DataNotFoundError{}
	}
	fmt.Println(record.UID)
	return toEntity(record), nil
}

func toRecord(user user.User) UserRecord {
	return UserRecord{
		UID:        user.UID,
		Nickname:   user.Nickname,
		JobGroupID: &user.JobGroup.ID,
		JobGroup: JobGroupRecord{
			ID:   user.JobGroup.ID,
			Name: user.JobGroup.Name,
		},
	}
}

func toEntity(userRecord UserRecord) user.User {
	fmt.Println(userRecord.JobGroup.Name)
	return user.User{
		UID:      userRecord.UID,
		Nickname: userRecord.Nickname,
		JobGroup: &user.JobGroup{
			ID:   userRecord.JobGroup.ID,
			Name: userRecord.JobGroup.Name,
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
