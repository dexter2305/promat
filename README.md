# promat
Profile matcher

Project directory structure. 

++promat (project)
++++promat-core(module)
++++++src/main/java (all the java sources)
++++++src/test/java (all the test sources - like junit)
++++settings.gradle

Git commands: 

Commands to create a project from local workspace and push to remote
> git config --user.name <>
> git config --user.email <>
> git init
> git add <add all files>
> git commit -m 'message'
> git remote add <any name, 'origin' for example> <remote url>

Commands to pull from an existing remote repository
> git clone https://github.com/dexter2305/promat.git [local_folder_name] 
	-- this creates a local directory. 
> git pull origin master
	-- this pulls any new changes in the remote repository to local


IDE related tasks. 

Eclipse: 

> gradlew eclipse
 	-- creates the eclipse artifacts '.project' and '.classpath' files. 
> gradlew cleanEclipse
	-- deletes the eclipse artifacts

The above tasks will require internet access for the first run, after which the switch '--offline' can be used as mentioned below. 
> gradlew --offline eclipse
> gradlew --offline cleanEclipse


IDEA

> gradlew idea 
> gradlew cleanIdea

