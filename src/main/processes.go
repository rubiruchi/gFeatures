package main

import (
	"os"
	"os/exec"
	"io"
	"bufio"
	"time"
)

var Servers = make(map[string]*Server)

/*
 * Server struct and methods
 */

type Server struct {
	Settings   ServerConfig
	Log        string
	Channel    chan string
	Process    *exec.Cmd
	OutputPipe io.ReadCloser
	InputPipe  io.WriteCloser
	AutoStart  bool
	IsOnline   bool
}

func (server *Server) start() {
	println("Starting " + server.Settings.InstanceName)
	server.Process = exec.Command("java", "-jar", server.Settings.ExecutableName)
	server.Process.Dir = server.Settings.HomeDirectory //set working directory

	//Handle minecraft related tasks
	if (server.Settings.MinecraftMode) {
		if _, err := os.Stat(server.Settings.HomeDirectory + "/update"); os.IsNotExist(err) {
			os.Mkdir(server.Settings.HomeDirectory+"/update", 0755)
			println("Created the update directory!")
		}
	}

	//Initializes input and output pipes
	server.OutputPipe, _ = server.Process.StdoutPipe()
	pipe, err := server.Process.StdinPipe()
	if err != nil {
		println("Process error for " + server.Settings.InstanceName + ": " + err.Error())
	}
	server.InputPipe = pipe

	//Start process
	err2 := server.Process.Start()
	if err2 != nil {
		println("Error starting process " + server.Settings.InstanceName + ": " + err2.Error())
		return
	}
	server.IsOnline = true

	//Function called when process ends
	deferFunc := func() {
		server.Process.Wait()
		server.IsOnline = false
		server.InputPipe.Close()
		server.OutputPipe.Close()
		println(server.Settings.InstanceName + " has stopped.")
		if (server.AutoStart) {
			time.Sleep(time.Second * 2) //Let's not die right TODO
			go server.start()
		}
	}

	defer deferFunc()

	//Print output
	buff := bufio.NewScanner(server.OutputPipe)
	for buff.Scan() {
		server.Log += buff.Text() + "\n"
		if curServerView != nil && server.Settings.InstanceName == curServerView.Settings.InstanceName {
			println(buff.Text())
		}
	}
}

func (server *Server) kill() {
	if err := server.Process.Process.Kill(); err != nil {
		println("Failed to kill process" + server.Settings.InstanceName + ": " + err.Error())
	} else {
		println("Killed " + server.Settings.InstanceName)
	}
}

func (server *Server) stop() {
	server.input(server.Settings.StopProcessCommand)
}

func (server *Server) input(input string) {
	io.WriteString(server.InputPipe, input + "\n")
}

/*
 * Init and start all clients
 */

func ClientsStart() {
	for _, element := range instanceSettings.Servers {
		server := new(Server)
		server.Settings = element
		Servers[server.Settings.InstanceName] = server
		server.AutoStart = true

		println("Initialized server " + server.Settings.InstanceName + ".")

		go server.start()
	}
	println("Completed process initialization!")
}

/*
 * Stop all clients
 */

func ClientsStop() {
	println("Stopping all clients...")
	end := func(server *Server) {
		server.AutoStart = false
		server.stop()
		time.Sleep(time.Second * time.Duration(server.Settings.ServerUnresponsiveKillTimeSeconds))
		if server.IsOnline {
			server.Process.Process.Kill()
		}
		//TODO replace with better solution than blocking shutdown for many seconds...
	}
	for key, _ := range Servers {
		if Servers[key].IsOnline {
			println("Stopping " + key + "...")
			go end(Servers[key])
		}
	}
}

func StartClient(name string) string {

}

func StopClient(name string) string {

}

func KillClient(name string) string {
	if _, ok := Servers[name]; ok {
		if !Servers[name].IsOnline {
			return "Process is not online."
		} else {
			Servers[name].AutoStart = false
			Servers[name].kill()
			return "Killed process " + name + "."
		}
	} else {
		return "Server not found."
	}
}
