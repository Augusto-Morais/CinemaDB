CREATE TABLE IF NOT EXISTS actor(
    actor_ID SERIAL PRIMARY KEY,
    name varchar(50),
    born varchar(120),
    image_link varchar(350),
    general_info text ARRAY
);