var distDir = 'dist';
var gulp = require('gulp');
var clean = require('gulp-clean');
var concat = require('gulp-concat');
var jshint = require('gulp-jshint');
var connect = require('gulp-connect');
var shell = require('gulp-shell');
var util = require('gulp-util');
var less = require('gulp-less');
var wrap = require('gulp-wrap');
var watch = require('gulp-watch');
var series = require('stream-series');
var inject = require('gulp-inject');
var angularFilesort = require('gulp-angular-filesort');

var paths = {
  js: 'app/js/**/*.*',
  templates: 'app/views/*.*',
  fonts: 'app/fonts/**.*',
  images: 'app/img/**/*.*',
  styles: 'app/less/**/*.less',
  index: 'app/index.html',
  bower_fonts: 'app/bower_components/**/*.{ttf,woff,eof,svg}',
  bower_components: 'app/bower_components/**/*.*',
};


gulp.task('copy-assets', ['copy-images', 'copy-fonts', 'copy-bower_fonts']);

gulp.task('copy-images', function () {
  return gulp.src(paths.images)
    .pipe(gulp.dest('dist/www/img'));
});

gulp.task('copy-fonts', function () {
  return gulp.src(paths.fonts)
    .pipe(gulp.dest('dist/www/fonts'));
});

gulp.task('copy-htmls', function () {
  gulp.src([paths.templates],
    {base: 'app'})
    .pipe(gulp.dest(distDir + '/www'));
});

gulp.task('index', function () {

  var vendorcss = gulp.src([
    'app/bower_components/bootstrap/dist/css/bootstrap.css',
    'app/bower_components/font-awesome/css/font-awesome.css',
    'app/bower_components/textAngular/src/textAngular.css',
    'app/bower_components/ngDialog/css/ngDialog.css',
    'app/bower_components/ngDialog/css/ngDialog-theme-default.css'
  ]).pipe(concat('vendors.css'))
    .pipe(gulp.dest('dist/www/css/'));

  var tiloscss = gulp.src('app/less/**/*.less')
    .pipe(concat('tilos.less'))
    .pipe(less())
    .pipe(gulp.dest('dist/www/css/'))


  var vendorjs = gulp.src([
    'app/bower_components/angular/angular.js',
    'app/bower_components/ng-file-upload/angular-file-upload.min.js',
    'app/bower_components/ng-file-upload/angular-file-upload-shim.min.js',
    'app/bower_components/angular-cookies/angular-cookies.js',
    'app/bower_components/angular-bootstrap/ui-bootstrap.js',
    'app/bower_components/angular-bootstrap/ui-bootstrap-tpls.js',
    'app/bower_components/angular-resource/angular-resource.js',
    'app/bower_components/angular-local-storage/dist/angular-local-storage.js',
    'app/bower_components/angular-route/angular-route.js',
    'app/bower_components/ngDialog/js/ngDialog.min.js',
    'app/bower_components/textAngular/dist/textAngular.min.js',
    'app/bower_components/textAngular/dist/textAngular-rangy.min.js',
    'app/bower_components/textAngular/dist/textAngular-sanitize.min.js'])
    .pipe(gulp.dest('dist/www/js/'));

  var tilosjs = gulp.src('app/js/**/*.js')
    .pipe(angularFilesort())
    .pipe(concat('tilos.js'))
    .pipe(gulp.dest('dist/www/js/'));
  ;

  gulp.src('app/index.html')
    .pipe(inject(vendorjs, {name: 'vendor', ignorePath: 'dist/www'}))
    .pipe(inject(vendorcss, {name: 'vendor', ignorePath: 'dist/www'}))
    .pipe(inject(tilosjs, {name: 'tilos', ignorePath: 'dist/www'}))
    .pipe(inject(tiloscss, {name: 'tilos', ignorePath: 'dist/www'}))
    .pipe(gulp.dest('dist/www/'));
})

gulp.task('copy-bower_fonts', function () {
  return gulp.src(paths.bower_fonts)
    .pipe(gulp.dest('dist/www/lib'));
});
gulp.task('watch', function () {
  gulp.watch([paths.images], ['copy-images']);
  gulp.watch([paths.fonts], ['copy-fonts']);
  gulp.watch([paths.bower_fonts], ['copy-bower_fonts']);
});

gulp.task('assets', function () {
  gulp.src([
      'app/template/**/*',
      'app/images/**/*',
      'app/styles/fonts/**',
      'app/jplayer/**/*'],
    {base: 'app'})
    .pipe(gulp.dest(distDir + '/www'));

  gulp.src([
      'app/bower_components/font-awesome/fonts/*.*'],
    {base: 'app/bower_components/font-awesome'})
    .pipe(gulp.dest(distDir + '/www'));

  gulp.src([
      'app/bower_components/bootstrap/dist/fonts/*.*'],
    {base: 'app/bower_components/bootstrap/dist/'})
    .pipe(gulp.dest(distDir + '/www'));


});


gulp.task('bower_components', function () {
  gulp.src(['app/bower_components/**/*'], {base: 'app'})
    .pipe(gulp.dest(distDir + '/www'));
});


gulp.task('clean', function () {
  return gulp.src([distDir], {read: false})
    .pipe(clean());
});


gulp.task('build', ['clean'], function () {
  gulp.start('default');
});

gulp.task('default', function () {
  gulp.start('copy-assets', 'copy-htmls', 'index');
});


gulp.task('watch', ['build'], function () {
  gulp.watch([distDir + "/www/**/*"], function (event) {
    return gulp.src(event.path)
      .pipe(connect.reload());
  });

  gulp.watch(["app/**/*"], ['default']);
});

gulp.task('connect', function () {
  connect.server({
    root: [distDir + '/www'],
    port: 9000,
    livereload: true
  });
});

gulp.task('server', ['connect', 'watch'], function () {
});
