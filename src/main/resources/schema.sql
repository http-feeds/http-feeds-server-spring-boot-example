drop table feed if exists;

create table feed
(
    id       varchar(1024) primary key,
    type     varchar(1024),
    source   varchar(1024),
    time     timestamp,
    subject  varchar(1024),
    method   varchar(1024),
    data     clob
);