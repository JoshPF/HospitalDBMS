SHELL:=/usr/local/bin/bash -O globstar
.PHONY: build run init

build: lib/junit-4.13-rc-1.jar
	mkdir -p bin
	javac -cp /usr/lib/java/:lib/*:src/ -g -d bin **/*.java

run: build lib/mariadb-java-client.jar
	java -cp lib/*:bin/ hospital_dbms.HospitalDBMS

init: build
	java -cp bin:lib/* hospital_dbms.InitializeDatabase

# JAR dependency for compiling JUnit tests
lib/junit-4.13-rc-1.jar:
	mkdir -p lib
	curl https://search.maven.org/remotecontent?filepath=junit/junit/4.13-rc-1/junit-4.13-rc-1.jar > lib/junit-4.13-rc-1.jar

# Standalone jar for running tests
lib/junit-platform-console-standalone-1.5.2.jar:
	mkdir -p lib
	curl https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.5.2/junit-platform-console-standalone-1.5.2.jar > lib/junit-platform-console-standalone-1.5.2.jar

lib/mariadb-java-client.jar:
	mkdir -p lib
	curl https://downloads.mariadb.com/Connectors/java/connector-java-2.4.4/mariadb-java-client-2.4.4.jar > lib/mariadb-java-client.jar
