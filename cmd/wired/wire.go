//go:build wireinject
// +build wireinject

package wired

import (
	"ac-project/api/internal/storage/firebase"
	"ac-project/api/internal/storage/mysql"
	"context"

	"github.com/google/wire"
)

func InitializeUserRepository(
	context.Context,
) (mysql.UserRepository, error) {
	wire.Build(
		mysql.NewRepository,
		ConnectDb,
	)
	return mysql.UserRepository{}, nil
}

func InitializeAuthHandler(
	context.Context,
) (firebase.AuthHandler, error) {
	wire.Build(
		firebase.NewAuthHandler,
		newFirebaseClient,
	)
	return firebase.AuthHandler{}, nil
}
