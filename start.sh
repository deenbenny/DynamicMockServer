#!/usr/bin/env bash

echo "************ Building the maven package ******************"
mvn clean package
sleep 5
echo "****************** Stopping the apache server ******************"
sh ~/Documents/apache-tomcat-8.5.35/bin/shutdown.sh
sleep 5
echo "****************** Renaming the war file to mock server ******************"
mv target/mockserver-0.0.1.war target/mockserver.war
sleep 3
echo "****************** Removing the war files in apache ******************"
rm -r -f ~/Documents/apache-tomcat-8.5.35/webapps/*.war
sleep 3
echo "****************** Copying the war to the apache webapps folder ******************"
cp target/mockserver.war ~/Documents/apache-tomcat-8.5.35/webapps/
sleep 5
echo "****************** Clearing the logs in apache ******************"
rm -r -f ~/Documents/apache-tomcat-8.5.35/logs/*
sleep 5
echo "****************** Starting the apache application ******************"
sh ~/Documents/apache-tomcat-8.5.35/bin/startup.sh