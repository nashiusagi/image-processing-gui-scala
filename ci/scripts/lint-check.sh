#!/bin/bash
set -x

sbt check

if [ $? != 0 ]; then
    echo "sbt checkに失敗しました"
    exit 1
fi

exit 0