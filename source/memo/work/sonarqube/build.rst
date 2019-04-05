.. include:: ../module.txt

SONARQUBEの環境構築
================================================================================

サンプルプロジェクトの作成
--------------------------------------------------------------------------------

SONARQUBEにログイン後、右上の＋ボタンを押下し、「Create New Project」を押下し、
ProjectKeyおよび、DisplayNameを入力する。

|br|

.. figure:: img/sonarqube_create_new_project_1.png
   :scale: 100%

|br|

プロジェクトのトークンを発行する。

.. note:: トークンはログイン情報として使用するので記録しておくこと。

|br|

.. figure:: img/sonarqube_create_new_project_2.png
   :scale: 100%

|br|

.. figure:: img/sonarqube_create_new_project_3.png
   :scale: 100%

|br|

プラグインの追加
--------------------------------------------------------------------------------

|br|

Javaのチェックルールとして、FindBugsのルールを追加する。「Administration」メニューを選択し、
「Marketplace」タブをにある、Plugin検索フォームから「FindBugs」を入力して、
ヒットしたプラグインをインストールする。

|br|

.. figure:: img/sonarqube_add_findbugs_1.png
   :scale: 100%

|br|

プロファイルの追加
--------------------------------------------------------------------------------

|br|

追加した「FindBugs」のチェックルールと合わせ、適用する新たなプロファイルを作成する。
「Quality Profile」メニューを選択し、Javaの「Sonar way」プロファイルのコンフィグメニューで
「コピー」を選択する。

|br|

.. figure:: img/sonarqube_create_new_profile_1.png
   :scale: 100%

|br|

新たなプロファイル名を入力する。

|br|

.. figure:: img/sonarqube_create_new_profile_2.png
   :scale: 100%

|br|

追加したプロファイルに追加のチェックルールを設定する場合は、「Activate More」ボタンから追加を行う。

|br|

.. figure:: img/sonarqube_create_new_profile_3.png
   :scale: 100%

|br|

IntellJ IDEAへのSONAR Lintインストール
--------------------------------------------------------------

|br|

SONARQUBEで作成したチェックルールをローカル端末のIntelliJ IDEAに適用するために、
SonarLintのインストールを行う。IntelliJのPreferenceから、「Plugin」メニューを選択し、
「Browse repositories」ボタンを押下する。

|br|

.. figure:: img/intellj_install_sonar_lint_1.png
   :scale: 100%

|br|

SonarLintを検索し、インストールを実行する。インストール後、再起動を行うこと。

|br|

.. figure:: img/intellj_install_sonar_lint_2.png
   :scale: 100%

|br|

SONARQUBEに設定したプロジェクトのプロファイルのチェックルールを適用するよう、SonarLintからSonarQubeへの接続を行う。
「Preference」から「Other Settings」 > 「SonarLint General Settings」を選択し、「＋」ボタンを押下する。

|br|

.. figure:: img/intellj_setting_sonarqube_1.png
   :scale: 100%

|br|

Configuration Nameを入力し、ConnectionTypeはSONARQUBEを選択する。

|br|

.. figure:: img/intellj_setting_sonarqube_2.png
   :scale: 100%

|br|

SONARQUBEの新規プロジェクト作成時に取得したトークンを入力する。

|br|

.. figure:: img/intellj_setting_sonarqube_3.png
   :scale: 100%

|br|

接続が繋がったことを確認して、「Finish」ボタンを押下する。

|br|

.. figure:: img/intellj_setting_sonarqube_4.png
   :scale: 100%

|br|

接続完了後、SONARQUBEに設定したプロジェクトの設定を行う。これで、静的チェックを行う際に、SONARQUBEでプロジェクトに設定したプロファイルのルールが適用されることになる。

|br|

.. figure:: img/intellj_setting_project.png
   :scale: 100%

|br|
