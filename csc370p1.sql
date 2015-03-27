CREATE TABLE Airline (
  code INT NOT NULL,
  name VARCHAR(100) NOT NULL,
  website VARCHAR(100) NOT NULL,
  PRIMARY KEY(code)
);

CREATE TABLE PlaneModel (
  code INT NOT NULL,
  capacity INT NOT NULL,
  PRIMARY KEY(code)
);

CREATE TABLE Flight (
  num INT NOT NULL,
  src CHAR(3) NOT NULL,
  destination CHAR(3) NOT NULL,
  acode INT NOT NULL,
  pmcode INT NOT NULL,
  PRIMARY KEY(num),
  CONSTRAINT fk_flight_airline FOREIGN KEY (acode) REFERENCES Airline(code),
  CONSTRAINT fk_flight_model FOREIGN KEY (pmcode) REFERENCES PlaneModel(code)
);

CREATE TABLE Passenger (
  pid INT NOT NULL,
  name VARCHAR(100) NOT NULL,
  birthdate DATE NOT NULL,
  birthplace VARCHAR(100) NOT NULL,
  citizenship VARCHAR(100) NOT NULL,
  PRIMARY KEY(pid)
);

CREATE TABLE Baggage (
  bid INT NOT NULL,
  pid INT NOT NULL,
  fnum INT NOT NULL,
  PRIMARY KEY(bid),
  CONSTRAINT fk_baggage_passenger FOREIGN KEY (pid) REFERENCES Passenger(pid),
  CONSTRAINT fk_baggage_flight FOREIGN KEY (fnum) REFERENCES Flight(num)
);

CREATE TABLE IncomingFlight (
  num INT NOT NULL,
  arrives_at INTERVAL DAY(0) TO SECOND(0),
  arrivalid INT NOT NULL,
  PRIMARY KEY(num),
  CONSTRAINT inc_is_a_flight FOREIGN KEY (num) REFERENCES Flight(num)
);

CREATE TABLE OutgoingFlight (
  num INT NOT NULL,
  departs_at INTERVAL DAY(0) TO SECOND(0),
  departureid INT NOT NULL,
  PRIMARY KEY(num),
  CONSTRAINT out_is_a_flight FOREIGN KEY (num) REFERENCES Flight(num)
);

CREATE TABLE Arrival (
  id INT NOT NULL,
  gate CHAR(3) NOT NULL,
  arrival_date DATE NOT NULL,
  status VARCHAR(100),
  fnum INT NOT NULL,
  PRIMARY KEY(id),
  CONSTRAINT fk_arrives_flight FOREIGN KEY (fnum) REFERENCES Flight(num)
);
ALTER TABLE IncomingFlight ADD CONSTRAINT fk_inc_has_arrival FOREIGN KEY (arrivalid) REFERENCES Arrival(id);

CREATE TABLE Departure (
  id INT NOT NULL,
  gate CHAR(3) NOT NULL,
  departing_date DATE NOT NULL,
  status VARCHAR(100),
  fnum INT NOT NULL,
  PRIMARY KEY(id),
  CONSTRAINT fk_departs_flight FOREIGN KEY (fnum) REFERENCES Flight(num)
);
ALTER TABLE OutgoingFlight ADD CONSTRAINT fk_out_has_departure FOREIGN KEY (departureid) REFERENCES Departure(id);

CREATE TABLE ArrivesOn (
  pid INT NOT NULL,
  arrivalid INT NOT NULL,
  CONSTRAINT fk_a_is_passenger FOREIGN KEY (pid) REFERENCES Passenger(pid),
  CONSTRAINT fk_arrives_on FOREIGN KEY (arrivalid) REFERENCES Arrival(id)
);

CREATE TABLE DepartsOn (
  pid INT NOT NULL,
  departureid INT NOT NULL,
  CONSTRAINT fk_d_is_passenger FOREIGN KEY (pid) REFERENCES Passenger(pid),
  CONSTRAINT fk_departs_on FOREIGN KEY (departureid) REFERENCES Departure(id)
);

CREATE TABLE EconomyClass (
  pid INT NOT NULL,
  PRIMARY KEY(pid),
  CONSTRAINT fk_eco_is_passenger FOREIGN KEY (pid) REFERENCES Passenger(pid)
);

CREATE TABLE FirstClass (
  pid INT NOT NULL,
  shaken_not_stirred CHAR(1),
  PRIMARY KEY(pid),
  CONSTRAINT fk_first_is_passenger FOREIGN KEY (pid) REFERENCES Passenger(pid)
);

CREATE TABLE SpecialNeeds (
  pid INT NOT NULL,
  condition VARCHAR(100) NOT NULL,
  PRIMARY KEY(pid),
  CONSTRAINT fk_spec_is_passenger FOREIGN KEY (pid) REFERENCES Passenger(pid)
);

CREATE TABLE Infant (
  pid INT NOT NULL,
  months_old INT NOT NULL,
  PRIMARY KEY(pid),
  CONSTRAINT fk_inf_is_passenger FOREIGN KEY (pid) REFERENCES Passenger(pid)
);

DROP TABLE Infant;
DROP TABLE SpecialNeeds;
DROP TABLE FirstClass;
DROP TABLE EconomyClass;

DROP TABLE ArrivesOn;
DROP TABLE DepartsOn;

ALTER TABLE IncomingFlight DROP CONSTRAINT fk_inc_has_arrival;
ALTER TABLE OutgoingFlight DROP CONSTRAINT fk_out_has_departure;

DROP TABLE OutgoingFlight;
DROP TABLE IncomingFlight;
DROP TABLE Arrival;
DROP TABLE Departure;

DROP TABLE Baggage;
DROP TABLE Passenger;
DROP TABLE Flight;
DROP TABLE PlaneModel;
DROP TABLE Airline;