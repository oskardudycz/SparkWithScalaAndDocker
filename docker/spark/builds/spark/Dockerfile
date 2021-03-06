FROM ubuntu:14.04
 
RUN apt-get -y update
RUN apt-get -y install curl
RUN apt-get -y install software-properties-common
RUN apt-get -y install apt-transport-https
RUN apt-get -y install bc
 
# JAVA
RUN \
  echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | debconf-set-selections && \
  add-apt-repository -y ppa:webupd8team/java && \
  apt-get update && \
  apt-get install -y oracle-java8-installer && \
  rm -rf /var/lib/apt/lists/* && \
  rm -rf /var/cache/oracle-jdk8-installer
ENV JAVA_HOME /usr/lib/jvm/java-8-oracle
ENV PATH $PATH:$JAVA_HOME/bin

RUN \
  echo "deb https://dl.bintray.com/sbt/debian /" | sudo tee -a /etc/apt/sources.list.d/sbt.list && \
  apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 2EE0EA64E40A89B84B2DF73499E82A75642AC823 && \
  apt-get update && \
  apt-get -y install sbt && \
  sbt --version
 
# RUN curl -s --insecure \
#  --header "Cookie: oraclelicense=accept-securebackup-cookie;" ${JAVA_ARCHIVE} \
#  | tar -xz -C /usr/local/ && ln -s $JAVA_HOME /usr/local/java 
 
# SPARK
ARG SPARK_ARCHIVE=http://ftp.ps.pl/pub/apache/spark/spark-2.2.0/spark-2.2.0-bin-hadoop2.7.tgz
ENV SPARK_HOME /usr/local/spark-2.2.0-bin-hadoop2.7
 
ENV PATH $PATH:${SPARK_HOME}/bin
RUN curl -s ${SPARK_ARCHIVE} | tar -xz -C /usr/local/

# Add a /app volume
VOLUME ["/app"]


# Add postgres to classpath
RUN \
  cd /app \
  wget http://central.maven.org/maven2/org/postgresql/postgresql/42.1.4/postgresql-42.1.4.jar

  
RUN \
  cd /usr/local/spark-2.2.0-bin-hadoop2.7/bin \
  wget http://central.maven.org/maven2/org/postgresql/postgresql/42.1.4/postgresql-42.1.4.jar

ENV CLASSPATH $CLASSPATH:/app/postgresql-42.1.4.jar

# Add crontab file in the cron directory
ADD crontab /etc/cron.d/hello-cron

# Give execution rights on the cron job
RUN chmod 0644 /etc/cron.d/hello-cron

# Create the log file to be able to run tail
RUN touch /var/log/cron.log

#Install Cron
RUN apt-get update
RUN apt-get -y install cron


# Run the command on container startup
CMD touch /var/log/cron.log && cron && tail -f /var/log/cron.log
 
WORKDIR $SPARK_HOME