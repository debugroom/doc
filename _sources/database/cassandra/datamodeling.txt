.. include:: ../../module.txt

.. _section4-cassndra-database-manipulate-label:

データベースの操作
=====================================================

.. _section4-1-environment-label:

動作環境
-----------------------------------------------------

インストール手順は、`Cassandraのインストール <http://debugroom.github.io/doc/memo/work/cassandra/install.html>`_ を、キースペース、テーブルの作成は、`データベースの定義 <http://debugroom.github.io/doc/database/cassandra/definition.html>`_ を参照すること。

[OS]
MacOSX 10.9.5

[JVM]
Java(TM) SE Runtime Environment (build 1.8.0_40-b27) Java HotSpot(TM) 64-Bit Server VM (build 25.40-b25, mixed mode)

[バージョン]

* cqlsh 5.0.1
* Cassandra 3.7
* CQL spec 3.4.2
* Native protocol v4

.. _section4-2-cassandra-manipulate-data-label:

データの操作
----------------------------------------------------

ここでは、データの検索、挿入、更新、削除を実行するCQLコマンドについて概説する。

.. note:: 外部ファイルに定義したCQLコマンド(xxxxx.cql)を実行したい場合は、ファイルがあるディレクトリ上でcqlshを実行し、"source 'xxxxxx.cql'"を実行する。

.. note:: 使用可能なデータ型は `CREATE TABLE <section3-2-5-cassandra-create-table-label:>`_ を参照のこと。

.. _section4-2-1-cassandra-manipulate-select-label:

SELECTステートメント
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

SELECTステートメントは以下の構文で記述される。

.. sourcecode:: sql

   select_statement ::=  SELECT [ JSON | DISTINCT ] ( select_clause | '*' )
                           FROM table_name
                             [ WHERE where_clause ]
                             [ GROUP BY group_by_clause ]
                             [ ORDER BY ordering_clause ]
                             [ PER PARTITION LIMIT (integer | bind_marker) ]
                             [ LIMIT (integer | bind_marker) ]
                             [ ALLOW FILTERING ]
   select_clause    ::=  selector [ AS identifier ] ( ',' selector [ AS identifier ] )
   selector         ::=  column_name
                             | term
                             | CAST '(' selector AS cql_type ')'
                             | function_name '(' [ selector ( ',' selector )* ] ')'
                             | COUNT '(' '*' ')'
   where_clause     ::=  relation ( AND relation )*
   relation         ::=  column_name operator term
                            '(' column_name ( ',' column_name )* ')' operator tuple_literal
                               TOKEN '(' column_name ( ',' column_name )* ')' operator term
   operator         ::=  '=' | '<' | '>' | '<=' | '>=' | '!=' | IN | CONTAINS | CONTAINS KEY
   group_by_clause  ::=  column_name ( ',' column_name )*
   ordering_clause  ::=  column_name [ ASC | DESC ] ( ',' column_name [ ASC | DESC ] )*


実行例は以下の通りである。

.. sourcecode:: sql

   SELECT name, occupation FROM users WHERE userid IN (199, 200, 207);
   SELECT JSON name, occupation FROM users WHERE userid = 199;
   SELECT name AS user_name, occupation AS user_occupation FROM users;

   SELECT time, value
           FROM events
       WHERE event_type = 'myEvent'
           AND time > '2011-02-03'
           AND time <= '2012-01-01'

   SELECT COUNT (*) AS user_count FROM users;

.. todo:: 公式マニュアルを和訳して記述

.. _section4-2-2-cassandra-manipulate-insert-label:

INSERTステートメント
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

INSERTステートメントは以下の構文で記述される。

.. sourcecode:: sql

   insert_statement ::=  INSERT INTO table_name ( names_values | json_clause )
                         [ IF NOT EXISTS ]
                         [ USING update_parameter ( AND update_parameter )* ]
   names_values     ::=  names VALUES tuple_literal
   json_clause      ::=  JSON string [ DEFAULT ( NULL | UNSET ) ]
   names            ::=  '(' column_name ( ',' column_name )* ')'


実行例は以下の通りである。

.. sourcecode:: sql

   INSERT INTO NerdMovies (movie, director, main_actor, year)
      VALUES ('Serenity', 'Joss Whedon', 'Nathan Fillion', 2005)
      USING TTL 86400;

   INSERT INTO NerdMovies JSON 
   ' { "movie": "Serenity", "director": "Joss Whedon", "year": 2005 }';

.. todo:: 公式マニュアルを和訳して記述

.. _section4-2-3-cassandra-manipulate-update-label:

UPDATEステートメント
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. todo:: 公式マニュアルを和訳して記述

.. _section4-2-4-cassandra-manipulate-delete-label:

DELETEステートメント
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. todo:: 公式マニュアルを和訳して記述
