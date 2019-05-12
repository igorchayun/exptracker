insert into usr (id, username, password, active)
    select 1 as id,
          'admin' as username,
          '$2a$08$onrFLrFrrxqEDnZi7BKQouuHGnyntjAIiZAX0SGLY.56ob30mxRn.' as password,
          true as active
    where not exists (select 1 from usr where id = 1);

insert into user_role (user_id, roles)
    select 1 as user_id, 'ADMIN' as roles
    where not exists (select 1 from user_role where user_id = 1);