create table Slides(
    slide_id integer primary key autoincrement,
    name text,
    image bit not null,
    duration integer,
    url text not null
);