# Compiler and classpath setup
JAVAC = javac
JAVA = java
JUNIT = /home/uppal/private/cs400/junit5.jar
CP = .:$(JUNIT)

# Compile and run the application
runApplication:
	$(JAVAC) -cp $(CP) App.java
	$(JAVA) -cp $(CP) App

# Compile and run JUnit tests for BackendTests.java or FrontendTests.java
runAllTests:
	$(JAVAC) -cp $(CP) BackendTests.java
	$(JAVA) -cp $(CP) org.junit.platform.console.ConsoleLauncher --select-class=BackendTests
# Remove compiled class files
clean:
	rm -f *.class

