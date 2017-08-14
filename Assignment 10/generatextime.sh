./xtime java RLEEncoder < $1 > 2_$1 && ./xtime java BWTEncoder < $1 > 3_$1 && ./xtime java RLEEncoder < 3_$1 > 4_$1 && 
wc -c $1 2_$1 3_$1 4_$1;
