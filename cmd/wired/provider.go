package wired

import (
	"ac-project/api/internal/service/user"

	"firebase.google.com/go/auth"
	"gorm.io/gorm"
)

type UserRepoSet struct {
	UserClientSet UserClientSet
}

type UserClientSet struct {
	authclient auth.Client
	dbclient   gorm.DB
}

// type UserHandler struct {
// 	userService user.Service
// }

func NewClients() UserClientSet {
	var authclient = newFirebaseClient()
	var dbclient = ConnectDb()
	return UserClientSet{*authclient, *dbclient}
}

// func NewHandler() UserHandlerSet {
// 	var
// }

func NewService(repoSet UserRepoSet) user.Service {
	return &user.ServiceImpl{repoSet}
}

func NewUserRepos(clientset UserClientSet) UserRepoSet {
	return UserRepoSet{clientset}
}
