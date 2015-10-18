#!/bin/bash
set -e
docker build -t tilosradio/admin .
docker tag -f tilosradio/admin tilos.hu:5555/tilosradio/admin
sudo docker push tilos.hu:5555/tilosradio/admin

