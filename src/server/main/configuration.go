package main

import (
	"os"
	"io"
	"encoding/json"
	"bytes"
	"reflect"
	"fmt"
	"archive/zip"
	"time"
)

/*
 * User json object
 */

type Users struct {
	Name     string `json:"name"`
	Password string `json:"password"`
}

/*
 * Struct containing the server settings.
 */

type InstanceConfig struct {
	InstanceName string         `json:"instance_name"`
	InstancePort uint           `json:"instance_port"`
	Servers      []ServerConfig `json:"servers"`
	Users        []Users        `json:"users"`
}

/*
 * Struct containing settings for individual servers.
 */

type ServerConfig struct {
	InstanceName                      string `json:"instance_name"`
	HomeDirectory                     string `json:"home_directory"`
	ExecutableName                    string `json:"executable_name"`
	MinRam                            string `json:"min_ram"`
	MaxRam                            string `json:"max_ram"`
	JavaArgs                          string `json:"java_args"`
	MaxLines                          uint   `json:"max_lines"`
	AmountOfLinesToCutOnMax           uint   `json:"amount_of_lines_to_cut_on_max"`
	StopProcessCommand                string `json:"stop_process_command"`
	ServerUnresponsiveKillTimeSeconds uint   `json:"server_unresponsive_kill_time_seconds"`
	MinecraftMode                     bool   `json:"minecraft_mode"` //TODO not implemented
}

/*
 * Returns the configuration structs with default values.
 */

func ConfigDefault() (InstanceConfig, ServerConfig, Users) {
	con := InstanceConfig{}
	con.InstanceName = "Server"
	con.InstancePort = 6921

	wi := ServerConfig{}
	wi.InstanceName = "Server1"
	wi.HomeDirectory = "./"
	wi.MinRam = "512M"
	wi.MaxRam = "2G"
	wi.ExecutableName = "minecraft_server.jar"
	wi.JavaArgs = "-XX:+UseG1GC -XX:ParallelGCThreads=2 -XX:+AggressiveOpts -d64 -server"
	wi.MaxLines = 2000
	wi.AmountOfLinesToCutOnMax = 100
	wi.StopProcessCommand = "stop"
	wi.ServerUnresponsiveKillTimeSeconds = 20
	wi.MinecraftMode = true

	users := Users{}
	users.Name = "default"
	users.Password = "password"

	return con, wi, users
}

var configPath = "./config.json"

/*
 * Setups, and loads the config.json file.
 */

func LoadConfig() {
	//Check if file exists
	var _, err = os.Stat(configPath)

	createdFile := false
	if os.IsNotExist(err) {
		var file, err = os.Create(configPath)
		if err != nil { //Crash if the program can't load config file.
			logFatal(err)
		}
		file.Close()
		info("Created config.json!")
		createdFile = true
	}
	//Open file with R&W permissions and read it
	var file, err2 = os.OpenFile(configPath, os.O_RDWR, 0755)
	if err2 != nil {
		logFatal(err2)
	}
	var text = make([]byte, 1024) //Read the file and set it to text
	for {
		_, err = file.Read(text)
		if err == io.EOF {
			break
		}
		if err != nil && err != io.EOF {
			logFatal(err)
			break
		}
	}
	file.Close()
	text = bytes.Trim(text, "\x00") //Trim null characters
	info("Extracted config contents!")
	//Parse json
	var config InstanceConfig
	err3 := json.Unmarshal(text, &config)
	if err3 != nil {
		info(err3.Error())
		if string(text) != "" && !createdFile {
			os.Rename(configPath, configPath+".old")
			info("Moved the old config to config.json.old.")
			var file, err = os.Create(configPath)
			if err != nil { //Crash if the program can't load config file.
				logFatal(err)
			}
			file.Close()
			info("Created config.json!")
		}
		//Create default values
		instance, server, users := ConfigDefault()
		instance.Servers = []ServerConfig{server}
		instance.Users = []Users{users}
		js, err := json.MarshalIndent(instance, "", "    ") //pretty JSON
		if err != nil { //JSON incorrect catch (if there is a programmer error) .-.
			info("This error shouldn't happen. Please contact an administrator. (Default JSON Incorrect)")
			logFatal(err)
		}
		var file, err2 = os.OpenFile(configPath, os.O_RDWR, 0755) //Check if file is openable (and get file object)
		if err2 != nil {
			logFatal(err2)
		}
		file.Write(js) //write JSON to file
		info("Updated the config. Please check the config.json file and adjust the appropriate settings.")
		info("Once you are done updating, please start the server again.")
		os.Exit(0)
	}

	//Verify that all of the settings are there (possible config update)
	instance, server, users := ConfigDefault()
	inst := reflect.Indirect(reflect.ValueOf(instance))
	conf := reflect.Indirect(reflect.ValueOf(config))
	confSet := reflect.ValueOf(&config).Elem()
	for i := 0; i < conf.NumField(); i++ {
		if conf.Field(i).Interface() == "" || conf.Field(i).Interface() == 0 {
			info("Please check your config, a setting has been updated. (" + inst.Field(i).String() + ")")
			confSet.Field(i).SetString(inst.Field(i).String())
		}
	}
	for i := 0; i < len(config.Servers); i++ {
		sever := reflect.ValueOf(config.Servers[i])
		for j := 0; j < sever.NumField(); j++ {
			//fmt.Println(sever.Field(j).Interface()) //TODO
			//debug(" " + sever.Field(j).String())
			if sever.Field(j).Interface() == nil {
				info("Please check your config, a setting has been updated. (" + sever.Field(j).String() + ")")
				severSet := reflect.ValueOf(&config.Servers[i]).Elem()
				severSet.Field(j).Set(reflect.ValueOf(server).Field(j))
			}
		}
	}
	for i := 0; i < len(config.Users); i++ {
		sever := reflect.ValueOf(config.Users[i])
		severSet := reflect.ValueOf(&config.Users[i]).Elem();
		for j := 0; j < sever.NumField(); j++ {
			if sever.Field(j).Interface() == nil {
				info("Please check your config, a setting has been updated. (" + sever.Field(j).String() + ")")
				severSet.Field(j).Set(reflect.ValueOf(users).Field(j))
			}
		}
	}

	js, err := json.MarshalIndent(config, "", "    ") //pretty JSON

	if err != nil {
		logFatal(err)
	}

	os.Remove(configPath)

	var file3, err5 = os.Create(configPath)
	if err5 != nil { //Crash if the program can't load config file.
		logFatal(err5)
	}
	file3.Close()

	var file2, err4 = os.OpenFile(configPath, os.O_RDWR, 0755) //Check if file is openable (and get file object)
	if err4 != nil {
		logFatal(err4)
	}
	file2.Write(js) //write JSON to file
	file2.Close()

	//Verify settings before starting the program (if the settings are incorrect, the program stops)
	verifySettings(&config)
	instanceSettings = config
}

/*
 * Settings verification (crashes with error)
 */

func verifySettings(config *InstanceConfig) {
	namesUsed := make([]string, 1)

	/*
	 * Verify each server's settings
	 */
	for i, server := range config.Servers {

		_, err := os.Stat(server.HomeDirectory)
		if os.IsNotExist(err) {
			info(server.InstanceName + "'s home directory does not exist! Please fix this error in the config.")
			logFatal(err)
		}
		if server.HomeDirectory[len(server.HomeDirectory)-1] == '/' {
			config.Servers[i].HomeDirectory = substring(server.HomeDirectory, 0, len(server.HomeDirectory)-1)
		}

		_, err2 := os.Stat(server.HomeDirectory + "/" + server.ExecutableName)
		if os.IsNotExist(err2) {
			logFatalStr(server.InstanceName + "'s path for executable " + server.HomeDirectory + "/" + server.ExecutableName + " does not exist! Check your path in the config.")
		}

		for _, k := range namesUsed {
			if k == server.InstanceName {
				logFatalStr("The name " + server.InstanceName + " is already taken, check for duplicates!")
			}
		}
		namesUsed = append(namesUsed, server.InstanceName)
	}
}

/*
 * Initializes log files
 */
func InitLog() {
	if _, err := os.Stat(logDirPath); os.IsNotExist(err) {
		os.Mkdir(logDirPath, 0755)
		fmt.Println(time.Now().Format("2006-01-02 15:04:05") + " [INFO] Created the logging directory!")
	}
	InitLogFile(logDirPath)
}

/*
 * Initializes the log file and compresses the old one
 */

func InitLogFile(directory string) {
	if _, err := os.Stat(directory + "/current.log"); !os.IsNotExist(err) {
		fmt.Println(time.Now().Format("2006-01-02 15:04:05") + " [INFO] Compressing previous log file...")

		ZipFiles(directory+"/"+time.Now().Format("2006-01-02 15-04-05")+".zip", []string{directory + "/current.log"})

		os.Remove(directory + "/current.log") //remove main log file
		fmt.Println(time.Now().Format("2006-01-02 15:04:05") + " [INFO] Done!")
	}
	os.Create(directory + "/current.log")
	info("Created the main log file!")
}

/*
 * Initializes log files after configuration is loaded in
 */
func PostInitLog() {
	for _, server := range instanceSettings.Servers {
		_, err := os.Stat(logDirPath + "/" + server.InstanceName)
		if os.IsNotExist(err) {
			os.Mkdir(logDirPath+"/"+server.InstanceName, 0755)
		}
		InitLogFile(logDirPath + "/" + server.InstanceName)
	}
}

/*
 * Zip file utility
 */

func ZipFiles(filename string, files []string) error {

	newfile, err := os.Create(filename)
	if err != nil {
		return err
	}
	defer newfile.Close()

	zipWriter := zip.NewWriter(newfile)
	defer zipWriter.Close()

	// Add files to zip
	for _, file := range files {

		zipfile, err := os.Open(file)
		if err != nil {
			return err
		}
		defer zipfile.Close()

		// Get the file information
		info, err := zipfile.Stat()
		if err != nil {
			return err
		}

		header, err := zip.FileInfoHeader(info)
		if err != nil {
			return err
		}

		// Change to deflate to gain better compression
		// see http://golang.org/pkg/archive/zip/#pkg-constants
		header.Method = zip.Deflate

		writer, err := zipWriter.CreateHeader(header)
		if err != nil {
			return err
		}
		_, err = io.Copy(writer, zipfile)
		if err != nil {
			return err
		}
	}
	return nil
}