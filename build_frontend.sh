set -e
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
mkdir -p $DIR/build
cd $DIR/backend
php composer.phar update
cd $DIR/frontend
gulp build
cd $DIR/frontend/dist
rm $DIR/build/frontend.zip
zip -r $DIR/build/frontend.zip *
cd $DIR
