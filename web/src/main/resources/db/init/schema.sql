-- This table is needed for the "Remember-Me" function to work when logging in.
create table if not exists persistent_logins (
    username varchar(64) not null,
    series varchar(64) primary key,
    token varchar(64) not null,
    last_used timestamp not null
);