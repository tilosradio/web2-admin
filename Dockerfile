FROM tilos/nginx

WORKDIR /host

ADD nginx/run /etc/service/nginx/
RUN chmod +x /etc/service/nginx/run
RUN rm /etc/nginx/sites-enabled/default
ADD nginx/default /etc/nginx/sites-available/
ADD nginx/tilos-admin.conf /etc/nginx/
ADD nginx/variables.tpl /etc/nginx/

ADD dist/www /host/www



