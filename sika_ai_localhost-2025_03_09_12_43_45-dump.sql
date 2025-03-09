--
-- PostgreSQL database dump
--

-- Dumped from database version 17.4
-- Dumped by pg_dump version 17.4

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: public; Type: SCHEMA; Schema: -; Owner: pg_database_owner
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO pg_database_owner;

--
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: pg_database_owner
--

COMMENT ON SCHEMA public IS 'standard public schema';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: ANNEX; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."ANNEX" (
    "ID" character varying(64) NOT NULL,
    "DISPLAY_NAME" character varying(255),
    "EXTENSION_TYPE" character varying(255),
    "FILE_URL" character varying(255),
    "STATUS" smallint,
    "BUCKET_NAME" character varying(255),
    "BUCKET_ID" character varying(255),
    "STAFF_ID" character varying(64),
    "MESSAGE_ID" character varying(64),
    "CREATE_TIME" timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    "CREATE_BY" character varying(64)
);


ALTER TABLE public."ANNEX" OWNER TO postgres;

--
-- Name: TABLE "ANNEX"; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public."ANNEX" IS '附件表';


--
-- Name: COLUMN "ANNEX"."ID"; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public."ANNEX"."ID" IS '附件ID';


--
-- Name: COLUMN "ANNEX"."DISPLAY_NAME"; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public."ANNEX"."DISPLAY_NAME" IS '文件名';


--
-- Name: COLUMN "ANNEX"."EXTENSION_TYPE"; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public."ANNEX"."EXTENSION_TYPE" IS '扩展名,文件后缀';


--
-- Name: COLUMN "ANNEX"."FILE_URL"; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public."ANNEX"."FILE_URL" IS '文件URL';


--
-- Name: COLUMN "ANNEX"."STATUS"; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public."ANNEX"."STATUS" IS '文件状态:1正常,2删除';


--
-- Name: COLUMN "ANNEX"."BUCKET_NAME"; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public."ANNEX"."BUCKET_NAME" IS '分区名';


--
-- Name: COLUMN "ANNEX"."BUCKET_ID"; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public."ANNEX"."BUCKET_ID" IS '分区ID';


--
-- Name: COLUMN "ANNEX"."STAFF_ID"; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public."ANNEX"."STAFF_ID" IS '所属用户ID';


--
-- Name: COLUMN "ANNEX"."MESSAGE_ID"; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public."ANNEX"."MESSAGE_ID" IS '所属消息ID';


--
-- Name: COLUMN "ANNEX"."CREATE_TIME"; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public."ANNEX"."CREATE_TIME" IS '创建时间';


--
-- Name: COLUMN "ANNEX"."CREATE_BY"; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public."ANNEX"."CREATE_BY" IS '创建人ID';


--
-- Name: MESSAGE; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."MESSAGE" (
    "ID" character varying(64) NOT NULL,
    "SESSION_ID" character varying(64) NOT NULL,
    "SEND_ID" character varying(64),
    "SEND_NAME" character varying(255),
    "CONTENT" text,
    "TYPE" smallint,
    "ANNEX" smallint,
    "CREATE_TIME" timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public."MESSAGE" OWNER TO postgres;

--
-- Name: TABLE "MESSAGE"; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public."MESSAGE" IS '消息表';


--
-- Name: COLUMN "MESSAGE"."ID"; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public."MESSAGE"."ID" IS '消息ID';


--
-- Name: COLUMN "MESSAGE"."SESSION_ID"; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public."MESSAGE"."SESSION_ID" IS '会话ID';


--
-- Name: COLUMN "MESSAGE"."SEND_ID"; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public."MESSAGE"."SEND_ID" IS '发送人ID';


--
-- Name: COLUMN "MESSAGE"."SEND_NAME"; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public."MESSAGE"."SEND_NAME" IS '发送人昵称';


--
-- Name: COLUMN "MESSAGE"."CONTENT"; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public."MESSAGE"."CONTENT" IS '消息内容';


--
-- Name: COLUMN "MESSAGE"."TYPE"; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public."MESSAGE"."TYPE" IS '消息类型';


--
-- Name: COLUMN "MESSAGE"."ANNEX"; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public."MESSAGE"."ANNEX" IS '是否有附件:1是2否';


--
-- Name: COLUMN "MESSAGE"."CREATE_TIME"; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public."MESSAGE"."CREATE_TIME" IS '创建时间';


--
-- Name: SESSIONS; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."SESSIONS" (
    "ID" character varying(64) NOT NULL,
    "SESSION_NAME" character varying(255),
    "SESSION_TYPE" smallint,
    "STAFF_ID" character varying(64),
    "CREATE_TIME" timestamp without time zone,
    "STATUS" smallint
);


ALTER TABLE public."SESSIONS" OWNER TO postgres;

--
-- Name: TABLE "SESSIONS"; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public."SESSIONS" IS '会话记录表';


--
-- Name: COLUMN "SESSIONS"."ID"; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public."SESSIONS"."ID" IS '会话ID';


--
-- Name: COLUMN "SESSIONS"."SESSION_NAME"; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public."SESSIONS"."SESSION_NAME" IS '会话名称';


--
-- Name: COLUMN "SESSIONS"."SESSION_TYPE"; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public."SESSIONS"."SESSION_TYPE" IS '类型: 1单聊, 2群聊';


--
-- Name: COLUMN "SESSIONS"."STAFF_ID"; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public."SESSIONS"."STAFF_ID" IS '操作人ID';


--
-- Name: COLUMN "SESSIONS"."CREATE_TIME"; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public."SESSIONS"."CREATE_TIME" IS '创建时间';


--
-- Name: COLUMN "SESSIONS"."STATUS"; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public."SESSIONS"."STATUS" IS '会话状态';


--
-- Name: USER; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."USER" (
    "ID" character varying(64) NOT NULL,
    "NICKNAME" character varying(255),
    "USERNAME" character varying(255),
    "PASSWORD" character varying(255),
    "CREATE_TIME" timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    "CREATE_BY" character varying(64)
);


ALTER TABLE public."USER" OWNER TO postgres;

--
-- Name: TABLE "USER"; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public."USER" IS '用户表';


--
-- Name: COLUMN "USER"."ID"; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public."USER"."ID" IS 'ID';


--
-- Name: COLUMN "USER"."NICKNAME"; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public."USER"."NICKNAME" IS '昵称';


--
-- Name: COLUMN "USER"."USERNAME"; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public."USER"."USERNAME" IS '用户名';


--
-- Name: COLUMN "USER"."PASSWORD"; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public."USER"."PASSWORD" IS '密码';


--
-- Name: COLUMN "USER"."CREATE_TIME"; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public."USER"."CREATE_TIME" IS '创建时间';


--
-- Name: COLUMN "USER"."CREATE_BY"; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public."USER"."CREATE_BY" IS '创建人ID';


--
-- Data for Name: ANNEX; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."ANNEX" ("ID", "DISPLAY_NAME", "EXTENSION_TYPE", "FILE_URL", "STATUS", "BUCKET_NAME", "BUCKET_ID", "STAFF_ID", "MESSAGE_ID", "CREATE_TIME", "CREATE_BY") FROM stdin;
\.


--
-- Data for Name: MESSAGE; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."MESSAGE" ("ID", "SESSION_ID", "SEND_ID", "SEND_NAME", "CONTENT", "TYPE", "ANNEX", "CREATE_TIME") FROM stdin;
\.


--
-- Data for Name: SESSIONS; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."SESSIONS" ("ID", "SESSION_NAME", "SESSION_TYPE", "STAFF_ID", "CREATE_TIME", "STATUS") FROM stdin;
\.


--
-- Data for Name: USER; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."USER" ("ID", "NICKNAME", "USERNAME", "PASSWORD", "CREATE_TIME", "CREATE_BY") FROM stdin;
1	Sika	Sika	\N	2025-03-08 22:24:52.032792	1
\.


--
-- Name: ANNEX ANNEX_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."ANNEX"
    ADD CONSTRAINT "ANNEX_pkey" PRIMARY KEY ("ID");


--
-- Name: MESSAGE MESSAGES_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."MESSAGE"
    ADD CONSTRAINT "MESSAGES_pkey" PRIMARY KEY ("ID");


--
-- Name: SESSIONS SESSIONS_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."SESSIONS"
    ADD CONSTRAINT "SESSIONS_pkey" PRIMARY KEY ("ID");


--
-- Name: USER USER_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."USER"
    ADD CONSTRAINT "USER_pkey" PRIMARY KEY ("ID");


--
-- PostgreSQL database dump complete
--

