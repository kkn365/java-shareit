CREATE TABLE IF NOT EXISTS users (
  id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name  VARCHAR(255) NOT NULL,
  email VARCHAR(512) NOT NULL,
  CONSTRAINT pk_user PRIMARY KEY (id),
  CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);
CREATE TABLE IF NOT EXISTS requests (
    id              BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    description     VARCHAR(1024)                   NOT NULL,
    requestor_id    BIGINT                          NOT NULL,
    created         TIMESTAMP WITHOUT TIME ZONE     NOT NULL,
    CONSTRAINT pk_request PRIMARY KEY (id),
    CONSTRAINT USERS_FK4 FOREIGN KEY (REQUESTOR_ID) REFERENCES USERS(ID) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE IF NOT EXISTS items (
  id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name        VARCHAR(255) NOT NULL,
  description VARCHAR(512) NOT NULL,
  available   BOOLEAN      NOT NULL,
  owner_id    BIGINT       NOT NULL,
  request_id  BIGINT,
  CONSTRAINT pk_item PRIMARY KEY (id),
  CONSTRAINT USERS_FK FOREIGN KEY (OWNER_ID) REFERENCES USERS(ID) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT REQUEST_FK FOREIGN KEY (REQUEST_ID) REFERENCES REQUESTS(ID) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE IF NOT EXISTS bookings (
  id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  start_date TIMESTAMP WITHOUT TIME ZONE  NOT NULL,
  end_date   TIMESTAMP WITHOUT TIME ZONE  NOT NULL,
  item_id    BIGINT                       NOT NULL,
  booker_id  BIGINT                       NOT NULL,
  status     VARCHAR(25)                  NOT NULL,
  CONSTRAINT pk_booking PRIMARY KEY (id),
  CONSTRAINT USERS_FK2 FOREIGN KEY (BOOKER_ID) REFERENCES USERS(ID) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT ITEMS_FK FOREIGN KEY (ITEM_ID) REFERENCES ITEMS(ID) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE IF NOT EXISTS comments (
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    text      varchar(1024)               NOT NULL,
    item_id   BIGINT                      NOT NULL,
    author_id BIGINT                      NOT NULL,
    created   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_comment PRIMARY KEY (id),
    CONSTRAINT ITEMS_FK2 FOREIGN KEY (ITEM_ID) REFERENCES ITEMS(ID) ON DELETE CASCADE,
    CONSTRAINT USERS_FK3 FOREIGN KEY (AUTHOR_ID) REFERENCES USERS(ID) ON DELETE CASCADE
);