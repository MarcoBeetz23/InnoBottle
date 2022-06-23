#include <HX711.h>

//clock
int sck = 12;

// ping configuration
// dout.... = loadCell  ---  Number .... = GPIO Pin
int dout1 = 5;
int dout2 = 17;
int dout3 = 16;
int dout4 = 4;
int dout5 = 32;
int dout6 = 33;
int dout7 = 25;
int dout8 = 26;
int dout9 = 27;

// hx711 modules
HX711 loadCell1;
HX711 loadCell2;
HX711 loadCell3;
HX711 loadCell4;
HX711 loadCell5;
HX711 loadCell6;
HX711 loadCell7;
HX711 loadCell8;
HX711 loadCell9;

// normalize the value to arrive at 0
float resetCell1 = 30000.00;
float resetCell2 = 407200.00;
float resetCell3 = 232131.00;
float resetCell4 = 248325.00;
float resetCell5 = 241413.00;
float resetCell6 = 2415135.00;
float resetCell7 = 241235.00;
float resetCell8 = 13241.00;
float resetCell9 = 1254315.00;

// calculation factor
float calculationFactor = 0.000044444;

// pre-declare variables
float valueCell1 = 0.0;
float valueCell2 = 0.0;
float valueCell3 = 0.0;
float valueCell4 = 0.0;
float valueCell5 = 0.0;
float valueCell6 = 0.0;
float valueCell7 = 0.0;
float valueCell8 = 0.0;
float valueCell9 = 0.0;


void setup() {
  Serial.begin(115200);
  delay(2000);
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

void loop() {
  readDataFromCells();
  printCells();
  Serial.println("------");
  delay(1000);
}

void readDataFromCells(){
  valueCell1 = (loadCell1.read() + resetCell1) * calculationFactor;
  valueCell2 = (loadCell2.read() + resetCell2) * calculationFactor;  
  valueCell3 = (loadCell3.read() + resetCell3) * calculationFactor;  
  valueCell4 = (loadCell4.read() + resetCell4) * calculationFactor;  
  valueCell5 = (loadCell5.read() + resetCell5) * calculationFactor;  
  valueCell6 = (loadCell6.read() + resetCell6) * calculationFactor;  
  valueCell7 = (loadCell7.read() + resetCell7) * calculationFactor;  
  valueCell8 = (loadCell8.read() + resetCell8) * calculationFactor;  
  valueCell9 = (loadCell9.read() + resetCell9) * calculationFactor;  

}

void printCells(){
  Serial.println(valueCell1);
  Serial.println(valueCell2);
  Serial.println(valueCell3);
  Serial.println(valueCell4);
  Serial.println(valueCell5);
  Serial.println(valueCell6);
  Serial.println(valueCell7);
  Serial.println(valueCell8);
  Serial.println(valueCell9);

}
