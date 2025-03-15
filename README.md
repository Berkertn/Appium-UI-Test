# Appium Test Automation with Docker & Android Emulator

## **1. Setup Emulators on Host Machine**

### **1. Init emulators**

1. Open Android Studio
2. More Actions -> Virtual Device Manager -> Device Manager Page should open
3. Run the emulator which added in devices folder with properties file

**- Extra:**

- If you want to open the emulator with specific port and cli: (emulator name get from `emulator -list-avds`)

```bash
emulator -avd Emulator-1 -port 5554
```

### **2. Check Emulator Up**

1. Check the emulator is up and running with the following command:

```bash
adb devices
```

2. You should see the following output:

```emulator-5554	device```

3. Open port for emulator to connect from docker container(5557 is for emulator-5556 that's my second emulator I want to
   use)
   ``emulator-5554`` can open with 5555 port by default like emulatorId+1 is port we can use !!

```bash
adb connect 127.0.0.1:5555
adb connect 127.0.0.1:5557
```

4. Check the devices are connected or not with the following command:

```bash 
adb devices
```

5. You should see the following output:

```
List of devices attached
127.0.0.1:5555	device
emulator-5554	device
```

---

## **2. Setup & Run Docker Containers**

### **1.Build the Docker Containers**

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
4. If not please check the troubleshooting section.

### !! **Troubleshooting**

#### **1. Connection Refused in ADB?**

- To be sure emulators created (`emulator -list-avds`)
- Restart ADB and reconnect:

```bash
adb kill-server
adb start-server
adb connect 127.0.0.1:5555
adb connect 127.0.0.1:5557
```

#### **2. Emulator Can not be found**

1. Connect to Appium Docker
2. Once the containers are running, open a shell inside `appium-tests` using:

```bash
docker exec -it appium-tests bash
```

- When you got into the container check the devices are listed or not with:

```bash
adb devices
```

- If the no device is listed and in your local machine devices are showing please trigger the following part:
  `(:5555 is a port which we open from local machine to docker containers if you added with different port please change
  it. Also, connection command added in entrypoint.sh)`

```bash
adb connect host.docker.internal:5555
adb connect host.docker.internal:5557
adb devices
```

**- Expected output:**

```
host.docker.internal:5555    device
host.docker.internal:5556    device
```

#### **3. unAuth Error in ADB devices list**

- When you compose up the docker containers entrypoint.sh will execute connect to emulator and in that time device shows
  a pop to give permissions. If you can tap to "yes" and check again with ``adb devices`` you can see the auth problem
  will fix.

#### **4. Emulators crashed and restart emulators**

```bash
adb kill-server
adb start-server
adb disconnect 127.0.0.1:5555
adb disconnect 127.0.0.1:5557
adb disconnect emulator-5556
adb disconnect emulator-5554
adb devices
```

**And to kill emulators**

```bash
adb -s emulator-5554 emu kill || true
adb -s emulator-5556 emu kill || true
adb -s 127.0.0.1:5555 emu kill || true
adb -s 127.0.0.1:5557 emu kill || true
```

#### **5. Docker had an error with containers**
**Docker**
-clean stopped containers etc.

```bash
docker container prune -f
docker image prune -f
docker network prune -f
```

---

## **3.  Run Tests**

If all precondition is done, you can run the tests with jenkins pipeline or manually with the following command:

*Jenkins Pipeline*

1. Create a new pipeline job (you can insert the script from "Jenkinsfile" in the root)
2. Run the job (Parameters explained below and also in parameter section in pipeline)
3. Check the test results in the artifacts or console output


---

***If run is local tests will get the properties only suffix with: "*.local.device.properties"**  
**If run is docker tests will get the properties only suffix with: "*.docker.device.properties"**

----
readme'e eklemek istediklerime örnek olarak:

#Single Thread run in Local Machine (test run sadece pipeline'da değil localde olabilir ondan böyle bi section da istedim)

```bash
mvn test -Djunit.jupiter.execution.parallel.enabled=false
```
(bunun gibi bi ton değişimler var projede dosyalarını inceleyerek üretirsen sevinirim.)
## copy file
(manuel kopyalamak için kullanılabilir)
docker cp appium-tests:/workspace/test-output ./test-output
----

# Test Run with Parallel Execution için gereli olan parmeters ve commandlar
mvn test -Djunit.jupiter.execution.parallel.enabled=true -DrunOnLocal=true
-Djunit.jupiter.execution.parallel.mode.default=concurrent
-Djunit.jupiter.execution.parallel.mode.classes.default=concurrent
-Djunit.jupiter.execution.parallel.config.fixed.parallelism=2

-DapiKey => config de olan api keyi değiştirmek için kullanılır
-DrunOnLocal => localde mi docker da mı calısacağını belirlemek için kullanılır default'u false'dur yani docker'a göre
ayarlanmıstır
-Dtag => tag for tests to filter
