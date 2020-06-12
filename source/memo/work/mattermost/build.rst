
.. include:: ../module.txt

Mattermostの環境構築
================================================================================

Mattermost Server on AWSのDockerfile作成
--------------------------------------------------------------------------------

MattermostをDockerを使って構築する方法は `Mattermost公式GitHub <https://github.com/mattermost/docs/blob/master/source/install/prod-docker.rst>`_ にも掲載されているが、
Docker Composeを使用したマルチサーバ構成であり、DBのバックアップやロードバランサなども特定のインスタンス上のコンテナに構築され、可用性・保存性ともに望ましくない。
ここでは、Mattermost Server自体をAWS環境上のECSクラスタ上に、データベースをRDSに、ファイルストレージ(イメージデータ等)をS3に、ロードバランサをALBで構築する。


.. note:: Dockerfile上に記載したwgetコマンドで以下のようなタイムアウトエラーが発生するときは、コンテナのDNSが正しく設定されていない可能性がある。

   .. sourcecode:: none

      Cannot find a valid baseurl for repo: base/7/x86_64
      Could not retrieve mirrorlist http://mirrorlist.centos.org/?release=7&arch=x86_64&repo=os&infra=container error was
      12: Timeout on http://mirrorlist.centos.org/?release=7&arch=x86_64&repo=os&infra=container: (28, 'Resolving timed out after 30540 milliseconds')

   この場合ホストOSのDocker起動ファイル(/usr/lib/systemd/system/docker.service)にDNSサーバの指定を行う。

   * （変更前）：ExecStart=/usr/bin/docker daemon -H fd://
   * （変更後）：ExecStart=/usr/bin/docker daemon -H fd:// -dns=8.8.8.8

   編集後、Dockerデーモンのリロードと再起動を行うこと。

   .. sourcecode:: none

      # systemctl daemon-reload
      # systemctl restart docker

.. todo:: 構築した資材は `GitHub mattermost-for-aws <https://github.com/debugroom/mattermost-for-aws>`_ にあるので、構築手順を整理する。
