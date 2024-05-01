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
    std::ofstream offile("mesure", std::ofstream::out);
    offile << hx.weight(35) << std::endl;
    sleep(1);
  }
  return 0;
}
