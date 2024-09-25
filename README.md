# Image Transfer

## How to setup

### Prerequisites

- A server with Docker Compose installed
- Set up Yandex.Cloud S3 Object Storage

### Installation & Configuration

1. Download the release archive and extract it to the chosen work directory on the host. In total, there should be 5 files:

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

3. Run `docker compose up` to start the service
