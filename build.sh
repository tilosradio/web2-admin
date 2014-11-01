set -e
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
mkdir -p $DIR/build
npm install
./node_modules/bower/bin/bower update
gulp build
cd dist
touch $DIR/build/admin.zip
rm $DIR/build/admin.zip
zip -r $DIR/build/admin.zip *
cd $DIR
