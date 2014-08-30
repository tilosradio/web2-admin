DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
mkdir -p $DIR/build
cd $DIR/frontend
gulp build
cd dist
rm $DIR/frontend.zip
zip -r $DIR/build/frontend.zip *
cd $DIR
