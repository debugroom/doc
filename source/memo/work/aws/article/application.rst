.. include:: ../module.txt

.. _section7-application-label:

Application Category
======================================================

.. _section7-1-sns-label:

Amazon Simple Notification Service(SNS)
------------------------------------------------------

Amazon Simple Notification ServiceはPublish/Subscribe型のメッセージ配信サービスである。
発行者(Publisher)は、複数の購読者(Subscriver：WebServer, Mail, Amazon SQS, Amazon Lamda)へ非同期メッセージ送信する。

SNSは以下のような特徴をもつ。

* 単一で複数の購読者へメッセージを送信できる。
* メッセージの順序は保証されない。
* 発行済みのメッセージは削除できない。
* メッセージ配信の失敗時は配信ポリシーによりリトライが可能。
* メッセージには最大256KBのテキストデータ(XML、JSON、テキスト)を含めることができる。

.. list-table:: AmazonSNSの操作
   :widths: 20, 80
   :header-rows: 1

   * - API
     - 概要

   * - CreateTopic
     - 通知の公開先トピックを作成する。
   * - Subscrive
     - エンドポイントに登録確認メッセージを送信して、 |br| エンドポイントを受信(Subscrive)登録する。
   * - DeleteTopic
     - トピックとその登録サブスクリプションをすべて削除する。
   * - Publish
     - トピックを受信登録しているエンドポイントにメッセージを送信する。


.. _section7-2-sqs-label:

Amazon Simple Queue Service(SQS)
------------------------------------------------------

Amazon Simple Queue Serviceは分散型キュー配信方式のメッセージサービスである。複数のサーバに複数のメッセージのコピーを保存して信頼性を確保しながら最低一度の配信を保証する。
大容量のデータを高スループットで転送可能であり、メッセージ送信の信頼性が高く、各コンポーネントを疎結合にできる。

キューは以下のようなURLフォーマット、メッセージID、受信ハンドル(ReceiptHandle)で識別される。キューは専用のAPIにより、以下のような操作が可能である。

> http://sqs.<region>.amazonaws.com/<accout-id>

.. list-table:: キューの操作
   :widths: 20, 80
   :header-rows: 1

   * - API
     - 概要

   * - CreateQueue
     - 新しいキューを作成するか、既存のキューのURLを返す
   * - SetQueueAttribute
     - キューの属性を設定する。属性値は下記表を参照。
   * - GetQueueAttribute
     - キューの属性を取得する。属性値は下記表を参照。
   * - GetQueueUrl
     - キューのURLを取得する。
   * - ListQueues
     - キューのリストを取得する。
   * - DeleteQueues
     - キューを削除する。

.. list-table:: キューの属性
   :widths: 20, 80
   :header-rows: 1

   * - 属性名
     - 概要

   * - DelaySecounds
     - メッセージの遅延配信時間
   * - MaximumMessageSize
     - メッセージに含まれる最大バイト数(デフォルト・最大値ともに256KiB)
   * - MessageRetentionPeriod
     - メッセージが保持される秒数(デフォルト４日、最大１４日)
   * - RecieveMessageWaitTimeSeconds
     - 呼び出しがメッセージ到着を待機する時間(デフォルト0秒、最大20秒)
   * - Visibility Timeout
     - 特定のアプリケーションコンポーネントからキューがメッセージを取得した後、 |br| その他からメッセージが不可視となる時間。

.. note:: SQSではメッセージの処理順序でシーケンス性が保証されないので注意。メッセージ順序を保証する場合はFIFOキュー(2018年2月現在、東京リージョンでは未対応)の利用を検討する。

.. _section7-3-kinesis-stream-label:

Amazon kinesis Streams
------------------------------------------------------

Amazon Kinesisは完全マネージド型のストリーミングデータサービスで、大規模なデータレコードストリームをリアルタイムで収集・処理できる。
WebアプリケーションやIoT・モバイルデバイス等から収集したデータをストリームデータとして処理する。Kinesis Streamプラットフォームの主要なコンポーネントは以下の通りである。

.. list-table:: Kinesis Streamのコンポーネント
   :widths: 20, 80
   :header-rows: 1

   * - コンポーネント
     - 概要

   * - データレコード
     - データの最小単位で、イベント契機で発生するデータストリームの構成要素。 |br| シーケンス番号、パーティションキー、データBLOBで構成される。
   * - ストリーム
     - 順序付けされたデータレコードのシーケンス
   * - シャード
     - ストリームからパーティションキーに基づき分割したデータ群。 |br| ストリームを作成する際は、シャード数を指定する必要がある。
   * - プロデューサ
     - データを発生させるイベントアプリケーション
   * - コンシューマ
     - ストリームからデータレコードを取得し、処理を行うアプリケーション

.. _section7-3-1-kinesis-producer-detail-label:

プロデューサアプリケーション
------------------------------------------------------

プロデューサアプリケーションでは、パーティションキーとデータBLOBを持つデータレコードを発生させる。
その後、データレコードをストリームに追加すると、Kinesis Streamがシーケンス番号をデータレコードに割り当てる。
アプリケーションでは、以下２種類のAPIが利用可能である。

* Amazon Kinesis Stream API
* Amazon Kinesis Producer Library(KPL)

Stream APIでは、PutRecordリクエストを利用して、ストリームにデータを追加するAPIで、
KPLは、プロデューサとストリームの仲介より柔軟な機能を提供するライブラリである。
具体的には、レコードを複数のストリームに同期・非同期で書き込む、リトライ機能、レコードを集約し、
複数のシャードに書き込む機能、CloudWatchとの連携機能が提供される。

.. _section7-3-2-kinesis-consumer-detail-label:

コンシューマアプリケーション
------------------------------------------------------

コンシューマアプリケーションでは、シャードイテレータを使用して特定のシャードから読み取りを行う。
シャードイテレータとはコンシューマにおいてレコードの読み取りが開始されるストリームの位置を指定するもので、
読み取りオペレーションを実行すると、シャードイテレータを用いてレコードデータを取得する。
プロデューサ同様、コンシューマアプリケーションでも、以下２種類のAPIが利用可能である。

* Amazon Kinesis Stream API
* Amazon Kinesis Consumer Library(KCL)

StreamAPIでは、シャードイテレータを取得して、GetRecordリクエストからデータ取得を行う。
KCLは、KPLと同様、ストリームとの間を仲介する機能を提供する。ワーカーと呼ばれるスレッドをインスタンス化して
ストリームからデータを読み込み、チェックポイント作成、シャードの再割り当て（リシャーディング）等を行う。

.. note:: KCLでは、チェックポイントやワーカーとシャードのマッピング情報をAmazonDynamoにテーブルを作成して管理する。
          Kinesisを利用する場合は、IAMでDynamoDBやCloudWatchのアクセス権限を作成する必要がある。

.. note:: kinesisの最大送信メッセージサイズは1MBである。

.. _section7-3-3-kinesis-firehose-label:

Amazon kinesis firehose　/ Amazon kinesis Analytics
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Amazon kinesis firehoseは上述のStreamの派生版で、ストリーミングデータを直接データストアや分析ツールにロードするサービスである。
ストリーミングデータをキャプチャして変換し、Amazon S3、Amazon Redshift、Amazon Elasticsearch Service、Splunkにロードできる。
同様に、派生版のサービスであるmazon kinesis Analyticsはプログラミング言語や処理フレームワークを習得することなく、
標準SQLでストリーミングデータをリアルタイムで処理できるサービスである。

.. note:: 2018年2月現在、東京リージョンでは利用はできない。


.. _section7-4-lamda-label:

Amazon Lamda
------------------------------------------------------

Amazon Lamdaはユーザのコードを実行し、同時に基盤となるコンピューティングリソースをユーザに代わって管理するコンピューティングサービス。
S3バケットに対する変更やDynamoテーブルの更新、アプリケーションやデバイスによって生成されたカスタムイベントなどに応答してコードを実行可能である。



.. note:: ５分までの処理が限界、リージョンは選べるが、実行環境は完全マネージド(処理のメモリやスペックは選べる->料金次第)。

重たい処理はやめた方がいい？、イニシャルコストが低い処理。パフォーマンス食うようならEC2で処理したほうがよい？

料金=リクエスト回数、時間×メモリサイズ

.. _section7-5-swf-label:

Amazon Simple Workflow Service(SWF)
------------------------------------------------------

Amazon Simple Workflow ServiceはEC2を前提としたマネージド型ワークフロー実行基盤である。
分散コンポーネント間でまたがって協調処理させ一元管理する。

.. _section7-5-1-kinesis-firehose-label:

AWS Flow Framework
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

AWS Flow Frameworkはタスクレベルで協調処理を抽象化するフレームワークである。
