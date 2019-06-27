.. include:: ../module.txt

.. _section9-iot-label:

AWS IoT
======================================================

.. _section9-1-aws-iot-overview-label:

Overview
------------------------------------------------------

AWS IoTは、インターネットを介してデータ連携するセンサー等のIoTデバイスからAWSクラウドへのセキュアなデータ通信・デバイス認証や、
IoTデバイス自体のリモート管理・ファームウェアアップデート等に加え、通信データの処理、保存、分析などを提供する包括的なサービスである。

2019年1月現在、AWS IoTは以下のような分類でサービス提供されている。

.. list-table:: AWS IoTが提供するサービス
   :widths: 3, 7

   * - サービス名
     - 概要

   * - IoT Core
     - 数十万規模のデバイスのデータ収集・リモート制御を目的としたサービス。|br| AWSクラウドへのセキュア接続やデバイス認証、リクエストの受付やメッセージのルーティングを行う。

   * - IoT Device Management
     - (大量の)デバイスの導入、整理、監視、リモート管理、ファームアップデート、パッチ管理を行う。

   * - IoT Device Defender
     - デバイスの監査、アノマリー検出・アラート通知などを行う。

   * - IoT Analytics
     - IoTデバイスから送信されたデータを収集、処理、保存、解析、可視化するサービス。

   * - IoT Greengrass
     - エッジコンピューティング環境向けのサービス。AWS IoTの各種機能を |br| エッジコンピューティング環境で実現するためのデバイスにインストールするソフトウェアとサーバ側のサービスがある。

   * - Amazon FreeRTOS
     - マイクロコントローラ向けのIoTオペレーティングシステム。マイコンベースの機器をIoTデバイス化できる。

   * - IoT 1-click
     - AWS IoT エンタープライズボタン、SORACOM LTE-M ボタンといった |br| 専用のデバイスからLambdaFunctionを実行するためのサービス。

   * - IoT SiteWise
     - Snowball Edgeなどのゲートウェイデバイスで動作するAWSクラウドへ |br| データをセキュアに送信するソフトウェアパッケージ。また、複数のゲートウェイから収集したデータを構造化、 |br| ラベリングし、リアルタイムに指標を作成し、可視化するダッシュボード機能もある。

   * - IoT Events
     - デバイスから収集されたデータを継続的に監視し、定義したDetector Modelに従って |br| イベントを検出、アクションを実行するサービス。

   * - IoT Things Graph
     - AWS IoT Greengrassに対応したエッジデバイスで実行するアプリケーションを、|br| モデルを用いて視覚的に設計するUIツール。

.. _section9-2-iot-core-label:

AWS IoT Core
------------------------------------------------------

IoT CoreはAWS IoTの中核となるサービスで、主にデバイス間の通信やデバイスの状態管理を担当する。
IoT Coreは以下の６つのコンポーネントにより構成される。

.. list-table:: AWS IoT Coreのコンポーネントの概要
   :widths: 3, 7

   * - サービス名
     - 概要

   * - Device Gateway
     - MQTTプロトコル、HTTP1.1、WebSocketsを用いて(TLS1.2)、Publish、Subscribe型で通信する。また、デバイスとの間でどのくらい通信を維持するか(KeepAlive)設定する。

   * - Identity Service(認証・認可)
     - 証明書によるデバイスごとの認証、独自もしくはAWS IoT X.509証明書による認証。機械同士の認証になるため、証明書、秘密鍵を使用して相互認証を行う。IoTポリシーを証明書にアタッチすることできめ細やかな権限設定が可能。

   * - Message Broker
     - メッセージのルーティング

   * - Rule Engine
     - SQL文を使ったトピックのフィルタやデータ変換・拡張などを実行する。また別AWSサービスへのルーティングなども実行する

   * - Device Shadow
     - デバイスの状態管理、状態データの取得・保存を行う。

   * - Registry
     - デバイスの状態管理、状態データの取得・保存を行う。

.. note:: Identity Serviceでは、あるデバイスのLeakした場合、全デバイスがRevokeの影響を受ける。そのため、特定のデバイスのみRevokeできるようにする。

.. note:: Device Shadowは以下のような要領で状態を管理する。

   #. Thingは自身の状態をReportedとしてShadowに登録
   #. Mobileは状態をクラウド側のShadowに登録
   #. MobileはThingの状態の更新をDesiredとして登録
   #. Thingとの状態がずれている場合、クラウド側から差分(Delta)の通知を送信し要求されている状態に変更する。

.. _section9-3-iot-device-management-label:

AWS IoT Device Management
------------------------------------------------------

IoT Device Managementでは、主に(大量の)デバイスの導入、整理、監視、リモート管理、ファームアップデート、パッチ管理を行う。以下のような機能を持つ。

* シリアルIDなどデバイス情報の一括登録(テンプレートによるメタデータ、証明書、ポリシーを一括登録)
* デバイスのグルーピングと検索(属性やシャドウを組み合わせて対象のデバイスを検索)
* デバイスのロギングとモニタリング(デバイスやグループ(トラブルシューティング対象等)単位でログレベルの設定が可能)
* OTA(OverTheAir)アップデート(リリース済みのデバイスに検証済み署名を送付する等)とアップデート対象モニタリング(どのくらいファームウェアのアップデートが完了したかを監視)
* Jobs Fleet Rollouts(デバイスへのファームのカナリアリリース(Jobs)で動的なロールアウトが行える)

.. _section9-4-iot-device-defender-label:

AWS IoT Device Defender
------------------------------------------------------

.. todo:: IoT Device Defenderについて記述する。

.. _section9-5-iot-greengrass-label:

AWS IoT Greengrass
------------------------------------------------------

インターネットに直接アクセスできないデバイスへのデータ配信等、エッジコンピューティング向けのサービス。
エッジデバイスにインストールするソフトウェアであり、クラウド上のプログラムをデバイス側へ転送し、デバイス側で実行する。
エッジコンピューティングでは主にレイテンシや帯域幅が問題になり、オフライン上での実行が必要になるケースも多いが、以下のような機能をもち、セキュアに実行できる。

[機能]

* クラウドから配布したLambdaをローカルで実行
* ローカルでメッセジングとLambdaのTrigger
* データの状態と同期
* 終端とのデバイスとの通信(MQTT)のためのプロトコルアダプタ
* OTAアップデート
* 機械学習のMLモデルをエッジデバイスにデプロイし、デバイス側で推論を行い、結果を活用する。

.. _section9-6-iot-analytics-label:

AWS IoT Analytics
------------------------------------------------------

データノイズのクレンジング・フィルタリング、処理変換(圧縮等)、保存、定期的な蓄積、分析バッチ処理、可視化を行うサービス。

* AWS IoT Analytics -> Channel
* Pipleline
* Store Processed datastore
* Data set
* Advanced Analytics  or Visualization

.. _section9-7-FreeRTOS-label:

Amazon FreeRTOS
------------------------------------------------------

.. todo:: FreeRTOSについて記述する。

.. _section9-8-iot-one-click-label:

AWS IoT 1-Click
------------------------------------------------------

.. todo:: 1-Clickについて記述する。

.. _section9-9-iot-events-label:

AWS IoT Events
------------------------------------------------------

.. todo:: IoT Eventsについて記述する。

.. _section9-10-iot-things-graph-label:

AWS IoT Things Graph
------------------------------------------------------

.. todo:: IoT Things Graphについて記述する。

.. _section9-11-iot-sitewise-label:

AWS IoT SiteWise
------------------------------------------------------

.. todo:: IoT SiteWiseについて記述する。

Hundson Memo

.. sourcecode:: bash

   kawabataku:~/environment $ sudo pip install AWSIoTPythonSDK
   Collecting AWSIoTPythonSDK
      Downloading https://files.pythonhosted.org/packages/01/6f/ee3c174d87c3cd0b9cfa8e7e0599549ea28f495be2559dcc64c6d6485973/AWSIoTPythonSDK-1.4.3.tar.gz (79kB)
        100% |████████████████████████████████| 81kB 3.5MB/s
   Building wheels for collected packages: AWSIoTPythonSDK
      Running setup.py bdist_wheel for AWSIoTPythonSDK ... done
      Stored in directory: /root/.cache/pip/wheels/60/43/ee/3df322dae6a74e27af57d797beb8cf35d2b1391f09073fa189
    Successfully built AWSIoTPythonSDK
    Installing collected packages: AWSIoTPythonSDK
    Successfully installed AWSIoTPythonSDK-1.4.3
    You are using pip version 9.0.3, however version 18.1 is available.
    You should consider upgrading via the 'pip install --upgrade pip' command.
    kawabataku:~/environment $
    kawabataku:~/environment $
    kawabataku:~/environment $
    kawabataku:~/environment $ wget http://bit.ly/2QqqRqx -O dummyclient.tar.gz
    --2019-01-16 07:09:15--  http://bit.ly/2QqqRqx
    Resolving bit.ly (bit.ly)... 67.199.248.11, 67.199.248.10
    Connecting to bit.ly (bit.ly)|67.199.248.11|:80... connected.
    HTTP request sent, awaiting response... 404 Not Found
    2019-01-16 07:09:15 ERROR 404: Not Found.

    kawabataku:~/environment $ wget http://bit.ly/2QggRgx -O dummyclient.tar.gz
    --2019-01-16 07:10:03--  http://bit.ly/2QggRgx
    Resolving bit.ly (bit.ly)... 67.199.248.10, 67.199.248.11
    Connecting to bit.ly (bit.ly)|67.199.248.10|:80... connected.
    HTTP request sent, awaiting response... 301 Moved Permanently
    Location: https://s3-ap-northeast-1.amazonaws.com/awsj-iot-handson/data-collection/dummyclient.tar.gz [following]
    --2019-01-16 07:10:03--  https://s3-ap-northeast-1.amazonaws.com/awsj-iot-handson/data-collection/dummyclient.tar.gz
    Resolving s3-ap-northeast-1.amazonaws.com (s3-ap-northeast-1.amazonaws.com)... 52.219.68.84
    Connecting to s3-ap-northeast-1.amazonaws.com (s3-ap-northeast-1.amazonaws.com)|52.219.68.84|:443... connected.
    HTTP request sent, awaiting response... 200 OK
    Length: 6036 (5.9K) [application/x-gzip]
    Saving to: ‘dummyclient.tar.gz’

    dummyclient.tar.gz                 100%[=============================================================>]   5.89K  --.-KB/s    in 0s

    2019-01-16 07:10:03 (210 MB/s) - ‘dummyclient.tar.gz’ saved [6036/6036]

    kawabataku:~/environment $
    kawabataku:~/environment $
    kawabataku:~/environment $ tar -zxvf dummyclient.tar.gz
    tar: Ignoring unknown extended header keyword `SCHILY.dev'
    tar: Ignoring unknown extended header keyword `SCHILY.ino'
    tar: Ignoring unknown extended header keyword `SCHILY.nlink'
    ./._DummyDevice
    tar: Ignoring unknown extended header keyword `LIBARCHIVE.creationtime'
    tar: Ignoring unknown extended header keyword `SCHILY.dev'
    tar: Ignoring unknown extended header keyword `SCHILY.ino'
    tar: Ignoring unknown extended header keyword `SCHILY.nlink'
    DummyDevice/
    tar: Ignoring unknown extended header keyword `SCHILY.dev'
    tar: Ignoring unknown extended header keyword `SCHILY.ino'
    tar: Ignoring unknown extended header keyword `SCHILY.nlink'
    DummyDevice/._.DS_Store
    tar: Ignoring unknown extended header keyword `SCHILY.dev'
    tar: Ignoring unknown extended header keyword `SCHILY.ino'
    tar: Ignoring unknown extended header keyword `SCHILY.nlink'
    DummyDevice/.DS_Store
    tar: Ignoring unknown extended header keyword `SCHILY.dev'
    tar: Ignoring unknown extended header keyword `SCHILY.ino'
    tar: Ignoring unknown extended header keyword `SCHILY.nlink'
    DummyDevice/._deviceMain.py
    tar: Ignoring unknown extended header keyword `SCHILY.dev'
    tar: Ignoring unknown extended header keyword `SCHILY.ino'
    tar: Ignoring unknown extended header keyword `SCHILY.nlink'
    DummyDevice/deviceMain.py
    tar: Ignoring unknown extended header keyword `SCHILY.dev'
    tar: Ignoring unknown extended header keyword `SCHILY.ino'
    tar: Ignoring unknown extended header keyword `SCHILY.nlink'
    DummyDevice/._certs
    tar: Ignoring unknown extended header keyword `LIBARCHIVE.creationtime'
    tar: Ignoring unknown extended header keyword `SCHILY.dev'
    tar: Ignoring unknown extended header keyword `SCHILY.ino'
    tar: Ignoring unknown extended header keyword `SCHILY.nlink'
    DummyDevice/certs/
    tar: Ignoring unknown extended header keyword `SCHILY.dev'
    tar: Ignoring unknown extended header keyword `SCHILY.ino'
    tar: Ignoring unknown extended header keyword `SCHILY.nlink'
    DummyDevice/._dependency.py
    tar: Ignoring unknown extended header keyword `SCHILY.dev'
    tar: Ignoring unknown extended header keyword `SCHILY.ino'
    tar: Ignoring unknown extended header keyword `SCHILY.nlink'
    DummyDevice/dependency.py
    tar: Ignoring unknown extended header keyword `SCHILY.dev'
    tar: Ignoring unknown extended header keyword `SCHILY.ino'
    tar: Ignoring unknown extended header keyword `SCHILY.nlink'
    DummyDevice/._IoT_common.py
    tar: Ignoring unknown extended header keyword `LIBARCHIVE.creationtime'
    tar: Ignoring unknown extended header keyword `SCHILY.dev'
    tar: Ignoring unknown extended header keyword `SCHILY.ino'
    tar: Ignoring unknown extended header keyword `SCHILY.nlink'
    DummyDevice/IoT_common.py
    tar: Ignoring unknown extended header keyword `SCHILY.dev'
    tar: Ignoring unknown extended header keyword `SCHILY.ino'
    tar: Ignoring unknown extended header keyword `SCHILY.nlink'
    DummyDevice/certs/._root.pem
    tar: Ignoring unknown extended header keyword `SCHILY.dev'
    tar: Ignoring unknown extended header keyword `SCHILY.ino'
    tar: Ignoring unknown extended header keyword `SCHILY.nlink'
    DummyDevice/certs/root.pem
    kawabataku:~/environment $
    kawabataku:~/environment $
    kawabataku:~/environment $ ls
    dummyclient.tar.gz  DummyDevice  README.md
    kawabataku:~/environment $
    kawabataku:~/environment $
    kawabataku:~/environment $ cd DummyDevice/
    kawabataku:~/environment/DummyDevice $ ls
    certs  dependency.py  deviceMain.py  IoT_common.py
    kawabataku:~/environment/DummyDevice $ python deviceMain.py --device_name sample_iot_thing -- root_ca ./certs/root.pem --private ./certs/ccaf1d27c4-private.pem.key --endpoint a2zv4jn2dn9jft-ats.iot.us-west-2.amazonaws.com
    usage: deviceMain.py [-h] --device_name DEVICE_NAME --endpoint ENDPOINT
                         --root_ca ROOT_CA --cert CERT --private PRIVATE
    deviceMain.py: error: argument --endpoint is required
    kawabataku:~/environment/DummyDevice $ python deviceMain.py --device_name sample_iot_thing --root_ca ./certs/root.pem --private ./certs/ccaf1d27c4-private.pem.key --endpoint a2zv4jn2dn9jft-ats.iot.us-west-2.amazonaws.com
    usage: deviceMain.py [-h] --device_name DEVICE_NAME --endpoint ENDPOINT
                         --root_ca ROOT_CA --cert CERT --private PRIVATE
    deviceMain.py: error: argument --cert is required
    kawabataku:~/environment/DummyDevice $ python deviceMain.py --device_name sample_iot_thing --root_ca ./certs/root.pem --private ./certs/ccaf1d27c4-private.pem.key --endpoint a2zv4jn2dn9jft-ats.iot.us-west-2.amazonaws.com --cert ./certs/
    ccaf1d27c4-certificate.pem.crt  ccaf1d27c4-public.pem.key       root.pem
    ccaf1d27c4-private.pem.key      ._root.pem
    kawabataku:~/environment/DummyDevice $ python deviceMain.py --device_name sample_iot_thing --root_ca ./certs/root.pem --private ./certs/ccaf1d27c4-private.pem.key --endpoint a2zv4jn2dn9jft-ats.iot.us-west-2.amazonaws.com --cert ./certs/ccaf1d27c4-certificate.pem.crt
    send back state payload:{"state": {"reported": {"wait_time": 5}}}
