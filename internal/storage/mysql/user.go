package mysql

type UserRecord struct {
	UID        string `gorm:"primaryKey"`
	Nickname   *string
	JobGroupID *uint
	JobGroup   JobGroupRecord
}

type JobGroupRecord struct {
	ID   uint `gorm:"primaryKey"`
	Name string
}
