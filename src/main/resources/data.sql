INSERT into roles (id, name, authority)
VALUES (1, 'USER', 'USER'),
       (2, 'ADMIN', 'ADMIN') ON CONFLICT (id) DO NOTHING;