docker build -t tilosradio/backend .
docker tag tilosradio/admin localhost:5000/tilosradio/admin
docker push localhost:5000/tilosradio/admin

