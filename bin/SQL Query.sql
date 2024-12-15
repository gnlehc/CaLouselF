CREATE DATABASE calouself;

USE calouself;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    address TEXT NOT NULL,
    role ENUM('seller', 'buyer') NOT NULL
);

CREATE TABLE items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    category VARCHAR(255),
    size VARCHAR(255),
    price DOUBLE,
    status ENUM('Pending', 'Approved', 'Declined') DEFAULT 'Pending'
);

CREATE TABLE wishlist (
    user_id INT,
    item_id INT,
    PRIMARY KEY (user_id, item_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (item_id) REFERENCES items(id)
);

CREATE TABLE purchases (
    user_id INT,
    item_id INT,
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (item_id) REFERENCES items(id)
);

CREATE TABLE decline_reasons (
    item_id INT,
    reason TEXT,
    FOREIGN KEY (item_id) REFERENCES items(id)
);
