#!/bin/bash

if [ -z "${JSERVE_BUILDX_PLATFORMS:-}" ]; then
    echo "Build Docker image"
    docker build -t kremi151/jserve:latest .

    if [[ "${JSERVE_PUBLISH:-}" == "true" ]]; then
        docker push kremi151/jserve:latest
    fi
else
    echo "Build Docker image using buildx"

    JSERVE_BUILDX_ARGS="--tag kremi151/jserve:latest"

    if [[ "${JSERVE_PUBLISH:-}" == "true" ]]; then
        JSERVE_BUILDX_ARGS="${JSERVE_BUILDX_ARGS} --push"
    fi

    docker buildx build --platform $JSERVE_BUILDX_PLATFORMS $JSERVE_BUILDX_ARGS .
fi
