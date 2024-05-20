#include <iostream>
#include <hx711/common.h>
#include <fstream>
#include <unistd.h> 

int main() {

  using namespace HX711;

  SimpleHX711 hx(5,6,-420,-8789);

  hx.setUnit(Mass::Unit::G);

  for(;;)
  {
    auto str = hx.weight(35);
    std::ofstream offile("/home/pi/Projet-Android/C/mesure", std::ofstream::out);
    offile << str << std::endl;
  }
  return 0;
}
