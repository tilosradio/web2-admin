var distDir = 'dist';
var gulp = require('gulp');
var clean = require('gulp-clean');
var sass = require('gulp-ruby-sass');
var concat = require('gulp-concat');
var jshint = require('gulp-jshint');
var connect = require('gulp-connect');
var shell = require('gulp-shell');

var less = require('gulp-less');
var usemin = require('gulp-usemin');
var wrap = require('gulp-wrap');
var watch = require('gulp-watch');

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

gulp.task('copy-images', function(){
    return gulp.src(paths.images)
        .pipe(gulp.dest('dist/www/img'));
});

gulp.task('copy-fonts', function(){
    return gulp.src(paths.fonts)
        .pipe(gulp.dest('dist/www/fonts'));
});

gulp.task('copy-htmls', function () {
    gulp.src([paths.templates],
        {base: 'app'})
        .pipe(gulp.dest(distDir + '/www'));
});

gulp.task('copy-bower_fonts', function(){
    return gulp.src(paths.bower_fonts)
        .pipe(gulp.dest('dist/www/lib'));
});
gulp.task('watch', function () {
    gulp.watch([paths.styles, paths.index, paths.js], ['usemin']);
    gulp.watch([paths.images], ['copy-images']);
    gulp.watch([paths.fonts], ['copy-fonts']);
    gulp.watch([paths.bower_fonts], ['copy-bower_fonts']);
});

gulp.task('usemin', function() {
    return gulp.src(paths.index)
        .pipe(usemin({
            less: ['concat', less()],
            js: ['concat', wrap('(function(){ \n<%= contents %>\n})();')]
        }))
        .pipe(gulp.dest('dist/www/'));
});


gulp.task('scripts', function () {
    gulp.src(["app/js/**/*.js"])
        .pipe(jshint('../.jshintrc'))
        .pipe(jshint.reporter('default'))
        .pipe(concat('tilos.js'))
        .pipe(gulp.dest(distDir + "/www/scripts"));

    gulp.src([
        "app/bower_components/angular/angular.js",
        "app/bower_components/angular-route/angular-route.js",
        "app/bower_components/angular-cookies/angular-cookies.js",
        "app/bower_components/angular-resource/angular-resource.js",
        "app/bower_components/angular-sanitize/angular-sanitize.js",
        "app/bower_components/textAngular/textAngular.js"
    ])
        .pipe(concat('angular.js'))
        .pipe(gulp.dest(distDir + "/www/scripts"));
});


gulp.task('assets', function () {
    gulp.src([
            'app/template/**/*',
            'app/images/**/*',
            'app/styles/fonts/**',
            'app/jplayer/**/*'],
        {base: 'app'})
        .pipe(gulp.dest(distDir + '/www'));
});


gulp.task('php', function () {
    gulp.src([
        '../backend/init_autoloader.php',
        '../backend/config/**/*',
        '../backend/data/**/*',
        '../backend/module/Radio/**/*',
        '../backend/module/RadioAdmin/**/*',
        '../backend/module/RadioCommon/**/*',
        '../backend/vendor/**/*'], {base: '../backend'})
        .pipe(gulp.dest(distDir + '/'));

    gulp.src([
        '../backend/www/admin.php'])
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
    gulp.start('copy-assets', 'copy-htmls', 'usemin', 'php', 'bower_components', 'cachedir');
});


gulp.task('watch', ['build'], function () {
    gulp.watch([distDir + "/www/**/*"], function (event) {
        return gulp.src(event.path)
            .pipe(connect.reload());
    });

    gulp.watch(["app/**/*"], ['default']);
    gulp.watch(["../backend/**/*"], ['php']);
});

gulp.task('connect', function () {
    connect.server({
        root: [distDir + '/www'],
        port: 9000,
        livereload: true
    });
});

gulp.task('cachedir', shell.task([
    'mkdir -p dist/data/DoctrineORMModule/Proxy',
    'chmod o+w dist/data/DoctrineORMModule/Proxy'
]))

gulp.task('server', ['connect', 'watch'], function () {
});
