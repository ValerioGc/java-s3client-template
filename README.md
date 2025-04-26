# S3Utils Spring Boot Template

A Spring Boot template project for **rapid AWS S3 integration**, with:

- **Reusable S3 client** (`S3ClientUtil`) that you can inject anywhere in your code.
- **CRUD REST controller** (`S3RESTController`) demonstrating how to:
  - **Upload** a file to S3
  - **Download** (read) a file from S3
  - **Replace** (re-upload) an existing file
  - **Delete** a file
- **Exception handling** via custom exceptions and a global `@RestControllerAdvice` for clean, JSON-structured error responses.


## Prerequisites

- **Java 17** (or later)
- **Maven 3.6+**
- An **AWS account** and an existing S3 bucket
- **AWS credentials** (access key & secret key) with permissions to read/write/delete objects in that bucket

## Dependencies

- **Spring Boot Starter Web** (embedded Tomcat, MVC, etc.)
- **AWS SDK v2 – `software.amazon.awssdk:s3`**
- **SLF4J & Logback** for logging
- **Spring Boot Starter Test** for unit/integration tests

## Quickstart

1. **Configure AWS credentials** in `src/main/resources/application.yml` (or `application.properties`):

   ```yaml
   cloud:
     aws:
       s3:
         region:       us-east-1
         bucket-name:  your-bucket-name
         credentials:
           app-key:     YOUR_AWS_ACCESS_KEY
           secret-key:  YOUR_AWS_SECRET_KEY
   ```

2. **Build the project**

   ```bash
   mvn clean install
   mvn package
   ```

3. **Run the JAR**

   ```bash
   java -jar target/s3utils-0.0.1.jar
   ```

4. **Test the REST endpoints**

   - **Upload**

     ```bash
     curl -v -F "file=@/path/to/photo.png" http://localhost:8080/s3/create
     ```

   - **Download**

     ```bash
     curl -v http://localhost:8080/s3/read?key=photo.png --output photo.png
     ```

   - **Delete**

     ```bash
     curl -v -X DELETE "http://localhost:8080/s3/delete?key=photo.png&type=images"
     ```

## Project Structure

```
src/main/java/it/s3utils/
│
├─ client/
│   └─ S3ClientUtil.java
│   └─ S3ClientUtilImpl.java
│
├─ controller/
│   └─ S3RESTController.java
│
├─ exception/
│   ├─ ApiError.java
│   ├─ FileNotFoundException.java
│   ├─ FileUploadException.java
│   ├─ FileDeletionException.java
│   ├─ S3StorageException.java
│   └─ RestExceptionHandler.java
│
└─ S3UtilsApplication.java
```
