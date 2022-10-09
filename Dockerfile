# syntax=docker/dockerfile:1

FROM golang:1.17-alpine

WORKDIR /Users/heejin/git/ac-project-api

COPY go.mod ./
COPY go.sum ./
RUN go mod download

COPY *.go ./

RUN go build -o /ac-project-api ./cmd/main

EXPOSE 5000

CMD [ "/ac-project-api" ]