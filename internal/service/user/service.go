package user

import (
	"context"
	"ac-project/api/internal/storage/firebase"
	"ac-project/api/internal/storage/mysql"
	"errors"
)

var ErrNotFound = errors.New("user not found")

type Service interface {
	AddUser(token string) error
}

type AuthHandler interface {
	GetUIDByEmail(
		context context.Context,
		authHandler firebase.AuthHandler,
		email string
	) string
}

type UserRepository interface {
	GetUserByName(
		Name string,
		UserRepository mysql.UserRepository,
	) mysql.User 
}

type ServiceImpl struct {
	UserRepoSet UserRepoSet
}

func (s ServiceImpl) AddUser(token string) error {
	// authHandler로 토큰 인증 후 받은 UID DB에 저장
	return nil
}
