CREATE TABLE order_book (
  id UUID PRIMARY KEY,
  instrument_id varchar(10) NOT NULL,
  state varchar(10) NOT NULL
);

CREATE TABLE orders (
  id UUID PRIMARY KEY,
  instrument_id varchar(10) NOT NULL,
  price int NOT NULL DEFAULT -1,
  quantity int NOT NULL,
  invalid boolean,
  date TIMESTAMP NOT NULL,
  book_id UUID NOT NULL,
  FOREIGN KEY (book_id) references order_book(id)
);

CREATE TABLE executions (
  id UUID PRIMARY KEY,
  instrument_id varchar(10) NOT NULL,
  price int,
  quantity int NOT NULL
);


INSERT INTO order_book (id,instrument_id,state) VALUES ('e4944dab-2846-44d5-891a-0ba3a78fb1cc', 'ism', 'OPEN');
INSERT INTO order_book (id,instrument_id,state) VALUES ('e4944dab-2846-44d5-891a-0ba3a78fb1cd', 'ddm', 'CLOSED');

INSERT INTO orders (id,instrument_id,price,quantity,invalid,date,book_id) VALUES ('e4944dab-2846-44d5-891a-0ba3a78fb1cc', 'ism', 50,100,null, parsedatetime('17-07-2021 18:47:52.69', 'dd-MM-yyyy hh:mm:ss.SS'),'e4944dab-2846-44d5-891a-0ba3a78fb1cc');

INSERT INTO orders (id,instrument_id,price,quantity,invalid,date,book_id) VALUES ('d4944dab-2846-44d5-891a-0ba3a78fb1cc', 'ddm', 50,100,null, parsedatetime('17-07-2021 18:47:52.69', 'dd-MM-yyyy hh:mm:ss.SS'),'e4944dab-2846-44d5-891a-0ba3a78fb1cd');
INSERT INTO orders (id,instrument_id,quantity,invalid,date,book_id) VALUES ('d4944dab-2846-44d5-891a-0ba3a78fb1cd', 'ddm' ,80,null, parsedatetime('17-07-2021 18:50:52.69', 'dd-MM-yyyy hh:mm:ss.SS'),'e4944dab-2846-44d5-891a-0ba3a78fb1cd');
INSERT INTO orders (id,instrument_id,price,quantity,invalid,date,book_id) VALUES ('d4944dab-2846-44d5-891a-0ba3a78fb1ce', 'ddm', 70,110,null, parsedatetime('17-07-2021 19:47:52.69', 'dd-MM-yyyy hh:mm:ss.SS'),'e4944dab-2846-44d5-891a-0ba3a78fb1cd');
INSERT INTO orders (id,instrument_id,price,quantity,invalid,date,book_id) VALUES ('d4944dab-2846-44d5-891a-0ba3a78fb1cf', 'ddm', 80,50,null, parsedatetime('17-07-2021 20:47:52.69', 'dd-MM-yyyy hh:mm:ss.SS'),'e4944dab-2846-44d5-891a-0ba3a78fb1cd');

INSERT INTO executions (id,instrument_id,price,quantity) VALUES ('d4944dab-2846-44d5-891a-0ba3a78fb1cf', 'ddm', 80,50);


