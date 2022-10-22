package errors


type DataNotFoundError struct {
}

func (d DataNotFoundError) Error() string {
	return "data not found"
}