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
    JSERVE_MANIFEST_SOURCES=""

    while IFS="," read -r JSERVE_BUILDX_PLATFORM; do
        echo "Build image for platform ${JSERVE_BUILDX_PLATFORM}"

        JSERVE_INTERMEDIATE_TAG="platform-${JSERVE_BUILDX_PLATFORM//\//_}"

        docker buildx build --platform $JSERVE_BUILDX_PLATFORM --tag kremi151/jserve:${JSERVE_INTERMEDIATE_TAG} .

        JSERVE_MANIFEST_SOURCES="${JSERVE_MANIFEST_SOURCES} kremi151/jserve:${JSERVE_INTERMEDIATE_TAG}"
    done <<< "$JSERVE_BUILDX_PLATFORMS"

    if [[ "${JSERVE_PUBLISH:-}" != "true" ]]; then
        JSERVE_MANIFEST_SOURCES=" --dry-run${JSERVE_MANIFEST_SOURCES}"
    fi

    docker buildx imagetools create -t kremi151/jserve:${JSERVE_VERSION}${JSERVE_MANIFEST_SOURCES}
    docker buildx imagetools create -t kremi151/jserve:latest${JSERVE_MANIFEST_SOURCES}
fi
