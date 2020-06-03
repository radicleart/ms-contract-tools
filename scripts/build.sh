#!/bin/bash -e

export SERVICE=$1

printf "\n-----------------------------------------------------------------------------------------------------\n";
printf "Running script: $0 \n";
printf "Running argument: $1 \n";
printf "\n-----------------------------------------------------------------------------------------------------\n";
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )";
pushd $DIR > /dev/null

pwd
mvn -f ./pom.xml -Dmaven.test.skip=true clean install

pushd ../radsoc

docker-compose build assets
docker-compose up -d

popd;
popd;

exit 0;
