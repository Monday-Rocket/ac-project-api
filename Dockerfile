# syntax=docker/dockerfile:1

FROM golang:1.17-alpine

WORKDIR /app

COPY . .

RUN go get -d -v ./...

RUN go install -v ./...

EXPOSE 5000

CMD [ "main" ]