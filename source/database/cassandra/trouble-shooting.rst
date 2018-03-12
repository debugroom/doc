.. include:: ../../module.txt

.. _section7-cassandra-trouble-shooting-label:

トラブルシューティング
====================================

.. _section7-cassandra-launch-error-label:

起動エラー
------------------------------------

* cqlshを実行すると、以下のようなConnection Errorが発生する。

.. sourcecode:: bash

   Connection error: ('Unable to connect to any servers', {'::1': error(61, "Tried connecting to [('::1', 9042, 0, 0)]. Last error: Connection refused"), 'fe80::1%lo0': error(61, "Tried connecting to [('fe80::1%lo0', 9042, 0, 1)]. Last error: Connection refused"), '127.0.0.1': error(61, "Tried connecting to [('127.0.0.1', 9042)]. Last error: Connection refused")})

* 対処

1. /usr/local/var/log/cassandra/system.logを確認する。
2. 以下の様なエラーログが出力されていることを確認。実行中のJVMが異常終了した際に、コミットログが破損する等した場合に発生する模様。

.. sourcecode:: java

   ERROR [main] 2018-02-11 05:14:11,722 JVMStabilityInspector.java:82 - Exiting due to error while processing commit log during initialization.
   org.apache.cassandra.db.commitlog.CommitLogReplayer$CommitLogReplayException: Could not read commit log descriptor in file /usr/local/var/lib/cassandra/commitlog/CommitLog-6-1517069302120.log
     at org.apache.cassandra.db.commitlog.CommitLogReplayer.handleReplayError(CommitLogReplayer.java:616) [apache-cassandra-3.7.jar:3.7]
     at org.apache.cassandra.db.commitlog.CommitLogReplayer.recover(CommitLogReplayer.java:378) [apache-cassandra-3.7.jar:3.7]
     at org.apache.cassandra.db.commitlog.CommitLogReplayer.recover(CommitLogReplayer.java:228) [apache-cassandra-3.7.jar:3.7]
     at org.apache.cassandra.db.commitlog.CommitLog.recover(CommitLog.java:185) [apache-cassandra-3.7.jar:3.7]
     at org.apache.cassandra.db.commitlog.CommitLog.recover(CommitLog.java:165) [apache-cassandra-3.7.jar:3.7]
     at org.apache.cassandra.service.CassandraDaemon.setup(CassandraDaemon.java:314) [apache-cassandra-3.7.jar:3.7]
     at org.apache.cassandra.service.CassandraDaemon.activate(CassandraDaemon.java:585) [apache-cassandra-3.7.jar:3.7]
     at org.apache.cassandra.service.CassandraDaemon.main(CassandraDaemon.java:714) [apache-cassandra-3.7.jar:3.7]

3. データが失われても問題なければ、コミットログを削除し、再起動。

.. sourcecode:: bash

   > launchctl unload ~/Library/LaunchAgents/homebrew.mxcl.cassandra.plist
   > mkdir backup
   > mv /usr/local/var/lib/cassandra/commitlog/CommitLog-* backup/
   > launchctl load ~/Library/LaunchAgents/homebrew.mxcl.cassandra.plist

.. _section7-cassandra-connection-error-label:

接続エラー
------------------------------------

* Javaアプリケーション上からDriver経由でCassandraに接続しようとすると、コネクションエラーが発生する。

.. sourcecode:: java

   Caused by: com.datastax.driver.core.exceptions.NoHostAvailableException: All host(s) tried for query failed (tried: /XXX.XXX.XXX.XXX:9042
   (com.datastax.driver.core.exceptions.TransportException: [/XXX.XXX.XXX.XXX:9042] Cannot connect))
   at com.datastax.driver.core.ControlConnection.reconnectInternal(ControlConnection.java:233) ~[cassandra-driver-core-3.1.4.jar!/:na]
   at com.datastax.driver.core.ControlConnection.connect(ControlConnection.java:79) ~[cassandra-driver-core-3.1.4.jar!/:na]

* 原因：接続先の通信がファイアフォールがブロックされている際に発生する。
* 対処

1. Cassandaサーバのファイアウォール設定状況を確認し、接続先クライアントのIPの接続を許可する。
