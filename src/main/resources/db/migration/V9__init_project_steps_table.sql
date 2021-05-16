DROP TABLE if exists project_steps;
CREATE TABLE project_steps(
                         id int primary key auto_increment,
                         description varchar(100) not null,
                         project_id int,
                         days_to_deadline bigint
);

alter table project_steps
    add foreign key (project_id) references projects (id);