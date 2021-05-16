DROP TABLE if exists projects;
CREATE TABLE projects(
                      id int primary key auto_increment,
                      description varchar(100) not null
)