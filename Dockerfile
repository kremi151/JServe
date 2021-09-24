FROM ghcr.io/graalvm/graalvm-ce:java8-21.2.0 AS graalvm

COPY . /workspace

RUN cd workspace \
    && ./gradlew clean \
    && ./gradlew :docker:nativeBuild

FROM scratch

COPY docker/assets/index.html /opt/jserve/files/
COPY core/src/main/resources/mime/types.csv /opt/jserve/types.csv
COPY --from=graalvm /workspace/docker/build/native/nativeBuild/jserve /opt/jserve/jserve

CMD ["/opt/jserve/jserve"]
