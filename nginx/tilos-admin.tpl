    set $backend __BACKEND__HOST__;
    set $port __BACKEND__PORT__;
    set $my_hostname __HOSTNAME__;
    set $fix 0;



    location / {
        try_files $uri $uri/ @index;
    }

    location @index {
        rewrite .* /index.html last;
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


