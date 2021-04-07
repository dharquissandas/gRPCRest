# gRPCRest

REST API communicating with 8 GPRC servers hosted on AWS for Matrix Multiplication.

#### Server Directory
`grpc/src/main/java/com/grpc/lab1/server`

#### Client Directory
`grpc/src/main/java/com/grpc/lab1/client`

#### Runnable Directory
`grpc/`

#### REST API Client Run Command
`./mvnw spring-boot:run`

#### gRPC Server Run Command
`mvn exec:java -e -Dexec.mainClass="com.grpc.lab1.server.GrpcServer"`

### Resource Used
#### Uploading Files Resource:
https://github.com/spring-guides/gs-uploading-files
