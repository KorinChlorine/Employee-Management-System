To access-- install JavaFX first

Step 1: Download JavaFX
Go to this website: https://gluonhq.com/products/javafx/

Download the JavaFX SDK for your system.

After downloading, extract the ZIP file to a folder (example: C:\javafx-sdk-21)

Step 2: Install Java (if not yet installed)
Download JDK 17 or JDK 21 from: https://adoptium.net

Install it.

Open Command Prompt and type:

nginx
Copy
Edit
java -version
If it shows the version, Java is installed.

Step 3: Create a Java Project
Open your IDE (e.g., IntelliJ IDEA).

Create a New Java Project.

Step 4: Add JavaFX to Your Project
Go to File > Project Structure > Libraries.

Click the + icon, choose Java, and select the lib folder inside your extracted JavaFX folder.
(Example: C:\javafx-sdk-21\lib)

Click OK to add the JavaFX library.

Step 5: Add VM Options to Run JavaFX
When you run your JavaFX app, add this to VM options in your Run Configuration:

cpp
Copy
Edit
--module-path "C:\javafx-sdk-21\lib" --add-modules javafx.controls
(Replace the path with the actual location of your JavaFX SDK.)
