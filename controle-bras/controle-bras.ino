#include <Servo.h>

Servo servoPince;
Servo servoBase1;
Servo servoBase2;
Servo servoBras1;
Servo servoBras2;
Servo servoBras3;

void setup() {
  servoPince.attach(0);
  servoBase1.attach(1);
  servoBase2.attach(2);
  servoBras1.attach(3);
  servoBras2.attach(4);
  servoBras3.attach(5);
  // put your setup code here, to run once:

}

void loop() {
  // put your main code here, to run repeatedly:
  // servoPince.write(15); // move MG996R's shaft to angle 0°
  // delay(2000); // wait for one second
  // servoPince.write(80); // move MG996R's shaft to angle 45°
  // delay(2000); // wait for one second 
  servoBase1.write(0); // move MG996R's shaft to angle 90°
  servoBase2.write(0); // move MG996R's shaft to angle 90°
  servoBras1.write(0); // move MG996R's shaft to angle 90°
  servoBras2.write(0); // move MG996R's shaft to angle 90°
  servoBras3.write(0); // move MG996R's shaft to angle 90°
  // delay(1000); // wait for one second
  // servoPince.write(180); // move MG996R's shaft to angle 90°
  // delay(1000); // wait for one second
  // servoPince.write(360); // move MG996R's shaft to angle 90°
  // delay(1000); // wait for one second
  // servoPince.write(270); // move MG996R's shaft to angle 90°
  // delay(1000); // wait for one second
  // servoPince.write(720); // move MG996R's shaft to angle 90°
  // delay(1000); // wait for one second
  // servo.write(135); // move MG996R's shaft to angle 135°
  // delay(1000); // wait for one second
  // servo.write(180); // move MG996R's shaft to angle 180°
  // delay(1000); // wait for one second
  // servo.write(15); // move MG996R's shaft to angle 180°
  // delay(1000); // wait for one second
}
