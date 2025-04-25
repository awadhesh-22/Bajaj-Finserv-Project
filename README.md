# Bajaj-Finserv-Project
📦 **Bajaj Finserv Hiring Challenge – Spring Boot Solution**
This project contains a fully functional solution developed using Spring Boot, designed to interact with a remote webhook provided by Bajaj Finserv. It demonstrates the ability to consume external APIs, process nested data structures, and respond back with the computed outcome.

🚀 **Features**
Uses Spring Boot framework for clean and scalable code.

Automatically triggers logic on application startup using @EventListener.

Efficiently handles relationship-based graph traversal.

Sends POST request with results to a specified webhook.

Includes robust error handling with retry mechanism.

⚙️ **Tech Stack**
Java 17+

Spring Boot 3.4.5

Maven

REST API (RestTemplate)

JUnit (for testing)

🔧 **Prerequisites**
Make sure you have the following installed:

Java 17 or above

Maven

Git

🛠️ **How to Build and Run**
Clone this repository

bash
Copy
Edit
git clone https://github.com/awadhesh-22/Bajaj-Finserv-Project.git
cd Bajaj-Finserv-Project
Build the project

bash
Copy
Edit
./mvnw clean package
Run the application

bash
Copy
Edit
./mvnw spring-boot:run
Or Run the executable JAR

bash
Copy
Edit
java -jar jars/bajaj-app-0.0.1-SNAPSHOT.jar

📂 **Project Structure**
css
Copy
Edit
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.bajaj
│   │   │       ├── BajajAppApplication.java
│   │   │       ├── model
│   │   │       │   ├── User.java
│   │   │       │   └── WebhookResponse.java
│   │   │       └── service
│   │   │           └── StartupService.java
│   │   └── resources
│   │       └── application.properties
├── target
│   └── bajaj-app-0.0.1-SNAPSHOT.jar

📦 **Executable JAR**
You can find the packaged .jar inside the jars/ directory.


👤 Author
**Awadhesh Jindal
Reg No: RA2211003011520
GitHub: awadhesh-22**
