#include <iostream>
#include <hx711/common.h>
#include <fstream>

int main() {

  using namespace HX711;

  SimpleHX711 hx(5,6,-420,-8789);

  hx.setUnit(Mass::Unit::G);

  std::ofstream offile("mesure", std::ofstream::out);

  for(;;) std::cout << hx.weight(35) << std::endl;

  return 0;

}
