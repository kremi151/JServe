#!/bin/bash

# For debugging purposes
echo "Is release build? ${JSERVE_IS_RELEASE}"

if [ -z "${JSERVE_BUILDX_PLATFORMS:-}" ]; then
    echo "Build Docker image"
    docker build -t kremi151/jserve:latest .
else
    echo "Build Docker image using buildx"
    docker buildx build --platform $JSERVE_BUILDX_PLATFORMS --tag kremi151/jserve:latest
fi
