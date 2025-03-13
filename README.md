# 📌 Appium Test Automation with Docker & Android Emulator

This guide explains how to set up an Appium test environment using Docker and connect to an Android Emulator running on your local machine.

---
# **Android Emulator + Docker Appium Setup Guide**

## **📌 Ön Koşullar: Mac Üzerinde Emülatörleri Hazırlama**
Docker ortamı içinde **Appium testlerini çalıştırmadan önce**, **Mac üzerinde Android Emulator’leri başlatıp TCP/IP modunda erişilebilir hale getirmen gerekiyor**. Aşağıdaki adımları takip et:

### **1️⃣ ADB Server’ı Yeniden Başlat**
Öncelikle, **ADB’nin stabil çalıştığından emin olmak için** aşağıdaki komutları çalıştır:
```bash
adb kill-server
adb start-server
```

### **2️⃣ Android Emulator’leri TCP/IP Modunda Aç**
Aşağıdaki komutları sırayla çalıştırarak **iki emülatörü başlat ve TCP/IP moduna geçir**:

#### **İlk Emülatörü Aç (Port 5554)**
```bash
emulator -avd Emulator-1 -port 5554 -no-snapshot -no-audio -gpu off -accel off &
```
# for no window
```bash
emulator -avd Emulator-2 -port 5556 -no-window -no-audio -gpu off -accel off &

```

#### **İkinci Emülatörü Aç (Port 5556)**
```bash
emulator -avd Emulator-2 -port 5556 -no-snapshot -no-audio -gpu off -accel off &
```

#### **ADB Üzerinden TCP Moduna Geçir**
1️⃣ Emülatörlerin çalıştığını kontrol et:
```bash
adb devices
```
**Şu şekilde çıkmalı:**
```
List of devices attached
emulator-5554	device
emulator-5556	device
```

3️⃣ Emülatörleri `localhost` üzerinden bağla:
```bash
adb connect 127.0.0.1:5555
adb connect 127.0.0.1:5557
```

4️⃣ Bağlantıları kontrol et:
```bash
adb devices
```
**Çıkması gereken sonuç:**
```
List of devices attached
127.0.0.1:5555	device
127.0.0.1:5556	device
```


**Eğer hala offline gözüküyorsa:**
```bash
adb kill-server
adb start-server
adb disconnect 127.0.0.1:5555
adb disconnect 127.0.0.1:5557
adb disconnect emulator-5556
adb devices
```
**And kill emulators**
```bash
adb -s emulator-5554 emu kill || true
adb -s emulator-5556 emu kill || true
adb -s 127.0.0.1:5555 emu kill || true
adb -s 127.0.0.1:5557 emu kill || true
```
**Restart emulators**
```bash
adb connect 127.0.0.1:5555
adb connect 127.0.0.1:5557
adb devices

```






---

## **🚀 Docker Compose ile Ortamı Başlat**
Yukarıdaki **ön hazırlıkları tamamladıktan sonra** artık Docker ortamını çalıştırabilirsin.

Aşağıdaki komutlarla **Docker container'larını başlat**:
```bash
docker-compose build --no-cache
docker-compose up -d
```

✔ **Eğer tüm adımları doğru yaptıysan, Docker içindeki Appium testlerin Mac’te çalışan emülatörleri kullanabilecek!** 🎉








---
## ⚙️ **2. Setup Android Emulator on Host Machine**

### **1️⃣ List available AVDs**
```bash
emulator -list-avds
```
You should see something like:
```
Emulator-1
Emulator-2
```

Your emulator is now **ready to communicate** with Docker.

---
## 🐳 **3. Setup & Run Docker Containers**

### **1️⃣ Build the Docker Containers**
```bash
docker-compose build --no-cache
```

### **2️⃣ Start the Containers**
```bash
docker-compose up -d
```

### **3️⃣ Connect Docker to Emulator**
Once the containers are running, open a shell inside `appium-tests`:
```bash
docker exec -it appium-tests bash
```
Then connect to the emulator inside the container:
```bash
adb connect host.docker.internal:5555
adb connect host.docker.internal:5556
adb devices
```
Expected output:
```
host.docker.internal:5555    device
host.docker.internal:5556    device
```
Now the container can communicate with the emulator.

---
## 🏗 **4. Running Appium Tests**

### **1️⃣ Start Appium Server in Container**
Inside the `appium-tests` container, run:
```bash
appium --relaxed-security --log-timestamp
```

### **2️⃣ Configure Test Framework**
Set your desired capabilities in your test framework (e.g., Java, Python):

```java
capabilities.setCapability("deviceName", "host.docker.internal:5555");
capabilities.setCapability("platformName", "Android");
capabilities.setCapability("automationName", "UiAutomator2");
capabilities.setCapability("appPackage", "com.example");
capabilities.setCapability("appActivity", ".MainActivity");
capabilities.setCapability("noReset", true);
```

### **3️⃣ Run Tests**
From the `appium-tests` container:
```bash
mvn clean test
```

---
## 🔄 **5. Stopping & Cleaning Up**

### **Stop Containers:**
```bash
docker-compose down -v
```

### **Restart Everything:**
```bash
docker-compose build --no-cache
docker-compose up -d
```

---
## 🎯 **Troubleshooting**

**1️⃣ Appium Server Not Starting?**
- Make sure the **emulator is connected** (`adb devices`)
- Try restarting ADB:
```bash
adb kill-server
adb start-server
```

**2️⃣ Emulator Not Showing in Docker?**
- Ensure **ADB TCP mode** is enabled (`adb tcpip 5555`)
- Use **host.docker.internal** instead of `localhost`

**3️⃣ Connection Refused in ADB?**
- Ensure the emulator is running (`emulator -list-avds`)
- Restart ADB and reconnect:
```bash
adb kill-server
adb start-server
adb connect 127.0.0.1:5555
```

---

**Tests**

```bash
mvn test -Djunit.jupiter.execution.parallel.enabled=false
```

## copy file 
docker cp appium-tests:/workspace/test-output ./test-output


-DrunOnLocal=true => localde çalışmaya uygun ayarlar uygulanır device olarak .local.device.properties fileları cekilir ve globalnode path içinde local makine configi okunur

