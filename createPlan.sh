#!/bin/bash

vultr server create -n '15' -r 25 -p 201 -o 127 > create_info
sleep 5m
mid=`cat create_info | tail -n 1 | awk '{print $1;}'`
echo $mid
ip=`vultr server show $mid | grep 'IP' | awk '{print $2;}'`
echo $ip
passwd=`vultr server show $mid | tail -n 3 | awk '{print $3;}' | head -n 1`
echo $passwd
./downloadgmap.sh $ip $passwd
vultr server delete $mid  -f
