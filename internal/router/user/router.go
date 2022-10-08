package user

import (
	"net/http"

	"github.com/gin-gonic/gin"
)

func NewRouter() *gin.Engine {
	engine := gin.New()

	engine.GET("/health", healthCheck)

	apiV1 := engine.
		Group("/api/v1")
	Use(gin.Logger(), gin.Recovery())
	{
		apiV1.GET("/users")
	}
}

func healthCheck(c *gin.Context) {
	c.String(http.StatusOK, "OK")
}
