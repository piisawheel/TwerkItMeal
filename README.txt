This is a modified version of Kyle B Cox's TwerkItMeal.

It has been updated to forge 1.16.5.
The config has been update with more options.
Dirt can spawn grass blacks.
Grass blocks can be twerked and have a small chance to spawn bamboo.
Whitelist and blacklist code updated (either or).
Default is to use whitelist now with dirt, grass, and bamboo varients.
These changes are so I can include this mod into a curated skyblock modpack.

The mod used to use a 50% chance to decide if it would bonemeal everything in the area.
Now there's a 50% chance (configurable) to trigger on any particular growable except grass (5%).
This is because it was bonemealing every grass block creating insane amounts of tall grass.
There's a 10% chance of dirt becoming grass, and a 5% chance of grass spawning bamboo.
That can be disabled by removing dirt from the whitelist or adding it to your blacklist.
Bamboo has it's own setting, as well as the effective range.
See the config for more details.
(You might need to run the game with the mod once to generate it).

Below follows the original Readme.

-------------------------------------------
Source installation information for modders
-------------------------------------------
This code follows the Minecraft Forge installation methodology. It will apply
some small patches to the vanilla MCP source code, giving you and it access 
to some of the data and functions you need to build a successful mod.

Note also that the patches are built against "unrenamed" MCP source code (aka
srgnames) - this means that you will not be able to read them directly against
normal code.

Source pack installation information:

Standalone source installation
==============================

See the Forge Documentation online for more detailed instructions:
http://mcforge.readthedocs.io/en/latest/gettingstarted/

Step 1: Open your command-line and browse to the folder where you extracted the zip file.

Step 2: You're left with a choice.
If you prefer to use Eclipse:
1. Run the following command: "gradlew genEclipseRuns" (./gradlew genEclipseRuns if you are on Mac/Linux)
2. Open Eclipse, Import > Existing Gradle Project > Select Folder 
   or run "gradlew eclipse" to generate the project.
(Current Issue)
4. Open Project > Run/Debug Settings > Edit runClient and runServer > Environment
5. Edit MOD_CLASSES to show [modid]%%[Path]; 2 times rather then the generated 4.

If you prefer to use IntelliJ:
1. Open IDEA, and import project.
2. Select your build.gradle file and have it import.
3. Run the following command: "gradlew genIntellijRuns" (./gradlew genIntellijRuns if you are on Mac/Linux)
4. Refresh the Gradle Project in IDEA if required.

If at any point you are missing libraries in your IDE, or you've run into problems you can run "gradlew --refresh-dependencies" to refresh the local cache. "gradlew clean" to reset everything {this does not affect your code} and then start the processs again.

Should it still not work, 
Refer to #ForgeGradle on EsperNet for more information about the gradle environment.
or the Forge Project Discord discord.gg/UvedJ9m

Forge source installation
=========================
MinecraftForge ships with this code and installs it as part of the forge
installation process, no further action is required on your part.

LexManos' Install Video
=======================
https://www.youtube.com/watch?v=8VEdtQLuLO0&feature=youtu.be

For more details update more often refer to the Forge Forums:
http://www.minecraftforge.net/forum/index.php/topic,14048.0.html
