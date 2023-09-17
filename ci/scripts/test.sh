#!/bin/bash
set -x

sbt test

if [ $? != 0 ]; then
    echo "sbt testに失敗しました"
    exit 1
fi

exit 0