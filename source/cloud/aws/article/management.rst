.. include:: ../module.txt

.. _section9-management-label:

Management Tools
======================================================

.. _section9-1-cloud-watch-label:

CloudWatch
------------------------------------------------------

.. _section9-1-1-cloud-watch-overview-label:

Overview
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

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


|br|

.. _section9-1-2-cloud-watch-event-alert-label:

CloudWatch EventとSNSを用いたアラートの作成
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

|br|

ここでは、AWSマネジメントコンソールにログインすることをCloudWatch Eventに設定し、イベントを検知したらSNSを使用してメール通知するアラートを設定する。
最初にSNSでEmailを発信するトピックを作成する。必要に応じて、 :ref:`section7-1-1-sns-overview-label` も参照すること。

Amazon SNSサービスを選択し、「トピック」メニューから「トピックの作成」ボタンを押下する。

|br|

.. figure:: img/management-console-sns-create-topic-for-cloud-watch-event-1.png

|br|

トピック名を入力し、「トピックの作成」ボタンを押下する。

|br|

.. figure:: img/management-console-sns-create-topic-for-cloud-watch-event-2.png

.. figure:: img/management-console-sns-create-topic-for-cloud-watch-event-3.png

|br|

トピックを購読するサブスクリプションとしてEmail配信を設定する。「サブスクリプション」メニューから「サブスクリプションの作成」ボタンを押下する。

|br|

.. figure:: img/management-console-sns-create-subscription-email-for-cloud-watch-event-1.png

|br|

以下の要領でサブスクリプションの設定を行う。

* トピックARN: 上記で作成したトピックのARN
* プロトコル：Eメール
* エンドポイント：送信対象となるEmail

|br|

.. figure:: img/management-console-sns-create-subscription-email-for-cloud-watch-event-2.png

|br|

続いて、CloudWatchEventを作成する。「CloudWatch」サービスで、「イベント」メニューを選択し、「今すぐ始める」ボタンを押下する。

|br|

.. figure:: img/management-console-cloudwatch-create-event-1.png

|br|

以下の要領で、イベントを設定し、「設定の詳細」ボタンを押下する。

* イベントソース

  * イベントパターン：サービス別のイベントに一致するイベントパターンの構築
  * サービス名：AWSコンソールのサインイン
  * イベントタイプ：サインインイベント
  * 任意のユーザ

* ターゲット

  * SNSトピック：上記で作成したSNSトピック
  * 入力の設定：一致したイベント


|br|

.. figure:: img/management-console-cloudwatch-create-event-2.png

|br|

ルール名を入力し、「ルールの作成」ボタンを押下する。

|br|

.. figure:: img/management-console-cloudwatch-create-event-4.png

|br|

一度サインアウトした後に、しばらく時間を置いたのち、ログインすると設定したアドレス宛にメールが送信できるようになる。

|br|

.. _section9-1-3-cloud-watch-event-lambda-label:

CloudWatch EventとLambdaの実行
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

ここでは、特定のセキュリティグループの変更を監視し、変更があるとLambdaファンクションを使って設定を元に戻すアクションを実行する。
まず、Lambdaファンクションを実行する際に必要となるIAMポリシー及びロールを作成する。「IAM」サービスから、「ポリシー」メニューを選択し、「ポリシーの作成」ボタンを押下する。

|br|

.. figure:: img/management-console-iam-create-policy-for-cloudwatch-monitoring-security-group-1.png

|br|

「JSON」タブを選択し、以下の通りポリシーを設定する。

.. sourcecode:: javascript

   {
       "Version": "2012-10-17",
       "Statement": [
            {
                "Effect" : "Allow",
                "Action": "ec2:DescribeSecurityGroups",
                "Resource": "*"
            },
            {
                "Effect" : "Allow",
                "Action": "logs:CreateLogGroup",
                "Resource": "*"
            },
            {
                "Effect" : "Allow",
                "Action": [
                    "Logs:CreateLogStream",
                    "logs:PutLogEvents"
                ],
                "Resource": "*"
            }
        ]
    }

|br|

.. figure:: img/management-console-iam-create-policy-for-cloudwatch-monitoring-security-group-2.png

|br|

ポリシー名を入力し、「ポリシーの作成」ボタンを押下する。

|br|

.. figure:: img/management-console-iam-create-policy-for-cloudwatch-monitoring-security-group-3.png

|br|

続いて、「ロール」メニューから「ロールの作成」ボタンを押下する。

|br|

.. figure:: img/management-console-iam-create-lambda-role-for-cloudwatch-monitoring-security-group-1.png

|br|

「このロールを使用するサービスを選択」として「Lambda」を選択し、「次のステップ：アクセス権限」ボタンを押下する。

|br|

.. figure:: img/management-console-iam-create-lambda-role-for-cloudwatch-monitoring-security-group-2.png

|br|

上記で作成したポリシーをアタッチし、「次のステップ：タグ」ボタンを押下する。タグは特に設定せず先へ進む。

|br|

.. figure:: img/management-console-iam-create-lambda-role-for-cloudwatch-monitoring-security-group-3.png

|br|

ロール名を入力し、「ロールの作成」ボタンを押下する。

|br|

.. figure:: img/management-console-iam-create-lambda-role-for-cloudwatch-monitoring-security-group-4.png

|br|

次に実行するLambdaファンクションを作成する。「AWS Lambda」サービスを選択し、「関数」メニューから「関数の作成」ボタンを押下する。

|br|

.. figure:: img/management-console-lambda-create-function-repair-security-group-for-python-1.png

|br|

以下の要領で関数を作成し、「関数の作成」ボタンを押下する。

* 関数名：関数名を入力
* ランタイム：Python2.7
* アクセス権限：前節で作成したIAMロール

|br|

.. figure:: img/management-console-lambda-create-function-repair-security-group-for-python-2.png

.. figure:: img/management-console-lambda-create-function-repair-security-group-for-python-3.png

|br|

関数として、以下のPythonコードを入力する。

.. sourcecode:: python

   # Copyright [2016]-[2016] Amazon.com, Inc. or its affiliates. All Rights Reserved.
   #
   # Licensed under the Apache License, Version 2.0 (the "License").
   # You may not use this file except in compliance with the License.
   # A copy of the License is located at
   #
   #     http://aws.amazon.com/apache2.0/
   #
   # or in the "license" file accompanying this file.
   # This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   # either express or implied. See the License for the specific language governing permissions
   # and limitations under the License.
   #
   # Description: Checks that all security groups block access to the specified ports.
   #
   # awscwevents_lambda_security_group.py
   #
   # Author: jeffscottlevine
   # Date: 2016-09-05
   #
   # This file contains an AWS Lambda handler which responds to AWS API calls that modify the ingress
   # permissions of security groups to see if the permissions now differ from the required permissions

   # as specificed in the REQUIRED_PERMISSIONS variable below.
   #
   # Note: The permissions are not remediated within this function because doing so could possibly
   # trigger a recursion issue with this Lambda function triggering itself.
   #
   # 2017-01-13 - Added "Ipv6Ranges" to REQUIRED_PERMISSIONS to accommodate IPv6 within Amazon VPC.

   import boto3
   import botocore
   import json


   APPLICABLE_APIS = ["AuthorizeSecurityGroupIngress", "RevokeSecurityGroupIngress"]

   # Specify the required ingress permissions using the same key layout as that provided in the
   # describe_security_group API response and authorize_security_group_ingress/egress API calls.

   REQUIRED_PERMISSIONS = [
       {
           "IpProtocol" : "tcp",
           "FromPort" : 80,
           "ToPort" : 80,
           "UserIdGroupPairs" : [],
           "IpRanges" : [{"CidrIp" : "0.0.0.0/0"}],
           "PrefixListIds" : [],
           "Ipv6Ranges": []
       },
       {
           "IpProtocol" : "tcp",
           "FromPort" : 443,
           "ToPort" : 443,
           "UserIdGroupPairs" : [],
           "IpRanges" : [{"CidrIp" : "0.0.0.0/0"}],
           "PrefixListIds" : [],
           "Ipv6Ranges": []
       }
   ]


   # evaluate_compliance
   #
   # This is the main compliance evaluation function.

   def evaluate_compliance(event):
       event_name = event["detail"]["eventName"]
       if event_name not in APPLICABLE_APIS:
           print("This rule does not apply for the event ", event_name, ".")
           return

       group_id = event["detail"]["requestParameters"]["groupId"]
       print("group id: ", group_id)

       client = boto3.client("ec2");

       try:
            response = client.describe_security_groups(GroupIds=[group_id])
       except botocore.exceptions.ClientError as e:
            print("describe_security_groups failure on group ", group_id, " .")
            return

       print("security group definition: ", json.dumps(response, indent=2))

       ip_permissions = response["SecurityGroups"][0]["IpPermissions"]
       authorize_permissions = [perm for perm in REQUIRED_PERMISSIONS if perm not in ip_permissions]
       revoke_permissions = [perm for perm in ip_permissions if perm not in REQUIRED_PERMISSIONS]

       if authorize_permissions or revoke_permissions:
           if authorize_permissions:
               for perm in authorize_permissions:
                   print("This permission must be authorized: ", json.dumps(perm, separators=(',',':')))
           if revoke_permissions:
               for perm in revoke_permissions:
                   print("This permission must be revoked: ", json.dumps(perm, separators=(',',':')))
       else:
           print("All permissions are correct.")

   # lambda_handler
   #
   # This is the main handle for the Lambda function.  AWS Lambda passes the function an event and a context.

   def lambda_handler(event, context):
       print("event: ", json.dumps(event))

       evaluation = evaluate_compliance(event)




|br|

.. figure:: img/management-console-lambda-create-function-repair-security-group-for-python-4.png

|br|

変更監視対象とするセキュリティグループを作成する。EC2サービスから、セキュリティグループメニューから、「セキュリティグループの作成」を押下する。

|br|

.. figure:: img/management-console-ec2-create-security-group-for-cloudwatch-monitoring-1.png

|br|

特にルールは設定せず作成する。

|br|

.. figure:: img/management-console-ec2-create-security-group-for-cloudwatch-monitoring-2.png

|br|

作成したセキュリティグループを変更監視対象に設定する。CloudWatchサービスから「イベント」メニューを選択し、「今すぐ始める」を押下する。

|br|

.. figure:: img/management-console-cloudwatch-create-event-lambda-1.png

|br|

* イベントパターン：サービス別のイベントに一致するイベントパターンの構築を選択

  * サービス名：CloudTrail
  * イベントタイプ：AWS API Call via CloudTrail
  * 任意のオペレーションを選択

|br|

.. figure:: img/management-console-cloudwatch-create-event-lambda-2.png

|br|

編集リンクを押下し、イベントパターンを以下のように変更する。

.. sourcecode:: javascript

   {
      "detail-type": [
          "AWS API Call via CloudTrail"
      ],
      "detail": {
          "eventSource": [
              "ec2.amazonaws.com"
          ],
          "eventName": [
              "AuthorizeSecurityGroupIngress",
              "RevokeSecurityGroupIngress"
          ],
          "requestParameters": {
              "groupId": [
                 "sg-0d7b9ee604811ba7d"
              ]
          }
      }
   }

|br|

.. figure:: img/management-console-cloudwatch-create-event-lambda-3.png

|br|

ターゲットは以下のように設定し、「設定の詳細」を押下する。

* Lambda関数：前節で作成したLambda関数を設定
* SNSトピック：前節で作成したSNSトピック

|br|

.. figure:: img/management-console-cloudwatch-create-event-lambda-4.png

|br|

名前を入力後、「ルールの作成」を押下する。

|br|

.. figure:: img/management-console-cloudwatch-create-event-lambda-5.png

|br|

セキュリティグループを変更すると自動的に変更が通知されるようになる。

|br|

.. _section9-2-cloud-trail-label:

CloudTrail
------------------------------------------------------

.. _section9-2-1-cloud-trail-overview-label:

Overview
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

|br|

CloudTrailはアカウントユーザに対する全てのAPIリクエストを記録するサービスである。
セキュリティ分析やAWSリソースの変更の追跡、トラブルシューティングに活用できる。

|br|

.. _section9-2-2-cloud-trail-cloudwatchlogs-integration-label:

CloudTrailとCloudWatchLogsの統合
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

|br|

操作履歴をCloudWatchLogsで確認できるようにCloudTrailと統合を行う。「CloudTrail」サービスから「証跡情報」メニューを選択し、「証跡の作成」ボタンを押下する。

|br|

.. figure:: img/management-console-cloudtrail-cloudwatchlogs-integration-1.png

|br|

以下の要領で証跡情報の作成を行い、「次へ」ボタンを押下する。

* 証跡名：任意の証跡名を入力
* 証跡情報を全てのリージョンに適用：はい
* 管理イベント：全て
* ストレージの場所：新しいバケットを作成する：はい
* S3バケット：新規作成するバケット名を入力
* 新規または既存のロググループ：なければ新しいロググループを作成


|br|

.. figure:: img/management-console-cloudtrail-cloudwatchlogs-integration-2.png

.. figure:: img/management-console-cloudtrail-cloudwatchlogs-integration-3.png

.. figure:: img/management-console-cloudtrail-cloudwatchlogs-integration-5.png

|br|

デフォルトで用意されているCloudTrailからのCloudWatchLogsへのアクセス権限がついたIAMロールとポリシー名を設定し、「許可」ボタンを押下する。

|br|

.. figure:: img/management-console-cloudtrail-cloudwatchlogs-integration-6.png

|br|

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


.. _section9-7-cost-report-label:

請求レポート
------------------------------------------------------

.. todo:: 請求レポートについて詳述。

.. _section9-8-trusted-advisor-label:

Trusted Advisor
------------------------------------------------------

AWS Trusted Advisorでは、パフォーマンスとセキュリティに関する最も一般的な4つの推奨事項をチェックする。

* コストの最適化
* セキュリティ
* 耐障害性
* パフォーマンス向上

.. todo:: Trusted Advisorについて記述。

.. _section9-9-Systems-Manager-label:

AWS Systems Manager
------------------------------------------------------

.. _section9-9-1-systems-manager-overview-label:

Overview
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

AWS Systems Managerは、EC2をはじめとしたサーバリソースの大規模な運用・管理を効率化・自動化するサービスである。
EC2インスタンスへのパッチ適用やインベントリ(システム構成)情報の収集、OSのアップデートやドライバの更新の実行、AMIの管理などを、リソース単位でまとめ大規模に実行することが可能である。
また、複数のリソースで共有可能なパラメータストアの作成やブラウザベースでのEC2インスタンスへのアクセスなどシステム管理に関わる複数の機能群で構成される。Systems Managerの主な特徴・機能は以下の通りである。

.. list-table:: Systems Managerの主な特徴・機能
   :widths: 2, 8

   * - 機能
     - 概要

   * - Remote Connect
     - 通常必ず行うセキュリティインバウンド設定やSSHキーなしで、ブラウザやAWS CLIを使ってEC2インスタンスへアクセスする機能

   * - Resource Group
     - AWSの複数のリソースをグルーピング化できる機能

   * - Insight & Dashboard
     - リソースごとに収集したソフトウェアインベントリ情報(OSバージョンなどの構成管理情報)やCloudTrail、Trusted Advisorなどのインサイト情報をダッシュボードで俯瞰的に参照する機能

   * - RunningTasks on group resources
     - リソースグループに対し、オペレーションタスク・スクリプトを自動的に実行する機能

   * - Parameter store
     - 複数のリソースで共有可能な、認証キーやIDなど秘匿情報やプレインテキストデータを階層的に管理して保存、参照する機能

   * - OS patch management
     - 複数のリソースまたはリソースグループに対し、パッチ適用などを実行する機能

   * - Meintenance windows
     - タスク実行をスケジューリング化し実行する機能

   * - Consistent Configuration
     - ステートマネージャを使用した一貫した設定・メンテナンス機能

|br|

.. _section9-9-2-systems-manager-remote-connect-label:

Remote Connect
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

インバウンド接続設定がないプライベートサブネットのEC2インスタンスにSessionManagerを使ってリモート接続を行う。
まず、SSMへのアクセス権限を付与したIAMロールを作成する。IAMロールメニューを選択し、「ロールの作成」ボタンを押下する。

|br|

.. figure:: img/management-console-iam-create-role-for-ssm-1.png

|br|

ロールを使用するサービスとして、「EC2」を選択する。

|br|

.. figure:: img/management-console-iam-create-role-for-ssm-2.png

|br|

「AmazonEC3RoleforSSM」ポリシーをアタッチする。

|br|

.. figure:: img/management-console-iam-create-role-for-ssm-3.png

|br|

ロール名を入力し、「ロールの作成」ボタンを押下する。

|br|

.. figure:: img/management-console-iam-create-role-for-ssm-4.png

|br|

続いて、プライベートサブネットにインバウンド接続のないセキュリティグループを設定したEC2を作成する。
EC2メニューから「インスタンスの作成」ボタンを押下する。

|br|

.. figure:: img/management-console-ec2-create-instance-for-ssm-1.png

|br|

マシンイメージとして「Amazon Linux2(HVM), SSD Volume Type」を選択する。

.. note:: これらのマシンイメージにはデフォルトで「SSMエージェント」がインストールされている。

|br|

.. figure:: img/management-console-ec2-create-instance-for-ssm-2.png

|br|

適当なインスタンスタイプを選択する。

|br|

.. figure:: img/management-console-ec2-create-instance-for-ssm-3.png

|br|

プラベートサブネットがあるVPCを選択し、上記で作成したIAMロールを設定する。

.. note:: LaunchするとEC2からSSM APIへポーリングが実行されるようになる。

|br|

.. figure:: img/management-console-ec2-create-instance-for-ssm-4.png

|br|

ストレージはデフォルトのまま設定しておく。

|br|

.. figure:: img/management-console-ec2-create-instance-for-ssm-5.png

|br|

後の手順でインスタンスを識別しやすいようにタグを設定しておく。

|br|

.. figure:: img/management-console-ec2-create-instance-for-ssm-6.png

|br|

インバウンド接続設定がないセキュリティグループ(デフォルトのもの)を設定する。

|br|

.. figure:: img/management-console-ec2-create-instance-for-ssm-7.png

|br|

「起動」ボタンを押下し、インスタンスを実行する。

|br|

.. figure:: img/management-console-ec2-create-instance-for-ssm-8.png

|br|

.. note:: キーペアは特に設定せず実行して良い。

|br|

.. figure:: img/management-console-ec2-create-instance-for-ssm-9.png

|br|

インスタンスが実行された後、「SystemManager」サービスの「マネージドインスタンス」メニューを選択し、作成したインスタンスが表示されていることを確認する。

|br|

.. figure:: img/management-console-ssm-confirm-managed-instance-1.png

|br|

SessionManagerを利用して、セッションを開始する。「セッションマネージャ」メニューを選択し、「セッションの開始」ボタンを押下する。

|br|

.. figure:: img/management-console-ssm-session-manager-start-1.png

|br|

接続対象のインスタンスを選択する。

|br|

.. figure:: img/management-console-ssm-session-manager-start-2.png

|br|

コンソールが開き、コマンドが実行可能になる。

|br|

.. figure:: img/management-console-ssm-session-manager-start-3.png

|br|

SSHデーモンをストップしても継続してコンソール処理できることを確認する。

|br|

.. figure:: img/management-console-ssm-session-manager-start-4.png

|br|

.. _section9-9-3-systems-manager-resource-group-label:

Resource Group
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

|br|

.. todo:: Systems Managerを使ったリソースグループ設定のやり方を記載する。

|br|

.. _section9-9-4-systems-manager-insight-dashboard-label:

Insight & Dashboard
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

|br|

.. note:: Microsoftのブラウザだとうまくいかないので注意

|br|

:ref:`section9-9-2-systems-manager-remote-connect-label` の手順中に作成したEC2インスタンスのインベントリ情報を収集し、QuickSightを通して可視化する。
データの収集はEC2インスタンスにインストールされたSSMエージェントに対し、 ステートマネージャーが30分毎に情報送信を指示する。収集した情報は、
インベントリがS3へ集約して転送する。S3に集約されたデータはAWS Glueが12時間に1度の頻度でクローリングしてデータベースを構築する。
このデータベースに対し、AWS Athenaがクエリを実行して、QuickSightにより可視化する流れとなる。

なお、収集できるインベントリデータは、EC2にインストールされているアプリケーションパッケージの一覧やバージョン等。
また、マシンスペックといったインスタンスの情報やOSのバージョンなどが収集される。定期的なOSのアップデート対象やセキュリティ脆弱性対象の確認などの用途に適している。

以下順次環境構築していく。

SystemsManagerサービスを選択し、「インベントリ」メニューを選択後、「セットアップインベントリ」ボタンを押下する。

|br|

.. figure:: img/management-console-ssm-setup-inventory-1.png

|br|

インベントリの名前を入力し、ターゲットでインスタンスを手動選択し、 :ref:`section9-9-2-systems-manager-remote-connect-label` で作成したEC2インスタンスを選択する。

|br|

.. figure:: img/management-console-ssm-setup-inventory-2.png

.. figure:: img/management-console-ssm-setup-inventory-3.png

|br|

収集するパラメータは以下の通り設定する。

|br|

.. figure:: img/management-console-ssm-setup-inventory-4.png

|br|

選択後、「セットアップインベントリ」ボタンを押下する。

|br|

.. figure:: img/management-console-ssm-setup-inventory-5.png

|br|

「マネージドインスタンス」メニューから、収集設定したインスタンスを選択し、「インベントリ」タブを選択する。

|br|

.. figure:: img/management-console-ssm-confirm-inventory-1.png

|br|

インベントリタイプで、収集設定したパラメータが表示されることを確認する。

|br|

.. figure:: img/management-console-ssm-confirm-inventory-2.png

|br|

.. note:: なお、インベントリの収集頻度はステートマネージャーメニューから確認できる。

   .. figure:: img/management-console-ssm-statemanager-confirm-1.png

|br|

続いて、収集するインベントリ情報を保存するためのS3バケットの作成とアクセス制御設定を行う。「S3」サービスから「バケットの作成」ボタンを押下する。
バケット名を入力し、東京リージョンを選択する。

|br|

.. figure:: img/management-console-s3-create-bucket-for-ssm-inventory-1.png

|br|

プロパティはデフォルトのまま、「次へ」ボタンを押下する。

|br|

.. figure:: img/management-console-s3-create-bucket-for-ssm-inventory-2.png

|br|

アクセス許可は「パブリックアクセスを全てブロック」を選択し、「次へ」ボタンを押下する。

|br|

.. figure:: img/management-console-s3-create-bucket-for-ssm-inventory-3.png

|br|

「バケットの作成」ボタンを押下して、バケットを作成する。

|br|

.. figure:: img/management-console-s3-create-bucket-for-ssm-inventory-4.png

|br|

作成したバケットにアクセスポリシーを設定する。バケットを選択し、「アクセス権限」タブを選択する。

|br|

.. figure:: img/management-console-s3-setting-access-controll-for-ssm-inventory-1.png

|br|

以下の通り、ポリシーエディタに設定する。

.. sourcecode:: javascript

   {
       "Version": "2012-10-17",
       "Statement": [
           {
               "Sid": "SSMBucketPermissionsCheck",
               "Effect": "Allow",
               "Principal": {
                   "Service": "ssm.amazonaws.com"
               },
               "Action": "s3:GetBucketAcl",
               "Resource": "arn:aws:s3:::debugroom-sample-ssm-inventory"
          },
          {
               "Sid": " SSMBucketDelivery",
               "Effect": "Allow",
               "Principal": {
                   "Service": "ssm.amazonaws.com"
               },
               "Action": "s3:PutObject",
               "Resource": "arn:aws:s3:::debugroom-sample-ssm-inventory/*/accountid=XXXXXXXXXXXXXXX/*",
               "Condition": {
                   "StringEquals": {
                      "s3:x-amz-acl": "bucket-owner-full-control"
                   }
               }
          }
       ]
   }

|br|

.. figure:: img/management-console-s3-setting-access-controll-for-ssm-inventory-2.png

|br|

リソースデータを同期し、作成したS3バケットへデータ送信を行う。「インベントリ」メニューを選択し、「リソースデータの同期」ボタンを押下する。

|br|

.. figure:: img/management-console-ssm-resource-sync-1.png

|br|

「リソースデータの同期の作成」ボタンを押下する。

|br|

.. figure:: img/management-console-ssm-resource-sync-2.png

|br|

同期名を入力し、先ほど作成したバケット名を入力する。バケットのリージョンを選択して、「作成」ボタンを押下する。

|br|

.. figure:: img/management-console-ssm-resource-sync-3.png

|br|

「インベントリ」メニューを再度押下して。作成したインベントリの「詳細ビュー」タブから作成したリソースデータの同期を行う。
ロールの作成などしばらく時間がかかるので、時間を置いた後再度実行する。

|br|

.. figure:: img/management-console-ssm-resource-sync-4.png

|br|

インベントリタイプで、設定したインベントリデータが表示されれば、作成が成功する(S3にデータがあるか確認する)。

|br|

.. figure:: img/management-console-ssm-resource-sync-5.png

|br|

.. note:: このデータ同期により、Athena でクエリするために必要な Glue クローラや Glue データカタログの作成も合わせて行われる。Glueクローラーでは指定した場所にあるデータを自動で解析してAmazon Athenaのデータベースとテーブルを作成する。

|br|

作成したデータをQuickSightで可視化する(初回はサインアップ手順から)。QuickSightサービスを選択し、「Sign up for QuickSight」ボタンを押下する。

|br|

.. figure:: img/management-console-quicksight-create-account-1.png

|br|

「Standard」エディションを選択する。

|br|

.. figure:: img/management-console-quicksight-create-account-2.png

|br|

S3バケットがある同じリージョンを選択し、QuickSightアカウント名及び、Emailを入力する。

|br|

.. figure:: img/management-console-quicksight-create-account-3.png

|br|
以下にチェックを入れ、「完了」ボタンを押下する。

* Enable autodiscovery of data and users in your Amazon Redshift, Amazon RDS, and AWS IAM services
* Amazon Athena
* AmazonS3(バケットも忘れずに設定する)

|br|

.. figure:: img/management-console-quicksight-create-account-4.png

|br|

「Amazon QuickSightに移動する」ボタンを押下する。

|br|

.. figure:: img/management-console-quicksight-create-account-5.png

|br|

画面左上部の「新しい分析」ボタンを押下する。

|br|

.. figure:: img/management-console-quicksight-create-analysis-1.png

|br|

画面左上部の「新しいデータセット」ボタンを押下する。

|br|

.. figure:: img/management-console-quicksight-create-analysis-2.png

|br|

「Athena」を選択する。

|br|

.. figure:: img/management-console-quicksight-create-analysis-3.png

|br|

「データソース名」を入力し、「データソースを作成」ボタンを押下する。

|br|

.. figure:: img/management-console-quicksight-create-analysis-5.png

|br|

テーブルを選択する。

.. note:: ここでは上述の「リソース同期の作成」時に、S3上に転送されたインベントリデータをAWS Glueがクロールしてテーブルを自動構築している。クロール頻度など変更したい場合は、「AWS Glue」サービスなどを選択して変更すること。

|br|

.. figure:: img/management-console-quicksight-create-analysis-6.png

|br|

「Visualize」ボタンを押下する。

|br|

.. figure:: img/management-console-quicksight-create-analysis-7.png

|br|

以下の要領で、インベントリデータを可視化する。

1. QuickSight のメイン画面左上の「追加」を選択し、「ビジュアルを追加する」を選択する。 シート上にビジュアルフィールドが表示される。
2. 左下のビジュアルタイプの枠右上にある円グラフのアイコンをクリックする。
3. 左側のフィールドリストから architecture を選択し、ドラッグ&ドロップして「グループ/色」に配置する。ビジュアルフィールドに円グラフが表示される。これは対象となる全サーバの全パッケージ一覧のアーキテクチャの比率を示している。

|br|

.. figure:: img/management-console-quicksight-create-analysis-8.png

|br|

また、以下の要領で、別のインベントリデータを可視化する。ここでは、特定パッケージの特定バージョンのソフトウェアがいくつ導入されているのか表を新しく作成する。

1. 再度 QuickSight のメイン画面左上の「追加」を選択し、「ビジュアルを追加する」を選択する。シート上にビジュアルフィールドが表示される。
2. ビジュアルタイプから左下のほうにある「テーブル」を選択する(ピボットテーブルではなく)。
3. グループ化の条件に「name」 「version」、値に「name」をドラッグ&ドロップする。値に配置したものは name(カウント) となり、合計件数を表している。対象サーバが 1つだとカウントはすべて1になるが、異なるバージョンのソフトウエアが複数サーバに導入されていると、どのバージョンがいくつ導入されているのかを把握することが可能になる。これにフィルタを追加することで、特定ソフトウェアだけを表示することができる。

|br|

.. figure:: img/management-console-quicksight-create-analysis-9.png

|br|

.. _section9-9-5-systems-manager-runnging-task-label:

Running Tasks
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

|br|

SystemsManager を使用することで、多数の EC2インスタンスへのコマンドの実行を一括して行うことができる。ここでは、
:ref:`section9-9-2-systems-manager-remote-connect-label` の手順中に作成したEC2インスタンスに対し、コマンドの実行を行う。
「コマンドの実行」メニューを選択し、「コマンドを実行」ボタンを押下する。

|br|

.. figure:: img/management-console-ssm-run-command-1.png

|br|

「AWS-RunShellScript」を選択する。

|br|

.. figure:: img/management-console-ssm-run-command-2.png

|br|

実行コマンドを入力する。

|br|

.. figure:: img/management-console-ssm-run-command-3.png

|br|

ターゲットとなるインスタンスを選択する。インスタンスのタグや、手動選択、作成したリソースグループの選択も可能である。ここでは、作成したインスタンスを手動で選択する。

|br|

.. figure:: img/management-console-ssm-run-command-4.png

|br|

その他は特に入力せず、「コマンド実行」ボタンを押下する。

|br|

.. figure:: img/management-console-ssm-run-command-5.png

|br|

実行されたコマンドの結果が表示される。

|br|

.. figure:: img/management-console-ssm-run-command-6.png

|br|

2500文字までなら出力結果を確認することができる。

|br|

.. figure:: img/management-console-ssm-run-command-7.png

|br|
|br|


.. _section9-9-6-systems-manager-Parameter-store-label:

Paramter Store
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. _section9-9-6-1-systems-manager-parameter-store-overview-label:

Overview
""""""""""""""""""""""""""""""""""""""""""""""""""""""""""

パラメータストアはパスワードやデータベース文字列、ライセンスコードなどアプリケーションに直接実装せず環境変数を経由して設定するデータ等を一元的に管理するためのサービスである。
特に多くのEC2インスタンスやECSタスク上にデプロイされたアプリケーションなどで同一のデータを参照したい場合有効であり、またデータは階層構造をとることができる。
データ値はプレーンテキストまたはAWS KMSを使用して暗号化データとして使用でき、暗号化や復号化も同時に実行する。パラメータストアの参照はIAMにより細かくアクセス制御が可能であり、
以下のAWSサービスから利用可能である。

* Amazon EC2
* Amazon ECS
* AWS Lambda
* AWS CloudFormation
* AWS CodeBuild
* AWS CodeDeploy

また、暗号化や通知、モニタリング、監査を行うため、以下のサービスと連動して機能する。

* AWS KMS
* Amazon SNS
* Amazon CloudWatch
* AWS CloudTrail

従来のパラメータストアでは、データサイズが4KBまで、スループット上限は規定範囲内だったが、`2019年4月のアップデートでアドバンスドパラメータオプションが導入 <https://aws.amazon.com/jp/about-aws/whats-new/2019/04/aws_systems_manager_parameter_store_introduces_advanced_parameters/>`_ され、
10000を変えるパラメータの作成、最大8KB、スループットの上限緩和(デフォルト40tpsから最大1000tpsまで)の拡張がなされた。
ただし、アドバンスドパラメータオプションは有料となるため注意すること。

.. _section9-9-6-2-systems-manager-parameter-store-create-parameter-label:

標準パラメータストアの設定
""""""""""""""""""""""""""""""""""""""""""""""""""""""""""

1. AWSコンソール上から、Systems Managerを選択し、「パラメータストア」メニューから「パラメータの作成」ボタンを押下する。

.. figure:: img/management-console-ssm-create-parameter-store-1.png


2. パラメータ名と値を設定し、「作成」ボタンを押下する。

.. figure:: img/management-console-ssm-create-parameter-store-2.png


.. note:: ここで設定したパラメータを参照する方法は、 :ref:`section8-2-2-1-codebuild-local-execution-label` を参照のこと。

.. _section9-9-7-systems-manager-os-patch-management-label:

OS patch management
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. todo:: パッチマネージャーを使ったOSパッチ管理方法を記載する。

.. _section9-9-8-systems-manager-maintenance-windows-label:

Maintenance windows
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^


|br|

メンテナンスウィンドウとコマンド実行機能を利用して、OSのパッチを適用するコマンドを実行する。「メンテナンスウィンドウ」メニューから「メンテナンスウィンドウの作成」ボタンを押下する。

|br|

.. figure:: img/management-console-ssm-create-maintenance-window-1.png

|br|

メンテナンスウィンドウ名を入力する。ここで、未登録のターゲットのチェックは外しておく。

|br|

.. figure:: img/management-console-ssm-create-maintenance-window-2.png

|br|

スケジュールを指定する。確認のため、ここでは30分に一回、クーロンスケジュールビルダーで設定しておく。

|br|

.. figure:: img/management-console-ssm-create-maintenance-window-3.png

|br|

「メンテナンスウィンドウの作成」ボタンを押下する。

|br|

.. figure:: img/management-console-ssm-create-maintenance-window-4.png

|br|

作成したメンテナンスウィンドウを選択し、「アクション」ボタンからターゲットの登録を選択する。

|br|

.. figure:: img/management-console-ssm-create-maintenance-window-target-1.png

|br|

ターゲット名を入力する。

|br|

.. figure:: img/management-console-ssm-create-maintenance-window-target-2.png

|br|

適用対象とするインスタンスを選択する。ここでも同じくインスタンスのタグや、手動指定、リソースグループを使った指定ができるが、手動でインスタンスを選択する。

|br|

.. figure:: img/management-console-ssm-create-maintenance-window-target-3.png

|br|
|br|

.. figure:: img/management-console-ssm-create-maintenance-window-target-4.png

|br|

続いて、メンテナンスウィンドウを選択し、「タスク」タブから、「タスクを登録する」ボタンを押下する
|br|

.. figure:: img/management-console-ssm-create-maintenance-window-task-1.png

|br|

「run command タスクの登録」を選択する。

|br|

.. figure:: img/management-console-ssm-create-maintenance-window-task-2.png

|br|

「タスクの名称」を入力する。

|br|

.. figure:: img/management-console-ssm-create-maintenance-window-task-3.png

|br|

コマンドのドキュメントには、「AWS-RunPatchBaseline」を選択し、優先度を1に設定する。

|br|

.. figure:: img/management-console-ssm-create-maintenance-window-task-4.png

|br|

上記で作成したターゲットを指定する。「レート制御」゙は一度に 2つのインスタンスの処理を実行し、全ての処理が失敗した場合にのみタスクを停止する(途中で失敗するインスタンスがあった場合でも全てのインスタンスに一度は処理が実行される)よう
並行性ターゲットを1に指定し、誤差閾値を100%に設定する。

|br|

.. figure:: img/management-console-ssm-create-maintenance-window-task-5.png

|br|

IAMロールは「Systems Manager用のサービスにリンクされたロールを使用する」を選択する。

|br|

.. figure:: img/management-console-ssm-create-maintenance-window-task-6.png

|br|

パラメータのOperationには「Install」を指定し、「run commandタスクの登録」ボタンを押下する。

|br|

.. figure:: img/management-console-ssm-create-maintenance-window-task-7.png

|br|

登録後、指定時刻になると、実行されるので、履歴タブから結果を確認する。

|br|

.. figure:: img/management-console-ssm-create-maintenance-window-task-8.png

|br|


.. _section9-9-9-systems-manager-consistent-configuration-label:

Consistent Configuration
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. todo:: ステートマネージャを使った設定・実行方法を記載する。

.. _section9-10-label:

Landing Zone
------------------------------------------------------

.. _section9-10-1-landingzone-overview-label:

Overview
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

マルチアカウントのポリシーやパーミッションをコントロールし自動で簡単に設定できるソリューション(無償)。ControllTownerの前身。
構成する各サービスの利用料は負担する形になる。

.. _section9-11-label:

ControlTower
------------------------------------------------------

.. _section9-11-1-ControlTower-overview-label:

Overview
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

マルチアカウントに対応したセキュアな環境(ガードレール)を簡単に設定・管理

.. list-table:: 設定されたガードレール
   :widths: 8, 2, 3,2

   * - ルール
     - ガイダンス
     - カテゴリ
     - 動作

   * - ログアーカイブの保存時に暗号化を有効にする
     - 必須
     - 監査ログ
     - 予防

   * - ログアーカイブのアクセスログ作成を有効にする
     - 必須
     - 監査ログ
     - 予防

   * - ログアーカイブへのポリシーの変更を不許可にします
     - 必須
     - モニタリング
     - 予防

   * - ログアーカイブへのパブリック読み取りアクセスを不許可にする
     - 必須
     - 監査ログ
     - 検出

   * - ログアーカイブへのパブリック書き込みアクセスを不許可にする
     - 必須
     - 監査ログ
     - 検出

   * - ログアーカイブの保持ポリシーを設定する
     - 必須
     - 監査ログ
     - 予防

   * - CloudTrail への設定変更を不許可にします
     - 必須
     - 監査ログ
     - 予防

   * - CloudTrail イベントと CloudWatch logs を統合する
     - 必須
     - モニタリング
     - 予防

   * - 利用可能なすべてのリージョンで CloudTrail を有効にする
     - 必須
     - 監査ログ
     - 予防

   * - CloudTrail ログファイルの整合性検証を有効にする
     - 必須
     - 監査ログ
     - 予防

   * - AWS Control Tower によって設定された CloudWatch への変更を不許可にします
     - 必須
     - Control Tower のセットアップ
     - 予防

   * - AWS Control Tower によって設定された AWS Config アグリゲーションへの変更を不許可にします
     - 必須
     - Control Tower のセットアップ
     - 予防

   * - AWS Config への設定変更を不許可にします
     - 必須
     - 監査ログ
     - 予防

   * - 利用可能なすべてのリージョンで AWS Config を有効にする
     - 必須
     - 監査ログ
     - 予防

   * - AWS Control Tower によって設定された AWS Config ルールへの変更を不許可にします
     - 必須
     - Control Tower のセットアップ
     - 予防

   * - EBS 用に最適化されていない EC2 インスタンスタイプの起動を許可しない
     - 強く推奨
     - オペレーション
     - 検出

   * - EC2 インスタンスにアタッチされていない EBS ボリュームを許可しない
     - 強く推奨
     - オペレーション
     - 検出

   * - EC2 インスタンスにアタッチされた EBS ボリュームの暗号化を有効にする
     - 強く推奨
     - データセキュリティ
     - 検出

   * - AWS Control Tower によって設定された IAM ロールへの変更を不許可にします
     - 必須
     - Control Tower のセットアップ
     - 予防

   * - MFA なしで IAM ユーザーへのアクセスを許可しない
     - 選択的
     - IAM
     - 検出

   * - AWS Control Tower によって設定された Lambda 関数への変更を不許可にします
     - 必須
     - Control Tower のセットアップ
     - 予防

   * - MFA なしで IAM ユーザーへのコンソールアクセスを許可しない
     - 選択的
     - IAM
     - 検出

   * - RDP を介したインターネット接続を許可しない
     - 強く推奨
     - ネットワーク
     - 検出

   * - SSH を介したインターネット接続を許可しない
     - 強く推奨
     - ネットワーク
     - 検出

   * - RDS データベースインスタンスへのパブリックアクセスを許可しない
     - 強く推奨
     - データセキュリティ
     - 検出

   * - RDS データベーススナップショットへのパブリックアクセスを許可しない
     - 強く推奨
     - データセキュリティ
     - 検出

   * - 暗号化されたストレージではない RDS データベースインスタンスを許可しない
     - 強く推奨
     - データセキュリティ
     - 検出

   * - ルートユーザーとしてのアクションを許可しない
     - 強く推奨
     - IAM
     - 予防

   * - ルートユーザーのアクセスキーの作成を許可しない
     - 強く推奨
     - IAM
     - 予防

   * - S3 バケットのクロスリージョンレプリケーションを許可しない
     - 選択的
     - データセキュリティ
     - 予防

   * - MFA なしで S3 バケットでの削除アクションを許可しない
     - 選択的
     - データセキュリティ
     - 予防

   * - ルートユーザーに対して、MFA を有効化する
     - 強く推奨
     - IAM
     - 検出

   * - S3 バケットへのパブリック読み取りアクセスを不許可にする
     - 強く推奨
     - データセキュリティ
     - 検出

   * - S3 バケットへのパブリック書き込みアクセスを不許可にする
     - 強く推奨
     - データセキュリティ
     - 検出

   * - バージョンが有効かされていないS3 バケットを許可しない
     - 選択的
     - データセキュリティ
     - 検出

   * - Amazon SNS によって設定された AWS Control Tower への変更を不許可にします
     - 必須
     - Control Tower のセットアップ
     - 予防

   * - AWS Control Tower によって設定された Amazon SNS のサブスクリプションへの変更を不許可にします
     - 必須
     - Control Tower のセットアップ
     - 予防


.. _section9-12-2-memo-label:

AWS運用Memo
------------------------------------------------------

■エンタープライズ運用ユースケース・パターン

* サーバを可視化

   * SSM Inventoryを使った可視化
   * SSMエージェントをインストールすれば、オンプレミスマシンの情報収集も可能
   * EC2→SSM Inventory→S3→Athena→QuickSight
   * AWS Configとの連携により違反状況を検知・通知も可能

* 実行一括オペレーション

   * SSM RunCommandによる規定操作の一括実行

* サーバログイン操作の事前許可と事後確認

   * SSM SessionManager上で、許可された人が許可されたサーバに許可された時間にログイン可能なように制御
   * 実行コマンド履歴の事後確認

* インスタンスを指定時間帯のみ起動する

   * SSM MaintenanceWindowによるスケジューリング
   * Instance Scheduler

* メモリ使用率などを収集する監視エージェント(CloudWatch統合Agent)の一括設定

* セキュリティパッチの適用

   * SSM PatchManagerを使用して一定のルールで全社のサーバに適用する

* ソフトウェアライセンスの管理

   * SSM Inventoryを使って情報収集し、LicenceManagerで突きあわせ。

* Control Tower

■マルチアカウントとして、以下のようにアカウントを分離することを推奨。

* 請求アカウント
* 共有サービス
* セキュリティ&監査

■ 運用の種類と目的

* システム運用

   * イベント管理
   * インシデント管理
   * セキュリティ管理

* 保守運用

    * 構成管理
    * パッチ管理
    * ライセンス管理
    * 変更管理
    * 資産管理
    * リリース管理
    * テスト

* 障害対応

* サービス運用
