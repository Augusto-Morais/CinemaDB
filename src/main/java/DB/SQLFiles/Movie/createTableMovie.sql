CREATE TABLE IF NOT EXISTS movie (
    movie_ID SERIAL PRIMARY KEY,
    title varchar(150),
    studio varchar(150),
    Plot text ARRAY,
    directors varchar(200),
    starring varchar(400),
    poster_link varchar(350),
    release_date varchar(150),
    budget varchar(200),
    box_office varchar(200)
);