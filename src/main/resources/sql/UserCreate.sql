CREATE DATABASE IF NOT EXISTS jenkins_test;
DROP TABLE IF EXISTS jenkins_test.users;
create table jenkins_test.users(
    id bigint unsigned auto_increment primary key,
    account varchar(50) not null,
    password varchar(60) not null,
    nickname varchar(50) not null,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);