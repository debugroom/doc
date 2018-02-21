.. include:: ../module.txt

.. _section9-management-label:

Management Tools
======================================================

.. _section9-2-cloud-trail-label:

CloudTrail
------------------------------------------------------

CloudTrailはアカウントユーザに対する全てのAPIリクエストを記録するサービスである。
セキュリティ分析やAWSリソースの変更の追跡、トラブルシューティングに活用できる。

.. _section9-3-cloud-config-label:

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

.. _section9-4-cost-report-label:

請求レポート
------------------------------------------------------
