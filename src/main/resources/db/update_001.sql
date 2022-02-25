create table if not exists employees (
    id serial primary key,
    name varchar(50),
    surname varchar(50),
    inn varchar(30),
    hiring timestamp
);
create table if not exists person (
    id serial primary key not null,
    login varchar(2000),
    password varchar(2000),
    employees_id int not null references employees(id)
);