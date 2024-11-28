CREATE TABLE HOTEL (
    id UUID PRIMARY KEY,
    name VARCHAR(255),
    rating DOUBLE,
    location VARCHAR(255),
    description VARCHAR(255),
    periodOfWork VARCHAR(255)
);

CREATE TABLE AMENITIES (
    id UUID PRIMARY KEY,
    wifi BOOLEAN,
    pool BOOLEAN,
    airConditioner BOOLEAN,
    parking BOOLEAN,
    hotel_id UUID,
    FOREIGN KEY (hotel_id) REFERENCES HOTEL(id)
);

CREATE TABLE MANAGER (
    id UUID PRIMARY KEY,
    name VARCHAR(255),
    login VARCHAR(255),
    password VARCHAR(255),
    hotel_id UUID,
    FOREIGN KEY (hotel_id) REFERENCES HOTEL(id)
);

CREATE TABLE SOCIAL (
    id UUID PRIMARY KEY,
    followersCount BIGINT,
    hotel_id UUID,
    FOREIGN KEY (hotel_id) REFERENCES HOTEL(id)
);

CREATE TABLE ROOM (
    id UUID PRIMARY KEY,
    roomNumber INTEGER,
    type VARCHAR(255),
    pricePerNight INTEGER,
    isAvailable BOOLEAN,
    hotel_id UUID,
    FOREIGN KEY (hotel_id) REFERENCES HOTEL(id)
);

CREATE TABLE CLIENT (
    id UUID PRIMARY KEY,
    name VARCHAR(255),
    login VARCHAR(255),
    password VARCHAR(255)
);

CREATE TABLE BOOKING (
    id UUID PRIMARY KEY,
    checkInDate DATE,
    checkOutDate DATE,
    room_id UUID,
    user_id UUID,
    FOREIGN KEY (room_id) REFERENCES ROOM(id),
    FOREIGN KEY (user_id) REFERENCES CLIENT(id)
);

INSERT INTO HOTEL (id, name, rating, location, description, periodOfWork)
VALUES
('f47ac10b-58cc-4372-a567-0e02b2c3d479', 'Grand Plaza Hotel', 4.5, 'New York, USA', 'Luxury hotel in the heart of New York.', 'All time');

INSERT INTO AMENITIES (id, wifi, pool, airConditioner, parking, hotel_id)
VALUES
('76f73a65-e3b4-42e8-bb1b-56168816c378', TRUE, TRUE, TRUE, TRUE, 'f47ac10b-58cc-4372-a567-0e02b2c3d479');

INSERT INTO MANAGER (id, name, login, password, hotel_id)
VALUES
('a2cd9d5e-bf5c-4a8b-bd88-c6e7998b3957', 'John Doe', 'johndoe', 'password123', 'f47ac10b-58cc-4372-a567-0e02b2c3d479');

INSERT INTO SOCIAL (id, followersCount, hotel_id)
VALUES
('f8d1e5fc-cb22-4000-92f9-4e123b0bba0b', 1, 'f47ac10b-58cc-4372-a567-0e02b2c3d479');

INSERT INTO ROOM (id, roomNumber, type, pricePerNight, isAvailable, hotel_id)
VALUES
('7a90c702-6941-48ea-9b88-8254769b82e4', 101, 'Single', 150, TRUE, 'f47ac10b-58cc-4372-a567-0e02b2c3d479'),
('3d11c198-ef73-497f-a450-8e4e19fa7f6b', 102, 'Double', 250, FALSE, 'f47ac10b-58cc-4372-a567-0e02b2c3d479');

INSERT INTO CLIENT (id, name, login, password)
VALUES
('b1c8e4a7-4f3f-4c5c-8bda-745d1d8a4d9c', 'Alice Johnson', 'alicejohnson', 'securepass');

INSERT INTO BOOKING (id, checkInDate, checkOutDate, room_id, user_id)
VALUES
('9c430f82-ff7a-4f45-b01d-402dbf8ff625', '2024-12-01', '2024-12-05', '3d11c198-ef73-497f-a450-8e4e19fa7f6b', 'b1c8e4a7-4f3f-4c5c-8bda-745d1d8a4d9c');
