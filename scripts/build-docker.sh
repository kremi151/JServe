#!/bin/bash

echo "Build native image"
./gradlew :docker:nativeBuild

echo "Build Docker image"
docker build -t kremi151/jserve:latest .
