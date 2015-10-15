FROM tilosradio/nginx

WORKDIR /host

ADD nginx/docker/run /etc/service/nginx/
RUN chmod +x /etc/service/nginx/run
RUN rm /etc/nginx/sites-enabled/default
ADD nginx/docker/default /etc/nginx/sites-enabled/
ADD nginx/tilos-admin.conf /etc/nginx/
ADD nginx/docker/variables.tpl /etc/nginx/

ADD dist/www /host/www



