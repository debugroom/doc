.. include:: ../module.txt

.. _section9-appendix-label:

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
