package net.estinet.gFeatures.Feature.gRanksNew;

import net.estinet.gFeatures.Command.EstiCommand;
import net.estinet.gFeatures.Configs;
import net.estinet.gFeatures.Feature.gRanksNew.Commands.TestCommand;
import net.estinet.gFeatures.SQL.Player.EstiSet;
import net.estinet.gFeatures.SQL.Player.Key;
import net.estinet.gFeatures.gFeatures;

import java.util.HashMap;

/*
gFeatures
https://github.com/EstiNet/gFeatures

   Copyright 2016 EstiNet

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

/*
gFeatures
https://github.com/EstiNet/gFeatures

   Copyright 2017 EstiNet

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
public class Configure{
	@Configs
	public static void onSetup(){
		gRanksNew base = new gRanksNew("Base", "1.2.5");
		gFeatures.addFeature(base);
		
		EstiCommand test = new EstiCommand("test", "Says test!", "/test", "gFeatures.test", base, new TestCommand());
		gFeatures.addCommand(test);
		
		HashMap<Key<String>, String> hash = new HashMap<>();
		Key<String> key = new Key<>();
		key.setKey("Health");
		hash.put(key, "He's healthy!");
		EstiSet<String, String> set = new EstiSet<>();
		set.setData(hash);
		set.setTag(base.getName());
		gFeatures.addSQLFields(set);
		
	}
}
