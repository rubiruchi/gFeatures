package net.genesishub.gFeatures.Feature.gRanks;

import org.bukkit.Bukkit;

/*
gFeatures
https://github.com/GenesisHub/gFeatures

   Copyright 2015 GenesisHub

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

public class Enable{
	static ConfigHub ch = new ConfigHub();
	public static void onEnable(){
		Bukkit.getLogger().info("gRanks Plugin enabled!");
		ch.setupConfig();
		SQLConnect c = new SQLConnect();
		Retrieve cc = new Retrieve();
		String Address, Port, Tablename, Username, Password;
		Address = cc.getAddress();
		Port = cc.getPort();
		Tablename = cc.getTablename();
		Username = cc.getUsername();
		Password = cc.getPassword();
		String URL = c.toURL(Port, Address, Tablename);
		c.checkOnline(URL, Username, Password);
		c.Connect(URL, Username, Password, "CREATE TABLE IF NOT EXISTS People(UUID VARCHAR(200), Rank VARCHAR(200) ENGINE=InnoDB;");
		c.Connect(URL, Username, Password, "CREATE TABLE IF NOT EXISTS Ranks(Name VARCHAR(200), Prefix VARCHAR(200) ENGINE=InnoDB;");
		int i = Integer.parseInt(c.ConnectReturn(URL, Username, Password, "SELECT COUNT(*) FROM Ranks(Name, Prefix)").get(1));
		//TODO Debug the loop condition if something is wrong :D
		Basis.resetAll();
		for(int iter = 0; iter>i; iter++){
			String name = c.ConnectReturn(URL, Username, Password, "SELECT Name FROM Ranks WHERE id='" + iter + "'").get(1);
			String prefix = c.ConnectReturn(URL, Username, Password, "SELECT Prefix FROM Ranks WHERE id='" + iter + "'").get(1);
			Rank newrank = new Rank(name, prefix);
			Basis.addRank(newrank);
		}
	}
}
