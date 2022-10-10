package mysql

type UserRecord struct {
	UID        string `gorm:"primaryKey"`
	Nickname   *string
	JobGroup   JobGroupRecord `gorm:"foreignKey:ID"`
}

type JobGroupRecord struct {
	ID   uint
	name string
}