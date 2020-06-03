#!/bin/bash -e

printf "\n-----------------------------------------------------------------------------------------------------\n";
printf "Running script: $0 \n";
printf "\n-----------------------------------------------------------------------------------------------------\n";
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )";
pushd $DIR > /dev/null

pwd
mvn -f ./pom.xml -Dmaven.test.skip=true clean install

docker build --tag ms-contracts:1.0 .

docker run --publish 3500:8080 --detach --name rs_contracts ms-contracts:1.0

exit 0;
