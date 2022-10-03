// Code generated by Wire. DO NOT EDIT.

//go:generate go run github.com/google/wire/cmd/wire
//go:build !wireinject
// +build !wireinject

package wired

import (
	"ac-project/api/internal/service/user"
	"context"
	"github.com/google/wire"
)

// Injectors from wire.go:

func InitalizeUserService(contextContext context.Context) (user.Service, error) {
	userClientSet := NewClients()
	userRepoSet := NewUserRepos(userClientSet)
	service := NewService(userRepoSet)
	return service, nil
}

// wire.go:

var userSet = wire.NewSet(
	NewService,
	NewUserRepos,
	NewClients,
)
