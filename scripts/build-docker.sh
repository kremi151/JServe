#!/bin/bash

# Give Gradle wrapper the chance to be downloaded first in order to not pollute the JSERVE_VERSION VARIABLE
./gradlew --version

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
    JSERVE_MANIFEST_AMENDS=""

    OLD_IFS="$IFS"
    IFS=","
    for JSERVE_BUILDX_PLATFORM in $JSERVE_BUILDX_PLATFORMS; do
        echo "Build image for platform ${JSERVE_BUILDX_PLATFORM}"

        JSERVE_INTERMEDIATE_TAG="${JSERVE_VERSION}-${JSERVE_BUILDX_PLATFORM//\//_}"

        docker buildx build --platform $JSERVE_BUILDX_PLATFORM -o type=docker --tag kremi151/jserve:${JSERVE_INTERMEDIATE_TAG} .
        
        if [[ "${JSERVE_PUBLISH:-}" == "true" ]]; then
            docker push kremi151/jserve:${JSERVE_INTERMEDIATE_TAG}
        fi

        JSERVE_MANIFEST_AMENDS="${JSERVE_MANIFEST_AMENDS} --amend kremi151/jserve:${JSERVE_INTERMEDIATE_TAG}"
    done
    IFS="$OLD_IFS"

    if [[ "${JSERVE_PUBLISH:-}" != "true" ]]; then
        echo "Skip publishing"
        exit 0
    fi

    echo "Publishing multi-platform images"
    docker manifest create kremi151/jserve:${JSERVE_VERSION}${JSERVE_MANIFEST_AMENDS}
    docker manifest push kremi151/jserve:${JSERVE_VERSION}

    docker manifest create kremi151/jserve:latest${JSERVE_MANIFEST_AMENDS}
    docker manifest push kremi151/jserve:latest
fi
