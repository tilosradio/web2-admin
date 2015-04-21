docker build -t tilos/admin .
docker tag tilos/admin localhost:5000/tilos/admin
docker push localhost:5000/tilos/admin