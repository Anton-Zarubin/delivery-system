#!/usr/bin/env sh

java -version

java  -Dspring.profiles.active=container -jar /apps/app.jar
