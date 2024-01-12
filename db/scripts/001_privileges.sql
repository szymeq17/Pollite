CREATE USER 'pollite'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON * . * TO 'pollite'@'localhost';
CREATE USER 'pollite'@'%' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON * . * TO 'pollite'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES
