.. include:: ../module.txt

.. _section2-docker-install-label:

Dockerのインストール
====================================

AWSにおけるDockerの導入は `公式サイト <http://docs.aws.amazon.com/ja_jp/AmazonECS/latest/developerguide/docker-basics.html>`_ も参照のこと。

1. EC2のCentOS7上で、パッケージとパッケージキャッシュを更新する。

.. sourcecode:: bash

   [centos@ip-XXXX-XXX-XXX-XXX ~]$ sudo yum update -y
   Loaded plugins: fastestmirror
   base                                                     | 3.6 kB     00:00     
   extras                                                   | 3.4 kB     00:00     
   http://ftp.jaist.ac.jp/pub/Linux/CentOS/7.3.1611/updates/x86_64/repodata/repomd.xml: [Errno 12] Timeout on http://ftp.jaist.ac.jp/pub/Linux/CentOS/7.3.1611/updates/x86_64/repodata/repomd.xml: (28, 'Operation too slow. Less than 1000 bytes/sec transferred the last 30 seconds')
   Trying other mirror.
   updates                                                  | 3.4 kB     00:00     
   (1/4): extras/7/x86_64/primary_db                          | 151 kB   00:00     
   (2/4): base/7/x86_64/group_gz                              | 155 kB   00:00     
   (3/4): base/7/x86_64/primary_db                            | 5.6 MB   00:00     
   (4/4): updates/7/x86_64/primary_db                         | 4.8 MB   00:14     
   Determining fastest mirrors
    * base: ftp.iij.ad.jp
    * extras: ftp.iij.ad.jp
    * updates: ftp.iij.ad.jp
   Resolving Dependencies
   --> Running transaction check
   ---> Package libtirpc.x86_64 0:0.2.4-0.8.el7 will be updated
   ---> Package libtirpc.x86_64 0:0.2.4-0.8.el7_3 will be an update
   ---> Package rpcbind.x86_64 0:0.2.0-38.el7 will be updated
   ---> Package rpcbind.x86_64 0:0.2.0-38.el7_3 will be an update
   --> Finished Dependency Resolution

   Dependencies Resolved

   ================================================================================
    Package          Arch           Version                  Repository       Size
   ================================================================================
   Updating:
    libtirpc         x86_64         0.2.4-0.8.el7_3          updates          88 k
    rpcbind          x86_64         0.2.0-38.el7_3           updates          59 k

   Transaction Summary
   ================================================================================
   Upgrade  2 Packages

   Total download size: 147 k
   Downloading packages:
   Delta RPMs disabled because /usr/bin/applydeltarpm not installed.
   warning: /var/cache/yum/x86_64/7/updates/packages/libtirpc-0.2.4-0.8.el7_3.x86_64.rpm: Header V3 RSA/SHA256 Signature, key ID f4a80eb5: NOKEY
   Public key for libtirpc-0.2.4-0.8.el7_3.x86_64.rpm is not installed
   (1/2): libtirpc-0.2.4-0.8.el7_3.x86_64.rpm                 |  88 kB   00:00     
   (2/2): rpcbind-0.2.0-38.el7_3.x86_64.rpm                   |  59 kB   00:00     
   --------------------------------------------------------------------------------
   Total                                              921 kB/s | 147 kB  00:00     
   Retrieving key from file:///etc/pki/rpm-gpg/RPM-GPG-KEY-CentOS-7
   Importing GPG key 0xF4A80EB5:
    Userid     : "CentOS-7 Key (CentOS 7 Official Signing Key) <security@centos.org>"
    Fingerprint: 6341 ab27 53d7 8a78 a7c2 7bb1 24c6 a8a7 f4a8 0eb5
    Package    : centos-release-7-3.1611.el7.centos.x86_64 (installed)
    From       : /etc/pki/rpm-gpg/RPM-GPG-KEY-CentOS-7
   Running transaction check
   Running transaction test
   Transaction test succeeded
   Running transaction
     Updating   : libtirpc-0.2.4-0.8.el7_3.x86_64                              1/4 
     Updating   : rpcbind-0.2.0-38.el7_3.x86_64                                2/4 
     Cleanup    : rpcbind-0.2.0-38.el7.x86_64                                  3/4 
     Cleanup    : libtirpc-0.2.4-0.8.el7.x86_64                                4/4 
     Verifying  : libtirpc-0.2.4-0.8.el7_3.x86_64                              1/4 
     Verifying  : rpcbind-0.2.0-38.el7_3.x86_64                                2/4 
     Verifying  : rpcbind-0.2.0-38.el7.x86_64                                  3/4 
     Verifying  : libtirpc-0.2.4-0.8.el7.x86_64                                4/4 

   Updated:
     libtirpc.x86_64 0:0.2.4-0.8.el7_3       rpcbind.x86_64 0:0.2.0-38.el7_3      

   Complete!

2. Dockerをインストール。

.. sourcecode:: bash

   [centos@ip-XXX-XXX-XXX-XXX ~]$ sudo yum install -y docker
   Loaded plugins: fastestmirror
   Loading mirror speeds from cached hostfile
    * base: ftp.iij.ad.jp
    * extras: ftp.iij.ad.jp
    * updates: ftp.iij.ad.jp
   Resolving Dependencies
   --> Running transaction check
   ---> Package docker.x86_64 2:1.12.6-16.el7.centos will be installed
   --> Processing Dependency: docker-common = 2:1.12.6-16.el7.centos for package: 2:docker-1.12.6-16.el7.centos.x86_64
   --> Processing Dependency: docker-client = 2:1.12.6-16.el7.centos for package: 2:docker-1.12.6-16.el7.centos.x86_64
   --> Processing Dependency: oci-systemd-hook >= 1:0.1.4-9 for package: 2:docker-1.12.6-16.el7.centos.x86_64
   --> Processing Dependency: oci-register-machine >= 1:0-3.10 for package: 2:docker-1.12.6-16.el7.centos.x86_64
   --> Processing Dependency: lvm2 >= 2.02.112 for package: 2:docker-1.12.6-16.el7.centos.x86_64
   --> Processing Dependency: container-selinux >= 2:2.10-2 for package: 2:docker-1.12.6-16.el7.centos.x86_64
   --> Processing Dependency: skopeo-containers for package: 2:docker-1.12.6-16.el7.centos.x86_64
   --> Processing Dependency: libseccomp.so.2()(64bit) for package: 2:docker-1.12.6-16.el7.centos.x86_64
   --> Running transaction check
   ---> Package container-selinux.noarch 2:2.10-2.el7 will be installed
   ---> Package docker-client.x86_64 2:1.12.6-16.el7.centos will be installed
   ---> Package docker-common.x86_64 2:1.12.6-16.el7.centos will be installed
   ---> Package libseccomp.x86_64 0:2.3.1-2.el7 will be installed
   ---> Package lvm2.x86_64 7:2.02.166-1.el7_3.4 will be installed
   --> Processing Dependency: lvm2-libs = 7:2.02.166-1.el7_3.4 for package: 7:lvm2-2.02.166-1.el7_3.4.x86_64
   --> Processing Dependency: device-mapper-persistent-data >= 0.6.3-1 for package: 7:lvm2-2.02.166-1.el7_3.4.x86_64
   --> Processing Dependency: liblvm2app.so.2.2(Base)(64bit) for package: 7:lvm2-2.02.166-1.el7_3.4.x86_64
   --> Processing Dependency: libdevmapper-event.so.1.02(Base)(64bit) for package: 7:lvm2-2.02.166-1.el7_3.4.x86_64
   --> Processing Dependency: liblvm2app.so.2.2()(64bit) for package: 7:lvm2-2.02.166-1.el7_3.4.x86_64
   --> Processing Dependency: libdevmapper-event.so.1.02()(64bit) for package: 7:lvm2-2.02.166-1.el7_3.4.x86_64
   ---> Package oci-register-machine.x86_64 1:0-3.11.gitdd0daef.el7 will be installed
   ---> Package oci-systemd-hook.x86_64 1:0.1.7-2.git2788078.el7 will be installed
   --> Processing Dependency: libyajl.so.2()(64bit) for package: 1:oci-systemd-hook-0.1.7-2.git2788078.el7.x86_64
   ---> Package skopeo-containers.x86_64 1:0.1.18-1.el7 will be installed
   --> Running transaction check
   ---> Package device-mapper-event-libs.x86_64 7:1.02.135-1.el7_3.4 will be installed
   ---> Package device-mapper-persistent-data.x86_64 0:0.6.3-1.el7 will be installed
   --> Processing Dependency: libaio.so.1(LIBAIO_0.4)(64bit) for package: device-mapper-persistent-data-0.6.3-1.el7.x86_64
   --> Processing Dependency: libaio.so.1(LIBAIO_0.1)(64bit) for package: device-mapper-persistent-data-0.6.3-1.el7.x86_64
   --> Processing Dependency: libaio.so.1()(64bit) for package: device-mapper-persistent-data-0.6.3-1.el7.x86_64
   ---> Package lvm2-libs.x86_64 7:2.02.166-1.el7_3.4 will be installed
   --> Processing Dependency: device-mapper-event = 7:1.02.135-1.el7_3.4 for package: 7:lvm2-libs-2.02.166-1.el7_3.4.x86_64
   ---> Package yajl.x86_64 0:2.0.4-4.el7 will be installed
   --> Running transaction check
   ---> Package device-mapper-event.x86_64 7:1.02.135-1.el7_3.4 will be installed
   ---> Package libaio.x86_64 0:0.3.109-13.el7 will be installed
   --> Finished Dependency Resolution

   Dependencies Resolved

   ================================================================================
    Package                       Arch   Version                     Repository
                                                                              Size
   ================================================================================
   Installing:
    docker                        x86_64 2:1.12.6-16.el7.centos      extras   14 M
   Installing for dependencies:
    container-selinux             noarch 2:2.10-2.el7                extras   28 k
    device-mapper-event           x86_64 7:1.02.135-1.el7_3.4        updates 178 k
    device-mapper-event-libs      x86_64 7:1.02.135-1.el7_3.4        updates 177 k
    device-mapper-persistent-data x86_64 0.6.3-1.el7                 base    368 k
    docker-client                 x86_64 2:1.12.6-16.el7.centos      extras  3.2 M
    docker-common                 x86_64 2:1.12.6-16.el7.centos      extras   72 k
    libaio                        x86_64 0.3.109-13.el7              base     24 k
    libseccomp                    x86_64 2.3.1-2.el7                 base     56 k
    lvm2                          x86_64 7:2.02.166-1.el7_3.4        updates 1.1 M
    lvm2-libs                     x86_64 7:2.02.166-1.el7_3.4        updates 986 k
    oci-register-machine          x86_64 1:0-3.11.gitdd0daef.el7     extras  1.0 M
    oci-systemd-hook              x86_64 1:0.1.7-2.git2788078.el7    extras   30 k
    skopeo-containers             x86_64 1:0.1.18-1.el7              extras  7.6 k
    yajl                          x86_64 2.0.4-4.el7                 base     39 k

   Transaction Summary
   ================================================================================
   Install  1 Package (+14 Dependent packages)

   Total download size: 21 M
   Installed size: 71 M
   Downloading packages:
   (1/15): container-selinux-2.10-2.el7.noarch.rpm            |  28 kB   00:00     
   (2/15): device-mapper-event-1.02.135-1.el7_3.4.x86_64.rpm  | 178 kB   00:00     
   (3/15): docker-client-1.12.6-16.el7.centos.x86_64.rpm      | 3.2 MB   00:00     
   (4/15): libaio-0.3.109-13.el7.x86_64.rpm                   |  24 kB   00:00     
   (5/15): libseccomp-2.3.1-2.el7.x86_64.rpm                  |  56 kB   00:00     
   (6/15): lvm2-2.02.166-1.el7_3.4.x86_64.rpm                 | 1.1 MB   00:00     
   (7/15): device-mapper-event-libs-1.02.135-1.el7_3.4.x86_64 | 177 kB   00:00     
   (8/15): device-mapper-persistent-data-0.6.3-1.el7.x86_64.r | 368 kB   00:00     
   (9/15): lvm2-libs-2.02.166-1.el7_3.4.x86_64.rpm            | 986 kB   00:00     
   (10/15): oci-systemd-hook-0.1.7-2.git2788078.el7.x86_64.rp |  30 kB   00:00     
   (11/15): docker-common-1.12.6-16.el7.centos.x86_64.rpm     |  72 kB   00:00     
   (12/15): skopeo-containers-0.1.18-1.el7.x86_64.rpm         | 7.6 kB   00:00     
   (13/15): oci-register-machine-0-3.11.gitdd0daef.el7.x86_64 | 1.0 MB   00:00     
   (14/15): yajl-2.0.4-4.el7.x86_64.rpm                       |  39 kB   00:00     
   (15/15): docker-1.12.6-16.el7.centos.x86_64.rpm            |  14 MB   00:02     
   --------------------------------------------------------------------------------
   Total                                              8.1 MB/s |  21 MB  00:02     
   Running transaction check
   Running transaction test
   Transaction test succeeded
   Running transaction
     Installing : 7:device-mapper-event-libs-1.02.135-1.el7_3.4.x86_64        1/15 
     Installing : 2:docker-common-1.12.6-16.el7.centos.x86_64                 2/15 
     Installing : 2:docker-client-1.12.6-16.el7.centos.x86_64                 3/15 
     Installing : 7:device-mapper-event-1.02.135-1.el7_3.4.x86_64             4/15 
     Installing : 7:lvm2-libs-2.02.166-1.el7_3.4.x86_64                       5/15 
     Installing : libaio-0.3.109-13.el7.x86_64                                6/15 
     Installing : device-mapper-persistent-data-0.6.3-1.el7.x86_64            7/15 
     Installing : 7:lvm2-2.02.166-1.el7_3.4.x86_64                            8/15 
   Created symlink from /etc/systemd/system/sysinit.target.wants/lvm2-lvmpolld.socket to /usr/lib/systemd/system/lvm2-lvmpolld.socket.
     Installing : yajl-2.0.4-4.el7.x86_64                                     9/15 
     Installing : 1:oci-systemd-hook-0.1.7-2.git2788078.el7.x86_64           10/15 
     Installing : 1:oci-register-machine-0-3.11.gitdd0daef.el7.x86_64        11/15 
     Installing : 2:container-selinux-2.10-2.el7.noarch                      12/15 
     Installing : libseccomp-2.3.1-2.el7.x86_64                              13/15 
     Installing : 1:skopeo-containers-0.1.18-1.el7.x86_64                    14/15 
     Installing : 2:docker-1.12.6-16.el7.centos.x86_64                       15/15 
     Verifying  : 7:lvm2-libs-2.02.166-1.el7_3.4.x86_64                       1/15 
     Verifying  : 1:skopeo-containers-0.1.18-1.el7.x86_64                     2/15 
     Verifying  : libseccomp-2.3.1-2.el7.x86_64                               3/15 
     Verifying  : 7:device-mapper-event-1.02.135-1.el7_3.4.x86_64             4/15 
     Verifying  : 2:docker-1.12.6-16.el7.centos.x86_64                        5/15 
     Verifying  : 1:oci-systemd-hook-0.1.7-2.git2788078.el7.x86_64            6/15 
     Verifying  : device-mapper-persistent-data-0.6.3-1.el7.x86_64            7/15 
     Verifying  : 2:container-selinux-2.10-2.el7.noarch                       8/15 
     Verifying  : 7:device-mapper-event-libs-1.02.135-1.el7_3.4.x86_64        9/15 
     Verifying  : 7:lvm2-2.02.166-1.el7_3.4.x86_64                           10/15 
     Verifying  : 1:oci-register-machine-0-3.11.gitdd0daef.el7.x86_64        11/15 
     Verifying  : 2:docker-common-1.12.6-16.el7.centos.x86_64                12/15 
     Verifying  : yajl-2.0.4-4.el7.x86_64                                    13/15 
     Verifying  : 2:docker-client-1.12.6-16.el7.centos.x86_64                14/15 
     Verifying  : libaio-0.3.109-13.el7.x86_64                               15/15 

   Installed:
     docker.x86_64 2:1.12.6-16.el7.centos                                          

   Dependency Installed:
     container-selinux.noarch 2:2.10-2.el7                                         
     device-mapper-event.x86_64 7:1.02.135-1.el7_3.4                               
     device-mapper-event-libs.x86_64 7:1.02.135-1.el7_3.4                          
     device-mapper-persistent-data.x86_64 0:0.6.3-1.el7                            
     docker-client.x86_64 2:1.12.6-16.el7.centos                                   
     docker-common.x86_64 2:1.12.6-16.el7.centos                                   
     libaio.x86_64 0:0.3.109-13.el7                                                
     libseccomp.x86_64 0:2.3.1-2.el7                                               
     lvm2.x86_64 7:2.02.166-1.el7_3.4                                              
     lvm2-libs.x86_64 7:2.02.166-1.el7_3.4                                         
     oci-register-machine.x86_64 1:0-3.11.gitdd0daef.el7                           
     oci-systemd-hook.x86_64 1:0.1.7-2.git2788078.el7                              
     skopeo-containers.x86_64 1:0.1.18-1.el7                                       
     yajl.x86_64 0:2.0.4-4.el7                                                     

   Complete!

3. Dockerサービスを起動する。

.. sourcecode:: bash

   [centos@ip-172-31-31-80 ~]$ sudo systemctl enable docker.service
   Created symlink from /etc/systemd/system/multi-user.target.wants/docker.service to /usr/lib/systemd/system/docker.service.

   [centos@ip-172-31-31-80 ~]$ sudo systemctl start docker.service


.. note:: 
   dockerコマンドを利用する際は `sudoが必要。 <https://docs.docker.com/installation/ubuntulinux/#create-a-docker-group>`_ 
   sudoなしでdockerコマンドを実行するには、dockerグループを作成し、実行ユーザを追加すれば良い。

   .. sourcecode:: bash

      [centos@ip-XXX-XXX-XXX-XXX ~]$ sudo groupadd docker
      [centos@ip-XXX-XXX-XXX-XXX ~]$ sudo gpasswd -a $USER docker
      Adding user centos to group docker
      [centos@ip-XXX-XXX-XXX-XXX ~]$ sudo systemctl restart docker
      [centos@ip-XXX-XXX-XXX-XXX ~]$ exit



.. note:: 

   RHEL7では、デフォルトではパッケージが用意されていないため、ExtraChanelの登録が必要な模様。

   .. sourcecode:: bash

      [ec2-user@ip-XXX-XXX-XXX-XXX ~]$ sudo yum -y install docker
      Loaded plugins: amazon-id, rhui-lb, search-disabled-repos
      rhui-REGION-client-config-server-7                       | 2.9 kB     00:00     
      rhui-REGION-rhel-server-releases                         | 3.5 kB     00:00     
      rhui-REGION-rhel-server-rh-common                        | 3.8 kB     00:00     
      (1/7): rhui-REGION-client-config-server-7/x86_64/primary_d | 5.5 kB   00:00     
      (2/7): rhui-REGION-rhel-server-releases/7Server/x86_64/gro | 701 kB   00:00     
      (3/7): rhui-REGION-rhel-server-rh-common/7Server/x86_64/gr |  104 B   00:00     
      (4/7): rhui-REGION-rhel-server-rh-common/7Server/x86_64/pr | 118 kB   00:00     
      (5/7): rhui-REGION-rhel-server-rh-common/7Server/x86_64/up |  33 kB   00:00     
      (6/7): rhui-REGION-rhel-server-releases/7Server/x86_64/upd | 1.9 MB   00:00     
      (7/7): rhui-REGION-rhel-server-releases/7Server/x86_64/pri |  35 MB   00:02     
      No package docker available.
      Error: Nothing to do
