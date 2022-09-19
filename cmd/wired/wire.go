package wired

import (
	"context"

	"github.com/google/wire"
)

func InitializeEvent(
	context.Context,
) (Event, error) {
	wire.Build(
		APIsSet,
		wire.Struct(new(Context), "*"),
	)
	return Event{}, nil
}
