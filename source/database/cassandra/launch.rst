.. include:: ../../module.txt

.. _section2-launch-label:

Cassandraの起動
=====================================================

.. _section2-1-environment-label:

動作環境
-----------------------------------------------------

インストール手順は、`Cassandraのインストール <section1-cassandra-install-label>`_ を参考のこと。

[OS]
MacOSX 10.9.5

[JVM]
Java(TM) SE Runtime Environment (build 1.8.0_40-b27) Java HotSpot(TM) 64-Bit Server VM (build 25.40-b25, mixed mode)

[バージョン]

* cqlsh 5.0.1
* Cassandra 3.7
* CQL spec 3.4.2
* Native protocol v4

.. _section2-2-setting-java-vm-memory-label:

JavaVMのヒープメモリの設定
----------------------------------------------------

`DATASTAXのJavaリソースの調整 <http://docs.datastax.com/ja/cassandra-jajp/3.0/cassandra/operations/opsTuneJVM.html>`_ も参考にすること。/usr/local/etc/cassandra/jvm.optionsの-Xms及び、-Xmxオプションを設定。

.. sourcecode:: html
   :linenos:
   :caption: jvm.optionsの記載例 |br|

   -Xms512M
   -Xmx1G


.. _section2-3-launch-cassandra-label:

Cassandraの起動
-----------------------------------------------------

ターミナルコマンドで以下を実行。

.. sourcecode:: sh
   
   launchctl load ~/Library/LaunchAgents/homebrew.mxcl.cassandra.plist


.. _section2-3-launch-cqlsh-label:

cqlshの起動
-----------------------------------------------------

ターミナルコマンドで以下を実行。

.. sourcecode:: sh
   
   cqlsh localhost 9042


