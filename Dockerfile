# Dockerfile

FROM openjdk:11-jdk

# Install dependencies
RUN apt-get update && apt-get install -y wget unzip

# Install Android SDK
RUN mkdir -p /usr/local/lib/android/sdk/cmdline-tools && \
    cd /usr/local/lib/android/sdk/cmdline-tools && \
    wget https://dl.google.com/android/repository/commandlinetools-linux-8512546_latest.zip -O commandlinetools.zip && \
    mkdir latest && \
    unzip commandlinetools.zip -d latest && \
    rm commandlinetools.zip

# List directory contents to verify the structure
RUN ls -R /usr/local/lib/android/sdk/cmdline-tools/latest

# Set environment variables
ENV ANDROID_SDK_ROOT=/usr/local/lib/android/sdk
ENV PATH=$PATH:/usr/local/lib/android/sdk/cmdline-tools/latest/bin

# Install Android SDK components
RUN yes | /usr/local/lib/android/sdk/cmdline-tools/latest/cmdline-tools/bin/sdkmanager --sdk_root=/usr/local/lib/android/sdk --licenses && \
    /usr/local/lib/android/sdk/cmdline-tools/latest/cmdline-tools/bin/sdkmanager --sdk_root=/usr/local/lib/android/sdk "platform-tools" "build-tools;30.0.3" "platforms;android-30"

# Verify installation
RUN /usr/local/lib/android/sdk/cmdline-tools/latest/cmdline-tools/bin/sdkmanager --sdk_root=/usr/local/lib/android/sdk --list
