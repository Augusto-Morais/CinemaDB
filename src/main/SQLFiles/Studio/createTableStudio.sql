CREATE TABLE IF NOT EXISTS studio(
    studio_ID SERIAL PRIMARY KEY,
    tradeName varchar(75),
    logo_Link varchar(145),
    dateFounded varchar(221),
    founders varchar(140),
    headquarters varchar(115),
    general_info text ARRAY
);