FROM scratch

COPY docker/assets/index.html /opt/jserve/files/
COPY core/src/main/resources/mime/types.csv /opt/jserve/types.csv
COPY docker/build/native/nativeBuild/jserve /opt/jserve/jserve

CMD ["/opt/jserve/jserve"]
