# HospitalDBMS

## How To Run
Start MariaDB server: `docker-compose up --build -d`

Initialize HospitalDBMS system (creates Tables; inserts dummy values): `make init`

Run HospitalDBMS system: `make run`

## Connect to MariaDB server
Connect: `docker exec -it database /bin/bash`

Login to MySQL DB: `mysql -u root -p` (enter `changeme` as default password)

Changes can be made to MariaDB server (including username, password, port, etc.) by editing the `docker-compose.yml` file

