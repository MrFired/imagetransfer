# Image Transfer

## How to setup

### Prerequisites

- A server with Docker Compose installed
- Set up Yandex.Cloud S3 Object Storage

### Installation & Configuration

1. Download the release archive and extract it to the chosen working directory on the host. In total, there should be 5 files:

    - `app.jar`, the app itself;
    - `docker-compose.yml`, the Compose file that defines and configures containers;
    - `.env`, the environment configuration that defines paths to cloud and database credentials and other
  configurations;
    - `nginx.conf`, the NGINX front-end container configuration;
    - `init.sql`, the database initialization script.

2. In the `.env` file, configure:

    - `DB_DATA_FOLDER` to be the path on the host to the folder where database data should be persistently stored;
    - `YACLOUD_BUCKET_NAME`, `YACLOUD_ENDPOINT` and `YACLOUD_REGION` to match your specific Yandex.Cloud info;
    - `AWS_ACCESS_KEY_ID_FILE`, `AWS_SECRET_ACCESS_KEY_FILE` and `DB_PASSWORD_FILE` to be the path to the plaintext files on the host containing the cloud keys and database password, respectively.
    - (Optional) Change other fields accordingly if you want to store the app file, NGINX config or DB init script somewhere else

3. Run `docker compose up` in the working directory to start the service

Right now, the service should be up. However, there is no way to login yet, because there are no users registered. New users have to be added to the database manually. Perhaps in the future a more convinient way will be implemented.
To register a new test user:

1. From working directory, enter the database container with `docker exec -it imagetransfer-db-1 psql -U postgres`
2. Add a new user with SQL query `insert into users values (0, '$2a$10$wCqqqVNMe8oR2IY.6HHAJumTwP4W.uZPbL04wW45qxkDWGVH1ICr2', 'test_user');`

    - `0` is the unique user id;
    - `$2a$10$wCqqqVNMe8oR2IY.6HHAJumTwP4W.uZPbL04wW45qxkDWGVH1ICr2` is the user password hashed with BCrypt. For testing purposes, you can use this hash that is equivalent to `1234` password;
    - `test_user` is the username.

