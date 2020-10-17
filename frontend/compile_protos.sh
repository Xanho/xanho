#!/bin/sh

rm -r ./tmp/protos || true
rm -r ./lib/src/proto || true

mkdir -p ./tmp/protos || true
cp -r ../protos/org ./tmp/protos/org

mkdir -p ./lib/src/proto || true
protoc \
    --proto_path=./tmp/protos \
    --dart_out=grpc:./lib/src/proto -Iproto/ \
    $(find tmp/protos -iname "*.proto")