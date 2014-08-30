DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
mkdir -p $DIR/build
cd $DIR/util
mvn clean install
cp $DIR/util/service/target/service.war $DIR/build/
cp $DIR/util/streamer/target/streamer.war $DIR/build/
cd $DIR
