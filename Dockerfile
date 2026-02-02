ARG KOBWEB_APP_ROOT="site"

FROM eclipse-temurin:25 AS java

FROM java AS export

ENV KOBWEB_CLI_VERSION=0.9.21
ARG KOBWEB_APP_ROOT

ENV NODE_MAJOR=20

COPY . /project

RUN apt-get update \
    && apt-get install -y ca-certificates curl gnupg unzip wget gnupg2 \
    && mkdir -p /etc/apt/keyrings \
    && curl -fsSL https://deb.nodesource.com/gpgkey/nodesource-repo.gpg.key | gpg --dearmor -o /etc/apt/keyrings/nodesource.gpg \
    && echo "deb [signed-by=/etc/apt/keyrings/nodesource.gpg] https://deb.nodesource.com/node_$NODE_MAJOR.x nodistro main" | tee /etc/apt/sources.list.d/nodesource.list \
    && apt-get update \
    && apt-get install -y nodejs \
    && npm init -y \
    && npx playwright install --with-deps chromium

RUN wget https://github.com/varabyte/kobweb-cli/releases/download/v${KOBWEB_CLI_VERSION}/kobweb-${KOBWEB_CLI_VERSION}.zip \
    && unzip kobweb-${KOBWEB_CLI_VERSION}.zip \
    && rm kobweb-${KOBWEB_CLI_VERSION}.zip

ENV PATH="/kobweb-${KOBWEB_CLI_VERSION}/bin:${PATH}"

WORKDIR /project/${KOBWEB_APP_ROOT}

RUN mkdir -p ~/.gradle

RUN kobweb export -l FULLSTACK --notty

FROM java AS run

ARG KOBWEB_APP_ROOT

COPY --from=export /project/${KOBWEB_APP_ROOT}/.kobweb .kobweb

# Database environment (kept as requested)
ENV DB_HOST=db
ENV DB_PORT=5432
ENV DB_NAME=todos
ENV DB_USER=malefic
ENV DB_PASSWORD=password

EXPOSE 8080

# Start the Kobweb server provided by the export
ENTRYPOINT [".kobweb/server/start.sh"]
