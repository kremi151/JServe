#!/bin/bash

JSERVE_VERSION=$(./gradlew printVersion -q)

echo "JServe version is ${JSERVE_VERSION}"

if [ -z "${JSERVE_BUILDX_PLATFORMS:-}" ]; then
    echo "Build Docker image"
    docker build -t kremi151/jserve:${JSERVE_VERSION} .
	docker tag kremi151/jserve:${JSERVE_VERSION} kremi151/jserve:latest

    if [[ "${JSERVE_PUBLISH:-}" == "true" ]]; then
        echo "Publish Docker image"
        docker push kremi151/jserve:${JSERVE_VERSION}
        docker push kremi151/jserve:latest
    fi
else
    JSERVE_BUILDX_ARGS="--memory 1g --tag kremi151/jserve:latest --tag kremi151/jserve:${JSERVE_VERSION}"

    if [[ "${JSERVE_PUBLISH:-}" == "true" ]]; then
        echo "Build and publish Docker image using buildx"
        JSERVE_BUILDX_ARGS="${JSERVE_BUILDX_ARGS} --push"
    else
        echo "Build Docker image using buildx"
    fi

    docker buildx build --platform $JSERVE_BUILDX_PLATFORMS $JSERVE_BUILDX_ARGS .
fi
