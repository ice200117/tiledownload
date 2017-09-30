#!/bin/bash


ip=$1
passwd=$2

sshpass -p $passwd scp -r gmapoffline/ root@$ip:~/
sshpass -p $passwd ssh  root@$ip 'yum install -y jre' 
sshpass -p $passwd ssh  root@$ip 'cd ~/gmapoffline && java gmapdownloader' 
sshpass -p $passwd ssh root@$ip 'tar czvf ~/tiles.tar.gz ~/gmapoffline/tiles'
sshpass -p $passwd scp root@$ip:~/tiles.tar.gz .
sshpass -p $passwd scp -r root@$ip:~/gmapoffline/xyz.conf gmapoffline/xyz.conf
tar xvf tiles.tar.gz
mv tiles.tar.gz tiles/
