DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS item_requests CASCADE;
DROP TABLE IF EXISTS items CASCADE;
DROP TABLE IF EXISTS bookings CASCADE;
DROP TABLE IF EXISTS comments CASCADE;

CREATE TABLE IF NOT EXISTS Users
(
    id    int8 GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name  VARCHAR(30),
    email VARCHAR(50),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE iF NOT EXISTS Requests
(
    id           int8 GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    description  TEXT,
    requestor_id int8 REFERENCES Users (id),
    creation     TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE IF NOT EXISTS Items
(
    id           int8 GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name         VARCHAR(40),
    description  TEXT,
    is_available BOOLEAN DEFAULT TRUE,
    request_id   int8 REFERENCES Requests (id),
    owner_id     int8 REFERENCES Users (id)
);

CREATE TABLE IF NOT EXISTS Bookings
(
    id         int8 GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    start_date TIMESTAMP WITHOUT TIME ZONE,
    end_date   TIMESTAMP WITHOUT TIME ZONE,
    item_id    int8 REFERENCES Items (id),
    booker_id  int8 REFERENCES Users (id),
    status     VARCHAR(15),
    CONSTRAINT START_BEFORE CHECK ( end_date > start_date AND end_date > CURRENT_TIMESTAMP)
);


CREATE TABLE IF NOT EXISTS Comments
(
    id        int8 GENERATED BY DEFAULT AS IDENTITY,
    text      TEXT,
    item_id   int8 REFERENCES Items (id),
    author_id int8 REFERENCES Users (id),
    created   TIMESTAMP WITHOUT TIME ZONE
);
