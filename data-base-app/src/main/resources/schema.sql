CREATE TABLE HOTEL (
    id UUID PRIMARY KEY,
    name VARCHAR(255),
    location VARCHAR(255),
    description VARCHAR(255),
    period_of_work VARCHAR(255)
);

CREATE TABLE AMENITIES (
    id UUID PRIMARY KEY,
    wifi BOOLEAN,
    pool BOOLEAN,
    air_conditioner BOOLEAN,
    parking BOOLEAN,
    hotel_id UUID,
    FOREIGN KEY (hotel_id) REFERENCES HOTEL(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE SOCIAL (
    id UUID PRIMARY KEY,
    rating DOUBLE,
    followers_count BIGINT,
    hotel_id UUID,
    FOREIGN KEY (hotel_id) REFERENCES HOTEL(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE ROOM (
    id UUID PRIMARY KEY,
    room_number INTEGER,
    type VARCHAR(255),
    price_per_night INTEGER,
    is_available BOOLEAN,
    hotel_id UUID,
    FOREIGN KEY (hotel_id) REFERENCES HOTEL(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE BOOKING (
    id UUID PRIMARY KEY,
    check_in_date DATE,
    check_out_date DATE,
    room_id UUID,
    FOREIGN KEY (room_id) REFERENCES ROOM(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE PROFILE (
    id UUID PRIMARY KEY,
    role VARCHAR(255),
    name VARCHAR(255),
    login VARCHAR(255),
    password VARCHAR(255),
    hotel_id UUID,
    booking_id UUID,
    FOREIGN KEY (hotel_id) REFERENCES HOTEL(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (booking_id) REFERENCES BOOKING(id) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE NOTIFICATION (
    id UUID PRIMARY KEY,
    message VARCHAR(255),
    profile_id UUID,
    FOREIGN KEY (profile_id) REFERENCES PROFILE(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE profile_favorite_hotels (
    profile_id UUID,
    favorite_hotels UUID,
    PRIMARY KEY (profile_id),
    FOREIGN KEY (profile_id) REFERENCES PROFILE(id) ON DELETE CASCADE
);

ALTER TABLE HOTEL ADD COLUMN amenities_id UUID;
ALTER TABLE HOTEL ADD COLUMN social_id UUID;

ALTER TABLE HOTEL
ADD CONSTRAINT fk_amenities
FOREIGN KEY (amenities_id)
REFERENCES AMENITIES(id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE HOTEL
ADD CONSTRAINT fk_social
FOREIGN KEY (social_id)
REFERENCES SOCIAL(id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE BOOKING
ADD COLUMN profile_id UUID;

ALTER TABLE BOOKING
ADD CONSTRAINT fk_profile
FOREIGN KEY (profile_id)
REFERENCES PROFILE(id) ON DELETE CASCADE ON UPDATE CASCADE;

INSERT INTO HOTEL (id, name, location, description, period_of_work,amenities_id,social_id)
VALUES
('f6d00b2c-8ab6-49e6-910d-c55fa7d1f9a6', 'Sunset Resort', 'Miami, FL', 'A luxury hotel with stunning ocean views', 'All year round',NULL,NULL),
('e8a3c5b7-7c1e-4f9a-ae8b-9c7f4e8d6a7b', 'Marriott', 'Minsk, RB', 'Hotel with exquisite rooms and excellent service', 'All year round',NULL,NULL);


INSERT INTO AMENITIES (id, wifi, pool, air_conditioner, parking, hotel_id)
VALUES
('d7f7a8d3-56b7-47bb-9a57-1de1167a9cc0', FALSE, TRUE, TRUE, TRUE, 'f6d00b2c-8ab6-49e6-910d-c55fa7d1f9a6'),
('2f1c30a0-3e25-4ae4-b3aa-637b9eaa40f4', TRUE, TRUE, TRUE, TRUE, 'e8a3c5b7-7c1e-4f9a-ae8b-9c7f4e8d6a7b');

INSERT INTO SOCIAL (id, rating, followers_count, hotel_id)
VALUES
('44e3bbf8-81ec-46a9-88c3-9c9eb3d2d801', 4.8, 1, 'f6d00b2c-8ab6-49e6-910d-c55fa7d1f9a6'),
('9b71214c-64da-4e3b-8bc6-c920e8f84e12', 4.0, 0, 'e8a3c5b7-7c1e-4f9a-ae8b-9c7f4e8d6a7b');

INSERT INTO ROOM (id, room_number, type, price_per_night, is_available, hotel_id)
    VALUES
    ('7f9d2d0e-6a2e-4ed9-b9a1-14ec7e1d0f40', 101, 'Standard', 150, FALSE, 'f6d00b2c-8ab6-49e6-910d-c55fa7d1f9a6'),
    ('32d24517-76a1-4c1c-9831-9d779c3a5a3a', 102, 'Deluxe', 250, TRUE, 'f6d00b2c-8ab6-49e6-910d-c55fa7d1f9a6');

INSERT INTO BOOKING (id, check_in_date, check_out_date, room_id,profile_id)
VALUES
('b5ac98f0-7d58-475e-913d-efb51fa9d52b', '2024-12-10', '2024-12-15', '7f9d2d0e-6a2e-4ed9-b9a1-14ec7e1d0f40',NULL);

INSERT INTO PROFILE (id, role, name, login, password, hotel_id, booking_id)
VALUES
('1e8a2c28-650b-4f92-9b13-8f79c4728f26', 'Admin', 'John Doe', 'admin', 'adminpassword', NULL, NULL),
('39ab36f5-7487-465d-bcc2-6f91a4b3e3f7', 'Manager', 'Alice Smith', 'manag', 'pas', 'f6d00b2c-8ab6-49e6-910d-c55fa7d1f9a6', NULL),
('d9d2e0a5-8cf4-44bc-8a35-c11abf5a50de', 'Client', 'Alex Nosko', 'al', '123', NULL, 'b5ac98f0-7d58-475e-913d-efb51fa9d52b');

INSERT INTO profile_favorite_hotels (profile_id, favorite_hotels)
VALUES
('d9d2e0a5-8cf4-44bc-8a35-c11abf5a50de', 'f6d00b2c-8ab6-49e6-910d-c55fa7d1f9a6');

UPDATE HOTEL
SET amenities_id = 'd7f7a8d3-56b7-47bb-9a57-1de1167a9cc0'
WHERE id = 'f6d00b2c-8ab6-49e6-910d-c55fa7d1f9a6';

UPDATE HOTEL
SET social_id = '44e3bbf8-81ec-46a9-88c3-9c9eb3d2d801'
WHERE id = 'f6d00b2c-8ab6-49e6-910d-c55fa7d1f9a6';

UPDATE HOTEL
SET amenities_id = '2f1c30a0-3e25-4ae4-b3aa-637b9eaa40f4'
WHERE id = 'e8a3c5b7-7c1e-4f9a-ae8b-9c7f4e8d6a7b';

UPDATE HOTEL
SET social_id = '9b71214c-64da-4e3b-8bc6-c920e8f84e12'
WHERE id = 'e8a3c5b7-7c1e-4f9a-ae8b-9c7f4e8d6a7b';

UPDATE BOOKING
SET profile_id = 'd9d2e0a5-8cf4-44bc-8a35-c11abf5a50de'
WHERE id = 'b5ac98f0-7d58-475e-913d-efb51fa9d52b';


