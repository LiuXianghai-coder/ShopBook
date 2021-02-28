--
-- PostgreSQL database dump
--

-- Dumped from database version 12.1
-- Dumped by pg_dump version 12.1

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: delete_book(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.delete_book() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        DELETE FROM book_belong WHERE isbn = old.isbn;
        DELETE FROM book_image_address WHERE isbn = old.isbn;
        DELETE FROM book_publisher WHERE isbn = old.isbn;
        DELETE FROM book_upload WHERE isbn = old.isbn;
        DELETE FROM shop_book WHERE isbn = old.isbn;

        RETURN old;
    END IF;
END
$$;


ALTER FUNCTION public.delete_book() OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: add_shop_cart; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.add_shop_cart (
    user_id character varying(20) NOT NULL,
    shop_id character varying(20) NOT NULL,
    isbn bigint NOT NULL
);


ALTER TABLE public.add_shop_cart OWNER TO postgres;

--
-- Name: author; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.author (
    author_id integer NOT NULL,
    author_name character varying(70) NOT NULL
);


ALTER TABLE public.author OWNER TO postgres;

--
-- Name: author_author_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.author_author_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.author_author_id_seq OWNER TO postgres;

--
-- Name: author_author_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.author_author_id_seq OWNED BY public.author.author_id;


--
-- Name: book; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.book (
    isbn bigint NOT NULL,
    book_name character varying(100) NOT NULL
);


ALTER TABLE public.book OWNER TO postgres;

--
-- Name: book_belong; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.book_belong (
    isbn bigint NOT NULL,
    book_author_id integer NOT NULL
);


ALTER TABLE public.book_belong OWNER TO postgres;

--
-- Name: book_image_address; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.book_image_address (
    shop_id character varying(20) NOT NULL,
    isbn bigint NOT NULL,
    image_address text NOT NULL
);


ALTER TABLE public.book_image_address OWNER TO postgres;

--
-- Name: book_publisher; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.book_publisher (
    isbn bigint NOT NULL,
    publisher_id integer NOT NULL,
    publisher_date date NOT NULL
);


ALTER TABLE public.book_publisher OWNER TO postgres;

--
-- Name: book_upload; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.book_upload (
    seller_id character varying(20) NOT NULL,
    isbn bigint NOT NULL,
    shop_id character varying(20) NOT NULL,
    upload_date date
);


ALTER TABLE public.book_upload OWNER TO postgres;

--
-- Name: chat; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.chat (
    user_id character varying(20) NOT NULL,
    seller_id character varying(20) NOT NULL,
    chat_date date NOT NULL
);


ALTER TABLE public.chat OWNER TO postgres;

--
-- Name: publisher; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.publisher (
    publisher_id integer NOT NULL,
    publisher_name character varying(20) NOT NULL
);


ALTER TABLE public.publisher OWNER TO postgres;

--
-- Name: publisher_publisher_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.publisher_publisher_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.publisher_publisher_id_seq OWNER TO postgres;

--
-- Name: publisher_publisher_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.publisher_publisher_id_seq OWNED BY public.publisher.publisher_id;


--
-- Name: purchase; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.purchase (
    user_id character varying(20) NOT NULL,
    shop_id character varying(20) NOT NULL,
    purchase_price double precision DEFAULT 0 NOT NULL,
    transaction_detail text NOT NULL
);


ALTER TABLE public.purchase OWNER TO postgres;

--
-- Name: shop; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.shop (
    shop_id character varying(20) NOT NULL,
    shop_name character varying(30) NOT NULL,
    shop_create_date date,
    seller_id character varying(20),
    shop_reputation double precision DEFAULT 0
);


ALTER TABLE public.shop OWNER TO postgres;

--
-- Name: shop_book; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.shop_book (
    shop_id character varying(20) NOT NULL,
    isbn bigint NOT NULL,
    price double precision DEFAULT 0,
    describe text
);


ALTER TABLE public.shop_book OWNER TO postgres;

--
-- Name: shop_image; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.shop_image (
    shop_id character varying(20) NOT NULL,
    image text NOT NULL
);


ALTER TABLE public.shop_image OWNER TO postgres;

--
-- Name: shop_seller; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.shop_seller (
    seller_id character varying(20) NOT NULL,
    seller_password character varying(50) NOT NULL,
    seller_name character varying(30) NOT NULL,
    seller_money double precision DEFAULT 0,
    seller_register_date date
);


ALTER TABLE public.shop_seller OWNER TO postgres;

--
-- Name: shop_user; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.shop_user (
    user_id character varying(20) NOT NULL,
    user_password character varying(50) NOT NULL,
    user_name character varying(30) NOT NULL,
    user_money double precision DEFAULT 0,
    user_register_date date
);


ALTER TABLE public.shop_user OWNER TO postgres;

--
-- Name: author author_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.author ALTER COLUMN author_id SET DEFAULT nextval('public.author_author_id_seq'::regclass);


--
-- Name: publisher publisher_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.publisher ALTER COLUMN publisher_id SET DEFAULT nextval('public.publisher_publisher_id_seq'::regclass);


--
-- Data for Name: add_shop_cart; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.add_shop_cart (user_id, shop_id, isbn) FROM stdin;
13245	abc_123456	9787115521637
13245	abc_123456	9787115428028
13245	abc_123456	9787111617945
13245	abc_123456	9787115275790
13245	abc_123456	9787115516756
123456	abc_123456	9787115508232
123456	abc_123456	9787115275790
123456	abc_123456	9787111407010
123456	abc_123456	9787111643432
123456	abc_123456	9787532169955
123456	abc_123456	9787115514226
123456	abc_123456	9787115428028
123456	abc_123456	9787115517807
\.


--
-- Data for Name: author; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.author (author_id, author_name) FROM stdin;
1	Eric Matthes
2	Martin Fowler
3	Jon Bentley
4	Stephen Prata
5	W. Richard Stevens
6	Stephen A. Rago
7	Nicholas C.Zakas
8	Andrew W.Appel
9	埃莱娜·费兰特
10	Bruce Eckel
11	Brian W. Kernighan
12	Dennis M.Ritchie
13	Thomas H. Cormen
14	Charles E. Leiserson
15	Ronald L. Rivest
16	Clifford Stein
17	Cay S.Horstmann
18	Harold Abelson
19	Gerald Jay Sussman
20	Julie Sussman
21	Abraham Silberschatz
22	Henry F.Korth
23	S.Sudarshan
24	Andrew S. Tanenbaum
25	Herbert Bos
26	Andrew M.Rudoff
27	刘畅
28	孙连英
29	彭涛
30	Robert Sedgewick
31	Kevin Wayne
34	吴军
69	查尔斯·狄更斯
71	Khaled Hosseini
75	b
76	东野圭吾
81	叶圣陶
85	WLOP
87	AA
88	AAAAAAAAAAAAAAAAAAAAAAAAABBBBBBBBBBBBBBBBBBBBBBBBBCCCCCCCCCCCCCCCCCCC
89	AAAAAAAAAAAAAAAAAAAAAAAAABBBBBBBBBBBBBBBBBBBBBBBBBCCCCCCCCCCCCCCCCCCCC
90	Kevin R. Fall
102	Gary R.Wrightl
103	W.Richard Stevens
\.


--
-- Data for Name: book; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.book (isbn, book_name) FROM stdin;
9787115428028	Python编程 从入门到实践
9787115508652	重构 改善既有代码的设计（第2版 平装版）
9787115357618	编程珠玑（第2版 修订版）
9787115521637	C Primer Plus
9787115516756	UNIX环境高级编程 第3版
9787115275790	JavaScript高级程序设计（第3版）
9787115476883	现代编译原理 C语言描述
9787020120130	那不勒斯四部曲：我的天才女友
9787532169948	那不勒斯四部曲：新名字的故事
9787020132003	那不勒斯四部曲：离开的留下的
9787020139927	那不勒斯四部曲：失踪的孩子
9787111213826	Java编程思想（第4版）
9787111617945	C程序设计语言
9787111407010	算法导论（原书第3版）
9787111630548	计算机程序的构造和解释
9787111636663	Java核心技术 卷I 基础知识
9787111643432	Java核心技术 卷II 高级特性
9787111375296	数据库系统概念（原书第6版）
9787111573692	现代操作系统（原书第4版）
9787115517791	UNIX网络编程 卷1
9787115517807	UNIX网络编程 卷2
9787302489078	Java面向对象程序设计
9787115293800	算法(第4版)
9787115390592	C Primer Plus(6th)
9787532169955	那不勒斯四部曲：离开的留下的
9787020125265	那不勒斯四部曲：新名字的故事
9787115516282	编程珠玑 第2版
9787115514226	浪潮之巅 第四版
9787115508232	操作系统导论
9787115307453	Linux命令行大全
9787111586647	现代网络技术：SDN、NFV、QoE、物联网和云计算
9787111617778	TCP/IP详解 卷3：TCP事务协议、HTTP、NNTP和UNIX域协议
\.


--
-- Data for Name: book_belong; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.book_belong (isbn, book_author_id) FROM stdin;
9787115428028	1
9787115508652	2
9787115357618	3
9787115521637	4
9787115517807	5
9787115517791	5
9787115516756	5
9787115516756	6
9787115275790	7
9787115476883	8
9787020139927	9
9787020132003	9
9787532169948	9
9787020120130	9
9787111213826	10
9787111617945	11
9787111617945	12
9787111407010	13
9787111407010	14
9787111407010	15
9787111407010	16
9787111643432	17
9787111636663	17
9787111630548	18
9787111630548	19
9787111630548	20
9787111375296	21
9787111375296	22
9787111375296	23
9787111573692	24
9787111573692	25
9787115517791	26
9787302489078	27
9787302489078	28
9787302489078	29
9787115293800	30
9787115293800	31
9787115390592	4
9787532169955	9
9787020125265	9
9787115516282	3
9787115514226	34
9787115508232	24
9787115508232	25
9787111617778	103
\.


--
-- Data for Name: book_image_address; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.book_image_address (shop_id, isbn, image_address) FROM stdin;
abc_123456	9787111617778	TCP_IP_3.jpg
abc_123456	9787111617945	C.jpg
abc_123456	9787115357618	program_perl.jpg
abc_123456	9787115521637	C Primer Plus.jpg
abc_123456	9787115516756	unix_advance.jpg
abc_123456	9787115275790	JavaScript Professinal.jpg
abc_123456	9787115476883	modern_compile.jpg
abc_123456	9787532169948	Nabules Vol2.jpg
abc_123456	9787020132003	Nabules Vol3.jpg
abc_123456	9787020139927	Nabules Vol4.jpg
abc_123456	9787111213826	Java Programmer Thinking.jpg
abc_123456	9787111407010	AlogrithmIntroduction.jpg
abc_123456	9787111630548	Computer Programmer Explain.jpg
abc_123456	9787111636663	Java Core Vol1.jpg
abc_123456	9787111643432	Java Core Vol2.jpg
abc_123456	9787111375296	Database Define.jpg
abc_123456	9787111573692	Modern Operating System.jpg
abc_123456	9787115517791	unix_networking_vol1.jpg
abc_123456	9787115517807	unix_networking_vol2.jpg
abc_123456	9787115293800	Alogrithm(4th).jpg
abc_123456	9787115390592	C Primer Plus.jpg
abc_123456	9787115516282	program_perl.jpg
abc_123456	9787020125265	Nabules Vol2.jpg
abc_123456	9787532169955	Nabules Vol3.jpg
abc_123456	9787115428028	Python.jpg
abc_123456	9787115514226	浪潮之巅.jpg
abc_123456	9787115508232	操作系统导论.jpg
12345620200521	9787115521637	C Primer Plus.jpg
12345620200521	9787115275790	JavaScript Professinal.jpg
abc_123456	9787115508652	refactor.jpg
\.


--
-- Data for Name: book_publisher; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.book_publisher (isbn, publisher_id, publisher_date) FROM stdin;
9787115293800	1	2012-12-12
9787115517807	1	2019-12-12
9787115517791	1	2019-10-01
9787115476883	1	2018-04-01
9787115516756	1	2019-10-01
9787115357618	1	2015-01-01
9787115508652	1	2019-05-01
9787115428028	1	2016-07-01
9787020139927	2	2018-07-01
9787020132003	2	2019-03-01
9787020120130	2	2017-01-01
9787111573692	3	2017-07-01
9787111375296	3	2012-04-01
9787111643432	3	2020-01-01
9787111636663	3	2019-12-01
9787111630548	3	2019-07-01
9787111407010	3	2012-12-01
9787111617945	3	2019-04-01
9787111213826	3	2007-06-01
9787302489078	4	2017-12-12
9787115390592	1	2019-11-01
9787532169955	2	2019-03-01
9787020125265	2	2017-05-01
9787115516282	1	2019-10-01
9787115514226	1	2019-07-01
9787115508232	1	2019-07-01
9787115307453	1	2013-03-01
9787115521637	1	2019-11-01
9787115275790	1	2012-03-01
9787111586647	3	2018-01-01
9787111617778	3	2019-03-01
\.


--
-- Data for Name: book_upload; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.book_upload (seller_id, isbn, shop_id, upload_date) FROM stdin;
123456	9787115428028	abc_123456	2020-05-15
123456	9787115508232	abc_123456	2020-05-20
123456	9787115307453	12345620200521	2020-05-21
123456	9787115521637	12345620200521	2020-05-21
123456	9787115275790	12345620200521	2020-05-21
123456	9787111586647	12345620200521	2020-05-21
123456	9787111617778	abc_123456	2020-07-08
\.


--
-- Data for Name: chat; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.chat (user_id, seller_id, chat_date) FROM stdin;
\.


--
-- Data for Name: publisher; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.publisher (publisher_id, publisher_name) FROM stdin;
1	人民邮电出版社
2	人民文学出版社
3	机械工业出版社
4	清华大学出版社
29	中国华侨出版社
37	a
50	AA
52	AAAAAAAAAAAAAAAAAAAA
\.


--
-- Data for Name: purchase; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.purchase (user_id, shop_id, purchase_price, transaction_detail) FROM stdin;
13245	abc_123456	78.8	交易书籍的ISBN: 9787115521637交易时间: 2020年5月21日星期4 13:44:50
13245	abc_123456	78	交易书籍的ISBN: 9787115275790\t交易时间: 2020年5月21日星期4 17:41:38
123456	abc_123456	84.5	交易书籍的ISBN: 9787111407010\t交易时间: 2020年5月22日星期5 19:33:35
\.


--
-- Data for Name: shop; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.shop (shop_id, shop_name, shop_create_date, seller_id, shop_reputation) FROM stdin;
abc_123456	lxh_Shop	2020-05-15	123456	100
12345620200521	当当网旗舰店	2020-05-21	123456	0
12345620200708	人民邮电出版社	2020-07-08	123456	100
\.


--
-- Data for Name: shop_book; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.shop_book (shop_id, isbn, price, describe) FROM stdin;
12345620200521	9787115521637	78.8	    《C Primer Plus（第6版）中文版》详细讲解了C语言的基本概念和编程技巧。 \r\n《C Primer Plus（第6版）中文版》共17章。第1章、第2章介绍了C语言编程的预备知识。第3章～第15章详细讲解了C语言的相关知识，包括数据类型、格式化输入/输出、运算符、表达式、语句、循环、字符输入和输出、函数、数组和指针、字符和字符串函数、内存管理、文件输入和输出、结构、位操作等。第16章、第17章介绍C预处理器、C库和高级数据表示。本书以丰富多样的程序为例，讲解C语言的知识要点和注意事项。每章末尾设计了大量复习题和编程练习，帮助读者巩固所学知识和提高实际编程能力。附录给出了各章复习题的参考答案和丰富的参考资料。 \r\n《C Primer Plus（第6版）中文版》可作为C语言的教材，适用于需要系统学习C语言的初学者，也适用于想要巩固C语言知识或希望进一步提高编程技术的程序员。            
abc_123456	9787115275790	78	作为JavaScript技术经典名著，《JavaScript高级程序设计（第3版）》承继了之前版本全面深入、贴近实战的特点，在详细讲解了JavaScript语言的核心之后，条分缕析地为读者展示了现有规范及实现为开发Web应用提供的各种支持和特性。
abc_123456	9787111617778	41	第3卷详细介绍了当今TCP/IP程序员和网络管理员必须非常熟悉的四个基本主题： TCP的扩展、Hyper文本传输协议、网络新闻传输协议和UNIX域协议。与前两卷一样，本书介绍了4.4BSD-Lite网络代码中的示例和实现细节。
abc_123456	9787115293800	64.35	Sedgewick之巨著，与高德纳TAOCP一脉相承, 几十年多次修订，经久不衰的畅销书, 涵盖所有程序员必须掌握的50种算法
abc_123456	9787115428028	65	本书是一本针对所有层次的Python读者而作的Python入门书。全书分两部分：首部分介绍用Python 编程所必须了解的基本概念，包括matplotlib、NumPy和Pygal等强大的Python库和工具介绍，以及列表、字典、if语句、类、文件与异常、代码测试等内容；第二部分将理论付诸实践，讲解如何开发三个项目，包括简单的Python 2D游戏开发，如何利用数据生成交互式的信息图，以及创建和定制简单的Web应用，并帮读者解决常见编程问题和困惑。
abc_123456	9787115390592	58.4	《C Primer Plus（第6版）中文版》详细讲解了C语言的基本概念和编程技巧。 《C Primer Plus（第6版）中文版》共17章。第1、2章介绍了C语言编程的预备知识。第3~15章详细讲解了C语言的相关知识，包括数据类型、格式化输入/输出、运算符、表达式、语句、循环、字符输入和输出、函数、数组和指针、字符和字符串函数、内存管理、文件输入输出、结构、位操作等。第16章、17章介绍C预处理器、C库和高级数据表示。本书以完整的程序为例，讲解C语言的知识要点和注意事项。每章末设计了大量复习题和编程练习，帮助读者巩固所学知识和提高实际编程能力。附录给出了各章复习题的参考答案和丰富的参考资料。 《C Primer Plus（第6版）中文版》可作为C语言的教材，适用于需要系统学习C语言的初学者，也适用于巩固C语言知识或希望进一步提高编程技术的程序员。
abc_123456	9787020139927	62	《失踪的孩子》是“那不勒斯四部曲”的第四部，小说聚焦了莉拉和埃莱娜（“我”）的壮年和晚年，为她们持续了五十多年的友谊划上了一个令人心碎的句号。“我”为了爱情和写作，离开丈夫带着两个女儿回到了那不勒斯，不可避免地与莉拉，还有我曾想要逃离的城区再度变得亲密。“我”和莉拉甚至在同一年怀孕、生子，并经历了恐怖残暴的那不勒斯大地震，一切都分崩离析，一切又将被重建。“我”在不自觉中卷入莉拉秘密的企图——她希望利用我的名声和写作技巧来对抗城区陈腐而猖獗的恶势力。但在经历了生命恐怖的打击之后，莉拉选择以一种怪异夸张的方式在城区彻底将自己流放。
abc_123456	9787532169955	48.1	《离开的，留下的》 是埃莱娜?费兰特的“那不勒斯四部曲”的第三部，聚焦“我”（埃莱娜）和莉拉躁动、紧密相依的中年。“我”在未婚夫彼得罗一家人的帮助下，出版了本小说，享受着成功的喜悦，而留在那不勒斯的莉拉却身陷贫困而卑贱的工厂生活。“我”像个骑士一样，再度介入莉拉的生活，并动用丈夫一家人的关系，让莉拉和恩佐的生活有了转机——他们成了那不勒斯地区早学习、掌握计算机技术的人，他们顽强、坚韧的学习能力让他们开始积累了巨大的财富。我和莉拉之间再次胜负难辨。婚后的“我”开始面临自己的创作危机，而平静的、中产阶级式的婚姻也令“我”疲惫不堪。对社会变), 革颇为冷淡的彼得罗希望“我”放弃作家的身份，而“我”在扮演“母亲”、“妻子”这些角色时，总是避免不了内心的分裂、紧张。
abc_123456	9787020125265	43	《新名字的故事》是埃莱娜·费兰特的“那不勒斯四部曲”的第二部，描述了埃莱娜和莉拉的青年时代。在她们的人生以zui快的速度急遽分化的那些年里，她们共同体验了爱、失去、困惑、挣扎、嫉妒和隐蔽的破坏。莉拉在结婚当天就发现婚姻根本不是她想象的那样，她的初夜几乎是一场强奸。她带着一种强大的破坏欲介入了斯特凡诺的家族生意，似乎成为了她和埃莱娜小时候都想成为的那种女人。久未有身孕的莉拉，和埃莱娜去海边度假休养。而在伊斯基亚岛的那个夏天，改变了所有人的一生……出于对莉拉所拥有的爱情的愤怒，“我”（埃莱娜）奋力摆脱这个破败、充满暴力和宿仇的街区。“我), ”成了街区的首位大学生，并和一个知识分子家庭的男孩订婚，甚至出版了首本小说。“我”以胜利者的形象回到那不勒斯，却发现告别了丑陋婚姻的莉拉，在一家肉食加工厂备受屈辱地打工。当“我”发现自己的小说，其实完全窃取了莉拉交托给“我”的秘密笔记本里那些独特的力量和灵感，“我”被迫面临一个极度痛苦的问题：“我”和莉拉，到底谁离开了，又是谁留下了？
abc_123456	9787115508652	49.5	本书是经典著作《重构》出版20年后的更新版。书中清晰揭示了重构的过程，解释了重构的原理和实践方式，并给出了何时以及何地应该开始挖掘代码以求改善。书中给出了60多个可行的重构，每个重构都介绍了一种经过验证的代码变换手法的动机和技术。本书提出的重构准则将帮助开发人员一次一小步地修改代码，从而减少了开发过程中的风险。 本书适合软件开发人员、项目管理人员等阅读，也可作为高等院校计算机及相关专业师生的参考读物。
abc_123456	9787115516282	58.4	本书是计算机科学方面的经典名著。书的内容围绕程序设计人员面对的一系列实际问题展开。作者Jon Bentley 以其独有的洞察力和创造力，引导读者理解这些问题并学会解决方法，而这些正是程序员实际编程生涯中至关重要的。本书的特色是通过一些精心设计的有趣而又颇具指导意义的程序，对实用程序设计技巧及基本设计原则进行了透彻而睿智的描述，为复杂的编程问题提供了清晰而完备的解决思路。本书对各个层次的程序员都具有很高的阅读价值。
abc_123456	9787115476883	74.5	本书全面讲述了现代编译器的各个组成部分，包括词法分析、语法分析、抽象语法、语义检查、中间代码表示、指令选择、数据流分析、寄存器分配以及运行时系统等。全书分成两部分，* 一部分是编译的基础知识，适用于* 一门编译原理课程（一个学期）；* 二部分是高 级主题，包括面向对象语言和函数语言、垃圾收集、循环优化、存储结构优化等，适合于后续课程或研究生教学。书中专门为学生提供了一个用C语言编写的实习项目，包括前端和后端设计，学生可以在一学期内创建功能完整的编译器。
abc_123456	9787111617945	47.9	        《C程序设计语言（原书第2版·新版 典藏版）》原著即为C语言的设计者之一DennisM.Ritchie和著名的计算机科学家BrianW.Kernighan合著的一本介绍C语言的经典著作。我们现在见到的大量论述C语言程序设计的教材和专著均以此书为蓝本。原著第1版中介绍的C语言成为后来广泛使用的C语言版本——标准C的基础。人们熟知的“hello，world”程序就是由《C程序设计语言（原书第2版·新版 典藏版）》首次引入的，现在，这一程序已经成为所有程序设计语言入门的第一课。原著第2版根据1987年制定的ANSIC标准做了适当的修订，引入了新的语言形式，并增加了新的示例。通过简洁的描述、典型的示例，作者全面、系统、准确地讲述了C语言的各个特性以及程序设计的基本方法。对于计算机从业人员来说，《C程序设计语言（原书第2版·新版 典藏版）》是一本必读的程序设计语言方面的参考书。                
abc_123456	9787111213826	71.3	《计算机科学丛书：Java编程思想（第4版）》赢得了全球程序员的广泛赞誉，即使是晦涩的概念，在BruceEckel的文字亲和力和小而直接的编程示例面前也会化解于无形。从Java的基础语法到高级特性（深入的面向对象概念、多线程、自动项目构建、单元测试和调试等），本书都能逐步指导你轻松掌握。
abc_123456	9787111636663	98.3	伴随着Java的成长，《Java核心技术》从第1版到第10版一路走来，得到了广大Java程序设计人员的青睐，成为一本畅销不衰的Java经典图书。2019年底，针对Java SE 9、10和11的，《Java核心技术》第11版新鲜出炉，这一版有了大幅的修订和更新，不仅补充了Java新版本的新特性，还对之前比较晦涩的部分做了删改，用更清晰明了的示例加以解释，特别是对内容结构做了调整，从而更有利于读者学习和应用。它将续写从前的辉煌，使人们能及时跟上Java前进的脚步。本卷介绍Java语言和UI编程的基础知识的专业级详解，包括对象、泛型、集合、Lambda表达式、Swing设计、并发和函数式编程等。
abc_123456	9787111643432	117.7	伴随着Java的成长，《Java核心技术》从第1版到第10版一路走来，得到了广大Java程序设计人员的青睐，成为一本畅销不衰的Java经典图书。2019年底，针对Java SE 9、10和11的，《Java核心技术》第11版新鲜出炉，这一版有了大幅的修订和更新，不仅补充了Java新版本的新特性，还对之前比较晦涩的部分做了删改，用更清晰明了的示例加以解释，特别是对内容结构做了调整，从而更有利于读者学习和应用。它将续写从前的辉煌，使人们能及时跟上Java前进的脚步。本卷主要介绍编程人员进行专业软件开发时需要了解的高级主题。建议与卷I*基础知识，一并阅读。
abc_123456	9787115517791	167.3	本书是UNIX网络编程的经典之作。书中全面深入地介绍了如何使用套接字API进行网络编程。全书不但介绍了基本编程内容，还涵盖了与套接字编程相关的高级主题，对于客户/服务器程序的各种设计方法也作了完整的探讨，最后还深入分析了流这种设备驱动机制。 本书内容详尽且具**性，几乎每章都提供精选的习题，并提供了部分习题的答案，是网络研究和开发人员理想的参考书。
abc_123456	9787115517807	107.9	本书是一部UNIX 网络编程的经典之作！进程间通信（IPC）几乎是所有Unix 程序性能的关键，理解IPC 也是理解如何开发不同主机间网络应用程序的必要条件。本书从对Posix IPC 和System V IPC 的内部结构开始讨论，全面深入地介绍了4 种IPC 形式：消息传递（管道、FIFO、消息队列）、同步（互斥锁、条件变量、读写锁、文件与记录锁、信号量）、共享内存（匿名共享内存、具名共享内存）及远程过程调用（Solaris门、Sun RPC）。附录中给出了测量各种IPC 形式性能的方法。 本书内容详尽且具**性，几乎每章都提供精选的习题，并提供了部分习题的答案，是网络研究和开发人员理想的参考书。
abc_123456	9787111407010	84.5	在有关算法的书中，有一些叙述非常严谨，但不够全面；另一些涉及了大量的题材，但又缺乏严谨性。《算法导论（原书第3版）/计算机科学丛书》将严谨性和全面性融为一体，深入讨论各类算法，并着力使这些算法的设计和分析能为各个层次的读者接受。全书各章自成体系，可以作为独立的学习单元；算法以英语和伪代码的形式描述，具备初步程序设计经验的人就能看懂；说明和解释力求浅显易懂，不失深度和数学严谨性。《算法导论（原书第3版）/计算机科学丛书》全书选材经典、内容丰富、结构合理、逻辑清晰，对本科生的数据结构课程和研究生的算法课程都是非常实用的教材，在IT专业人员的职业生涯中，《算法导论（原书第3版）/计算机科学丛书》也是一本案头必备的参考书或工程实践手册。
abc_123456	9787115516756	167.3	                        
abc_123456	9787111573692	98	本书是操作系统领域的经典教材，主要内容包括进程与线程、内存管理、文件系统、输入/输出、死锁、虚拟化和云、多处理机系统、安全，以及关于UNIX、Linux、Android和Windows的实例研究等。第4版对知识点进行了全面更新，反映了当代操作系统的发展与动向。本书适合作为高等院校计算机专业的操作系统课程教材，也适合相关技术人员参考。
abc_123456	9787111375296	81.7	《数据库系统概念（原书第6版）》是经典的数据库系统教科书《Database System Concepts》的新修订版，全面介绍数据库系统的各种知识，透彻阐释数据库管理的基本概念。本书内容丰富，不仅讨论了关系数据模型和关系语言、数据库设计过程、关系数据库理论、数据库应用设计和开发、数据存储结构、数据存取技术、查询优化方法、事务处理系统和并发控制、故障恢复技术、数据仓库和数据挖掘，而且对性能调整、性能评测标准、数据库应用测试和标准化、空间和地理数据、时间数据、多媒体数据、移动和个人数据库管理以及事务处理监控器、事务工作流、电子商务、高性能事务系统、实时事务系统和持续长时间的事务等高级应用主题进行了广泛讨论。
abc_123456	9787111630548	70.1	本书曾是美国麻省理工学院计算机科学专业的入门课程教材之一， 从理论上讲解计算机程序的创建、 执行和研究。 主要内容包括：构造过程抽象，构造数据抽象，模块化、 对象和状态，元语言抽象，寄存器机器里的计算等。
abc_123456	9787115508232	98	本书是操作系统领域的经典教材，主要内容包括进程与线程、内存管理、文件系统、输入/输 出、死锁、虚拟化和云、多处理机系统、安全，以及关于UNIX、Linux、Android和Windows的实例研究等。第4版对知识点进行了全面更新，反映了当代操作系统的发展与动向。本书适合作为高等院校计算机专业的操作系统课程教材，也适合相关技术人员参考
abc_123456	9787115514226	137.6	这不只是一部科技产业发展历史集……\r\n更是在这个智能时代，一部 IT 人非读不可，而非 IT 人也应该阅读的作品。\r\n一个企业的发展与崛起，绝非只是空有领导强人即可达成。任何的决策、同期的商业环境、各种能量的此消彼长，也在影响着企业的兴衰。《浪潮之巅》不只是一部历史书 ，除了讲述科技顶jian企业的发展规律， 对于华尔街如何左右科技公司，以及金融风暴对科技产业的冲击，也多有着\r\n《浪潮之巅 第四版》新增了6章内容，探讨硅谷不竭的创新精神究竟源自何处，进一步从工业革命的范式、生产关系的革命等角度深入全面阐述信息产业的规律性。从而，借助对信息时代公司管理特点进行的系统分析，对下一代科技产业浪潮给出判断和预测。
\.


--
-- Data for Name: shop_image; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.shop_image (shop_id, image) FROM stdin;
abc_123456	25475-102.jpg
12345620200521	89198-102.jpg
12345620200708	TCP_IP_1.jpg
\.


--
-- Data for Name: shop_seller; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.shop_seller (seller_id, seller_password, seller_name, seller_money, seller_register_date) FROM stdin;
123456	123456	lxh	200	2020-05-15
18373796017	123456	刘湘海	100	2020-05-22
abcd_1234	123456	李大伟	100	2020-05-22
\.


--
-- Data for Name: shop_user; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.shop_user (user_id, user_password, user_name, user_money, user_register_date) FROM stdin;
13245	123456	jack	100	2020-05-17
123456	123456	刘湘海	100	2020-05-22
\.


--
-- Name: author_author_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.author_author_id_seq', 104, true);


--
-- Name: publisher_publisher_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.publisher_publisher_id_seq', 66, true);


--
-- Name: add_shop_cart add_shop_cart_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.add_shop_cart
    ADD CONSTRAINT add_shop_cart_pkey PRIMARY KEY (user_id, shop_id, isbn);


--
-- Name: author author_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.author
    ADD CONSTRAINT author_pkey PRIMARY KEY (author_id);


--
-- Name: author author_unique_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.author
    ADD CONSTRAINT author_unique_key UNIQUE (author_name);


--
-- Name: book_belong book_belong_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_belong
    ADD CONSTRAINT book_belong_pkey PRIMARY KEY (isbn, book_author_id);


--
-- Name: book_image_address book_image_address_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_image_address
    ADD CONSTRAINT book_image_address_pkey PRIMARY KEY (shop_id, isbn, image_address);


--
-- Name: book book_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book
    ADD CONSTRAINT book_pkey PRIMARY KEY (isbn);


--
-- Name: book_publisher book_publisher_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_publisher
    ADD CONSTRAINT book_publisher_pkey PRIMARY KEY (isbn, publisher_id);


--
-- Name: book_upload book_upload_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_upload
    ADD CONSTRAINT book_upload_pkey PRIMARY KEY (seller_id, isbn, shop_id);


--
-- Name: chat chat_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chat
    ADD CONSTRAINT chat_pkey PRIMARY KEY (user_id, seller_id, chat_date);


--
-- Name: publisher publisher_name_unique; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.publisher
    ADD CONSTRAINT publisher_name_unique UNIQUE (publisher_name);


--
-- Name: publisher publisher_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.publisher
    ADD CONSTRAINT publisher_pkey PRIMARY KEY (publisher_id);


--
-- Name: purchase purchase_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase
    ADD CONSTRAINT purchase_pkey PRIMARY KEY (user_id, shop_id, purchase_price, transaction_detail);


--
-- Name: shop_book shop_book_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.shop_book
    ADD CONSTRAINT shop_book_pkey PRIMARY KEY (shop_id, isbn);


--
-- Name: shop_image shop_image_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.shop_image
    ADD CONSTRAINT shop_image_pkey PRIMARY KEY (shop_id, image);


--
-- Name: shop shop_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.shop
    ADD CONSTRAINT shop_pkey PRIMARY KEY (shop_id);


--
-- Name: shop_seller shop_seller_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.shop_seller
    ADD CONSTRAINT shop_seller_pkey PRIMARY KEY (seller_id);


--
-- Name: shop_user shop_user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.shop_user
    ADD CONSTRAINT shop_user_pkey PRIMARY KEY (user_id);


--
-- Name: author unique_author_name; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.author
    ADD CONSTRAINT unique_author_name UNIQUE (author_name);


--
-- Name: publisher unique_publisher_name; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.publisher
    ADD CONSTRAINT unique_publisher_name UNIQUE (publisher_name);


--
-- Name: book delete_book_trigger; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER delete_book_trigger BEFORE DELETE ON public.book FOR EACH ROW EXECUTE FUNCTION public.delete_book();


--
-- Name: book_belong book_belong_book_author_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_belong
    ADD CONSTRAINT book_belong_book_author_id_fkey FOREIGN KEY (book_author_id) REFERENCES public.author(author_id);


--
-- Name: book_belong book_belong_isbn_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_belong
    ADD CONSTRAINT book_belong_isbn_fkey FOREIGN KEY (isbn) REFERENCES public.book(isbn);


--
-- Name: book_image_address book_image_address_isbn_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_image_address
    ADD CONSTRAINT book_image_address_isbn_fkey FOREIGN KEY (isbn) REFERENCES public.book(isbn);


--
-- Name: book_image_address book_image_address_shop_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_image_address
    ADD CONSTRAINT book_image_address_shop_id_fkey FOREIGN KEY (shop_id) REFERENCES public.shop(shop_id);


--
-- Name: book_publisher book_publisher_isbn_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_publisher
    ADD CONSTRAINT book_publisher_isbn_fkey FOREIGN KEY (isbn) REFERENCES public.book(isbn);


--
-- Name: book_publisher book_publisher_publisher_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_publisher
    ADD CONSTRAINT book_publisher_publisher_id_fkey FOREIGN KEY (publisher_id) REFERENCES public.publisher(publisher_id);


--
-- Name: book_upload book_upload_isbn_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_upload
    ADD CONSTRAINT book_upload_isbn_fkey FOREIGN KEY (isbn) REFERENCES public.book(isbn);


--
-- Name: book_upload book_upload_seller_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_upload
    ADD CONSTRAINT book_upload_seller_id_fkey FOREIGN KEY (seller_id) REFERENCES public.shop_seller(seller_id);


--
-- Name: book_upload book_upload_shop_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_upload
    ADD CONSTRAINT book_upload_shop_id_fkey FOREIGN KEY (shop_id) REFERENCES public.shop(shop_id);


--
-- Name: chat chat_seller_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chat
    ADD CONSTRAINT chat_seller_id_fkey FOREIGN KEY (seller_id) REFERENCES public.shop_seller(seller_id);


--
-- Name: chat chat_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chat
    ADD CONSTRAINT chat_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.shop_user(user_id);


--
-- Name: purchase purchase_shop_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase
    ADD CONSTRAINT purchase_shop_id_fkey FOREIGN KEY (shop_id) REFERENCES public.shop(shop_id);


--
-- Name: purchase purchase_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase
    ADD CONSTRAINT purchase_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.shop_user(user_id);


--
-- Name: shop_book shop_book_isbn_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.shop_book
    ADD CONSTRAINT shop_book_isbn_fkey FOREIGN KEY (isbn) REFERENCES public.book(isbn);


--
-- Name: shop_book shop_book_shop_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.shop_book
    ADD CONSTRAINT shop_book_shop_id_fkey FOREIGN KEY (shop_id) REFERENCES public.shop(shop_id);


--
-- Name: shop_image shop_image_shop_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.shop_image
    ADD CONSTRAINT shop_image_shop_id_fkey FOREIGN KEY (shop_id) REFERENCES public.shop(shop_id);


--
-- Name: shop shop_seller_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.shop
    ADD CONSTRAINT shop_seller_id_fkey FOREIGN KEY (seller_id) REFERENCES public.shop_seller(seller_id);


--
-- PostgreSQL database dump complete
--

