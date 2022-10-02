package user

import (
	"errors"
)

var ErrNotFound = errors.New("user not found")

type Service interface {
	AddUser(token) error
	UpdateUser() error
	GetUserBy() error
}
