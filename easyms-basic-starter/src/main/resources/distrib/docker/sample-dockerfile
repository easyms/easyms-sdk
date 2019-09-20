FROM %_source.docker.registry.url_%/%_source.docker.openjdk.image.name_%:%_source.docker.openjdk.image.version_%



COPY bin/ microservice/bin/
COPY config/ microservice/config
COPY lib/ microservice/lib

HEALTHCHECK --interval=40s --timeout=6s --retries=3 CMD microservice/bin/%_project.artifactId_%-healthcheck-ms.sh

RUN chmod 755 microservice/bin/*.sh

ENTRYPOINT ["microservice/bin/%_project.artifactId_%-docker.sh","start"]