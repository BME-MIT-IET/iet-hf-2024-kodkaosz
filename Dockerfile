FROM almalinux

RUN yum update -y
RUN yum install -y java-11-openjdk-devel
RUN yum install -y java-11-openjdk

# Set environment
ENV JAVA_HOME /usr/lib/jvm/java-11-openjdk
ENV PATH ${PATH}:${JAVA_HOME}/bin

RUN yum -y install libX11-devel.x86_64
RUN yum -y install libXext.x86_64
RUN yum -y install libXrender.x86_64
RUN yum -y install libXtst.x86_64

RUN yum -y install maven

# COPY location of jar from local repository to the image
#COPY . /usr/local/proj
WORKDIR /apps
COPY . /apps

# Start the image with the jar file as the entrypoint
#ENTRYPOINT ["java", "-jar", "/usr/local/proj/target/hu.bme.projlab-0.5.0-SNAPSHOT.jar"]
CMD mvn exec:java -Dexec.mainClass="Main"
