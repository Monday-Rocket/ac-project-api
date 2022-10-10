# syntax=docker/dockerfile:1

FROM golang:1.17-alpine

WORKDIR /app

COPY . .

RUN go get -d -v ./...

RUN go install -v ./...

EXPOSE 80

ENV GOOGLE_APPLICATION_CREDENTIALS="./serviceAccountKey.json"

CMD [ "main" ]