# Extendz Spring Boot Example
## Database setup.
* Make sure you have installed Mysql database with following user.

User Name: extendz

Password : club.extendz

* Create a database named : extendz

use following to create a new user and a database in mysql. 
make sure your the admin of the database to grant privilages;

```mysql
CREATE DATABASE extendz;
CREATE USER 'extendz'@'localhost' IDENTIFIED BY 'club.extendz';
GRANT ALL PRIVILEGES ON extendz.* TO 'extendz'@'localhost';
FLUSH PRIVILEGES;
```