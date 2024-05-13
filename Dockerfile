FROM openjdk:11

# Set environment
ENV JAVA_HOME /opt/jdk
ENV PATH ${PATH}:${JAVA_HOME}/bin   

# COPY location of jar from local repository to the image
COPY ./target /usr/local/target

# Start the image with the jar file as the entrypoint
ENTRYPOINT ["java", "-jar", "/usr/local/target/hu.bme.projlab-0.5.0-SNAPSHOT.jar"]
