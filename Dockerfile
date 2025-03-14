FROM --platform=linux/amd64 openjdk:17-buster

# System needs
RUN apt-get update && apt-get install -y \
    curl unzip zip wget git

# sdk platfroms etc
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

# Install Android SDK and fix directory issues
RUN mkdir -p /opt/android-sdk-linux/cmdline-tools/latest && \
    curl -o sdk-tools-linux.zip https://dl.google.com/android/repository/commandlinetools-linux-10406996_latest.zip && \
    unzip -q sdk-tools-linux.zip -d /opt/android-sdk-linux/cmdline-tools/latest/ && \
    mv /opt/android-sdk-linux/cmdline-tools/latest/cmdline-tools/* /opt/android-sdk-linux/cmdline-tools/latest/ && \
    rm -rf /opt/android-sdk-linux/cmdline-tools/latest/cmdline-tools sdk-tools-linux.zip

# Set Android SDK environment variables
ENV ANDROID_HOME=/a/android-sdk-linux
ENV PATH="${ANDROID_HOME}/cmdline-tools/latest/bin:${ANDROID_HOME}/cmdline-tools/bin:${ANDROID_HOME}/platform-tools:${ANDROID_HOME}/build-tools/33.0.1:${PATH}"

# Make sdkmanager executable
RUN chmod +x /opt/android-sdk-linux/cmdline-tools/latest/bin/sdkmanager

# Accept Android SDK licenses and install required components
RUN yes | /opt/android-sdk-linux/cmdline-tools/latest/bin/sdkmanager --licenses || true && \
    /opt/android-sdk-linux/cmdline-tools/latest/bin/sdkmanager --sdk_root=/opt/android-sdk-linux \
    "platform-tools" "platforms;android-33" "build-tools;33.0.1" "cmdline-tools;latest" "system-images;android-33;google_apis;x86_64" || true

# Copy entrypoint.sh
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

# Set working directory
WORKDIR /workspace

# Copy project files into container
COPY . /workspace

# Start script
ENTRYPOINT ["/entrypoint.sh"]
