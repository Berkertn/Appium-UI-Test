FROM jenkins/jenkins:lts

USER root

RUN apt-get update && apt-get install -y \
    sudo \
    curl \
    git \
    unzip \
    docker.io \
    maven

RUN usermod -aG docker jenkins

RUN mvn -version

USER jenkins
