CREATE TABLE customer(

         id UUID PRIMARY KEY,
         name VARCHAR(100) NOT NULL,
         email VARCHAR UNIQUE NOT NULL,
         password VARCHAR NOT NULL,

         CONSTRAINT name_lenght
             CHECK (length(trim(name)) <= 100),

         CONSTRAINT password_length
             CHECK (length(trim(password)) >= 8)

);