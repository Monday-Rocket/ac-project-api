package config

var RuntimeConf = RuntimeConfig{}

type RuntimeConfig struct {
   Datasource Datasource `yaml:"datasource"`
   Server     Server     `yaml:"server"`
}

type Datasource struct {
	Db      Db `yaml:"db"`
	Redis Redis `yaml:"redis"`
 }

type Db struct {
   Url      string `yaml:"url"`
   UserName string `yaml:"userName"`
   Password string `yaml:"password"`
}

type Redis struct {
	Url      string `yaml:"url"`
}

type Server struct {
   Port int `yaml:"port"`
}

