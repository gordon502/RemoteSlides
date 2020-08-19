create table news_tickers (
    ticker_id integer primary key autoincrement,
    text text not null,
    duration integer not null check (duration > 0)
);