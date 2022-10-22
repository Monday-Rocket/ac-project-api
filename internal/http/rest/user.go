package rest

type UpdateUserRequest struct {
	Nickname   string
	JobGroupId uint `json:"job_group_id"`
}