.. include:: ../../../module.txt

.. _section1-install-cassandra-label:

Cassandraのインストール
====================================


以下のページを参考。Homebrewのインストールは、Cassandraのページのインストールコマンドがリンク切れだったのでHomebrew本家のインストールコマンドを使用した。

* `Installing Cassandra on Mac OS X <https://gist.github.com/hkhamm/a9a2b45dd749e5d3b3ae>`_ 
* `Homebrew <http://brew.sh>`_

* Homebrewをインストール

.. sourcecode:: sh
   :linenos:

   /usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"


* Pythonのアップデート

.. sourcecode:: sh
   :linenos:

   brew install python

* CQLのインストール

.. sourcecode:: sh
   :linenos:

   sudo pip install cql

  
* Cassandraのインストール

.. sourcecode:: sh
   :linenos:

   brew install cassandra

* Cassandraの起動(MacOS X 10.9.5 Mavericksの場合、Homebrewがplistファイルをコピーするのに失敗するので、手動でコピーしておく。)

.. sourcecode:: sh
   :linenos:

   cp /usr/local/Cellar/cassandra/3.7/homebrew.mxcl.cassandra.plist ~/Library/LaunchAgents/
   launchctl load ~/Library/LaunchAgents/homebrew.mxcl.cassandra.plist

* プロセス起動の確認

.. sourcecode:: sh
   :linenos:

   ps -ef | grep cassandra


* Cassandraの終了

.. sourcecode:: sh
   :linenos:

   launchctl unload ~/Library/LaunchAgents/homebrew.mxcl.cassandra.plist

.. note::

   設定ファイルは以下を参照すること。`DATASTAXのパッケージインストールディレクトリ <http://docs.datastax.com/ja/cassandra-jajp/3.0/cassandra/install/referenceInstallLocatePkg.html>`_ も参考にすること。
   
   * Properties: /usr/local/etc/cassandra
   * Logs: /usr/local/var/log/cassandra
   * Data: /usr/local/var/lib/cassandra/data




