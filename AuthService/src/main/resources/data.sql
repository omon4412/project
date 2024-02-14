insert into roles("name")
values ('ROLE_USER');
insert into roles("name")
values ('ROLE_ADMIN');
insert into users("username", email, password_hash)
values ('user', '1@1.1', '$2a$10$xyvEP/dHoo2Sdb8t6/0Fw.rObIOV9FMFPwbUfOMYJgIGYu7xvNxYy');
insert into users_roles("user_id", "role_id")
values (1, 1);
insert into users("username", email, password_hash)
values ('admin', '2@2.2', '$2a$10$xyvEP/dHoo2Sdb8t6/0Fw.rObIOV9FMFPwbUfOMYJgIGYu7xvNxYy');
insert into users_roles("user_id", "role_id")
values (2, 2);