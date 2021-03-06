#include "Jeep.h"
#include "Utils.h"

Jeep jeep; //the object which control the jeep
char incomingValue = 0; //temporary var which save the values which are received by bluetooth
String bufferTmp = ""; //buffer to save higher values than 1 byte of incomingValue


/*
 * 
 * To compile this code it's necessary to remove Bluetooth module Tx Rx connection.
 * The default password of the HC-06 it's 1234
 * To connect the Android and the Arduino they have to be paired
 * 
 */



void setup() {

  Serial.begin(9600);

  // Set all the motor control pins to outputs
  pinMode(enA, OUTPUT);
  pinMode(enB, OUTPUT);
  pinMode(in1, OUTPUT);
  pinMode(in2, OUTPUT);
  pinMode(in3, OUTPUT);
  pinMode(in4, OUTPUT);

  Serial.println("Arduino ready to read commands.");
  
}

void loop() {

  
  // send data to serial only when you receive data:
  if (Serial.available() > 0) {

    incomingValue = Serial.read();
    
    String command = getReceivedCommandProcessed(incomingValue);

    if(command.equals(""))
        return;
    
    //Serial.println(command);

    boolean result = processCommand(command);
//
//    if(result == false)
//      Serial.println("\nError processing the command.");
//    
    delay(100);
//    
//    printIntro();
//    
//    String command = Serial.readString();
//
//    Serial.println("Command printed: " + command);
//
//    boolean result = processCommand(command);
//
//    if(result == false)
//      Serial.println("\nError processing the command.");
//    
//    Serial.println("\nJeep printed:");
//    Serial.println(jeep.toString());
//    Serial.println("-----------------------------");
//    
  }

}

boolean processCommand(String commandTmp)
{
  int command, value;

  //validations...
  if (commandTmp == NULL || commandTmp.length() == 0)
    return false;

  command = commandTmp.toInt();
  
  switch(command)
  {
    case dGear:
      return jeep.selectGearSelected(dGear);
    break;
    case rGear:
      return jeep.selectGearSelected(rGear);
    break;
    
    case steeringLeft:
      return jeep.steering(steeringLeft);
    break;
    case steeringRight:
      return jeep.steering(steeringRight);
    break;
    default:
      //the user is throttling
      if(command >= minThrottle && command <= maxThrottle)
        return jeep.throttle(command);    
      else //unknown command
        return false;
    break;    
  }
}

void printIntro()
{
  Serial.println("RC Commands:");
  Serial.println("1- D Gear");
  Serial.println("2- R Gear");
  Serial.println("3- Steer Left");
  Serial.println("4- Steer Right");
  Serial.println("10~110 - Throotle");
  Serial.println("Command:");
}

String getReceivedCommandProcessed(char incomingBytes)
{
  if(incomingValue == '-')
  {
    //Serial.print("bufferTmp: ");
    //Serial.print(bufferTmp);
    //Serial.print("\n");
    
    String tmp = bufferTmp;
    
    bufferTmp = "";

    return tmp;
  }
  else
  {
    bufferTmp = bufferTmp + incomingValue;
  }

  return "";
}
