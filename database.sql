# CREATE DATABASE suggestedlocation;

USE suggestedlocation;

DROP TABLE locations;

CREATE TABLE locations(
    id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    `lat` DOUBLE NOT NULL,
    `long` DOUBLE NOT NULL,
    PRIMARY KEY (id)
);

# INSERT INTO locations VALUES (5881791, 'Abbotsford', 4905798, -12225257);
# INSERT INTO locations VALUES (5884467, 'Amherst', 4.583.345, -6.419.874);

SELECT * FROM locations LIMIT 10;

LOAD DATA INFILE '/Applications/XAMPP/xamppfiles/htdocs/cities_canada-usa.tsv'
    INTO TABLE locations
    FIELDS TERMINATED BY '\t'
    ENCLOSED BY '"'
    LINES TERMINATED BY '\n'
    IGNORE 1 LINES;

