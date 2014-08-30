DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
mkdir -p $DIR/build
cd $DIR/admin
gulp build
cd dist
rm $DIR/admin.zip
zip -r $DIR/build/admin.zip *
cd $DIR
