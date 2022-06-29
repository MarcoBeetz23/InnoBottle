/// DUMMY INCLUDES /// delete it later on
#include <cstdlib>
#include <ctime>
//////////

// Util packages
#include <WiFi.h>
#include <time.h>

// Firebase packages
#include <FirebaseESP32.h>
#include <addons/TokenHelper.h>
#include <addons/RTDBHelper.h>

// Load cell package
#include <HX711.h>

// WiFi constants
#define WIFI_SSID "iPhone"
#define WIFI_PASSWORD "cdudocv74rzmx"

// Firebase constants
#define USER_EMAIL "marcobeetz3@googlemail.com"
#define USER_PASSWORD "wupiwupi7"
#define API_KEY "jzbHTrwwmXlqTSQINJ71TB6Ty5uw7SmLRDTz5Dim"
#define DATABASE_URL "https://innolab-66e3b-default-rtdb.europe-west1.firebasedatabase.app/"


////////////// FILE SETUP ///////////////////////////////

unsigned long timeDelta;
//Define Firebase Data object
FirebaseData fbdo;
FirebaseAuth auth;
FirebaseConfig config;
String uid;

//Main path, will be overwritten later on, will tell ESP32 to which node the data will be attached
String pathToSensorRun = "";

//Further nodes for load cell path
String pathCell1 = "";
String pathCell2 = "";
String pathCell3 = "";
String pathCell4 = "";
String pathCell5 = "";
String pathCell6 = "";
String pathCell7 = "";
String pathCell8 = "";
String pathCell9 = "";

// Firebase pull values
String currentState = ""; // holds state which is set in Android App (pause / init / active)
String sensorSeriesName = ""; // holds name of sensor series (e.g. CocaCola_Knetzgau_V1)
String sensorSeriesInitTime = ""; // holds entire timestamp when Android App sets state to "init"
String sensorSeriesInitId = ""; // holds the unique ID of the sensor series (will be important later)
int childrenCount = 0; // holds the counter of sensor runs, thus how many children nodes will be created

// time in milliseconds since the start of the application
unsigned long timedelta;

// declare Firebase variables
FirebaseJson json;
const char* ntpServer = "pool.ntp.org";

////////////////////////////////////////////////////////
///////////////// LOAD CELL SETUP //////////////////////
// pin configuration for ESP32 (may be adapter later @todo), used as single variables for better separability and readability
int sck = 12; // tested, may be adapted
int dout1 = 27; // tested, may be adapted
int dout2 = 4; // tested, may be adapted
int dout3 = 25; // tested, may be adapted
int dout4 = 16; // tested, may be adapted
int dout5 = 26; // tested, may be adapted
int dout6 = 17; // tested, may be adapted
int dout7 = 33; // tested, may be adapted
int dout8 = 5; // tested, may be adapted
int dout9 = 32; // tested



// amplifier modules
HX711 amplifierCell1;
HX711 amplifierCell2;
HX711 amplifierCell3;
HX711 amplifierCell4;
HX711 amplifierCell5;
HX711 amplifierCell6;
HX711 amplifierCell7;
HX711 amplifierCell8;
HX711 amplifierCell9;

// declare and init an array of floats to reset the HX711 values, so they arrive at 0 (normalization)
float resetCell1 = 264669.00;
float resetCell2 = 377778.00;
float resetCell3 = 133271.00;
float resetCell4 = 322790.00;
float resetCell5 = 45196.00;
float resetCell6 = 222897.00;
float resetCell7 = -292954.00;
float resetCell8 = 118781.00;
float resetCell9 = 319.00;















// conversion value from serial input to realtime physical values
float calculationFactor = 0.00004444;

// finally, we declare an array of 9 floats for all 9 normalized and converted load cell values which will be send to Firebase later on
float loadCellValues[9];

////////////////////////////////////////////////////////////

void setup(){
    Serial.begin(115200);
    delay(2000);
    connectToWifi();
    initCells();
    initFirebase();
}

void connectToWifi(){
    WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
    while (WiFi.status() != WL_CONNECTED){
        Serial.println(".");
        delay(300);
    }
}

void initCells(){
    amplifierCell1.begin(dout1, sck);
    amplifierCell2.begin(dout2, sck);
    amplifierCell3.begin(dout3, sck);
    amplifierCell4.begin(dout4, sck);
    amplifierCell5.begin(dout5, sck);
    amplifierCell6.begin(dout6, sck);
    amplifierCell7.begin(dout7, sck);
    amplifierCell8.begin(dout8, sck);
    amplifierCell9.begin(dout9, sck);
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
    
    if(currentState == "ready"){
      //initNewDataPath with childrenCounter
      childrenCount = childrenCount + 1;
    }

    if(currentState == "active"){
      readDataFromCells();
      sendDataToFirebase();
        delay(300);
    }
}

void getCurrentStateFromFirebase(){
  Serial.println("hi");
    if(Firebase.ready()){
        Firebase.get(fbdo, "State");
        currentState = fbdo.to<String>();
        Serial.println(currentState);
    }
}


void readDataFromCells(){
    loadCellValues[0] = (amplifierCell1.read() + resetCell1) * calculationFactor;
    loadCellValues[1] = (amplifierCell2.read() + resetCell2) * calculationFactor;
    loadCellValues[2] = (amplifierCell3.read() + resetCell3) * calculationFactor;
    loadCellValues[3] = (amplifierCell4.read() + resetCell4) * calculationFactor;
    loadCellValues[4] = (amplifierCell5.read() + resetCell5) * calculationFactor;
    loadCellValues[5] = (amplifierCell6.read() + resetCell6) * calculationFactor;
    loadCellValues[6] = (amplifierCell7.read() + resetCell7) * calculationFactor;
    loadCellValues[7] = (amplifierCell8.read() + resetCell8) * calculationFactor;
    loadCellValues[8] = (amplifierCell9.read() + resetCell9) * calculationFactor;


    Serial.println(loadCellValues[0]);
    Serial.println(loadCellValues[1]);
    Serial.println(loadCellValues[2]);
    Serial.println(loadCellValues[3]);
    Serial.println(loadCellValues[4]);
    Serial.println(loadCellValues[5]);
    Serial.println(loadCellValues[6]);
    Serial.println(loadCellValues[7]);
    Serial.println(loadCellValues[8]);

}


void sendDataToFirebase(){
    timedelta = millis();
    pathCell1 = "/SensorData/" + String(childrenCount) + "/" + String(timedelta) + "/" + "LC1";
    pathCell2 = "/SensorData/" + String(childrenCount) + "/" + String(timedelta) + "/" + "LC2";
    pathCell3 = "/SensorData/" + String(childrenCount) + "/" + String(timedelta) + "/" + "LC3";
    pathCell4 = "/SensorData/" + String(childrenCount) + "/" + String(timedelta) + "/" + "LC4";
    pathCell5 = "/SensorData/" + String(childrenCount) + "/" + String(timedelta) + "/" + "LC5";
    pathCell6 = "/SensorData/" + String(childrenCount) + "/" + String(timedelta) + "/" + "LC6";
    pathCell7 = "/SensorData/" + String(childrenCount) + "/" + String(timedelta) + "/" + "LC7";
    pathCell8 = "/SensorData/" + String(childrenCount) + "/" + String(timedelta) + "/" + "LC8";
    pathCell9 = "/SensorData/" + String(childrenCount) + "/" + String(timedelta) + "/" + "LC9";

    Serial.println(pathCell1);
    
    Firebase.setString(fbdo, pathCell1, loadCellValues[0]);
    Firebase.setString(fbdo, pathCell2, loadCellValues[1]);
    Firebase.setString(fbdo, pathCell3, loadCellValues[2]);
    Firebase.setString(fbdo, pathCell4, loadCellValues[3]);
    Firebase.setString(fbdo, pathCell5, loadCellValues[4]);
    Firebase.setString(fbdo, pathCell6, loadCellValues[5]);
    Firebase.setString(fbdo, pathCell7, loadCellValues[6]);
    Firebase.setString(fbdo, pathCell8, loadCellValues[7]);
    Firebase.setString(fbdo, pathCell9, loadCellValues[8]);

}
