.. include:: ../module.txt

.. _section9-management-label:

Management Tools
======================================================

.. _section9-2-cloud-trail-label:

CloudWatch
------------------------------------------------------

Cloud WatchはAWSのリソースをモニタリングするサービスである。提供される機能は以下のとおりである。

* 以下のようなメトリクスのハイパーバイザレベルでの追跡・収集(EC2の標準メトリクス)
  #. CPU利用率
  #. DisK I/O
  #. ネットワーク転送量
  #. ステータスチェック(死活監視)
* 以下のようなカスタムメトリクス収集
  #. メモリ使用量
  #. ディスク空き容量
  #. ウェブページの読み込み時間
  #. リクエストのエラー発生率
  #. 同時処理・スレッド数
* メトリクスの閾値設定・アラーム通知、アクション実行
  #. (例)CPU使用率が50%を下回るとオートスケーリング、AmazonLamdaのイベント発生
* CloudWatch Logsを使用してアプリケーションログを保存・モニタリング

.. note:: 各サービスによって標準となるメトリクスは異なる。ALBではリクエスト数などをCloudWatchで収集しており、そのメトリクスに応じてEC2のオートスケールイベントを発生することは可能である。

.. note:: CloudWatchだけでシステム運用の全てのメトリクスの収集は難しいので、必要に応じて運用監視ミドルウェアの導入を行うこと。

.. _section9-3-cloud-trail-label:

CloudTrail
------------------------------------------------------

CloudTrailはアカウントユーザに対する全てのAPIリクエストを記録するサービスである。
セキュリティ分析やAWSリソースの変更の追跡、トラブルシューティングに活用できる。

.. _section9-4-cloud-config-label:

CloudConfig
------------------------------------------------------

CloudConfigはAWSのリソースの設定の履歴、変更通知利用できる。
例えば、セキュリティグループのこれまでの設定履歴や変更タイミングなどをトレースすることができる。
サポートされるリソースの対象は `こちら <https://docs.aws.amazon.com/config/latest/developerguide/resource-config-reference.html#supported-resources>`_　になる。
各々、以下のような情報の取得が可能である。

* AWS設定のスナップショット
* AWSリソースの設定履歴
* リソース変更通知
* リソースの関連性

.. _section9-5-cloud-formation-label:

CloudFormation
------------------------------------------------------

CloudFormationは、JSONとYAML形式のテンプレートを使用して、AWSリソースの起動、設定、接続を行うサービスである。

.. todo:: CloudFormationのテンプレート記述要領について記述。

.. _section9-6-elastic-beanstalk-label:

Elastic Beanstalk
------------------------------------------------------

Elastic Beanstalkは、Webアプリケーションを自動デプロイ、スケーリングするサービスである。

.. todo:: Elastic Beanstalkについて詳述。

.. _section9-7-cost-report-label:

請求レポート
------------------------------------------------------

.. todo:: 請求レポートについて詳述。

.. _section9-8-trusted-advisor-label:

Trusted Advisor
------------------------------------------------------

.. todo:: Trusted Advisorについて記述。
