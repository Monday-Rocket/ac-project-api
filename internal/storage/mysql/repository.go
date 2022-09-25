package mysql

import (
	"gorm.io/gorm"
	"log"
)



type UserRepository struct {
	Db	*gorm.DB
}

func NewRepository(
	Db *gorm.DB,
) UserRepository {
	return UserRepository{Db: Db}
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

func GetUserByName(
	Name string,
	UserRepository UserRepository,
) User {
	var user User
	UserRepository.Db.Where("name = ?", Name).First(&user)
	return user
}