package wired

import (
	"ac-project/api/internal/service/user"

	"ac-project/api/internal/storage/mysql"
	"ac-project/api/internal/storage/firebase"
	"ac-project/api/internal/http/rest"

	"firebase.google.com/go/auth"
	"gorm.io/gorm"
)

type UserClientSet struct {
	Authclient *auth.Client
	Dbclient   *gorm.DB
}

func newRepository(
	Db *gorm.DB,
) mysql.UserRepository {
	return mysql.UserRepository{Db: Db}
}

func newAuthMiddleware(
	authHandler *firebase.AuthHandler,
) rest.AuthMiddleware {
	return rest.AuthMiddleware{AuthHandler: authHandler}
}

func newAuthHandler(
	authClient *auth.Client,
) *firebase.AuthHandler {
	return &firebase.AuthHandler{AuthClient: authClient}
}

func newClients() *UserClientSet {
	var authclient = newFirebaseClient()
	var dbclient = ConnectDb()
	dbclient.AutoMigrate(&mysql.UserRecord{})
	return &UserClientSet{authclient, dbclient}
}

func newUserRepos(clientset *UserClientSet) user.UserRepoSet {
	return user.UserRepoSet{
		newRepository(clientset.Dbclient), 
		newAuthHandler(clientset.Authclient),
	}
}

func newService(repoSet user.UserRepoSet) user.Service {
	return &user.ServiceImpl{repoSet}
}
