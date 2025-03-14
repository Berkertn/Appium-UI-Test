FROM --platform=linux/amd64 openjdk:17-buster

# System update and install required tools
RUN apt-get update && apt-get install -y \
    curl unzip zip wget git

# En son Android SDK platform-tools'u indir ve yükle
RUN curl -o platform-tools.zip https://dl.google.com/android/repository/platform-tools-latest-linux.zip && \
    unzip platform-tools.zip -d /opt/android-sdk-linux/ && \
    rm platform-tools.zip && \
    chmod +x /opt/android-sdk-linux/platform-tools/adb

ENV PATH="/opt/android-sdk-linux/platform-tools:$PATH"

# Install Maven
RUN wget https://archive.apache.org/dist/maven/maven-3/3.9.1/binaries/apache-maven-3.9.1-bin.tar.gz && \
    tar xvz -C /opt/ -f apache-maven-3.9.1-bin.tar.gz && \
    ln -s /opt/apache-maven-3.9.1/bin/mvn /usr/bin/mvn && \
    rm apache-maven-3.9.1-bin.tar.gz

# Install Node.js, Appium, and Appium Doctor
RUN curl -fsSL https://deb.nodesource.com/setup_20.x | bash - && \
    apt-get install -y nodejs && \
    npm install -g appium appium-doctor && \
    appium driver install uiautomator2

# Copy entrypoint.sh
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

# Set working directory
WORKDIR /workspace

# Copy project files into container
COPY . /workspace

# Start script
ENTRYPOINT ["/entrypoint.sh"]
