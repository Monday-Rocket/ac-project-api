//go:build wireinject
// +build wireinject

package wired

import (
	"ac-project/api/internal/service/user"
	"ac-project/api/internal/http/rest"
	"github.com/google/wire"
)

var userSet = wire.NewSet(
	newService,
	newUserRepos,
	newClients,
)


func InitializeUserService() (user.Service, error) {
	wire.Build(
		userSet,
	)
	return user.ServiceImpl{}, nil
}

func InitializeAuthMiddleware() (rest.AuthMiddleware, error) {
	wire.Build(
		newAuthMiddleware,
		newAuthHandler, 
		newFirebaseClient,
	)
	return rest.AuthMiddleware{}, nil
}