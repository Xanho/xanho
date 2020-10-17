#!/bin/sh

rm -r ./tmp/protos
rm -r ./lib/src/proto

mkdir -p ./tmp/protos
cp -r ../protos/org ./tmp/protos/org

mkdir -p ./lib/src/proto
protoc \
    --proto_path=./tmp/protos \
    --dart_out=grpc:./lib/src/proto -Iproto/ \
    $(find tmp/protos -iname "*.proto")