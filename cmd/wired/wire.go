//go:build wireinject
// +build wireinject

package wired

import (
	"context"
	"ac-project/api/internal/service/user"
	"github.com/google/wire"
)

var userSet = wire.NewSet(
	NewService,
	NewUserRepos,
	NewClients,
)


func InitalizeUserService(
	context.Context,
) (user.Service, error) {
	wire.Build(
		userSet,
	)
	return user.ServiceImpl{}, nil
}