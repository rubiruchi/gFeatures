package net.estinet.gFeatures.Feature.CTF;

import net.estinet.gFeatures.ClioteSky.ClioteSky;
import net.estinet.gFeatures.Command.EstiCommand;
import net.estinet.gFeatures.Configs;
import net.estinet.gFeatures.Feature.CTF.Commands.SuicideCommand;
import net.estinet.gFeatures.gFeatures;

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
		CTF base = new CTF("CTF", "1.2.0");
		base.addEventListener(new EventHub());
		gFeatures.addFeature(base);

		ClioteSky.addHook(new GetMGInfoClioteHook("getmginfo", base.getName()));

		EstiCommand test = new EstiCommand("ctf", "CTF commands!", "/CTF", "gFeatures.minigames", base, true);
		gFeatures.addCommand(test);

		EstiCommand suicide = new EstiCommand("suicide", "Kills the player that executes this command!", "/suicide", base, new SuicideCommand());
		gFeatures.addCommand(suicide);
	}
}
