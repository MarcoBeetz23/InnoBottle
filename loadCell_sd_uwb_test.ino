#include <HX711.h>
#include <WiFi.h>
#include <WiFiUdp.h>
#include <time.h>

#define XSTR(x) #x
#define STR(x) XSTR(x)

//Sd card
#include <FS.h>
#include <FSImpl.h>
#include <vfs_api.h>
#include <SPI.h>
#include <SD.h>
#include <Preferences.h>

Preferences preferences;
 
// chip select IO pin for connecting SD Card module with ESP
#define CS  4
#define MISO 19
#define SCK 18
#define MOSI 23

//sd card file name
File file;
unsigned int counter;
unsigned long timedelta;


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

HX711 lc1, lc2, lc3, lc4, lc5, lc6, lc7, lc8, lc9;

//float v1, v2, v3, v4, v5, v6, v7, v8, v9 = 0.0;

//debug
float v1;

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
float calculationFactor = 0.00004444;

// UDP
WiFiUDP udp;

// WiFi credentials
char* SSID;
char* PW;
char* IP;
uint16_t PORT;

void setup(){
  setCpuFrequencyMhz(80);
  Serial.begin(115200);
  initSDCard();
  initCells();
  initWiFi();
  //sd card
  handleFileNaming();
}

void handleFileNaming() {
  //create a new load cell csv file for every restart of the esp to prevent too large files
  preferences.begin("my-app", false);
  counter = preferences.getUInt("counter", 0);
  counter++;
  preferences.putUInt("counter", counter);
  preferences.end();
  String filename = "/loadCells" + String(counter) + ".csv";
}

void loop(){
  retrieveValues();
  convertValues();
  sendData();
}

void initSDCard(){
    Serial.print("Initializing SD card...");
    // see if the card is present and can be initialized:
    SD.begin(CS);
    //SPIClass spi = SPIClass(VSPI);
    //spi.begin(SCK, MISO, MOSI, CS);
    if (!SD.begin(CS)) {
      Serial.println("Card Mount Failed");
      return;
    } 
    Serial.println("card initialized.");
}

void appendFile(fs::FS &fs, const char * path, const char * message){
    Serial.printf("Appending to file: %s\n", path);
 
    file = fs.open(path, FILE_APPEND);
    if(!file){
        Serial.println("Failed to open file for appending");
        return;
    }
    if(file.println(message)){
        Serial.println("Message appended");
    } else {
        Serial.println("Append failed");
    }
    file.close();
}

void initCells(){
  lc1.begin(dout1, sck);
  //lc2.begin(dout2, sck);
  //lc3.begin(dout3, sck);
  //lc4.begin(dout4, sck);
  //lc5.begin(dout5, sck);
  //lc6.begin(dout6, sck);
  //lc7.begin(dout7, sck);
  //lc8.begin(dout8, sck);
  //lc9.begin(dout9, sck);
}

void initWiFi(){
  SSID = "Krones-TB2019";
  PW = "TB@Kr0nes!2019";
  IP = "192.168.178.47";
  PORT = 5700;

  WiFi.mode(WIFI_STA);
  WiFi.begin(SSID, PW);
  udp.begin(PORT);
  
  
}

void retrieveValues(){
  timedelta = millis();
  
  v1 = lc1.read();
  //v2 = lc2.read();
  //v3 = lc3.read();
  //v4 = lc4.read();
  //v5 = lc5.read();
  //v6 = lc6.read();
  //v7 = lc7.read();
  //v8 = lc8.read();
  //v9 = lc9.read();
}

void convertValues(){
  v1 = (v1 + resetCell1) * calculationFactor;
  //v2 = (v2 + resetCell2) * calculationFactor;
  //v3 = (v3 + resetCell3) * calculationFactor;
  //v4 = (v4 + resetCell4) * calculationFactor;
  //v5 = (v5 + resetCell5) * calculationFactor;
  //v6 = (v6 + resetCell6) * calculationFactor;
  //v7 = (v7 + resetCell7) * calculationFactor;
  //v8 = (v8 + resetCell8) * calculationFactor;
  //v9 = (v9 + resetCell9) * calculationFactor;
}

void sendData(){
  String buffer = String("{\"loadCellValues\":{") +
                        "\"cell1\":" + v1 + "}}";
                        
    udp.beginPacket(IP, PORT);
    udp.write((uint8_t*)buffer.c_str(), buffer.length());    
    udp.endPacket();
    Serial.println(buffer);
    //write to sd card
    String filename = "/loadCells" + String(counter) + ".csv";
    String dataString = String(v1) + ", " + String(timedelta);
    appendFile(SD, filename.c_str(), dataString.c_str());
}  


