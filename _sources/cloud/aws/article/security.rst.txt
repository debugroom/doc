.. include:: ../module.txt

.. _section7-security-label:

Security Category
======================================================

.. _section7-1-iam-label:

IAM(Indentity Access Management)
------------------------------------------------------

.. _section7-1-1-iam-overview-label:

Overview
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

AWSには、AWSアカウント(ルートアカウント)とIAMアカウントの２種類がある。
ルートアカウントはAWSの全てのサービスに対し、ネットワーク上のどこからでも
操作できる強力な権限を持っている。そのため、複数の利用者でAWSを利用したい場合や、
アプリケーション上からAPIを通じて、AWSリソースにアクセスする場合など、
必要に応じて、各AWSサービスごとに操作の制御が可能なIAMアカウントを作成し、運用するのが一般的である。

IAMアカウントには、以下の3種類が定義できる。

* IAMユーザ
* IAMグループ
* IAMロール

.. list-table:: IAMアカウント
   :widths: 2, 5

   * - 種類
     - 説明

   * - IAMユーザ
     - ユーザは各利用者にAWSを利用するための認証情報。ユーザには、人に限らず |br| CLI(Commnad Line Interface)やAPIを呼び出すアプリケーションも含まれる。 |br| 認証はそれぞれ、ID・パスワードを使用する方法と、アクセスキーと |br| シークレットアクセスキーを使用する方法で行う。

   * - IAMグループ
     - グループは同じ権限をもつユーザの集まりである。認証はユーザで行い、 |br| 認可はIAMグループに対して適切な権限を設定する方法がよい。

   * - IAMロール
     - ロールは永続的な権限(アクセスキー、シークレットアクセスキー)を保持する |br| ユーザと異なり、一時的にAWSリソースへアクセス権限を付与する場合に使用する。

認可は、「Action(どのサービスの)」、「Resource(どういう機能や範囲を)」、「Effect(許可or拒否)」という形でアクセスポリシーとして定義する。
AWSが最初から設定しているポリシーをAWS管理ポリシーといい、ユーザが独自に作成したポリシーをカスタマー管理ポリシーと呼ぶ。
これらのポリシーを上述のIAMユーザ、グループ、ロール各々に適用できる。また、これらの設定を様々なグループ、ユーザで横断的に適用できるよう、
オブジェクトとして管理できるIAM管理ポリシーと、従来の通り、ユーザ・グループ、ロール個別に設定していくインラインポリシーの2種類がある。

.. _section7-1-2-iam-create-user-label:

IAMユーザ・グループの作成
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

アプリケーションからアクセスするためのユーザ、及びグループを作成し、AWS管理ポリシーを割り当てる。

■管理コンソールのIAMから、ユーザを選び「ユーザの作成」を選択する。

.. figure:: img/management-console-iam-create-user-1.png


■ユーザ名を入力し、アクセスの種類(ここでは、プログラムによるアクセス)を設定する。

.. figure:: img/management-console-iam-create-user-2.png


■新規グループの作成を選択し、S3へアクセスするAWS管理ポリシーを割り当てる。

.. figure:: img/management-console-iam-create-group-1.png



.. _section7-1-3-iam-create-role-label:

IAMロールの作成
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

IAMロールは、アプリケーションでS3へのダイレクトアップロードを行う場合など、一時的にAWSリソースへアクセス権限を付与する場合に使用する。
ここでは、S3へファイルの書き込みに対して、STS(Security Token Service)を利用することを想定した、IAMロールの設定を行う。

■管理コンソールのIAMからロールを選び、「ロールの作成」ボタンを押下する。

.. figure:: img/management-console-iam-create-role-1.png



■アプリケーション用のユーザ(のみ)が、STSに対して、AssumreRoleリクエストが発行できるように、「別のAWSアカウント」を選択し、アプリケーション用のユーザを作成したアカウントIDを入力する。

.. figure:: img/management-console-iam-create-role-2.png



.. warning:: AWSサービス(EC2等)に対してロールを割り当ててしまうと、アプリケーションの実装を知っていれば、第３者でもアクセスできてしまうため、アプリケーションのユーザのみがAssumeRoleリクエストを発行できるように設定する。

.. note:: アプリケーションユーザの割り当ては後ほど行うため、ここでは、アプリケーション用のIAMユーザを作成したアカウントだけ設定する。

■S3への書き込みが行えるポリシーを作成する。新規ポリシーの作成を選択し、以下の通り、S3のバケットオブジェクトへPutObject権限を割り当てる。

.. figure:: img/management-console-iam-create-policy-1.png



* サービス：S3
* アクション：PutObjectを設定
* リソース：書き込み用のバケット名とオブジェクト名(フォルダ名+ワイルドカード)を指定。
* リクエスト条件：ここでは特に設定しない


.. figure:: img/management-console-iam-create-policy-2.png



■「ReviewPolicy」を押下し、ポリシー名を入力して、「CreatePolicy」でポリシーを作成する。


.. figure:: img/management-console-iam-create-policy-3.png




■作成したポリシーをロールに対して割り当て、ロール名を入力し、ロールを作成する。


.. figure:: img/management-console-iam-create-role-3.png



■ロールを作成したのち、信頼関係タブで、「信頼関係の編集」を押下する。

.. figure:: img/management-console-iam-create-role-4.png



■PrincipalのAWS属性を以下の通り書き換える。

"arn:aws:iam::<アカウント>:user:<アプリケーション用のIAMユーザ>"

.. figure:: img/management-console-iam-create-role-5.png


.. note:: アプリケーションからSTSを利用する場合、アカウントIDを含むロールARNを取得する必要があるが、アプリケーションのセキュリティ対策上の問題で、アプリケーション内で定義したロール名からARNを取得したい場合(アカウントIDなどをプロパティに定義したくない場合)、AssumeRequestするユーザがロール情報を取得できるよう、以下のようなポリシーをユーザにアタッチしておく必要がある。

   .. sourcecode:: javascript

      {
        "Version": "2012-10-17",
        "Statement": [{
          "Effect": "Allow",
          "Action": [
            "iam:GetRole",
            "iam:PassRole"
          ],
          "Resource": "arn:aws:iam::<account-id>:role/XX-*"
        }]
      }

.. note:: Policyは、未記入 < ALLOW < DENYの順で優先される。


.. _section7-2-acm-label:

AWS Certificate Manager
------------------------------------------------------

.. _section7-2-1-acm-overview-label:

Overview
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

AWS Certification Managerは、AWSの各種サービスで使用する
Secure Sockets Layer/Transport Layer Security (SSL/TLS)
証明書のプロビジョニング、管理、およびデプロイを、簡単に行えるサービスである。具体的には、

* SSL/TLS 証明書をリクエスト、プロビジョンして、Elastic Load Balancing、Amazon CloudFront、Amazon API Gateway などのサービスへ組み込み、ウェブサイトやアプリケーションに証明書をデプロイできる。
* リクエストしたドメインの所有権を証明して、証明書が発行されると、AWSマネジメントコンソールのドロップダウンリストで SSL/TLS 証明書を選択し、デプロイできる。
* AWSコマンドラインインターフェイス(CLI)コマンドまたは API 呼び出しを使用して、ACM によって提供される証明書を AWS リソースにデプロイすることもできる。ACM では、証明書の更新とデプロイが自動的に管理される。

SSL証明書には、

* ドメイン認証証明書（Domain Validation）
* 組織認証証明書（Organization Validation）
* EV証明書（Extended Validation）

があるが、ACMはドメイン認証型のSSL証明書であり、コンソール内での認証や、ドメインの管理者に確認のメールが届いて、
メールに記載されているURLをクリックするだけで、証明書の取得が可能である。

.. _section7-2-2-acm-request-certication-label:

証明書のリクエスト
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

1. 証明書を取得したいドメインを入力し、「次へ」を押下する。ここでは、¥*.debugroom.orgを入力。

.. figure:: img/management-console-acm-request-certification-1.png


2. DNSの検証ラジオボタンにチェックをいれたまま、「次へ」を押下する。この方法では、Route53を使って認証を行うパターンである。

.. figure:: img/management-console-acm-request-certification-2.png


3. 「確定とリクエスト」を押下する。

.. figure:: img/management-console-acm-request-certification-3.png


4. ドメインのDNS設定でCNAMEレコードの追加を行うことで認証する。ここではRoute53を使用してレコードの追加を行う。「Route53でのレコードの作成」を押下する。

.. figure:: img/management-console-acm-request-certification-4.png


5. 「作成」を押下し、レコードを追加する。

.. figure:: img/management-console-acm-request-certification-5.png


6. DNSレコードが書き込まれ、検証フェーズに更新される。

.. figure:: img/management-console-acm-request-certification-6.png


7. ACMコンソールでは、証明書の検証のステータスが表示される。検証が完了するまで少々時間がかかる。

.. figure:: img/management-console-acm-request-certification-7.png


.. _section7-3-cognito-label:

Amazon Cognito
------------------------------------------------------

.. _section7-3-1-cognito-overview-label:

Overview
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Amazon CognitoはモバイルID管理とデバイス間のデータ同期を提供するサービスである。
バックエンドコードを記述したりすることなく、アプリケーションの設定や状態をモバイル⇔クラウド上で同期できる。

アプリケーションでCognitoを作成する場合は、IDプールを作成する必要がある。IDプールとはアカウント固有のユーザIDデータが保存される場所である。

.. _section7-3-1-cognito-create-idpool-label:

IDプールの作成
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

■AWSコンソールからAmazon Cognitoを選択し、「フェデレーテッドアイデンティティの管理」を押下する。

.. figure:: img/management-console-cognito-create-idpool-1.png


■「IDプール名」を入力し、「認証されていないIDに対してアクセスを有効にする」にチェックを入れて、「プールの作成」を押下する。

.. figure:: img/management-console-cognito-create-idpool-2.png


■Amazon Cognitoで認証されたユーザと認証されていないユーザのIAMロールが作成されるので、「詳細を表示」ペインを押下して、作成される制限付きアクセス制限ポリシーを表示して、「許可」をクリックする。

.. todo:: Cognitoの使用方法を整理し記述

.. _section7-4-sts-label:

Amazon STS
------------------------------------------------------

.. _section7-4-1-sts-overview-label:

Overview
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

STSはIAMユーザもしくはフェデレーテッドユーザがリクエスト可能な一時的セキュリティ認証情報を提供するWebサービスである。
STSでは、Security Assertion Markup Language (SAML) 2.0 をサポートする。この機能では、フェデレーティッドシングルサインオン (SSO) が有効になり、
ユーザーは Windows Active Directory Federation ServicesなどのSAML 互換アイデンティティプロバイダーからのアサーションを使用して、
AWS マネジメントコンソールへのサインインや、プログラムの AWS API 呼び出しを行うことができる。

.. note:: フェデレーテッドユーザとは、特定の認証を完了した認証済みのユーザを指す。

また、Amazon、Facebook、または Google へのログインなどの信頼できるアイデンティティプロ バイダーを使用して、アプリケーションを設定し、
フェデレーションされた認証情報を配布するWeb ID フェデレーションをサポートするが、以下のようなステップで実行可能である。

#. アイデンティティプロバイダーにアプリケーションを登録する。
#. アイデンティティプロバイダー用の IAM ロールを作成する。
#. そのIAMロールにアクセス権限を設定する。
#. アイデンティティプロバイダーのSDKを使用して、ログイン後にトークンにアクセスできるようにする。
#. AWS SDK for JavaScript を使用して、トークンを元にAWSリソースの一時的な認証情報をアプリケーションに取得させる

.. _section7-5-key-management-service-label:

AWS Key Management Service(KMS)
------------------------------------------------------

AWS Key Management Serviceとは、データを暗号化するキーのマネージド暗号化サービスである。
KMSでは、カスタマーマスターキー(CMK)と呼ばれるタイプのキーを使用して、データを暗号化および復号する。
データキーは暗号化対象毎に固有のため、キーが流出しても影響は限定的になる。KMSでは以下のようなキー管理機能を実行できる。

* 一意のエイリアスおよび説明を付けたキーを作成する。
* IAMのどのユーザーおよびロールがキーを管理できるかを定義する。
* IAMのどのユーザーおよびロールがキーを使用して、データを暗号化および復号できるかを定義する。
* AWS KMSが1年ごとに自動でキーを更新するよう設定する。
* キーを誰にも使用されないように一時的に無効にする。
* 無効にしたキーを再度有効にする。
* AWS CloudTrailでログを調査して、キーの使用を監査する。

アプリケーションのデータを暗号化する必要がある場合、開発者は、AWS SDKを使用することで、簡単に暗号化キーを使用および保護できる。

.. note:: AWS CloudHSMと類似するサービスであるが、KMSはマスターとなるキー自体を生成するのに対し、CloudHSMはキーを保管しておくだけのサービスである。

.. _section7-X-other-label:

AWSのセキュリティ対策
------------------------------------------------------

.. _section7-3-1-ddos-attack-label:

DDos対策
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

DDosを緩和する場合は

CloudFrontエッジロケーション > ロードバランサ > WAF > Webアプリケーション

といった形でシステム構成を行っておく。


.. _section7-3-2-security-inspector-label:

Security Inspector
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Security InspetorはAWSにデプロイされたアプリケーションのセキュリティ評価サービスである。

.. todo:: Security Inspectorについて記述。
