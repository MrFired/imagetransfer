CREATE TABLE IF NOT EXISTS public.users
(
    id bigint NOT NULL,
    password character varying(255) COLLATE pg_catalog."default",
    username character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT users_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

CREATE TABLE IF NOT EXISTS public.images
(
    name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    upload_timestamp timestamp(6) without time zone,
    user_uploaded character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT images_pkey PRIMARY KEY (name)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.images
    OWNER to postgres;

ALTER TABLE IF EXISTS public.users
    OWNER to postgres;

CREATE SEQUENCE IF NOT EXISTS public.users_seq
    INCREMENT 50
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE public.users_seq
    OWNER TO postgres;