.. include:: ../module.txt

.. _section9-iot-label:

IoT
======================================================

.. _section9-1-iot-overview-label:

Overview
------------------------------------------------------

* モニタリング
* 制御
* 最適化
* 自律化
* データ連携、モバイル
* 予防予知保全(異常検知)

* 予知保全、ウェルネスと健康ソリューション、生産性プロセス最適化、スマートシティ等
* 非機能要件

IoT
* IoT Core
  クラウドへの入り口、認証、デバイスとの接続、リクエスト受付

  * データ収集(数十万規模デバイス)
  * リモート制御

    * Device Gateway
    　MQTTプロトコル、HTTP1.1、WebSocketsで通信(TLS1.2)、Publish、Subscribe型
      デバイスとの間でどのくらい通信を維持するか(KeepAlive)
    * Identity service(認証・認可)
      証明書によるデバイスごとの認証、独自もしくはAWS IoT X.509証明書による認証。
      機械同士の認証になるため、証明書、秘密鍵を使用して相互認証を行う。
      課題：あるデバイスのLeakした場合、全デバイスがRevokeの影響を受ける。
      そのため、特定のデバイスのみRevokeできるようにする。
      IoTポリシーを証明書にアタッチすることできめ細やかな権限設定が可能。
    * Message Broker(メッセージのルーティング)
    * Rule Engine(他サービスとの連携)
       * SQL文を使ったトピックのフィルタ
       * データ変換、拡張
       * 別サービスへのルーティング
    * Device Shadow(デバイスの状態管理、取得、保存)
      1. Thingは自身の状態をReportedとしてShadowに登録
      2. Mobileは状態をクラウド側のShadowに登録
      3. MobileはThingの状態の更新をDesiredとして登録
      4. Thingとの状態がずれている場合、クラウド側から差分(Delta)の通知を送信し要求されている状態に変更する。
    * Registry

* Amazon FreeRTOS(フリーアールトス)
  低価格でのIoT実現に向けたサービス
* IoT 1-Click
* IoT Analytics
  データノイズのクレンジング・フィルタリング、処理変換(圧縮等)、保存、定期的な蓄積、分析バッチ処理、可視化
   * AWS IoT Analytics -> Channel
   * Pipleline
   * Store Processed datastore
   * Data set
   * Advanced Analytics  or Visualization
* IoT Device Defender
  鍵管理、不正検知、
* IoT Device Management
  (大量の)デバイスの導入、整理、監視、リモート管理、ファームアップデート、パッチ管理を行う。
    * シリアルIDなどデバイス情報の一括登録(テンプレートによるメタデータ、証明書、ポリシーを一括登録)
    * デバイスのグルーピングと検索(属性やシャドウを組み合わせて対象のデバイスを検索)
    * デバイスのロギングとモニタリング(デバイスやグループ(トラブルシューティング対象等)単位でログレベルの設定が可能)
    * OTA(OverTheAir)アップデート(リリース済みのデバイスに検証済み署名を送付する等)とアップデート対象モニタリング
    　(どのくらいファームウェアのアップデートが完了したかを監視)
    * Jobs Fleet Rollouts(デバイスへのファームのカナリアリリース(Jobs)で動的なロールアウトが行える)
* IoT Events
* IoT Greengrass(AWS Device Catalogにサポート情報あり)
  エッジコンピューティング向けのサービス、インターネットに直接アクセスできないデバイスへのデータ配信等。
  エッジデバイスにインストールするソフトウェアであり、クラウド上のプログラムをデバイス側へ転送し、デバイス側で実行する。
  [課題]
    * レイテンシ
    * 帯域幅
    * オフライン
    * セキュリティ
  [機能]
    * クラウドから配布したLambdaをローカルで実行
    * ローカルでメッセジングとLambdaのTrigger
    * データの状態と同期
    * 終端とのデバイスとの通信(MQTT)のためのプロトコルアダプタ
    * OTAアップデート
    * 機械学習のMLモデルをエッジデバイスにデプロイし、デバイス側で推論を行い、結果を活用する。
* IoT SiteWise
　　プラントのデータを
    * クラウド上にFactoryの時系列データを解析し、MLのためにIoT Analyticsを送信
* IoT Things Graph

Amazon Dash Replenishment Service
-------------------------------------


Hundson Memo

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
