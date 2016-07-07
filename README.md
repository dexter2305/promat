
Building the project
Check out the project - git clone https://github.com/dexter2305/promat.git

Test the project - ./gradlew test
Build the entire project - ./gradlew build
Run the web application with embedded jetty server  - ./gradlew jettyRun 
http://localhost:8080/promat


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

Command to run the ExcelReader (as a maven project)
> mvn compile exec:java -Dexec.mainClass=com.poople.promat.migrate.ExcelDataImport -Dexec.args=<path_to_excel_file> -Dexec.classpathScope=runtime


