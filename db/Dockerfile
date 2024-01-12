FROM mysql:8.0
VOLUME /var/lib/mysql
ADD scripts/* /docker-entrypoint-initdb.d/
CMD ["mysqld", "--character-set-server=utf8mb4", "--collation-server=utf8mb4_unicode_ci"]