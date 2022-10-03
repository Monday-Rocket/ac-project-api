package wired

import (
	"firebase.google.com/go/auth"
	"gorm.io/gorm"
	"ac-project/api/internal/service/user"
)

type UserRepoSet struct {
	UserClientSet UserClientSet
} 

type UserClientSet struct {
	authclient auth.Client
	dbclient gorm.DB
}

func NewClients() UserClientSet {
	var authclient = newFirebaseClient()
	var dbclient = ConnectDb()
	return UserClientSet{*authclient, *dbclient}
}

func NewService(repoSet UserRepoSet) user.Service {
	return &user.ServiceImpl{repoSet}
}

func NewUserRepos(clientset UserClientSet) UserRepoSet {
	return UserRepoSet{clientset}
}