package user

import (
	"context"
	"errors"
	"fmt"

	"ac-project/api/internal/storage/firebase"
	jwt "github.com/golang-jwt/jwt/v4"
)

var ErrNotFound = errors.New("user not found")

type Service interface {
	AddUser(token string) uint
}

type AuthHandler interface {
	GetUIDByEmail(
		context context.Context,
		authHandler firebase.AuthHandler,
		email string,
	) string
}

type UserRepository interface {
	GetUserByNickname(
		Nickname string,
	) User
	CreateUser(
		User User,
	) uint
}

type UserRepoSet struct {
	MysqlRepo UserRepository
	FirebaseRepo AuthHandler
}

type ServiceImpl struct {
	UserRepoSet UserRepoSet
}


func (s ServiceImpl) AddUser(token string) uint {
	claims := jwt.MapClaims{}
	parsedToken, err := jwt.Parse(token, nil)
	fmt.Println(parsedToken)

	// ... error handling
	if (err != nil) {
		fmt.Println(err)
	}
	
	// do something with decoded claims
	for key, val := range claims {
		fmt.Printf("Key: %v, value: %v\n", key, val)
	}
	
	// var userInfo = json.NewEncoder(w).Encode(parsedToken)

	// authHandler로 토큰 인증 후 받은 UID DB에 저장
	var res = s.UserRepoSet.MysqlRepo.CreateUser(User{
		Nickname: "희진", 
		Job: 1,
		UID: "test",
	})
	return res
}
