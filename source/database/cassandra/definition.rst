.. include:: ../../module.txt

.. _section3-cassandra-database-definition-label:

データベースの定義
=====================================================

インストール手順は、`Cassandraのインストール <section1-cassandra-install-label>`_ を参考のこと。
また、 `Apache Cassandraの公式ページ <http://cassandra.apache.org/doc/latest/cql/ddl.html>`_ も参照すること。動作環境は、インストール環境と同じく以下の通りである。

[バージョン]

* cqlsh 5.0.1
* Cassandra 3.7
* CQL spec 3.4.2
* Native protocol v4


CQLで格納されたデータは、テーブルによりその構造が決定され、キースペースによりグルーピングされる。キースペースでは、全てのテーブル定義に対し適用される多くのオプションの定義や、レプリケーション戦略を決定する。

.. _section3-1-cassandra-database-common-definition-label:

共通定義
-----------------------------------------------------

キースペース及びテーブルは以下の文法により記述される。

.. sourcecode:: sql
   
   keyspace_name ::=  name
   table_name    ::=  [ keyspace_name '.' ] name
   name          ::=  unquoted_name | quoted_name
   unquoted_name ::=  re('[a-zA-Z_0-9]{1, 48}')
   quoted_name   ::=  '"' unquoted_name '"'

キースペースとテーブル名は、空白を含まない、48文字以内の英数字のみで構成される必要がある。大文字小文字は区別はないが、ダブルクォートで囲むと大文字小文字区別する。テーブルはキースペースにより区別される必要があるが、指定しない場合、カレントで指定されているキースペースが使用される。


.. _section3-2-cassandra-create-keyspace-and-table-label:

キースペース・テーブルの作成
-----------------------------------------------------

.. _section3-2-1-cassandra-create-keyspace-label:

CREATE KEYSPACE ステートメント
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

キースペースは、以下の構文のCREATE KEYSPACEステートメントにより作成される。

.. sourcecode:: sql
   
   create_keyspace_statement ::=  CREATE KEYSPACE [ IF NOT EXISTS ] keyspace_name WITH options

オプションは、以下が選択できる。

.. list-table:: CREATE KEYSPACEのオプション
   :header-rows: 1
   :widths: 30,20,20,30,50

   * - オプション名
     - 形式
     - 必須
     - デフォルト値
     - 詳細
   * - replication
     - マップ
     - ●
     - 
     - キースペースのレプリケーション戦略。|br| replicationプロパティには"class"属性で、|br| レプリケーションクラス名を指定する。
   * - durable_writes
     - boolean
     - ×
     - true
     - キースペース内での更新にコミットログを |br| 使用するか否か。

.. note::

   アプリケーションごとに複数のキースペースを作成するのは可能であるが、あまり推奨されない。後述するレプリケーションファクタを絡むごとに分離したい場合などがあげられる。キースペースは、リレーショナルデータベースでいうところのデータベースに近い位置づけなので、直感的にアプリケーションで複数のデータベースに接続しない方が良い理由は直感的にも分かるだろう。

.. list-table:: レプリケーション戦略
   :header-rows: 1
   :widths: 30,70

   * - レプリケーション戦略クラス
     - 詳細

   * - SimpleStrategy
     - クラスター全体のレプリケーションファクタを |br| 定義する戦略。replication_factorのみが |br| 定義可能である。
   * - NetworkTopologyStrategy
     - それぞれのデータセンターで独立してレプリケーションファクタを |br| 設定する戦略。サブオプションの残りは、データセンタ名と、 |br| レプリケーションファクタを指定する。


.. note::

   レプリケーションファクタは各Rowがもつデータの複製を幾つのノードに行き渡させるかを決定する。レプリケーションファクタが3であれば、各Rowはリング上の３つのノードに複製される。レプリケーションの種類や配置戦略を別途 `レプリケーション戦略 <http>`_ 参考にすること。

ターミナルコマンドからcqlshを起動し、キースペースを作成する。

.. sourcecode:: sql
   
   cqlsh localhost 9042

   cqlsh> CREATE KEYSPACE sample WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 1};

   cqlsh> describe keyspaces;

   system_schema  system_auth  system  sample  system_distributed  system_traces

なお、上記のdescribeステートメントの詳細は、 :ref:`DESCRIBE ステートメント <section3-2-X-cassandra-describe-label>` を参照のこと。

.. _section3-2-2-cassandra-use-label:

USE ステートメント
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

USEステートメントは、現在のキースペースを切り替えるために、以下の構文で使用される。

.. sourcecode:: sql

   use_statement ::=  USE keyspace_name

実行例は以下の通りである。

.. sourcecode:: sql

   cqlsh> USE sample;


.. _section3-2-3-cassandra-alter-keyspace-label:

ALTER KEYSPACE ステートメント
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

ALTER KEYSPACEステートメントはキースペースの定義を変更するために、以下の構文で使用される。

.. sourcecode:: sql

   alter_keyspace_statement ::=  ALTER KEYSPACE keyspace_name WITH options

実行例は以下の通りである。

.. sourcecode:: sh

   cqlsh> ALTER KEYSPACE sample WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 4};


.. _section3-2-4-cassandra-alter-keyspace-label:

DROP KEYSPACE ステートメント
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

DROP KEYSPACEステートメントはキースペースを削除するために、以下の構文で使用される。

.. sourcecode:: sql

   drop_keyspace_statement ::=  DROP KEYSPACE [ IF EXISTS ] keyspace_name

実行例は以下の通りである。

.. sourcecode:: sh

   cqlsh> DROP KEYSPACE sample;


.. _section3-2-5-cassandra-create-table-label:

CREATE TABLE ステートメント
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

キースペースは、以下の構文の通り、CREATE TABLEステートメントにより作成される。

.. sourcecode:: sql
   
   create_table_statement ::=  CREATE TABLE [ IF NOT EXISTS ] table_name
                            '('
                                column_definition
                                ( ',' column_definition )*
                                [ ',' PRIMARY KEY '(' primary_key ')' ]
                            ')' [ WITH table_options ]
   column_definition      ::=  column_name cql_type [ STATIC ] [ PRIMARY KEY]
   primary_key            ::=  partition_key [ ',' clustering_columns ]
   partition_key          ::=  column_name
                                | '(' column_name ( ',' column_name )* ')'
   clustering_columns     ::=  column_name ( ',' column_name )*
   table_options          ::=  COMPACT STORAGE [ AND table_options ]
                            | CLUSTERING ORDER BY '(' clustering_order ')' [ AND table_options ]
                            | options
   clustering_order       ::=  column_name (ASC | DESC) ( ',' column_name (ASC | DESC) )*

利用可能なデータ型は以下の通りである。

.. list-table:: テーブルのデータ型
   :header-rows: 1
   :widths: 30,70

   * - データ型
     - 概要

   * - ascii
     - US-ASCII文字列
   * - bigint
     - 64bitLong型数値
   * - blob
     - 16進数で表記される任意のバイト
   * - boolean
     - True or False
   * - counter
     - 64bit型数値の累計
   * - decimal
     - 正確なDecimal型数値
   * - double
     - 64bit IEEE-754 Float数値
   * - float
     - 32bit IEEE-754 Float数値
   * - inet
     - IPv4 あるいはIPv6のIPアドレスを表す文字列
   * - int
     - 32bitint型数値
   * - list
     - 1つ以上の整列された要素
   * - map
     - JSON表記された文字列の連想配列
   * - set
     - 重複のない1つ以上の整列された要素
   * - text
     - UTF8エンコードされた文字列
   * - timestamp
     - 8byteで表現されたDateTime
   * - uuid
     - UUIDフォーマット
   * - timeuuid
     - Type 1 UUID
   * - varchar
     - UTF8エンコード文字列
   * - varint
     - 正確なint


実行例は以下の通りである。

.. sourcecode:: sql

   cqlsh> CREATE TABLE monkeySpecies (
              species text PRIMARY KEY,
              common_name text,
              population varint,
              average_size int
          ) WITH comment='Important biological records'
          AND read_repair_chance = 1.0;

   cqlsh> CREATE TABLE timeline (
              userid uuid,
              posted_month int,
              posted_time uuid,
              body text,
              posted_by text,
              PRIMARY KEY (userid, posted_month, posted_time)
          ) WITH compaction = { 'class' : 'LeveledCompactionStrategy' };

   cqlsh> CREATE TABLE loads (
              machine inet,
              cpu int,
              mtime timeuuid,
              load float,
          PRIMARY KEY ((machine, cpu), mtime)
          ) WITH CLUSTERING ORDER BY (mtime DESC);

.. note:: 

   上記の構文で、static修飾子は同一のパーティション内(同一のパーティションキーを持つ)で共有される値である。

static修飾された値は、値が共有されるので更新時には注意が必要である。下記の例では、static定義された"s"が共有されており、1番目のデータが後からINSERTされた２番目のデータに上書きされる例である。

.. sourcecode:: sql

   cqlsh> CREATE TABLE t (
              pk int,
              t int,
              v text,
              s text static,
              PRIMARY KEY (pk, t)
         ); 

   cqlsh> INSERT INTO t (pk, t, v, s) VALUES (0, 0, 'val0', 'static0');
   cqlsh> INSERT INTO t (pk, t, v, s) VALUES (0, 1, 'val1', 'static1');
   cqlsh> SELECT * FROM t;

     pk | t | v      | s
   "----+---+--------+-----------"
     0  | 0 | 'val0' | 'static1'
     0  | 1 | 'val1' | 'static1'

static定義されたカラムは以下の制約をもつ。

* COMPACT STORAGEオプションを選択したテーブルはstatic属性を利用できない。
* staticなカラムを持つことが出来ない、クラスタカラムなしのテーブル。
* PRIMARYキーを除くカラムのみがstatic定義できる。

.. note:: 

   上記の構文において、テーブルのデータは必ず一意のパーティションキーを持つ。パーティションキーは、どのノードで、データを保持、処理するのかを決定し、パーティションキーによってノードが決定した行をパーティションと呼ぶ。パーティション内には、複数の行を持つ場合があり、これらを一意に識別するのがプライマリーキーである。プライマリーキーは、パーティションキー単独の場合(パーティション内に１つの行しかない場合)と、パーティションキー+クラスタカラムで構成される場合(パーティション内に複数の行が存在し、クラスタごとに保持するデータがそれぞれの行で異なる場合)の２ケースが存在する。

プライマリーキーの表現方法は、

* PRIMARY KEY (a) : aはパーティションキーでもあり、クラスタカラムはない。
* PRIMARY KEY (a, b, c) : aはパーティションキーであり、bとcはクラスタカラムである。
* PRIMARY KEY ((a, b), c) : aとbは複合パーティションキーと呼び、cはクラスタカラムである。

以下の例は、row1とrow2のデータが同じパーティション、row3とrow4が同じパーティションに属する。ただし、row3とrow4は配置されたクラスタが異なる。

.. sourcecode:: sql

   cqlsh> CREATE TABLE t (
              a int,
              b int,
              c int,
              d int,
              PRIMARY KEY ((a, b), c, d)
          );

    cqlsh> SELECT * FROM t;

      a | b | c | d
    "---+---+---+---"
      0 | 0 | 0 | 0    // row 1
      0 | 0 | 1 | 1    // row 2
      0 | 1 | 2 | 2    // row 3
      0 | 1 | 3 | 3    // row 4
      1 | 1 | 4 | 4    // row 5

また、パーティションキーが同じであれば、レプリカ内にも同じデータセットで格納されていることをするが、クラスタカラムでは、レプリカセット内で行がどのように保存されるかを制御するために利用される。ソートされることで、SELECT * FROM t WHERE a = 0 AND b = 0 And c > 1 and c <= 3 のように、効率的なデータ検索に役立つ。

テーブルオプションはWITHキーワードを使用して指定するが、COMPACT STORAGEオプションと、CLUSTERING ORDERオプションについては、生成後に変更が出来ないことと、実行されるクエリに影響があるため注意が必要である。


.. list-table:: テーブルオプションの注意点
   :header-rows: 1
   :widths: 30,70

   * - オプション
     - 概要

   * - COMPACT STOAGE
     - 複合PRIMARYキーを使用してデータを作成する場合は、 |br| キー以外の項目は１つの行データにまとめるオプション。|br| このオプションを採用すると以下の制約が課せられる。|br|  |br| 
       1.コレクションやstatic定義カラムを使用することは出来ない。|br| 
       2.少なくともクラスタカラムを持ち、PRIMARYキーの外側に |br| 少なくとも１つのカラムが必要である。これはテーブル生成後に、|br| 追加したり削除することは出来ない。 |br| 
       3.作成されたインデックスに制約があり、マテリアライズドビューは |br| 使用できない。
   * - CLUSTERING ORDER
     - クラスタカラムのデータの並び順を昇順から降順に変更するオプション。 |br| このオプションを採用すると、以下の制約が課せられる。 |br|  |br| 
       1.テーブルにSELECTを実行した際のORDER順が変更される。 |br| 
       2.ORDER BYオプションを指定しなければ、CLUSTERING ORDERで |br| 定義されたデータは返却される。 |br| 
       3.逆順の定義は幾つかのクエリにおいて、若干のパフォーマンス低下を |br| 引き起こす。 

その他、以下のテーブルオプションをサポートする。

.. list-table:: CREATE TABLEのその他のオプション
   :header-rows: 1
   :widths: 20,20,20,70

   * - オプション名
     - 形式
     - デフォルト
     - 詳細
   * - comment
     - 文字列
     - none
     - フリーフォーマットの手書きコメント
   * - read_repair_chance
     - 数値
     - 0.1
     - Read_Repairのために、他のノードへクエリを送信する割合 |br| 求められる一貫性レベル以上のノード数に従って指定する。
   * - dclocal_read_repair_chance
     - 数値
     - 0
     - 同一データセンタにおけるレプリカへのread_repair_chance
   * - gc_grace_seconds
     - 数値
     - 864000
     - サーバがトゥームストーンのガーベージコレクションを待つ時間 |br| 
       シングルノードクラスタは安全に0を設定できる。
   * - bloom_filter_fp_chance
     - 数値
     - 0.00075
     - SSTableのブルームフィルタ誤検出(False Positive)の確率 |br| 
       この値で、メモリ上、ディスク上のフィルタのサイズが決定される。
   * - default_time_to_live
     - 数値
     - 0
     - テーブルのデフォルトの有効期限。制御がないときに |br|
       MapReduce/Hiveシナリオで使用される。
   * - compaction
     - マップ
     - 下表参照
     - コンパクションのオプション。
   * - compression
     - マップ
     - 下表参照
     - コンプレッションのオプション。
   * - caching
     - マップ
     - 下表参照
     - キャッシュオプション。

オプションをデフォルトにした場合の例は以下の通りである。

.. sourcecode:: sql

   CREATE TABLE sample.users (
       user_id bigint PRIMARY KEY,
       first_name text,
       last_name text,
       user_name text
   ) WITH bloom_filter_fp_chance = 0.01
       AND caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
       AND comment = ''
       AND compaction = {'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy', 'max_threshold': '32', 'min_threshold': '4'}
       AND compression = {'chunk_length_in_kb': '64', 'class': 'org.apache.cassandra.io.compress.LZ4Compressor'}
       AND crc_check_chance = 1.0
       AND dclocal_read_repair_chance = 0.1
       AND default_time_to_live = 0
       AND gc_grace_seconds = 864000
       AND max_index_interval = 2048
       AND memtable_flush_period_in_ms = 0
       AND min_index_interval = 128
       AND read_repair_chance = 0.0
       AND speculative_retry = '99PERCENTILE';

.. note::

   Read Repairとは、読み込み時に各レプリカでデータに違いがあった場合、 |br| 
   最新のデータでレプリカを修復することである。修復はバッググラウンドで行われる。


.. note:: 

   トゥームストーンは、RDBにおいて、削除のSQLを実行する代わりに、削除を定義されたカラムを更新する「論理削除」と同様の仕組みを実現するための目印に相当する。Cassandraでは、削除操作を実行しても、SSTableのコンパンクションが実行されるまで実際の削除操作は保留されるが、削除までの間、トゥームストーンが削除マーカーとして保持される。詳細は `DATASTAXの削除について <http://docs.datastax.com/ja/cassandra-jajp/2.0/cassandra/dml/dml_about_deletes_c.html>`_ を参照すること。

   この概念を理解するための、重要なポイントとしては、データ削除が行われる場合、レプリカが1つでもダウンしていると、削除が反映されず、レプリカ復帰時に、復帰したレプリカへのアクセスが発生すると、Read Repairによって、削除したはずのデータが復元してしまう可能性があることだ。そのため、ノード間のデータ不整合が発生した場合、トゥームストーンの有無により削除の要否を判断する。基本的にはトゥームストーンがあった場合、削除が優先される。トゥームストーンのデフォルトの設定は864000秒、すなわち10日である。実際の削除はSSTableのコンパクション時となるが、コンパクションの戦略によっては実際にデータ削除されるタイミングが長期に可能性があるので、必要に応じてデフォルトのコンパクション戦略を見直すこと。なお、この時間を過ぎて復旧できなかったノードは障害発生または交換が必要なノード対象となる。

.. note::
  
   SSTableブルームフィルタとは、指定されたキーがSSTable内のデータに含まれているかを検査するための高速な非決定性アルゴリズムで、パフォーマンス向上のために使用される。


コンパクションのオプションは、'class'属性に以下のクラスを指定する必要がある。

.. list-table:: コンパクションの戦略
   :header-rows: 1
   :widths: 20,70

   * - オプション名
     - 詳細

   * - SizeTieredCompactionStrategy
     - デフォルトのコンパクションストラテジ。min_thresholdで指定された |br| ファイルの個数を閾値として、同じサイズのファイルが上限を超えるとSSTableの |br| マイナーコンパクションが発生する。
   * - LeveledCompactionStrategy
     - 固定されたサイズのSSTableを格納する領域をレベルごとに分類し、 |br| 
       SSTableが作成されるごとに 順次下のレベルから占有させていき、 |br| 
       レベル単位でデータのマージを行う戦略。 |br| 
       データ検索時にはレベルごとにマージされたSSTableを検索する。
   * - TimeWindowCompactionStrategy
     - データのタイムスタンプにより、ウィンドウと呼ばれる単位で１ファイルにつき |br| 
       １つのSSTableを割当て、コンパクションを実行する戦略
   * - DateTieredCompactionStrategy
     - 非推奨、代わりにTimeWindowCompactionStrategyの使用を推奨。


.. note::

   コンパクションには、メジャーコンパクションとマイナーコンパクションが存在する。
   マイナーコンパクションは複数のSSTableを1つにまとめるもので、メジャーコンパクションは
   全てのSSTableを１つにまとめるものである。


コンプレッションのオプションは、SSTableの圧縮方法を指定するもので、以下が利用可能である。

.. list-table:: コンプレッションのオプション
   :header-rows: 1
   :widths: 20,20,70

   * - オプション名
     - デフォルト
     - 詳細

   * - class
     - LZ4Compressor
     - 圧縮アルゴリズムを指定する。その他、 |br| 
       SnappyCompressorとDeflateCompressorを選択できる。
   * - enabled
     - true
     - SSTableの圧縮可否
   * - chunk_length_in_kb
     - 64KB
     - SSTableの圧縮のブロックサイズを定義する。 |br|
       より大きい方が圧縮率はよいが、読み出しのデータサイズが増加する。
   * - crc_check_chance
     - 1.0
     - ブロックに含まれるチェックサムをチェックする割合

キャッシュオプションはテーブルのキーとRowのキャッシュを定義する。

.. list-table:: キャッシュオプション
   :header-rows: 1
   :widths: 20,20,70

   * - オプション名
     - デフォルト
     - 詳細

   * - keys
     - ALL
     - テーブルのキャッシュを行うかどうかをALLorNONE指定。
   * - rows_per_partition
     - NONE
     - パーティションにつきキャッシュする行の量。|br| 
       Integer型のNを指定すると、パーティションのクエリ行から取得した |br| 
       N行のクエリがキャッシュされる。その他ALLorNONEを指定可能。


.. _section3-2-6-cassandra-alter-table-label:

ALTER TABLE ステートメント
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

存在済みのテーブルの変更を加えるのは、以下のALTER TABLEステートメントを利用する。

.. sourcecode:: html

   alter_table_statement   ::=  ALTER TABLE table_name alter_table_instruction
   alter_table_instruction ::=  ALTER column_name TYPE cql_type
                                | ADD column_name cql_type ( ',' column_name cql_type )*
                                | DROP column_name ( column_name )*
                                | WITH options

以下は実行例である。

.. sourcecode:: sql

   cqlsh> ALTER TABLE addamsFamily ALTER lastKnownLocation TYPE uuid;

   cqlsh> ALTER TABLE addamsFamily ADD gravesite varchar;

   cqlsh> ALTER TABLE addamsFamily
          WITH comment = 'A most excellent and useful table'
          AND read_repair_chance = 0.2;

ALTER TABLE構文では、以下が可能である。

* ALTER命令で、テーブル内のカラムの型を変更できる。カラムの型は簡単に変更できるものではない。型の変更は以前の型から新しい型へ有効なデータである必要がある。加えて、クラスタカラムでは、セカンダリインデックスが定義されているため、新しい型も以前の型と同様にソートされる必要がある。型の競合は以降を参照すること。
* ADD命令で、テーブルへ新しいカラムを追加できる。PRIMARYキーは変更できないので、新しく追加したカラムをその一部に組み込むことはできない。同様にCOMPACT STORAGEオプションを付与しているテーブルはカラムの追加は制限される。
* REMOVE命令でテーブル内のカラムを削除できる。定義とデータ両方削除されるが、定義は即時で利用不能になるのに対して、データはコンパクション後に削除される。
* WITH句でテーブルオプションを変更できる。サポートオプションはCREATE時と同様だが、前述の通り、COMAPACT STORAGEオプションと、CLUSTERING ORDERは変更できない。コンパクションのオプションは以前のコンパクションを削除する影響があるので注意。コンプレッションオプションも同様。

.. list-table:: ALTER TABLE時の型の競合
   :header-rows: 1
   :widths: 50,50

   * - 変換前の型
     - 変更可能な型

   * - timestamp
     - bigint
   * - ascii, bigint, boolean, date, decimal, double,  |br| 
       float, inet, int, smallint, text, time, timestamp,  |br| 
       timeuuid, tinyint, uuid, varchar, varint
     - blob
   * - int
     - date
   * - ascii, varchar
     - text
   * - bigint
     - time, timestamp
   * - timeuuid
     - uuid
   * - ascii, text
     - varchar
   * - bigint, int, timestamp
     - varint

クラスタカラムにおいては、以下の変更のみが許容される。

.. list-table:: クラスタカラム変更時の制約
   :header-rows: 1
   :widths: 50,50

   * - 変換前の型
     - 変更可能な型

   * - ascii, text, varchar
     - blob
   * - ascii, varchar
     - text
   * - ascii, text
     - varchar

.. _section3-2-7-cassandra-drop-table-label:

DROP TABLE ステートメント
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

存在済みのテーブルを削除するには、以下のDROP TABLEステートメントを利用する。

.. sourcecode:: html

   drop_table_statement ::=  DROP TABLE [ IF EXISTS ] table_name

テーブルの削除に伴い、データも同時に削除される。

.. _section3-2-8-cassandra-drop-table-label:

TRUNCATEステートメント
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

存在済みのテーブル内のデータを削除するには、以下のDROP TABLEステートメントを利用する。

.. sourcecode:: html

   truncate_statement ::=  TRUNCATE [ TABLE ] table_name




.. _section3-2-X-cassandra-describe-label:

DESCRIBE ステートメント
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

describeステートメントの構文及びオプションである。

.. sourcecode:: sql
   
   DESCRIBE FULL( CLUSTER | SCHEMA )
   | KEYSPACES
   | ( KEYSPACE keyspace_name )
   | TABLES
   | ( TABLE table_name )
   | TYPES
   | ( TYPES user_defined_type )

.. list-table:: describeコマンドの利用例
   :header-rows: 1
   :widths: 30,70

   * - コマンド
     - 実行結果

   * - cqlsh> describe cluster;
     - Cluster: Test Cluster |br| Partitioner: Murmur3Partitioner
   * - cqlsh> describe keyspaces;
     - system_schema  system_auth  system  sample  system_distributed  system_traces

