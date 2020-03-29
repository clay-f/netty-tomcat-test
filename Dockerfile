FROM tomcat:jdk11-adoptopenjdk-hotspot
COPY build/libs/tomcat-servlet-app-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/hello.war