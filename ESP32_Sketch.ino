#include <WiFi.h>
#include <FirebaseESP32.h>
#include <addons/TokenHelper.h>
#include <addons/RTDBHelper.h>

#define WIFI_SSID "MBUX-42"
#define WIFI_PASSWORD "$olincoTourBite1"
#define USER_EMAIL "marcobeetz3@googlemail.com"
#define USER_PASSWORD "wupiwupi7"
#define API_KEY "jzbHTrwwmXlqTSQINJ71TB6Ty5uw7SmLRDTz5Dim"
#define DATABASE_URL "https://innolab-66e3b-default-rtdb.europe-west1.firebasedatabase.app/"

unsigned long myTime;
//Define Firebase Data object
FirebaseData fbdo;
FirebaseAuth auth;
FirebaseConfig config;
String uid;
//Main path
String databasePath;
// Child path nodes

String a = "";
String result = "";

// Firebase pull values
String currentState = "";
String sensorSeriesName = "";
String sensorSeriesInitTime = "";
String sensorSeriesInitId = "";
String testPath = "";

int childrenCount = 100;

// Firebase Path settings
String parentSensorSeriesPath = ""; // depth = 0;
String childSensorSeriesPath = ""; // depth = 1;
String childSensorRunPath = ""; // depth = 2;
String childSensorRunValuesPath = ""; // depth = 3;

FirebaseJson json;
const char* ntpServer = "pool.ntp.org";

// pre-declare functions
void connectToWifi();
void initFirebase();
void buildPath();

void setup(){
    Serial.begin(115200);
    delay(2000);
    connectToWifi();
    initFirebase();
}

void connectToWifi(){
    WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
    while (WiFi.status() != WL_CONNECTED){
        Serial.println(".");
        delay(300);
    }
}

void initFirebase(){
    Serial.printf("Firebase Client v%s\n\n", FIREBASE_CLIENT_VERSION);
    config.api_key = API_KEY;
    config.database_url = DATABASE_URL;
    fbdo.setResponseSize(4096);
    config.token_status_callback = tokenStatusCallback;
    Firebase.begin(DATABASE_URL, API_KEY);
}
void loop(){
    getCurrentStateFromFirebase();
    if(currentState == "init"){
        pullName();
        pullTimestamp();
        pullChildrenCount();
        pullId();
        buildPath();
    }

    if(Firebase.ready()){
        testPath = "/SensorTest";
    }
    if(currentState == "active"){
        sendDataToFirebase();
    }
    delay(500);
}

void buildPath(){
    parentSensorSeriesPath = "SensorSeries/";
    childSensorSeriesPath = parentSensorSeriesPath 
                            + sensorSeriesName + "_"  
                            + sensorSeriesInitId;
    Serial.println(childSensorSeriesPath);
}

void getCurrentStateFromFirebase(){
    Firebase.get(fbdo, "State");
    currentState = fbdo.to<String>();
    Serial.println(currentState);
}

void pullName(){
    Firebase.get(fbdo, "SensorSeriesValues/Name");
    sensorSeriesName = fbdo.to<String>();
    Serial.println(sensorSeriesName);
}

void pullTimestamp(){
    Firebase.get(fbdo, "SensorSeriesValues/Date");
    sensorSeriesInitTime = fbdo.to<String>();
    Serial.println(sensorSeriesInitTime);
}

void pullChildrenCount(){
    Firebase.get(fbdo, "SensorSeriesValues/ChildrenCout");
    childrenCount = fbdo.to<int>();
    Serial.println(childrenCount);
}

void pullId(){
    Firebase.get(fbdo, "SensorSeriesValues/Id");
    sensorSeriesInitId = fbdo.to<String>();
    Serial.println(sensorSeriesInitId);
}

void sendDataToFirebase(){
    // FIREBASE SET STRING IS THE FUNCTION CALL TO BUILD EVERYTHING
    testPath = "/SensorTest";
    Firebase.setString(fbdo, "/SensorTest", "hi");
}
