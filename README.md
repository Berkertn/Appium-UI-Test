# Appium Test Automation with Docker & Android Emulator

## **1. Setup Emulators on Host Machine**

### **1. Initialize Emulators**

1. Open Android Studio
2. More Actions -> Virtual Device Manager -> Device Manager Page should open
3. Run the emulator which is added in the devices folder with the properties file

**- Extra:**

- If you want to open the emulator with a specific port via CLI: (get emulator name from `emulator -list-avds`)

```bash
emulator -avd Emulator-1 -port 5554
```

### **2. Check Emulator Status**

1. Verify that the emulator is up and running with the following command:

```bash
adb devices
```

2. Expected output:

```
emulator-5554	device
```

3. Open the port for the emulator to connect from a Docker container (e.g., 5557 is used for `emulator-5556`):

```bash
adb connect 127.0.0.1:5555
adb connect 127.0.0.1:5557
```

4. Check the connected devices again:

```bash
adb devices
```

5. Expected output:

```
List of devices attached
127.0.0.1:5555	device
emulator-5554	device
```

---

## **2. Setup & Run Docker Containers**

### **1. Build the Docker Containers**

```bash
docker-compose build --no-cache
```

### **2. Start the Containers**

```bash
docker-compose up -d
```

### **3. Check the adb device is listed on docker container**

1. Execute following command to get into the container:

```bash
docker exec -it appium-tests /bin/bash
```

2. Check the devices are listed or not with:

```bash
adb devices
```

3. Expected output:

```
root@1955b7137bf0:/workspace# adb devices
List of devices attached
host.docker.internal:5555	device
```

4. If the device is not listed, please check the troubleshooting section.

---

## **3. Run Tests**

### **1. Single Thread Run in Local Machine**

```bash
mvn test -Djunit.jupiter.execution.parallel.enabled=false -DrunOnLocal=true
```

- it will use the `*.local.device.properties` files
- it will run the tests in a single thread with tag of "wip" (because wip is default tag in the `pom.xml`)

### **2. Run Tests with Parallel Execution**

```bash
mvn test \
    -Djunit.jupiter.execution.parallel.enabled=true \
    -DrunOnLocal=true \
    -Djunit.jupiter.execution.parallel.mode.default=concurrent \
    -Djunit.jupiter.execution.parallel.mode.classes.default=concurrent \
    -Djunit.jupiter.execution.parallel.config.fixed.parallelism=2
```

**OR**

```bash
mvn clean test -DrunOnLocal=true
```
(Rest of the values are default in the `junit-platform.properties`)

### **3. Test Run Parameters**

- **`-DapiKey=<your_api_key>`** → Overrides the API key in the config
- **`-DrunOnLocal=true/false`** → Determines whether the tests run on local or inside Docker (default: `false`, meaning
  Docker is used)
  - ``-.local.device.properties`` files are used when `true`, and ``-.docker.device.properties`` files are used when
    `false`
- **`-Dtag=<tag_name>`** → Filters test cases based on specified JUnit/Cucumber tags

### **4. Copy Test Output Files**

To manually copy test output files from the Docker container:

```bash
docker cp appium-tests:/workspace/test-output ./test-output
```

---

## **4. Device Configuration Details**

### **Local Emulator Configuration** (`emulator.local.device.properties`):

```properties
deviceName=Emulator-1
deviceUDID=emulator-5554
platform=android
port=4724
isRealDevice=false
isOnUse=true
```

### **Docker Emulator Configurations:**

**First Emulator (`emulator.first.docker.device.properties`):**

```properties
deviceName=Emulator-1
deviceUDID=host.docker.internal:5555
platform=android
port=4724
isRealDevice=false
isOnUse=true
```

**Second Emulator (`emulator.second.docker.device.properties`):**

```properties
deviceName=Emulator-2
deviceUDID=host.docker.internal:5557
platform=android
port=4725
isRealDevice=false
isOnUse=true
```

### **How Device Configurations Are Used in Tests**

- If `-DrunOnLocal=true`, the system loads **`*.local.device.properties`** files and only uses devices where
  `isOnUse=true`.
- If `-DrunOnLocal=false` (default), the system loads **`*.docker.device.properties`** files and only uses devices where
  `isOnUse=true`.

---

## **5. Troubleshooting**

### **1. Connection Refused in ADB?**

- Ensure emulators are created (`emulator -list-avds`)
- Restart ADB and reconnect:

```bash
adb kill-server
adb start-server
adb connect 127.0.0.1:5555
adb connect 127.0.0.1:5557
```

### **2. Emulator Not Found**

- Open a shell inside the Docker container:

```bash
docker exec -it appium-tests bash
```

- Check if devices are listed:

```bash
adb devices
```

- If no device is listed, try reconnecting:

```bash
adb connect host.docker.internal:5555
adb connect host.docker.internal:5557
adb devices
```

Expected output:

```
host.docker.internal:5555    device
host.docker.internal:5557    device
```

### **3. Unauthorized Device in ADB**

- When the Docker container starts, the device may prompt for authorization.
- Tap "Yes" on the device and check again:

```bash
adb devices
```

### **4. Restarting Crashed Emulators**

```bash
adb kill-server
adb start-server
adb disconnect 127.0.0.1:5555
adb disconnect 127.0.0.1:5557
adb disconnect emulator-5556
adb disconnect emulator-5554
adb devices
```

To force-kill emulators:

```bash
adb -s emulator-5554 emu kill || true
adb -s emulator-5556 emu kill || true
adb -s 127.0.0.1:5555 emu kill || true
adb -s 127.0.0.1:5557 emu kill || true
```

### **5. Clearing Docker Errors**

To clean up stopped containers and networks:

```bash
docker container prune -f
docker image prune -f
docker network prune -f
```

### **6. adb doesn't work: adb devices failed to check server version: protocol fault (couldn't read status): Connection reset by peer**

```bash
adb devices    
adb: failed to check server version: protocol fault (couldn't read status): Connection reset by peer
```
1. check if any result returning from the following command:
```bash
lsof -i :5037
```
2. If there is any result, kill the process:
```bash
kill -9 $(lsof -t -i :5037)
```
3. Restart adb:
```bash
adb kill-server            
adb start-server
adb devices
``
4. If the problem persists, restart the machine

