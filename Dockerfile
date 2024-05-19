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

# COPY location of application
WORKDIR /apps
COPY . /apps

# execute jar file with maven
CMD mvn exec:java -Dexec.mainClass="Main"
