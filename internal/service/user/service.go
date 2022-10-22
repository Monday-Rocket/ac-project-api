package user

import (
	"context"
	"errors"
	"fmt"

	jwt "github.com/golang-jwt/jwt/v4"
)

var ErrNotFound = errors.New("user not found")

type Service interface {
	AddUser(token string) CreatingUser
	UpdateUser(token string, nickname string, jobGroupId uint) User
	FindAllJobGroup() []JobGroup
	FindUserById(UID string) (User, error)
}

type AuthHandler interface {
	GetUIDByEmail(
		context context.Context,
		email string,
	) string

}

type UserRepository interface {
	// GetUserByNickname(
	// 	Nickname string,
	// ) User
	CreateUser(
		User User,
	) string
	UpdateUser(
		user User,
	) User
	FindJobGroupById(
		JobGroupId uint,
	) JobGroup
	FindAllJobGroup() []JobGroup
	FindUserById(
		UID string,
	) (User, error)
}

type UserRepoSet struct {
	MysqlRepo    UserRepository
	FirebaseRepo AuthHandler
}

type ServiceImpl struct {
	UserRepoSet UserRepoSet
}

func (s ServiceImpl) AddUser(token string) CreatingUser {
	var UID = getUIDFromJwt(token)
	user, error := s.FindUserById(UID)
	if (error != nil) {
		s.UserRepoSet.MysqlRepo.CreateUser(User{
			UID:      UID,
			Nickname: nil,
			JobGroup: nil,
		})
		return CreatingUser{
			UID:      UID,
			IsNew:    true,
		}
	} else {
		return CreatingUser{
			UID:      user.UID,
			IsNew:    false,
		}
	}
}

func (s ServiceImpl) UpdateUser(token string, nickname string, jobGroupId uint) User {
	var UID = getUIDFromJwt(token)

	var jobGroup = s.UserRepoSet.MysqlRepo.FindJobGroupById(jobGroupId)

	// authHandler로 토큰 인증 후 받은 UID DB에 저장
	return s.UserRepoSet.MysqlRepo.UpdateUser(User{
		UID:      UID,
		Nickname: &nickname,
		JobGroup: &jobGroup,
	})
}

func getUIDFromJwt(token string) string {
	claims := jwt.MapClaims{}

	parsedToken, err := jwt.Parse(token, nil)
	fmt.Println(parsedToken)

	// ... error handling
	if err != nil {
		fmt.Println(err)
	}

	// do something with decoded claims
	for key, val := range claims {
		fmt.Printf("Key: %v, value: %v\n", key, val)
	}

	claims, ok := parsedToken.Claims.(jwt.MapClaims)

	if !ok {
		fmt.Println("error")
	}

	return claims["user_id"].(string)
}

func (s ServiceImpl) FindAllJobGroup() []JobGroup {
	return s.UserRepoSet.MysqlRepo.FindAllJobGroup()
}

func (s ServiceImpl) FindUserById(
	UID string,
) (User, error) {
	return s.UserRepoSet.MysqlRepo.FindUserById(UID)
}
