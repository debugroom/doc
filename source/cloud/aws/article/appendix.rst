.. include:: ../module.txt

.. _section9-aws-appendix-label:

Appendix - 付録
======================================================

.. _section9-1-setting-credential-label:

SDK使用時の認証情報の設定
------------------------------------------------------

AWSのSDKを用いた開発では、開発端末に以下のとおり、アカウントの認証情報を設定しておくと、SDKが自動的に参照する。

.. list-table:: AWS認証情報の設定
   :widths: 20, 40, 40

   * - 認証情報のプロファイル
     - Linux, MacOSX, Unix : ~/.aws/credentials |br| Windows : C:\Users\USERNAME\.aws\credentials
     - [default] |br| aws_access_key_id = XXXXXXX |br| aws_secret_access_key = YYYYYYYYY
   * - デフォルトリージョン
     - ~/.aws/config
     - region=us-east-1

.. note:: EC2インスタンスには直接IAMロールを設定できるオプションがあり、アプリケーション内で設定したIAMロールに基づいたSTSを使用した認証情報の取得が行える。

.. _section9-2-cli-label:

AWS CLI
------------------------------------------------------

AWS CLIはAWSの各サービスを利用するためのコマンドラインインターフェースである。

.. _section9-2-1-cli-install-label:

インストール
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

インストール前に事前に以下をインストールしておく。手順は `公式のページ <https://docs.aws.amazon.com/ja_jp/cli/latest/userguide/cli-install-macos.html#awscli-install-osx-pip>`_ も参照のこと。

[OS]
MacOSX 10.12.6

* Homebrewをインストール

.. sourcecode:: sh
   :linenos:

   /usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"

* Pythonのアップデート

.. sourcecode:: sh
   :linenos:

   brew install python

■ Amazon CLIをインストールする。

.. sourcecode:: sh
   :linenos:

   sudo /usr/loca/bin/pip install awscli
   aws --v
   aws-cli/1.14.41 Python/2.7.12 Darwin/16.7.0 botocore/1.8.45


.. _section9-2-2-cli-setting-config-label:

CLIの環境設定
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. sourcecode:: sh
   :linenos:

   aws configure
   AWS Access Key ID [None]: XXXXXXX
   AWS Secret Access Key [None]: YYYYYYYYY
   Default region name [None]: ap-northeast-1
   Default output format [None]: text
