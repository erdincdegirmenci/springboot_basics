-- Kullanıcılar tablosu
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    firstname VARCHAR(50) NOT NULL,
    lastname VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    phone VARCHAR(20),
    title VARCHAR(50),
    bio TEXT,
    isactive BOOLEAN DEFAULT TRUE,
    islock BOOLEAN DEFAULT FALSE,
    isusingmfa BOOLEAN DEFAULT FALSE,
    imageurl VARCHAR(255),
    createdate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    createuser VARCHAR(100),
    lastupdateuser VARCHAR(100)
);

-- Roller tablosu
CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description TEXT
);

-- İzinler tablosu
CREATE TABLE permissions (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description TEXT
);

-- Rol izinleri tablosu
CREATE TABLE rolepermissions (
    id SERIAL PRIMARY KEY,
    roleid INT REFERENCES roles(id) ON DELETE CASCADE,
    permissionid INT REFERENCES permissions(id) ON DELETE CASCADE
);

-- Kullanıcı roller tablosu
CREATE TABLE userroles (
    id SERIAL PRIMARY KEY,
    userid INT,
    roleid INT
);

-- Denetim (Audit) tablosu
CREATE TABLE audits (
    id SERIAL PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    description TEXT
);

-- Kullanıcı denetimleri tablosu
CREATE TABLE useraudits (
    id SERIAL PRIMARY KEY,
    userid INT,
    auditid INT,
    device VARCHAR(100),
    ipaddress VARCHAR(45),
    createdate TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Hesap doğrulama tablosu
CREATE TABLE accountverifications (
    id SERIAL PRIMARY KEY,
    userid INT,
    url VARCHAR(255) NOT NULL
);

-- Şifre sıfırlama doğrulama tablosu
CREATE TABLE resetpasswordverifications (
    id SERIAL PRIMARY KEY,
    userid INT,
    url VARCHAR(255) NOT NULL,
    expirationdate TIMESTAMP NOT NULL
);

-- İki faktörlü doğrulama tablosu
CREATE TABLE twofactorverifications (
    id SERIAL PRIMARY KEY,
    userid INT,
    code VARCHAR(10) NOT NULL,
    expirationdate TIMESTAMP NOT NULL
);