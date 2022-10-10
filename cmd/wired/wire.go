//go:build wireinject
// +build wireinject

package wired

import (
	"ac-project/api/internal/service/user"
	"github.com/google/wire"
)

var userSet = wire.NewSet(
	newService,
	newUserRepos,
	newClients,
)


func InitalizeUserService() (user.Service, error) {
	wire.Build(
		userSet,
	)
	return user.ServiceImpl{}, nil
}