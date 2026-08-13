CREATE TABLE product (
         id UUID PRIMARY KEY,
         name VARCHAR NOT NULL,
         description VARCHAR NOT NULL,
         price NUMERIC(10, 2) NOT NULL,

         CONSTRAINT name_not_blank
             CHECK (length(trim(name)) > 0),

         CONSTRAINT description_not_blank
             CHECK (length(trim(description)) > 0),

         CONSTRAINT positive_price
             CHECK (price > 0)
);

CREATE TABLE stock (
       id UUID PRIMARY KEY,
       product_id UUID NOT NULL,
       quantity INTEGER NOT NULL,

       CONSTRAINT uk_stock_product
           UNIQUE (product_id),

       CONSTRAINT fk_stock_product
           FOREIGN KEY (product_id)
               REFERENCES product (id),

       CONSTRAINT non_negative_quantity
           CHECK (quantity >= 0)
);