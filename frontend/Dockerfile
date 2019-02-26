# Start with a lightweight node-js image
FROM node:11.10-alpine

MAINTAINER Ryan Susana "ryansusana@live.com"

# Start in src
WORKDIR /src

# Install Git
RUN apk update && apk upgrade && \
    apk add --no-cache bash git openssh

# Clone spotitube frontend repo
RUN git clone https://github.com/HANICA-DEA/spotitube.git

# CD to spotitube repo
WORKDIR /src/spotitube

# Install angular
RUN npm install -g @angular/cli

RUN npm install --save-dev

# Expose to the outside world
EXPOSE 4200

# This is how to start Spotitube
CMD ["ng", "serve"]

