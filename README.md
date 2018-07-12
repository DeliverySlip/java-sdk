# Secure Messaging Java Sample SDK
The Secure Messaging Java SDK allows clients to create applications to communicate with the DeliverySlip Secure
Messaging API. The client currently offers the most popular features and primarily is a reference for users looking
for guidelines on how to use the DeliverySlip API. 

The SDK is being expanded to eventually encompass all features offered by the Secure Messaging API.

# Prerequisites
* To compile the project and execute tests, you will need Java 8 installed. Current dependencies within the SDK are
not compatible with Java 9 at this point in time.

# Setup
To compile the Secure Messaging SDK execute the following command within the root of the project:
```
./gradlew.bat shadowJar  # windows
./gradlew shadowJar      # *nix
```
This will build a fat-jar file which can be imported into your project. The jar file will be located in the 
`build/libs` folder. 

 With an example app compilable as `Main.java`, compiling your project with the SDK may look something like this:
```$xslt
javac -cp "C:\secure-messaging-java\build\libs\deliveryslipclient.jar;." Main.java
```

# Usage
For examples on how to use the supported features in the Java SDK, see the unit tests folders. These includes examples
on how to login, send messages, search messages and handle attachments.

You can also generate the Javadoc, giving a general overview of supported functionality by executing the following
gradle command
```
./gradlew.bat javadoc   # windows
./gradlew javadoc       # *nix
```
The outputted javadoc files will be located in `build/docs`

# Testing
If you would like to run unit tests on the SDK, execute them with the following command:
```$xslt
./gradlew.bat test      #windows
./gradlew test          # *nix
```
This will execute all the setup unit tests and produce a report within the `build/reports` folder

# Developer Notes
* July 12, 2018 - Release of the Java client is just underway. The current code is stable but the priority is currently
on documentation of the code and repository to improve ease of access and involvement by others.
More to come!