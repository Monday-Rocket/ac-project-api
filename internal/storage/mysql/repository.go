package mysql

import (
	"log"

	"gorm.io/gorm"
)

type UserRepository struct {
	Db *gorm.DB
}



func CreateUser(
	User User,
	UserRepository UserRepository,
) int64 {
	result := UserRepository.Db.Create(&User)
	if result.Error != nil {
		log.Fatal(result.Error)
	}
	return result.RowsAffected
}

func (r UserRepository) GetUserByName(
	Name string,
	UserRepository UserRepository,
) User {
	var user User
	UserRepository.Db.Where("name = ?", Name).First(&user)
	return user
}
