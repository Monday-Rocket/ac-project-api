package user

type User struct {
	UID      string
	Nickname *string
	JobGroup *JobGroup
}


type JobGroup struct {
	ID   uint
	Name string
}
