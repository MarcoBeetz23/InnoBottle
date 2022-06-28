#if defined(ESP32)
#include <WiFi.h>
#include <FirebaseESP32.h>
#elif defined(ESP8266)
#include <ESP8266WiFi.h>
#include <FirebaseESP8266.h>
#endif
#include <HX711.h>
#include <addons/TokenHelper.h>
#include <addons/RTDBHelper.h>
#include <time.h>
#define WIFI_SSID "Krones-TB2019"
#define WIFI_PASSWORD "TB@Kr0nes!2019"
#define USER_EMAIL "marcobeetz3@googlemail.com"
#define USER_PASSWORD "wupiwupi7"
#define API_KEY "jzbHTrwwmXlqTSQINJ71TB6Ty5uw7SmLRDTz5Dim"
#define DATABASE_URL "https://innolab-66e3b-default-rtdb.europe-west1.firebasedatabase.app/"
 
// pin configuration for esp32
int sck = 12;
int dout1 = 5;
int dout2 = 17;
int dout3 = 16;
int dout4 = 4;
int dout5 = 32;
int dout6 = 33;
int dout7 = 25;
int dout8 = 26;
int dout9 = 27;

// amplifier modules
HX711 loadCell1;
HX711 loadCell2;
HX711 loadCell3;
HX711 loadCell4;
HX711 loadCell5;
HX711 loadCell6;
HX711 loadCell7;
HX711 loadCell8;
HX711 loadCell9;
 
// reset values for cells to arrive at 0
float resetCell1 = 43500.00;
float resetCell2 = 407200.00;
float resetCell3 = 156750.00;
float resetCell4 = 319300.00;
float resetCell5 = 67150.00;
float resetCell6 = 242000.00;
float resetCell7 = -245100.00;
float resetCell8 = 94600.00;
float resetCell9 = 14221412.00;
// conversion value from serial input to realtime physical values
float calculationFactor = 0.00004444;
// final values of cells
float valueCell1 = 0.0;
float valueCell2 = 0.0;
float valueCell3 = 0.0;
float valueCell4 = 0.0;
float valueCell5 = 0.0;
float valueCell6 = 0.0;
float valueCell7 = 0.0;
float valueCell8 = 0.0;
float valueCell9 = 0.0;
unsigned long myTime;
//Define Firebase Data object
FirebaseData fbdo;
FirebaseAuth auth;
FirebaseConfig config;
String uid;
//Main path
String databasePath;
// Child path nodes
String cell1Path = "/cell1";
String cell2Path = "/cell2";
String cell3Path = "/cell3";
String cell4Path = "/cell4";
String cell5Path = "/cell5";
String cell6Path = "/cell6";
String cell7Path = "/cell7";
String cell8Path = "/cell8";
String cell9Path = "/cell9";
String timePath = "/timestamp";
String parentPath;
unsigned long timestamp;
FirebaseJson json;
const char* ntpServer = "pool.ntp.org";
const long  gmtOffset_sec = 3600;   //Replace with your GMT offset (seconds)

// pre-declare functions
void connectToWifi();
void initCells();
void initFirebase();
void readDataFromCells();
void sendDataToFirebase();
void printCurrentTime();
unsigned long getTime();
void setup(){
    Serial.begin(115200);
    delay(2000);
    connectToWifi();
    configTime(gmtOffset_sec, 0, ntpServer);
    initCells();
    initFirebase();
    printCurrentTime();
}
void printCurrentTime() {
  struct tm timeinfo;
  if(!getLocalTime(&timeinfo)){
    Serial.println("failed!");
    return;
  }
  Serial.println(&timeinfo, "%A, %B %d %Y %H:%M:%S");
 
}
 
 
 
 
 
 
 

void connectToWifi(){
    WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
    while (WiFi.status() != WL_CONNECTED){
        Serial.println(".");
        delay(300);
    }
}
void initCells(){
    loadCell1.begin(dout1, sck);
    loadCell2.begin(dout2, sck);
    loadCell3.begin(dout3, sck);
    loadCell4.begin(dout4, sck);
    loadCell5.begin(dout5, sck);
    loadCell6.begin(dout6, sck);
    loadCell7.begin(dout7, sck);
    loadCell8.begin(dout8, sck);
    loadCell9.begin(dout9, sck);
}
void initFirebase(){
    Serial.printf("Firebase Client v%s\n\n", FIREBASE_CLIENT_VERSION);
    config.api_key = API_KEY;
    config.database_url = DATABASE_URL;
    fbdo.setResponseSize(8192);
    config.token_status_callback = tokenStatusCallback;
    databasePath = "/hi/";
}
void loop(){
    readDataFromCells();
    sendDataToFirebase();
}
void readDataFromCells(){
    valueCell1 = (loadCell1.read() - resetCell1) * calculationFactor;
    valueCell2 = (loadCell2.read() + resetCell2) * calculationFactor;
    valueCell3 = (loadCell3.read() + resetCell3) * calculationFactor;
    valueCell4 = (loadCell4.read() + resetCell4) * calculationFactor;
    valueCell5 = (loadCell5.read() + resetCell5) * calculationFactor;
    valueCell6 = (loadCell6.read() + resetCell6) * calculationFactor;
    valueCell7 = (loadCell7.read() + resetCell7) * calculationFactor;
    valueCell8 = (loadCell8.read() + resetCell8) * calculationFactor;
    valueCell9 = (loadCell9.read() + resetCell9) * calculationFactor;
    Serial.print('\n');
}

void sendDataToFirebase(){
    Firebase.begin(DATABASE_URL, API_KEY);
    if(Firebase.ready()){
        timestamp = millis();
        Serial.print("time: ");
        Serial.println(timestamp);
        parentPath = databasePath + "/" + String(timestamp);
        json.set(cell1Path.c_str(), String(valueCell1));
        json.set(cell2Path.c_str(), String(valueCell2));
        json.set(cell3Path.c_str(), String(valueCell3));
        json.set(cell4Path.c_str(), String(valueCell4));
        json.set(cell5Path.c_str(), String(valueCell5));
        json.set(cell6Path.c_str(), String(valueCell6));
        json.set(cell7Path.c_str(), String(valueCell7));
        json.set(cell8Path.c_str(), String(valueCell8));
        json.set(cell9Path.c_str(), String(valueCell9));
        json.set(timePath, String(timestamp));
        Serial.printf("Set json... %s\n", Firebase.RTDB.setJSON(&fbdo, parentPath.c_str(), &json) ? "ok" : fbdo.errorReason().c_str());
    }
}
