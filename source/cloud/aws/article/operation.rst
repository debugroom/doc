.. include:: ../module.txt

.. _section8-operation-label:

Operation on AWS
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

.. note:: EBSスナップショットは
   * 差分(増分)バックアップ
   * 圧縮された状態で保存
   * S3に３箇所複数される。
   * ディスクサイズはフルサイズ表示となるが、実態は差分バックアップのため、サイズが必ずしも実際のデータサイズと一致しているわけではない。

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

.. todo:: EC2インスタンス起動中にCPUやメモリスペックを変更する手順を記載する。

.. _section8-4-ondemand-disk-label:

ディスク容量を増設する
------------------------------------------------------

.. _section8-4-1-application-extend-volume-overview-label:

overview
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

起動中のEC2インスタンスの容量が足りなくなった場合、以下のようなメッセージが表示される。

.. sourcecode:: bash

   open /var/lib/docker/image/devicemapper/layerdb/tmp/layer-314207183/diff: no space left on device

dfコマンドでディスク残容量の確認は以下の通り可能であるが、ここでは、起動中のインスタンスのディスク増設する方法を記述する。

.. sourcecode:: bash

   [centos@ip-XXX-XXX-XXX-XXX ~]$ df -h
   Filesystem      Size  Used Avail Use% Mounted on
   /dev/xvda1      8.0G  8.0G   52M 100% /
   devtmpfs        7.4G     0  7.4G   0% /dev
   tmpfs           7.4G     0  7.4G   0% /dev/shm
   tmpfs           7.4G   17M  7.4G   1% /run
   tmpfs           7.4G     0  7.4G   0% /sys/fs/cgroup
   tmpfs           1.5G     0  1.5G   0% /run/user/1000

.. _section8-4-2-application-extend-volume-label:

EC2ボリュームサイズの拡張
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

■EC2コンソールメニューからボリュームを選び、拡張したいインスタンスのボリュームを選択する。アクションメニューから、「ボリュームの変更」を選択する。

.. figure:: img/management-console-ec2-modify-volume-1.png
   :scale: 100%

■ボリュームサイズを変更し、「変更」ボタンを押下する。

.. figure:: img/management-console-ec2-modify-volume-2.png
   :scale: 100%

.. figure:: img/management-console-ec2-modify-volume-3.png
   :scale: 100%

■EC2インスタンスにSSHでログインし、拡張したボリュームサイズにルートデバイスのパーティションを拡張させる。最初に、現状のディスクの状況を確認する。

.. sourcecode:: bash

   # ルートデバイスのパーティションサイズを確認。
   [centos@ip-XXX-XXX-XXX-XXX ~]$ lsblk
   NAME                         MAJ:MIN RM  SIZE RO TYPE MOUNTPOINT
   xvda                         202:0    0   50G  0 disk
   └─xvda1                      202:1    0    8G  0 part /
   loop0                          7:0    0  100G  0 loop
   └─docker-202:1-12881694-pool 253:0    0  100G  0 dm
   loop1                          7:1    0    2G  0 loop
   └─docker-202:1-12881694-pool 253:0    0  100G  0 dm

   # パーティションの占有状況を確認。

   [centos@ip-XXX-XXX-XXX-XXX ~]$ df -TH
   Filesystem     Type      Size  Used Avail Use% Mounted on
   /dev/xvda1     xfs       8.6G  8.6G   58M 100% /
   devtmpfs       devtmpfs  8.0G     0  8.0G   0% /dev
   tmpfs          tmpfs     8.0G     0  8.0G   0% /dev/shm
   tmpfs          tmpfs     8.0G   18M  8.0G   1% /run
   tmpfs          tmpfs     8.0G     0  8.0G   0% /sys/fs/cgroup
   tmpfs          tmpfs     1.6G     0  1.6G   0% /run/user/1000

   # ファイルシステムの確認。

   [centos@ip-XXX-XXX-XXX-XXX ~]$ sudo file -s /dev/xvd*
   /dev/xvda:  x86 boot sector; partition 1: ID=0x83, active, starthead 32, startsector 2048, 16775168 sectors, code offset 0x63
   /dev/xvda1: SGI XFS filesystem data (blksz 4096, inosz 512, v2 dirs)

.. warning:: `AWSの公式ガイド <https://docs.aws.amazon.com/ja_jp/AWSEC2/latest/UserGuide/recognize-expanded-volume-linux.html>`_ では、XFSファイルシステムのディスク拡張はxfs_growfsコマンドを使用しているが、更新が行われなかったため、fdiskコマンドを使ってパーティションの再作成を行う方法で実施する。

■ fdiskコマンドを使用して、/dev/xvdaのパーティションを作成し直し、再起動する。

.. sourcecode:: bash

   [centos@ip-XXX-XXX-XXX-XXX ~]$ sudo fdisk /dev/xvda
   Welcome to fdisk (util-linux 2.23.2).

   Changes will remain in memory only, until you decide to write them.
   Be careful before using the write command.


   Command (m for help): p

   Disk /dev/xvda: 53.7 GB, 53687091200 bytes, 104857600 sectors
   Units = sectors of 1 * 512 = 512 bytes
   Sector size (logical/physical): 512 bytes / 512 bytes
   I/O size (minimum/optimal): 512 bytes / 512 bytes
   Disk label type: dos
   Disk identifier: 0x000ae09f

   Device Boot      Start         End      Blocks   Id  System
   /dev/xvda1   *        2048    16777215     8387584   83  Linux

   Command (m for help): d
   Selected partition 1
   Partition 1 is deleted

   Command (m for help): n
   Partition type:
   p   primary (0 primary, 0 extended, 4 free)
   e   extended
   Select (default p): p
   Partition number (1-4, default 1): 1
   First sector (2048-104857599, default 2048):
   Using default value 2048
   Last sector, +sectors or +size{K,M,G} (2048-104857599, default 104857599):
   Using default value 104857599
   Partition 1 of type Linux and of size 50 GiB is set

   Command (m for help): p

   Disk /dev/xvda: 53.7 GB, 53687091200 bytes, 104857600 sectors
   Units = sectors of 1 * 512 = 512 bytes
   Sector size (logical/physical): 512 bytes / 512 bytes
   I/O size (minimum/optimal): 512 bytes / 512 bytes
   Disk label type: dos
   Disk identifier: 0x000ae09f

   Device Boot      Start         End      Blocks   Id  System
   /dev/xvda1            2048   104857599    52427776   83  Linux

   Command (m for help): w
   The partition table has been altered!

   Calling ioctl() to re-read partition table.

   WARNING: Re-reading the partition table failed with error 16: Device or resource busy.
   The kernel still uses the old table. The new table will be used at
   the next reboot or after you run partprobe(8) or kpartx(8)
   Syncing disks.

   [centos@ip-XXX-XXX-XXX-XXX ~]$ reboot

   # 再起動後、パーティションのサイズ変更を確認。
   [centos@ip-XXX-XXX-XXX-XXX ~]$ df -TH
   Filesystem     Type      Size  Used Avail Use% Mounted on
   /dev/xvda1     xfs        54G  8.6G   46G  16% /
   devtmpfs       devtmpfs  8.0G     0  8.0G   0% /dev
   tmpfs          tmpfs     8.0G     0  8.0G   0% /dev/shm
   tmpfs          tmpfs     8.0G   18M  8.0G   1% /run
   tmpfs          tmpfs     8.0G     0  8.0G   0% /sys/fs/cgroup
   tmpfs          tmpfs     1.6G     0  1.6G   0% /run/user/1000

   [centos@ip-XXX-XXX-XXX-XXX ~]$ lsblk
   NAME                         MAJ:MIN RM  SIZE RO TYPE MOUNTPOINT
   xvda                         202:0    0   50G  0 disk
   └─xvda1                      202:1    0   50G  0 part /
   loop0                          7:0    0  100G  0 loop
   └─docker-202:1-12881694-pool 253:0    0  100G  0 dm
   loop1                          7:1    0    2G  0 loop
   └─docker-202:1-12881694-pool 253:0    0  100G  0 dm

.. _section8-5-application-update-label:

冗長化構成のアプリケーションをアップデートする
---------------------------------------------------------------------

.. _section8-5-1-application-update-overview-label:

overview
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

ロードバランサーを使用して冗長化構成したアプリケーションをサービス停止せずにアップデートする。
片系のアプリケーションサーバをターゲットグループから除外した後、アプリケーションをアップデートし、
再度ターゲットグループに組み込む。その後もう片系のアプリケーションサーバを同様にターゲットグループから削除し、
アップデートを行い、組み戻しを行う。

.. _section8-5-2-exclude-target-group-label:

^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

1. APサーバのインスタンスをターゲットグループから除外

コンソールのメニュー「ターゲットグループ」にて、ターゲットタブから編集ボタンを押下する。

.. figure:: img/management-console-operation-app-update-1.png
   :scale: 100%

2. アップデート対象のアプリケーションインスタンスを選択し、削除ボタンを押下する。

.. figure:: img/management-console-operation-app-update-2.png
   :scale: 100%

.. note:: 削除したインスタンスはロードバランサーがターゲットのリクエストルーティングを即時指定するが、deregistration_delay.timeout_secondsとして設定した時間、Connection Dranining状態として、セッションが維持される。
