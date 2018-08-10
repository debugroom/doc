.. include:: ../module.txt

.. _section4-computing-label:

Computing Category
======================================================

.. _section3-1-ec2-label:

Elastic Compute Cloud(EC2)
------------------------------------------------------

.. _section3-1-1-ec2-overview-label:

Overview
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Elastic Compute Cloud(EC2)は仮想サーバサービスである。それぞれの仮想サーバを「インスタンス」と呼び、OS、CPUやメモリーといった性能、ストレージ、購入オプションを選択して、サービスを利用する。選択可能な可能なOSはAWSの `マーケットプレイス <https://aws.amazon.com/marketplace/b/2649367011>`_ から確認できる。

CPUやメモリ、ストレージといったリソースキャパシティを定義したものをインスタンスタイプと呼ぶ。インスタンスタイプはAmazonのインスタンス選択の `ガイドライン <https://aws.amazon.com/jp/ec2/instance-types/>`_ に沿って、選択するとよい。インスタンスタイプにはカテゴリがあり、それぞれ以下のようなアプリケーション特性に従い、インスタンスタイプを選択する。

.. list-table:: インスタンスタイプ
   :widths: 2, 3, 5

   * - タイプ
     - 種別
     - 用途

   * - T2
     - 汎用
     - 最も低コスト。開発環境や小規模なデータベース
   * - M3
     - 汎用
     - バランス型。小規模及び中規模データベース、 |br| キャッシュ、SAP、MicrosoftSharePoint、 |br| その他企業アプリケーション
   * - M4
     - 汎用
     - M3と同様のバランス型だが、最新世代の |br| 汎用インスタンスを利用
   * - C3
     - コンピューティング |br| 最適化
     - 高速なプロセッサを使用。高パフォーマンスの |br| フロントエンド群、バッチ処理、分散分析、 |br| 高パフォーマンスな科学・工学への応用 |br| 濃くサービス、MMOゲーム、ビデオエンコーディング
   * - C4
     - コンピューティング |br| 最適化
     - C3と同様のハイパフォーマンス型だが、 |br| 最新世代インスタンスを利用。
   * - F1
     - コンピューティング |br| 最適化
     - FPGA(フィールドプログラマブルゲートアレイ)を |br| 搭載したインスタンス。
   * - X1
     - メモリ最適化
     - RAM1GiBの料金が最も低コスト。インメモリ |br| データベース、ビッグデータ処理エンジン、 |br| ハイパフォーマンスコンピューティング(HPC)
   * - R3
     - メモリ最適化
     - RAM1GiBの料金が安価。ハイパフォーマンス |br| データベース。分散型メモリキャッシュ、 |br| インメモリ分析、その他エンタープライズ |br| アプリケーション
   * - R4
     - メモリ最適化
     - R3と同様、大量のメモリを使用するアプリケーション向け |br| 最新世代インスタンス。
   * - G2
     - GPU最適化
     - グラフィック向け。3Dアプリケーション |br| ストリーミング、 機械学習、|br| 動画エンコーディング
   * - I2
     - ストレージ最適化
     - 高速ランダムIOパフォーマンス最適化。 |br| NoSQLデータベース、トランザクションデータベース |br| データウェアハウス、Hadoop |br| クラスターファイルシステム
   * - I3
     - ストレージ最適化
     - I2と同様のストレージアクセス最適化 |br| 最新世代インスタンス。
   * - D2
     - ストレージ最適化
     - 最も低コストでディスクを提供。超並列処理 |br| (MPP)、データウェアハウス、 |br| MapReduceとHadoop分散処理、 |br| 分散ファイルシステム

.. note:: インスタンスタイプの数字は世代を表す。世代ごとにCPUなどのハードが異なり、数字が高いほど最新である。価格は必ずしも世代が新しいからといって高くはないので、検討のうえ世代を選択すること。

.. note:: EC2のインスタンスタイプを変更すると、ハイパーバイザを実行している物理ホストを変更することになり、EC2の停止が必要になる。



ストレージはEC2にアタッチされるElastic Block Store(EBS)というブロックレベルのストレージボリュームで、以下の通り4種類を選択できる。

.. list-table:: EBSの種類
   :widths: 3, 2, 2, 3, 4

   * - 種類
     - 最大サイズ
     - 費用
     - IOPS・ |br| スループット
     - 推奨用途

   * - マグネティック |br| ボリューム
     - 16TiB
     - 安い
     - ー
     - アクセス頻度が低いシステム |br| 下位互換のためで基本使用しない
   * - 汎用SSD
     - 16TiB
     - 普通
     - 10000,  |br| 160MiB/s
     - 汎用インスタンス用ボリューム
   * - プロビジョンド |br| IOPSボリューム
     - 16TiB
     - 高
     - 20000,  |br| 320MiB/s
     - ランダムなデータのアクセス頻度が高い |br| システム。RDB等。
   * - スループット |br| 最適化HDD
     - 16TiB
     - 安い
     - 500,  |br| 500MiB/s
     - 連続した大量データのシステム
   * - コールドHDD
     - 16TiB
     - 安い
     - 250,  |br| 250MiB/s
     - アクセス頻度の低い大量データのシステム

購入オプションは、

* オンデマンドインスタンス
* リザーブドインスタンス
* スポットインスタンス

の3種類を選択できる。オンデマンドインスタンスは時間単位の固定単価で料金が計算され、使った時間分だけ料金が発生する。
AWS管理コンソールからEC2インスタンスを起動した場合は、オンデマンドインスタンスになる。
リザーブドインスタンスは予約期間1年か3年かの長期オプションを選択できる代わりに、最大75%のディスカウントが得られる。
スポットインスタンスは入札の様な仕組みで提供されるインスタンスで、時間単位の料金が発生するという点においては、
オンデマンドインスタンスと同じだが、固定単価でなく、利用者が最大時間料金を入札形式で指定し、最大額で落札できた場合利用可能である。

.. note:: その他、ハードウェア占有インスタンス(ランダムにホストを占有し、インスタンスに課金され、他のユーザの使用がないタイプ)や物理ホスト自体を提供するDedicated Host(ホスト利用料金となり、インスタンス自体には課金されない)というインスタンスオプションがある。

.. _section3-1-2-ec2-settings-label:

EC2インスタンスの設定
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. _section3-1-2-X-ec2-settings-reserved-instance-label:

リザーブドインスタンスの購入
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. note:: リザーブドインスタンスの購入は「指定した条件のインスタンスを無料で利用できる権利を買う」イメージである。実際のインスタンスの起動はオンデマンドインスタンスと同様の操作で行い、リザーブドインスタンスを購入していれば料金がかからないといった形になる。

■東京リージョンにて、リザーブドインスタンスを購入する。Mangement ConsoleからEC2を選択し、グローバルメニュー"リザーブドインスタンス"を選択すると、リザーブドインスタンス購入ボタンが表示される。


.. figure:: img/management-console-ec2-buy-reserved-instance-1.png
   :height: 600px
   :scale: 100%


■リザーブドインスタンスの以下の通り、購入オプションを選択し、検索ボタンを押下する。

* プラットフォーム：Linux/UNIX
* テナンシー：デフォルト
* 提供クラス：すべて
* インスタンスタイプ：m3.medium
* 期間：1ヶ月 - 12ヶ月
* お支払い方法：全前払い

.. figure:: img/management-console-ec2-buy-reserved-instance-2.png
   :scale: 100%

■購入数量を記入し、"カートに入れる"ボタンを押下する。

.. figure:: img/management-console-ec2-buy-reserved-instance-3.png
   :scale: 100%

■ショッピングカートで購入するを押下すると、購入したインスタンスのステータスが一覧化して表示される。

.. figure:: img/management-console-ec2-buy-reserved-instance-4.png
   :scale: 100%


.. note:: アベイラビリティゾーンを指定してリザーブドインスタンスを購入すると、リソース予約となり、随時インスタンスの起動が可能である。リソース予約がないとインスタンスが枯渇している場合立ち上げできない場合がある。

.. note:: 事前に定められた時間帯に限定するスケジュールリザーブドインスタンスもあり、通常5-10%程度安価に購入できる。

.. _section3-1-2-X-ec2-create-instance-label:

インスタンスの作成
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

■東京リージョンに、インスタンスを作成する。Management ConsoleからEC2を選択し、グローバルメニュー"インスタンス"を選択すると、"インスタンスの作成"ボタンが表示される。


.. figure:: img/management-console-ec2-create-instance-1.png
   :scale: 100%


■マシンイメージを選択する。

.. figure:: img/management-console-ec2-create-instance-2.png
   :scale: 100%

■インスタンスタイプを選択する。リザーブドインスタンスで購入しているのであれば、無料枠のインスタンスタイプが表示される。

.. figure:: img/management-console-ec2-create-instance-3.png
   :scale: 100%

.. note:: EC2のストレージは、物理ホストに接続されている高性能なインスタンスストレージと、ネットワークに接続されているEBSベースのストレージがある。インスタンスストレージでは無料で利用できるが、シャットダウンするとEBSではないインスタンスストレージのデータは失われる。

.. note:: インスタンスのAMIはインスタンスストレージにOSを格納しているものと、EBSストレージにOSを格納しているタイプ(EBS-basedと呼ぶ)がある。インスタンスストレージは高速になるが、EBSでないものはバックアップ(スナップショット)の作成が難しいので注意。

.. note:: EBS最適化はネットワークに接続されたEBSストレージがI/Oする際に、専用のチャネルでデータ入出力しているものである。

■インスタンスの起動オプションを設定する。ここではデフォルトで設定。

.. figure:: img/management-console-ec2-create-instance-4.png
   :scale: 100%

■インスタンスにアタッチするストレージのオプションを選択する。

.. figure:: img/management-console-ec2-create-instance-5.png
   :scale: 100%

■インスタンスのタグを設定する。

.. figure:: img/management-console-ec2-create-instance-6.png
   :scale: 100%

■セキュリティグループの設定を行う。ここでは新規にセキュリティグループを作成する。

.. figure:: img/management-console-ec2-create-instance-7.png
   :scale: 100%

.. note:: 後述するSSH接続のために、セキュリティグループのインバウンドルールにSSH（送信元：0.0.0.0/0）が設定しておくこと。

■設定したインスタンスオプションの内容を確認し、"作成"ボタンを押下する。

.. figure:: img/management-console-ec2-create-instance-8.png
   :scale: 100%

■新規でキーペアを作成する場合、サーバへ接続するためのキーペアとなるpemファイルをダウンロードしておく。

.. figure:: img/management-console-ec2-create-instance-9.png
   :scale: 100%

■作成ボタンを押下すると、インスタンスが作成される。実行中のステータスになるまで少々待つ。

.. figure:: img/management-console-ec2-create-instance-10.png
   :scale: 100%

◇インスタンスが実行中になると、以下の通り、起動中のインスタンスの情報が一覧化できる。

.. figure:: img/management-console-ec2-create-instance-11.png
   :scale: 100%

.. note:: インスタンスを指定する際にマルチアベイラブルゾーン構成にしたい場合は、異なるアベライブルゾーンで作成したサブネットを指定してインスタンス起動すること。

.. note:: EC2インスタンス起動時には1つのNIC(ネットワークインターフェース)が割り当てられる。対応するMACアドレスはEC2インスタンスを起動するたび動的に変わっていくため、MACアドレスを指定したい場合は、EC2メニューからネットワークインターフェースを選んで作成し、ElasticIP(固定IP)を紐付けて、インスタンス起動時にアタッチするとよい。

.. note:: EC2インスタンス起動した場合のステータスチェックでは、以下２種類行われる。

   * システムステータスチェック：ハイパーバイザからのVMのチェック
   * インスタンスステータスチェック：OSのチェック

   EC2コンソールのダッシュボード>ステータスチェックタブにより、チェック状況に応じてインスタンスの再起動(AutoRecoverry)を行うか設定が可能である。

.. _section3-1-2-X-ec2-ssh-connect-instance-label:

EC2インスタンスへのSSH接続
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

■ターミナルを開き、キーペアとなるpemファイルを~/.ssh以下に配置し、アクセス権限をUserReadOnlyに変更する。

.. sourcecode:: bash

   chmod 400 ~/.ssh/*

.. note:: 秘密鍵ファイルに744など不必要に権限を与えるとエラーとなるため注意。

■SSH接続を行う。

.. sourcecode:: bash

   ssh -i /Users/username/.ssh/キー名.pem ec2-user@ec2-XX-XXX-XXX-XX.compute-1.amazonaws.com


.. note::

   * DNSはインスタンス情報の「パブリックDNS」を参照
   * Amazon Linuxインスタンスにおけるユーザ名はデフォルトでec2-user
   * 鍵の指定を絶対パスですること。相対パスや「~/.ssh/」などで指定するとErrorになる


.. _section3-1-3-X-ec2-autoscaling-label:

EC2のオートスケール設定
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

AutoScaleでは、指定された条件(スケーリングポリシー)に基づき、インスタンスを起動または終了する。ロードバランサーを使用している場合は、
追加したインスタンスを自動的にターゲットグループへ登録する。複数のアベイラビリティゾーンで起動可能である。

■起動設定(立ち上げるEC2インスタンスの設定)

.. todo:: オートスケール設定の画像イメージを添付。

起動設定では、以下を定義する。

* 名前
* 起動するAMI(Amazon Machine Image：OS)とインスタンスタイプ
* ユーザデータ(起動カスタムスクリプト)
* セキュリティグループ、IAMロール

■AutoScalingグループの設定(スケール・縮退する条件)

* 名前
* 適用する起動設定
* 最大・最小値
* 実行する環境のサブネット・対象
* スケーリングポリシー

  * アラーム設定
  * メトリクス閾値
  * 台数

.. note:: インスタンス起動時にAP設定などでIPなどの情報を取得したい場合は、「起動設定」の3.詳細設定>「高度な詳細」にて、ユーザデータでスクリプトを設定する。また、http://169.254.169.254/latest/meta-data/でインスタンスのメタデータを取得できる。

.. _section3-1-3-X-ec2-iam-role-setting-label:

EC2のIAMロールの設定
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

EC2には起動時にそのインスタンスが実行するIAM Roleを設定できる。重要なポイントはEC2上で実行しているアプリケーションが
AmazonSDKを通して、S3などのAWSリソースへアクセスするのに必要な認証情報(AccessKeyやSecretKey)を
インスタンスに設定したIAMロールから取得できることである。詳細は `Amazon EC2 での IAM ロールを使用した AWS リソースへのアクセス権限の付与 <https://docs.aws.amazon.com/ja_jp/sdk-for-java/v1/developer-guide/java-dg-roles.html>`_ を参照のこと。
ここでは、EC2インスタンス上で実行するアプリケーションに必要なロールとそれにアタッチするポリシーを以下の通り作成する。

■S3へアクセス可能なポリシーの作成

IAMサービスを選択し、ポリシーメニューから「ポリシーの作成」ボタンを押下する。
サービスはS3、実行するアクション、アクセス対象のバケットなどを指定し、ポリシー名を設定して作成する。

.. figure:: img/management-console-iam-create-policy-4.png
   :scale: 100%

.. figure:: img/management-console-iam-create-policy-5.png
   :scale: 100%

■S3へアクセスするIAMロールの作成

後述するAssumeRoleで権限委譲するために、S3へアクセスするポリシーをアタッチしたIAMロールを作成しておく。

IAMサービスを選択し、ロールメニューから「ロールの作成」ボタンを押下する。
ここでは、AssumeRole(権限を引き受ける = その権限与える許可をだす)の設定であるため、信頼されたエンティティとして、自身のアカウントを設定する。

.. figure:: img/management-console-iam-create-role-6.png
   :scale: 100%

上記で作成したS3へのアクセスポリシーをアタッチし、ロール名を設定して、ロールを作成する。

.. figure:: img/management-console-iam-create-role-7.png
   :scale: 100%

.. figure:: img/management-console-iam-create-role-8.png
   :scale: 100%

■S3へアクセス可能な権限を引き受けるAssumeRoleポリシーの作成

IAMサービスを選択し、ポリシーメニューから「ポリシーの作成」ボタンを押下し、
同様にAssumeRoleポリシーを作成する。権限委譲するIAMロールとして、上記で作成したS3へアクセスするIAMロールを設定する。

.. figure:: img/management-console-iam-create-policy-6.png
   :scale: 100%

.. figure:: img/management-console-iam-create-policy-7.png
   :scale: 100%

.. note:: AssumeRoleポリシーは、Amazon STSを使用して、AWSリソースに限定的にアクセス許可を与える署名付URLを発行するための権限である。
          ここでは、S3へのアクセス権限に関するAssumeRoleを作成するための権限を設定している。

■EC2インスタンスへ付与するIAMロールを作成

上記で作成した2つのポリシー(S3へアクセスするポリシーと、S3へアクセスするロールをAssumeRoleするポリシー)をアタッチする。

IAMサービスを選択し、ロールメニューから「ロールの作成」ボタンを押下する。信頼されたエンティティはEC2を選択する。

.. note:: EC2へIAM Roleを設定する場合は、信頼されたエンティティをEC2に設定しておく必要がある。

.. figure:: img/management-console-iam-create-role-for-ec2-1.png
   :scale: 100%

ポリシーをアタッチして、ロール名を設定し、ロールを作成する。

.. figure:: img/management-console-iam-create-role-for-ec2-2.png
   :scale: 100%

.. figure:: img/management-console-iam-create-role-for-ec2-3.png
   :scale: 100%

■EC2インスタンスへIAMロールをアタッチする。

EC2サービスを選択し、インスタンスメニューからアタッチ対象のインスタンスを選択し、「アクション」ボタンを押下し、
「インスタンスの設定」>「IAM ロールの割り当て/置換」を選択する。

.. figure:: img/management-console-ec2-setting-iam-role-1.png
   :scale: 100%

上記で作成したIAMロールを設定する。

.. figure:: img/management-console-ec2-setting-iam-role-2.png
   :scale: 100%

なお、アプリケーションの作成及びAWSリソースのアクセス方法はXXXを参照のこと。

.. todo:: Spring Clous AWSにおけるアプリケーション作成のリンクを設定。

.. _section3-2-ecs-label:

Elastic Container Service
------------------------------------------------------

.. _section3-2-1-ecs-overview-label:

Overview
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Amazon Elastic Container Service (Amazon ECS) は、クラスターで Docker コンテナを
簡単に実行、停止、管理できる非常にスケーラブルで高速なコンテナ管理サービスである。
Dockerの詳細については、:ref:`section1-docker-overview-label` を参照のこと。

ECSではリージョン内の複数のアベイラビリティーゾーンを跨いでアプリケーションコンテナを実行できる。
ECSでは、2018年8月現在時点では、ひとつまたは複数のEC2上にクラスタを構築し、
その上に任意のレジストリにあるDockerイメージをデプロイするEC2起動型と、
実行するクラスタ自体をマネージドとして扱い、コンテナだけを意識するFargateに分かれる。

EC2上にクラスタ構築し、アプリケーション環境を構築する方法とFargateを利用する方法各々、次章以降説明する。
なお、EC2起動型環境の構築は、以下の手順で行う。

#. ECSクラスタ作成
#. ECSタスク定義(コンテナ及びコンテナイメージの定義)
#. ロードバランサの作成
#. ECSサービスの定義

.. _section3-2-2-ecs-create-cluster-label:

EC2起動型-クラスタの作成
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

クラスタはコンテナを起動するためのホストマシン群である。

■AWSのサービスから「Elastic Container Service」を選択し、「クラスタメニュー」> 「クラスタの作成」を押下する。

.. figure:: img/management-console-ecs-create-cluster-1.png
   :height: 600px
   :scale: 100%

|

.. figure:: img/management-console-ecs-create-cluster-2.png
   :scale: 100%

|

■クラスタテンプレートで「EC2 Linux + ネットワーキング」を選択し、「次のステップへ」を押下する。

.. figure:: img/management-console-ecs-create-cluster-3.png
   :scale: 100%

■クラスタの定義設定を行う。

.. figure:: img/management-console-ecs-create-cluster-4.png
   :scale: 100%

[クラスタの設定]

* クラスター名：sample-cluster
* インスタンスの設定：オンデマンドインスタンス
* EC2インスタンス：m4.large
* インスタンス数：1
* EBSストレージ：22GiB
* キーペア：各自で発行したキーペアファイル

.. note:: EC2同様、指定したタイプのリザーブドインスタンスを購入しておくと、料金は発生しない。

.. note:: Dockerのイメージファイルは大きくなるため、EBSストレージは大きめに確保しておくこと。

.. figure:: img/management-console-ecs-create-cluster-5.png
   :scale: 100%

[ネットワーキング]

* VPC：既存のVPCネットワークを選択
* セキュリティグループ：アクセス用にSSH接続ができるセキュリティグループを設定しておく。
* コンテナインスタンスIAM Role : ecsInstanceRoleが付与されたIAM ロールを設定

.. warning:: ecsInstanceRoleはAmazonEC2ContainerServiceforEC2Roleポリシーが付与されたIAM ロールである。

   .. sourcecode:: java

      {
        "Version": "2012-10-17",
        "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "ecs:CreateCluster",
                "ecs:DeregisterContainerInstance",
                "ecs:DiscoverPollEndpoint",
                "ecs:Poll",
                "ecs:RegisterContainerInstance",
                "ecs:StartTelemetrySession",
                "ecs:UpdateContainerInstancesState",
                "ecs:Submit*",
                "ecr:GetAuthorizationToken",
                "ecr:BatchCheckLayerAvailability",
                "ecr:GetDownloadUrlForLayer",
                "ecr:BatchGetImage",
                "logs:CreateLogStream",
                "logs:PutLogEvents"
            ],
            "Resource": "*"
        }
        ]
      }

   当ポリシーをECSクラスタのIAMロールへアタッチしなければ、Dockerコンテナをサービスとして起動できなくなるため注意。

■クラスタの作成を押下し、クラスタを作成する。

.. figure:: img/management-console-ecs-create-cluster-6.png
   :scale: 100%

.. _section3-2-3-ecs-create-task-label:

EC2起動型-タスクの定義
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

タスク定義はDockerコンテナ実行定義および、コンテナイメージの定義である。なお、コンテナに設定するIAMロールを事前に作成しておく。
ここではコンテナ上で実行するアプリケーションからS3へアクセスすることを想定し、前もって作成したS3アクセスポリシーをアタッチした、ECS向けIAMロールを作成する。
S3アクセスポリシーの作成は :ref:`section7-1-3-iam-create-role-label` を参照のこと。

■IAMサービスから「ロール」メニューを選択し、「ロールを作成」ボタンを押下する。

.. figure:: img/management-console-iam-create-role-for-ecs-1.png
   :scale: 100%

■このロールを使用するサービスで「Elastic Container Service」からユースケース「Elastic Container Service Task」を選択し、「次のステップ：アクセス権限」を押下する。

.. figure:: img/management-console-iam-create-role-for-ecs-2.png
   :scale: 100%

■事前に作成指定おいたS3へアクセスするポリシーをアタッチする。ここでは、S3へのアクセスとS3アクセスのAssumeRoleを行うポリシーをアタッチする。

.. figure:: img/management-console-iam-create-role-for-ecs-3.png
   :scale: 100%

.. note:: S3アクセスのAssumeRoleとは、AmazonSTSに一時的にS3へのアクセス可能署名URLを発行させる権限である。

■ロール名を入力し、「ロールの作成」ボタンを押下する。

.. figure:: img/management-console-iam-create-role-for-ecs-4.png
   :scale: 100%


■ECSサービスを選択し、「タスク定義」メニューから「新しいタスク定義の作成」ボタンを押下する。

.. figure:: img/management-console-ecs-create-task-1.png
   :scale: 100%

■起動タイプの互換性で「EC2」を選び、「次のステップへ」を押下する。

.. figure:: img/management-console-ecs-create-task-2.png
   :scale: 100%

■タスク定義名を入力し、タスクのロールには、直前で作成したロールをアタッチする。

.. figure:: img/management-console-ecs-create-task-3.png
   :scale: 100%

■タスクサイズに、メモリサイズとCPUを割り当てる。

.. figure:: img/management-console-ecs-create-task-4.png
   :scale: 100%

■「コンテナの追加」ボタンを押下し、アプリケーションをLaunchしたコンテナイメージを指定する。

.. figure:: img/management-console-ecs-create-container-1.png
   :scale: 100%

* コンテナ名：sample-spring-cloud
* イメージ：DockerHubにあるイメージURIを指定
* メモリ：コンテナに割り当てるメモリを指定
* ポートマッピング：アプリケーションのポートを指定

■実行コンテナの詳細な設定を行う。

.. todo:: コンテナの詳細設定のオプションを整理

.. figure:: img/management-console-ecs-create-container-2.png
   :scale: 100%

.. figure:: img/management-console-ecs-create-container-3.png
   :scale: 100%

.. figure:: img/management-console-ecs-create-container-4.png
   :scale: 100%

.. figure:: img/management-console-ecs-create-container-5.png
   :scale: 100%

■コンテナ定義後、「作成」を押下する。

.. figure:: img/management-console-ecs-create-task-5.png
   :scale: 100%

.. figure:: img/management-console-ecs-create-task-6.png
   :scale: 100%

.. _section3-2-4-ecs-create-alb-label:

EC2起動型-ロードバランサ作成
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

各サービスに処理を分散させるためのALBの作成を行う。

■EC2サービスから、「ロードバランサー」メニューを選択し、「ロードバランサーの作成」を押下する。

.. figure:: img/management-console-ecs-create-alb-1.png
   :scale: 100%

■ロードバランサーの種類の選択で、Application Load Balancerを選択する。

.. figure:: img/management-console-ecs-create-alb-2.png
   :scale: 100%

■ロードバランサーの設定を行う。

* 名前：sample-ecs-alb(適当な名前を設定)
* アドレスタイプ：ipv4を選択
* リスナー：httpを選択
* アベイラビリティゾーン：2箇所のアベイラビリティゾーンを選択

.. figure:: img/management-console-ecs-create-alb-3.png
   :scale: 100%

■セキュリティ構成の設定では、HTTPなのでそのままスキップ。

.. figure:: img/management-console-ecs-create-alb-4.png
   :scale: 100%

.. note:: 今回はサンプルのためHTTPを設定しているが、本番ではHTTPS及び証明書等の設定を行うこと。

■セキュリティグループの設定で、HTTPポートを開ける設定を行う。

.. figure:: img/management-console-ecs-create-alb-5.png
   :scale: 100%

■ルーティングの設定で、新しいターゲットグループの設定、及びヘルスチェックの設定を行う。

[ターゲットグループ]

* ターゲットグループ：新しいターゲットグループを設定
* 名前：sample-ecs-target-group(適当な名前を設定)
* プロトコル：HTTP
* ポート：80
* ターゲットの種類：instance

[ヘルスチェック]

* HTTP
* index.html(アプリケーションのトップポータル・ログインに相当するパスを設定)

.. figure:: img/management-console-ecs-create-alb-6.png
   :scale: 100%

■ターゲットグループに :ref:`section3-2-2-ecs-create-cluster-label` で作成した、ECSクラスタを設定する。

.. figure:: img/management-console-ecs-create-alb-7.png
   :scale: 100%


.. _section3-2-5-ecs-create-service-label:

EC2起動型-サービスの定義
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

コンテナをLaunchさせる設定をサービスとして定義する。

■ECSサービスメニューから「クラスター」メニューを選択し、:ref:`section3-2-2-ecs-create-cluster-label` で作成した、ECSクラスタを選択する。「サービス」タブから「作成」ボタンを押下する。

.. figure:: img/management-console-ecs-create-service-1.png
   :scale: 100%

■サービスの設定で以下の通り、サービス起動設定を行う。

[サービスの設定]

* 起動タイプ：EC2
* タスク定義： :ref:`section3-2-3-ecs-create-task-label` で定義したタスクを指定
* クラスター：  :ref:`section3-2-2-ecs-create-cluster-label` で作成したクラスタを指定
* サービス名：samaple-ecs-service(適当な名前を設定)
* サービスタイプ： REPLICA
* タスクの数：1(実行するコンテナの数)
* 最小ヘルス率：50(デフォルト値)
* 最大率：200(デフォルト値)

[タスクの配置]

* AZバランススプレッド

.. todo:: サービス配置のオプションについて整理する。

.. figure:: img/management-console-ecs-create-service-2.png
   :scale: 100%

.. figure:: img/management-console-ecs-create-service-3.png
   :scale: 100%

■ネットワークの構成で、「VPCとセキュリティグループ」、「ヘルスチェックの猶予期間」、「ElasticLoadBalancing」、「負荷分散用のコンテナ」の設定を行う。

[VPCとセキュリティグループ]

* 設定済みのため省略

[ヘルスチェックの猶予期間]

* 100(アプリケーションの起動に時間がかかる場合の時間を設定)

[ElasticLoadBalancing]

* ELBタイプ： ApplicationLoadBalancerを選択
* サービス用のIAMロールの設定： :ref:`section3-2-3-ecs-create-task-label` で作成したECS Task用のIAM ロールを設定。
* ELB名： :ref:`section3-2-4-ecs-create-alb-label` で設定したALBを選択

[負荷分散用のコンテナ]

* :ref:`section3-2-3-ecs-create-task-label` で設定したコンテナ定義を選択し、「ELBへの追加」ボタンを押下する。
* ターゲットグループ名： :ref:`section3-2-4-ecs-create-alb-label` で作成したターゲットグループ名を指定。

.. figure:: img/management-console-ecs-create-service-4.png
   :scale: 100%


.. figure:: img/management-console-ecs-create-service-5.png
   :scale: 100%

■必要に応じて、AutoScalingオプションの設定を行い、「次のステップへ」を押下する。

.. figure:: img/management-console-ecs-create-service-6.png
   :scale: 100%

■設定内容を確認し、「サービスの作成」を押下する。

.. figure:: img/management-console-ecs-create-service-7.png
   :scale: 100%

■ECSサービスがLaunchする。ECSクラスタ上にコンテナが実行される。

.. figure:: img/management-console-ecs-create-service-8.png
   :scale: 100%
