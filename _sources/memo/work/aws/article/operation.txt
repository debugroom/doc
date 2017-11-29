.. include:: ../module.txt

.. _section8-operation-label:

AWSでの運用オペレーション
======================================================

.. _section8-1-snapshot-label:

実行中のEC2インスタンスのバックアップ
------------------------------------------------------

.. _section8-1-1-overview-label:

overview
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

クラウドデザインパターンでは「Snapshotパターン」と呼ばれる。
EC2インスタンスはディスクボリュームに「EBS(Elastic Block Store)」というストレージデバイスを使用しており、
EBSにはある瞬間のスナップショットとしてバックアップを作成する機能がある。作成したデータはS3に保存されるが、
特殊な形式のデータのため、直接S3から取り出すことはできない。作成したデータはEBSとして新たに作成し、
EC2インスタンスにマウントするか、AMIイメージを作成し、新たなEC2インスタンスとし起動できる。
これは次章「  :ref:`section8-2-stamp-label` 」にて記述する「Stampパターン」である。

.. _section8-1-1-operation-create-ebs-label:

EBSボリュームの作成手順
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

1. 対象のストレージの確認

EC2コンソールメニューから、「ボリューム」を選択し、スナップショットを作成する対象のストレージを選択する。

.. figure:: img/management-console-operation-snapshot-1.png
   :scale: 100%

.. note:: /dev/xvda はXenベースのLinuxシステムにおいて、1台目のハードディスクドライブを指す。

2. スナップショットの作成

VolumeIDを右クリックし、「Create Snapshot」を選択する。

.. figure:: img/management-console-operation-snapshot-2.png
   :scale: 100%

名称を入力する。

.. figure:: img/management-console-operation-snapshot-3.png
   :scale: 100%

.. figure:: img/management-console-operation-snapshot-4.png
   :scale: 100%

3. EBSボリュームを作成する。

作成したスナップショットを右クリックし、「Create Volume」を選択する。

.. figure:: img/management-console-operation-snapshot-5.png
   :scale: 100%

「ディスクの種類」、「ディスクの容量」、「アベイラビリティゾーンを選択する。」

.. figure:: img/management-console-operation-snapshot-6.png
   :scale: 100%

.. note:: EBSボリュームは同一のアベイラビリティゾーンでしか利用できないため、実行するEC2インスタンスのアベイラビリティゾーンに応じて、作成するゾーンを決定する。

実行すると、ボリュームにEBSがステータスavailabilityで作成される。

.. figure:: img/management-console-operation-snapshot-7.png
   :scale: 100%

.. note:: スナップショットの選択時に、右クリック「コピー」を選択すると、別のリージョンへボリュームをコピーできる。

.. _section8-1-2-operation-create-ami-label:

EBSスナップショットからAMIの作成手順
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

スナップショットを作成したEBSがブート領域を含むのであれば(1台目のディスク)、AMIを作成できる。

1. イメージの作成

スナップショットを右クリック「イメージの作成」を選択する。

.. figure:: img/management-console-operation-stamp-1.png
   :scale: 100%

AMIの名称、説明、仮想マシンの種別を入力する。

.. figure:: img/management-console-operation-stamp-2.png
   :scale: 100%

.. note:: T2インスタンスを使用する場合、「ハードウェアアシストの仮想化(Hardware-assisted virtualization)」を選択すること。

実行すると、AMIにイメージがステータスavailabilityで作成される。

.. figure:: img/management-console-operation-stamp-3.png
   :scale: 100%


.. _section8-2-stamp-label:

同じ構成のEC2インスタンスを作成する
------------------------------------------------------

.. _section8-2-1-stamp-overview-label:

overview
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

前節「 :ref:`section8-1-2-operation-create-ami-label` 」にて、EBSのスナップショットからAMIイメージを作成したが、
稼働中のEC2インスタンスからAMIイメージを作成できる。AMIからEC2インスタンスを作るときは、
CPUやメモリ構成も設定でき、スペックを任意に調整できる。

.. _section8-2-2-operation-create-ami-label:

実行中EC2インスタンスからAMIの作成手順
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

1. AMIイメージの作成

.. warning:: 当オペレーションを実行すると、完全なディスクの複製を作成するために、実行中のEC2インスタンスが再起動するので注意。再起動したくない場合は、再起動しないオプションにチェックを入れて実行すること。

メニュー「インスタンス」から、右クリック「イメージの作成」を選択する。

.. figure:: img/management-console-operation-stamp-4.png
   :scale: 100%

「 :ref:`section8-1-2-operation-create-ami-label` 」と同様、AMIの名称、説明、仮想マシンの種別を入力する。

.. figure:: img/management-console-operation-stamp-5.png
   :scale: 100%

実行すると、AMIにイメージがステータスavailabilityで作成される。

.. figure:: img/management-console-operation-stamp-6.png
   :scale: 100%

.. _section8-2-3-operation-launch-ec2-by-ami-label:

複製したAMIからEC2インスタンスを実行する手順
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

作成したAMIを使用して、EC2インスタンスを起動する。

1. EC2インスタンスの実行

メニュー「AMI」でイメージを選択し、右クリック「作成」を行う。

.. figure:: img/management-console-operation-stamp-7.png
   :scale: 100%

「 :ref:`section3-1-2-X-ec2-create-instance-label` 」と同様に、EC2インスタンスを起動する。

.. todo:: AMIイメージの作成方法により実行可能なインスタンスタイプが制限される模様。条件を確認する。

.. _section8-3-scale-up-label:

CPUやメモリスペックを上昇させる
------------------------------------------------------

.. _section7-4-ondemand-disk-label:

ディスク容量を増設する
------------------------------------------------------
