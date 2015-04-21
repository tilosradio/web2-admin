    set $backend __BACKEND__HOST__;
    set $port __BACKEND__PORT__;
    set $my_hostname __HOSTNAME__;
    set $fix 0;


    if ($http_host != $server_name) {
       set $fix 1;
    }
    if ($request_uri = '/serverinfo/server-status') {
       set $fix 0;
    }
    if ($fix = 1) {
       return $scheme://$my_hostname$request_uri;
    } 

    location / {
        try_files $uri $uri/ @index;
    }

    location @index {
#        if ($http_user_agent ~* (googlebot|yahoo|bingbot|baiduspider|yandex|yeti|yodaobot|gigabot|ia_archiver|facebookexternalhit|twitterbot|developers\.google\.com|testagent)) {
#            proxy_pass http://127.0.0.2:5004;
#            break;
#        }
        rewrite .* /index.html last;
    }



    location /apidoc {
        proxy_pass http://$backend:$port;
        break;
    }

    location ~ ^/api/v1/.+$ {
        proxy_pass http://$backend:$port;
        break;
    }

    location ~ ^/api/int/.+$ {
        proxy_pass http://$backend:$port;
        break;
    }


    location ~ ^/feed/.+$ {
        proxy_pass http://$backend:$port;
        break;
    }


    location ~ ^/mp3/.+$ {
        proxy_pass http://$backend:$port;
        break;
    }

    location ~ ^.*m3u$ {
        rewrite  ^/(.*) /api/v1/m3u/lastweek?stream=$1 break;
        proxy_pass http://$backend:$port;
        break;
    }

    location ~ ^/(api|m3u)/.+$ {
        rewrite ^/(api|m3u)/.+$ /backend.php last;
    }

    location ~ ^/upload/.*\.php$ {
        rewrite ^.*$ /index.html last;
    }


