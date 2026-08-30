CREATE TABLE orders (

        id UUID PRIMARY KEY,
        customer_id UUID NOT NULL,
        created_at TIMESTAMP WITH TIME ZONE NOT NULL,

        CONSTRAINT fk_orders_customer
            FOREIGN KEY (customer_id)
                REFERENCES customer (id)

);

CREATE TABLE order_items (

         id UUID PRIMARY KEY,
         order_id UUID NOT NULL,
         product_id UUID NOT NULL,
         quantity INTEGER NOT NULL,
         unit_price NUMERIC(10,2) NOT NULL,

         CONSTRAINT fk_order_items_order
             FOREIGN KEY (order_id)
                 REFERENCES orders (id),

         CONSTRAINT fk_order_items_product
             FOREIGN KEY (product_id)
                 REFERENCES product (id),

         CONSTRAINT uq_order_items_order_product
             UNIQUE (order_id, product_id),

         CONSTRAINT chk_order_items_quantity
             CHECK (quantity >= 1),

         CONSTRAINT chk_order_items_unit_price
             CHECK (unit_price >= 0)

);