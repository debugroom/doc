.. include:: ../module.txt

.. _section3-docker-usage-label:

Usage
======================================================

.. _section3-1-docker-command-label:

Dockerコマンド
------------------------------------------------------

.. _section3-1-1-docker-search-label:

Docker search
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Docker searchはDockerレジストリ(DockerHub)のコンテナイメージを検索するコマンドである。

docker search [OPTIONS] TERM

.. sourcecode:: bash

   Options:

       -f, --filter value   Filter output based on conditions provided (default [])
           --help           Print usage
           --limit int      Max number of search results (default 25)
           --no-index       Don't truncate output
           --no-trunc       Don't truncate output

   [centos@ip-XXX-XXX-XXX-XXX ~]$ docker search centos
	INDEX       NAME                                             DESCRIPTION                                     STARS     OFFICIAL   AUTOMATED
	docker.io   docker.io/centos                                 The official build of CentOS.                   3353      [OK]
	docker.io   docker.io/jdeathe/centos-ssh                     CentOS-6 6.9 x86_64 / CentOS-7 7.3.1611 x8...   69                   [OK]
	docker.io   docker.io/consol/centos-xfce-vnc                 Centos container with "headless" VNC sessi...   26                   [OK]
	docker.io   docker.io/nimmis/java-centos                     This is docker images of CentOS 7 with dif...   26                   [OK]
	docker.io   docker.io/gluster/gluster-centos                 Official GlusterFS Image [ CentOS-7 +  Glu...   19                   [OK]
	docker.io   docker.io/million12/centos-supervisor            Base CentOS-7 with supervisord launcher, h...   16                   [OK]
	docker.io   docker.io/kinogmt/centos-ssh                     CentOS with SSH                                 13                   [OK]
	docker.io   docker.io/egyptianbman/docker-centos-nginx-php   A simple and highly configurable docker co...   9                    [OK]
	docker.io   docker.io/torusware/speedus-centos               Always updated official CentOS docker imag...   8                    [OK]
	docker.io   docker.io/centos/mariadb55-centos7                                                               5                    [OK]
	docker.io   docker.io/nathonfowlie/centos-jre                Latest CentOS image with the JRE pre-insta...   5                    [OK]
	docker.io   docker.io/darksheer/centos                       Base Centos Image -- Updated hourly             2                    [OK]
	docker.io   docker.io/harisekhon/centos-java                 Java on CentOS (OpenJDK, tags jre/jdk7-8)       2                    [OK]
	docker.io   docker.io/harisekhon/centos-scala                Scala + CentOS (OpenJDK tags 2.10-jre7 - 2...   2                    [OK]
	docker.io   docker.io/blacklabelops/centos                   CentOS Base Image! Built and Updates Daily!     1                    [OK]
	docker.io   docker.io/freenas/centos                         Simple CentOS Linux interactive container       1                    [OK]
	docker.io   docker.io/sgfinans/docker-centos                 CentOS with a running sshd and Docker           1                    [OK]
	docker.io   docker.io/timhughes/centos                       Centos with systemd installed and running       1                    [OK]
	docker.io   docker.io/vorakl/centos                          CentOS7, EPEL, tools. Updated/Tested daily!     1                    [OK]
	docker.io   docker.io/grossws/centos                         CentOS 6 and 7 base images with gosu and l...   0                    [OK]
	docker.io   docker.io/januswel/centos                        yum update-ed CentOS image                      0                    [OK]
	docker.io   docker.io/kz8s/centos                            Official CentOS plus epel-release               0                    [OK]
	docker.io   docker.io/repositoryjp/centos                    Docker Image for CentOS.                        0                    [OK]
	docker.io   docker.io/vcatechnology/centos                   A CentOS Image which is updated daily           0                    [OK]
	docker.io   docker.io/wenjianzhou/centos                     centos                                          0                    [OK]


.. _section3-1-2-docker-pull-label:

Docker pull
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Docker pullはDocker Hubからコンテナイメージ、およびレポジトリを取得するコマンドである。

docker pull [OPTIONS] NAME[:TAG|@DIGEST]

.. sourcecode:: bash

   Options:

       -a, --all-tags                Download all tagged images in the repository
           --disable-content-trust   Skip image verification (default true)
           --help                    Print usage


   [centos@ip-XXX-XXX-XXX-XXX ~]$ docker pull centos
   Using default tag: latest
   Trying to pull repository docker.io/library/centos ...
   latest: Pulling from docker.io/library/centos
   343b09361036: Pull complete
   Digest: sha256:bba1de7c9d900a898e3cadbae040dfe8a633c06bc104a0df76ae24483e03c077


.. _section3-1-3-docker-run-label:

Docker run
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Docker runはコンテナを起動するコマンドである。

docker run [OPTIONS] IMAGE [COMMAND] [ARG...]

.. sourcecode:: bash

	 Options:
	      --add-host value              Add a custom host-to-IP mapping (host:ip) (default [])
	  -a, --attach value                Attach to STDIN, STDOUT or STDERR (default [])
	      --blkio-weight value          Block IO (relative weight), between 10 and 1000
	      --blkio-weight-device value   Block IO weight (relative device weight) (default [])
	      --cap-add value               Add Linux capabilities (default [])
	      --cap-drop value              Drop Linux capabilities (default [])
	      --cgroup-parent string        Optional parent cgroup for the container
	      --cidfile string              Write the container ID to the file
	      --cpu-percent int             CPU percent (Windows only)
	      --cpu-period int              Limit CPU CFS (Completely Fair Scheduler) period
	      --cpu-quota int               Limit CPU CFS (Completely Fair Scheduler) quota
	  -c, --cpu-shares int              CPU shares (relative weight)
	      --cpuset-cpus string          CPUs in which to allow execution (0-3, 0,1)
	      --cpuset-mems string          MEMs in which to allow execution (0-3, 0,1)
	  -d, --detach                      Run container in background and print container ID
	      --detach-keys string          Override the key sequence for detaching a container
	      --device value                Add a host device to the container (default [])
	      --device-read-bps value       Limit read rate (bytes per second) from a device (default [])
	      --device-read-iops value      Limit read rate (IO per second) from a device (default [])
	      --device-write-bps value      Limit write rate (bytes per second) to a device (default [])
	      --device-write-iops value     Limit write rate (IO per second) to a device (default [])
	      --disable-content-trust       Skip image verification (default true)
	      --dns value                   Set custom DNS servers (default [])
	      --dns-opt value               Set DNS options (default [])
	      --dns-search value            Set custom DNS search domains (default [])
	      --entrypoint string           Overwrite the default ENTRYPOINT of the image
	  -e, --env value                   Set environment variables (default [])
	      --env-file value              Read in a file of environment variables (default [])
	      --expose value                Expose a port or a range of ports (default [])
	      --group-add value             Add additional groups to join (default [])
	      --health-cmd string           Command to run to check health
	      --health-interval duration    Time between running the check (default 0s)
	      --health-retries int          Consecutive failures needed to report unhealthy
	      --health-timeout duration     Maximum time to allow one check to run (default 0s)
	      --help                        Print usage
	  -h, --hostname string             Container host name
	  -i, --interactive                 Keep STDIN open even if not attached
	      --io-maxbandwidth string      Maximum IO bandwidth limit for the system drive (Windows only)
	      --io-maxiops uint             Maximum IOps limit for the system drive (Windows only)
	      --ip string                   Container IPv4 address (e.g. 172.30.100.104)
	      --ip6 string                  Container IPv6 address (e.g. 2001:db8::33)
	      --ipc string                  IPC namespace to use
	      --isolation string            Container isolation technology
	      --kernel-memory string        Kernel memory limit
	  -l, --label value                 Set meta data on a container (default [])
	      --label-file value            Read in a line delimited file of labels (default [])
	      --link value                  Add link to another container (default [])
	      --link-local-ip value         Container IPv4/IPv6 link-local addresses (default [])
	      --log-driver string           Logging driver for the container
	      --log-opt value               Log driver options (default [])
	      --mac-address string          Container MAC address (e.g. 92:d0:c6:0a:29:33)
	  -m, --memory string               Memory limit
	      --memory-reservation string   Memory soft limit
	      --memory-swap string          Swap limit equal to memory plus swap: '-1' to enable unlimited swap
	      --memory-swappiness int       Tune container memory swappiness (0 to 100) (default -1)
	      --name string                 Assign a name to the container
	      --network string              Connect a container to a network (default "default")
	      --network-alias value         Add network-scoped alias for the container (default [])
	      --no-healthcheck              Disable any container-specified HEALTHCHECK
	      --oom-kill-disable            Disable OOM Killer
	      --oom-score-adj int           Tune host's OOM preferences (-1000 to 1000)
	      --pid string                  PID namespace to use
	      --pids-limit int              Tune container pids limit (set -1 for unlimited)
	      --privileged                  Give extended privileges to this container
	  -p, --publish value               Publish a container's port(s) to the host (default [])
	  -P, --publish-all                 Publish all exposed ports to random ports
	      --read-only                   Mount the container's root filesystem as read only
	      --restart string              Restart policy to apply when a container exits (default "no")
	      --rm                          Automatically remove the container when it exits
	      --runtime string              Runtime to use for this container
	      --security-opt value          Security Options (default [])
	      --shm-size string             Size of /dev/shm, default value is 64MB
	      --sig-proxy                   Proxy received signals to the process (default true)
	      --stop-signal string          Signal to stop a container, SIGTERM by default (default "SIGTERM")
	      --storage-opt value           Storage driver options for the container (default [])
	      --sysctl value                Sysctl options (default map[])
	      --tmpfs value                 Mount a tmpfs directory (default [])
	  -t, --tty                         Allocate a pseudo-TTY
	      --ulimit value                Ulimit options (default [])
	  -u, --user string                 Username or UID (format: <name|uid>[:<group|gid>])
	      --userns string               User namespace to use
	      --uts string                  UTS namespace to use
	  -v, --volume value                Bind mount a volume (default [])
	      --volume-driver string        Optional volume driver for the container
	      --volumes-from value          Mount volumes from the specified container(s) (default [])
	  -w, --workdir string              Working directory inside the container

   [centos@ip-XXX-XXX-XXX-XXX ~]$ docker run -it --name centos7 centos:latest /bin/bash
   [root@be8d9c7eff66 /]#

   [centos@ip-XXX-XXX-XXX-XXX ~]$ docker run -it -p 8000:80 --name webserver1 test:ver1.0

.. note:: docker runでコンテナを起動した際、以下の通りのデバイスファイルでマウントされる。

   .. sourcecode:: bash

      [root@be8d9c7eff66 /]# cat /etc/centos-release
      CentOS Linux release 7.3.1611 (Core)
      [root@be8d9c7eff66 /]# df -h /
      Filesystem                                                                                      Size  Used Avail Use% Mounted on
      /dev/mapper/docker-202:1-1598-d6f0a6a08d7e1d2946a1d23e9dea4d240ee41c134e5179fee5d683f365cde53c   10G  238M  9.8G   3% /

   また、ネットワーク関連の設定は以下の通りである。

   .. sourcecode:: bash

      [root@be8d9c7eff66 /]# ip a
		1: lo: <LOOPBACK,UP,LOWER_UP> mtu 65536 qdisc noqueue state UNKNOWN qlen 1
		    link/loopback 00:00:00:00:00:00 brd 00:00:00:00:00:00
		    inet 127.0.0.1/8 scope host lo
		       valid_lft forever preferred_lft forever
		    inet6 ::1/128 scope host
		       valid_lft forever preferred_lft forever
		4: eth0@if5: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue state UP
		    link/ether 02:42:ac:11:00:02 brd ff:ff:ff:ff:ff:ff link-netnsid 0
		    inet 172.17.0.2/16 scope global eth0
		       valid_lft forever preferred_lft forever
		    inet6 fe80::42:acff:fe11:2/64 scope link
		       valid_lft forever preferred_lft forever

   実行プロセスは以下の通りである。

   .. sourcecode:: bash

      [root@be8d9c7eff66 /]# ps -ef
		UID        PID  PPID  C STIME TTY          TIME CMD
		root         1     0  0 May25 ?        00:00:00 /bin/bash
		root        71     1  0 03:29 ?        00:00:00 ps -ef

.. note:: CentOS7からはifconfigなどのネットワーク関連コマンドが非推奨となり、代わりにiproute系のコマンドが推奨されることになった。標準のdockerイメージでは、デフォルトではインストールされていなかったため、必要に応じて、導入すること。

   .. sourcecode:: bash

      [root@be8d9c7eff66 /]# yum install iproute
		Loaded plugins: fastestmirror, ovl
		base                                                                                                                         | 3.6 kB  00:00:00
		extras                                                                                                                       | 3.4 kB  00:00:00
		updates                                                                                                                      | 3.4 kB  00:00:00
		(1/4): extras/7/x86_64/primary_db                                                                                            | 151 kB  00:00:00
		(2/4): base/7/x86_64/group_gz                                                                                                | 155 kB  00:00:00
		(3/4): updates/7/x86_64/primary_db                                                                                           | 4.8 MB  00:00:00
		(4/4): base/7/x86_64/primary_db                                                                                              | 5.6 MB  00:00:00
		Determining fastest mirrors
		 * base: ftp.iij.ad.jp
		 * extras: ftp.iij.ad.jp
		 * updates: ftp.iij.ad.jp
		Resolving Dependencies
		--> Running transaction check
		---> Package iproute.x86_64 0:3.10.0-74.el7 will be installed
		--> Processing Dependency: libmnl.so.0(LIBMNL_1.0)(64bit) for package: iproute-3.10.0-74.el7.x86_64
		--> Processing Dependency: libxtables.so.10()(64bit) for package: iproute-3.10.0-74.el7.x86_64
		--> Processing Dependency: libmnl.so.0()(64bit) for package: iproute-3.10.0-74.el7.x86_64
		--> Running transaction check
		---> Package iptables.x86_64 0:1.4.21-17.el7 will be installed
		--> Processing Dependency: libnfnetlink.so.0()(64bit) for package: iptables-1.4.21-17.el7.x86_64
		--> Processing Dependency: libnetfilter_conntrack.so.3()(64bit) for package: iptables-1.4.21-17.el7.x86_64
		---> Package libmnl.x86_64 0:1.0.3-7.el7 will be installed
		--> Running transaction check
		---> Package libnetfilter_conntrack.x86_64 0:1.0.4-2.el7 will be installed
		---> Package libnfnetlink.x86_64 0:1.0.1-4.el7 will be installed
		--> Finished Dependency Resolution

		Dependencies Resolved

		====================================================================================================================================================
		 Package                                       Arch                          Version                              Repository                   Size
		====================================================================================================================================================
		Installing:
		 iproute                                       x86_64                        3.10.0-74.el7                        base                        618 k
		Installing for dependencies:
		 iptables                                      x86_64                        1.4.21-17.el7                        base                        426 k
		 libmnl                                        x86_64                        1.0.3-7.el7                          base                         23 k
		 libnetfilter_conntrack                        x86_64                        1.0.4-2.el7                          base                         53 k
		 libnfnetlink                                  x86_64                        1.0.1-4.el7                          base                         26 k

		Transaction Summary
		====================================================================================================================================================
		Install  1 Package (+4 Dependent packages)

		Total download size: 1.1 M
		Installed size: 3.1 M
		Is this ok [y/d/N]: y
		Downloading packages:
		warning: /var/cache/yum/x86_64/7/base/packages/libnetfilter_conntrack-1.0.4-2.el7.x86_64.rpm: Header V3 RSA/SHA256 Signature, key ID f4a80eb5: NOKEY
		Public key for libnetfilter_conntrack-1.0.4-2.el7.x86_64.rpm is not installed
		(1/5): libnetfilter_conntrack-1.0.4-2.el7.x86_64.rpm                                                                         |  53 kB  00:00:00
		(2/5): iptables-1.4.21-17.el7.x86_64.rpm                                                                                     | 426 kB  00:00:00
		(3/5): libmnl-1.0.3-7.el7.x86_64.rpm                                                                                         |  23 kB  00:00:00
		(4/5): libnfnetlink-1.0.1-4.el7.x86_64.rpm                                                                                   |  26 kB  00:00:00
		(5/5): iproute-3.10.0-74.el7.x86_64.rpm                                                                                      | 618 kB  00:00:00
		----------------------------------------------------------------------------------------------------------------------------------------------------
		Total                                                                                                               3.5 MB/s | 1.1 MB  00:00:00
		Retrieving key from file:///etc/pki/rpm-gpg/RPM-GPG-KEY-CentOS-7
		Importing GPG key 0xF4A80EB5:
		 Userid     : "CentOS-7 Key (CentOS 7 Official Signing Key) <security@centos.org>"
		 Fingerprint: 6341 ab27 53d7 8a78 a7c2 7bb1 24c6 a8a7 f4a8 0eb5
		 Package    : centos-release-7-3.1611.el7.centos.x86_64 (@CentOS)
		 From       : /etc/pki/rpm-gpg/RPM-GPG-KEY-CentOS-7
		Is this ok [y/N]: y
		Running transaction check
		Running transaction test
		Transaction test succeeded
		Running transaction
		  Installing : libmnl-1.0.3-7.el7.x86_64                                                                                                        1/5
		  Installing : libnfnetlink-1.0.1-4.el7.x86_64                                                                                                  2/5
		  Installing : libnetfilter_conntrack-1.0.4-2.el7.x86_64                                                                                        3/5
		  Installing : iptables-1.4.21-17.el7.x86_64                                                                                                    4/5
		  Installing : iproute-3.10.0-74.el7.x86_64                                                                                                     5/5
		  Verifying  : libnetfilter_conntrack-1.0.4-2.el7.x86_64                                                                                        1/5
		  Verifying  : libnfnetlink-1.0.1-4.el7.x86_64                                                                                                  2/5
		  Verifying  : iptables-1.4.21-17.el7.x86_64                                                                                                    3/5
		  Verifying  : libmnl-1.0.3-7.el7.x86_64                                                                                                        4/5
		  Verifying  : iproute-3.10.0-74.el7.x86_64                                                                                                     5/5

		Installed:
		  iproute.x86_64 0:3.10.0-74.el7

		Dependency Installed:
		  iptables.x86_64 0:1.4.21-17.el7   libmnl.x86_64 0:1.0.3-7.el7   libnetfilter_conntrack.x86_64 0:1.0.4-2.el7   libnfnetlink.x86_64 0:1.0.1-4.el7

		Complete!


.. note:: 実行中のコンテナのシェルから抜けるときは、ctl+p+qで、実行元のbashに復帰する。exitで抜けてしまうとコンテナがストップするので注意。


.. _section3-1-4-docker-ps-label:

Docker ps
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Docker psは実行中のコンテナを表示するコマンドである。

docker ps [OPTIONS]

.. sourcecode:: bash

	 Options:
	  -a, --all             Show all containers (default shows just running)
	  -f, --filter value    Filter output based on conditions provided (default [])
	      --format string   Pretty-print containers using a Go template
	      --help            Print usage
	  -n, --last int        Show n last created containers (includes all states) (default -1)
	  -l, --latest          Show the latest created container (includes all states)
	      --no-trunc        Don't truncate output
	  -q, --quiet           Only display numeric IDs
	  -s, --size            Display total file sizes

	[centos@ip-XXX-XXX-XXX-XXX ~]$ docker ps
	CONTAINER ID        IMAGE               COMMAND             CREATED             STATUS              PORTS               NAMES
	be8d9c7eff66        centos:latest       "/bin/bash"         19 hours ago        Up 19 hours                             centos7


.. _section3-1-5-docker-attach-label:

Docker attach
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Docker attachは実行中のコンテナに再接続するコマンドである。

docker attach [OPTIONS] CONTAINER

.. sourcecode:: bash

   Options:
      --detach-keys string   Override the key sequence for detaching a container
      --help                 Print usage
      --no-stdin             Do not attach STDIN
      --sig-proxy            Proxy all received signals to the process (default true)

   [centos@ip-XXX-XXX-XXX-XXX ~]$ docker attach centos7
   [root@be8d9c7eff66 /]#


.. _section3-1-6-docker-stop-label:

Docker stop
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Docker stopは実行中の1つ、もしくはそれ以上のコンテナに停止するコマンドである。

docker stop [OPTIONS] CONTAINER [CONTAINER...]

.. sourcecode:: bash

   Options:
          --help       Print usage
      -t, --time int   Seconds to wait for stop before killing it (default 10)

   [centos@ip-XXX-XXX-XXX-XXX ~]$ docker ps
   CONTAINER ID        IMAGE               COMMAND             CREATED             STATUS              PORTS               NAMES
   be8d9c7eff66        centos:latest       "/bin/bash"         3 days ago          Up 4 minutes                            centos7
   [centos@ip-XXX-XXX-XXX-XXX ~]$ docker stop centos7
   centos7
   [centos@ip-XXX-XXX-XXX-XXX ~]$ docker ps
   CONTAINER ID        IMAGE               COMMAND             CREATED             STATUS              PORTS               NAMES

.. _section3-1-7-docker-start-label:

Docker start
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Docker stopは停止している1つ、もしくはそれ以上のコンテナに起動するコマンドである。

docker start [OPTIONS] CONTAINER [CONTAINER...]

.. sourcecode:: bash

	 Options:
	  -a, --attach               Attach STDOUT/STDERR and forward signals
	      --detach-keys string   Override the key sequence for detaching a container
	      --help                 Print usage
	  -i, --interactive          Attach container's STDIN

	[centos@ip-XXX-XXX-XXX-XXX ~]$ docker ps -a
	CONTAINER ID        IMAGE               COMMAND             CREATED             STATUS                       PORTS               NAMES
	be8d9c7eff66        centos:latest       "/bin/bash"         3 days ago          Exited (137) 4 minutes ago                       centos7
	[centos@ip-XXX-XXX-XXX-XXX ~]$ docker start centos7
	centos7
	[centos@ip-XXX-XXX-XXX-XXX ~]$ docker ps -a
    CONTAINER ID        IMAGE               COMMAND             CREATED             STATUS              PORTS               NAMES
    be8d9c7eff66        centos:latest       "/bin/bash"         3 days ago          Up 12 seconds                           centos7

.. _section3-1-8-docker-commit-label:

Docker commit
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Docker commitは既存のコンテナイメージから新たなイメージを作成するコマンドである。

docker commit [OPTIONS] CONTAINER [REPOSITORY[:TAG]]

.. sourcecode:: bash

	 Options:
	  -a, --author string    Author (e.g., "John Hannibal Smith <hannibal@a-team.com>")
	  -c, --change value     Apply Dockerfile instruction to the created image (default [])
	      --help             Print usage
	  -m, --message string   Commit message
	  -p, --pause            Pause container during commit (default true)

   [centos@ip-XXX-XXX-XXX-XXX ~]$ docker commit centos7 test:ver1.0
   sha256:9ddcf0f1c82dfc725113c59cb76c022622fc6026bb4a99d917f7f2f3360051cc
   [centos@ip-XXX-XXX-XXX-XXX ~]$ docker images
   REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
   test                ver1.0              9ddcf0f1c82d        11 seconds ago      283.2 MB
   docker.io/centos    latest              8140d0c64310        2 weeks ago         192.5 MB

.. _section3-1-9-docker-rm-label:

Docker rm
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Docker rmは１つ、もしくはそれ以上のコンテナを削除するコマンドである。

docker rm [OPTIONS] CONTAINER [CONTAINER...]

.. sourcecode:: bash

	 Options:
	  -f, --force     Force the removal of a running container (uses SIGKILL)
	      --help      Print usage
	  -l, --link      Remove the specified link
	  -v, --volumes   Remove the volumes associated with the container

   [centos@ip-XXX-XXX-XXX-XXX ~]$ docker rm webserver1
   webserver1


.. _section3-1-10-docker-diff-label:

Docker diff
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Docker diffはコンテナのファイルシステムに加えられた変更差分を表示するコマンドである。

docker diff CONTAINER

.. sourcecode:: bash

   Options:
      --help   Print usage

   [centos@ip-XXX-XXX-XXX-XXX ~]$ docker diff webserver1
    C /usr
	C /usr/lib64
	A /usr/lib64/libapr-1.so.0.4.8
	A /usr/lib64/apr-util-1
	A /usr/lib64/httpd
	A /usr/lib64/httpd/modules
	# omit

.. _section3-1-11-docker-cp-label:

Docker cp
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Docker cpはコンテナのファイルシステム上のファイルやディレクトリをローカルのファイルシステム間でコピーするコマンドである。

docker cp [OPTIONS] CONTAINER:SRC_PATH DEST_PATH|-
docker cp [OPTIONS] SRC_PATH|- CONTAINER:DEST_PATH

.. sourcecode:: bash

	Options:
	  -L, --follow-link   Always follow symbol link in SRC_PATH
	      --help          Print usage

   [centos@ip-XXX-XXX-XXX-XXX ~]$ docker cp webserver1:/var/log/httpd /tmp/

.. _section3-1-12-docker-login-label:

Docker login
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Docker login はDockerレジストリにログインするためのコマンドである。

docker login [OPTIONS] [SERVER]

.. sourcecode:: bash

	 Options:
	      --help              Print usage
	  -p, --password string   Password
	  -u, --username string   Username

   [centos@ip-XXX-XXX-XXX-XXX ~]$ docker login
   Login with your Docker ID to push and pull images from Docker Hub. If you dont have a Docker ID, head over to https://hub.docker.com to create one.
   Username: xxxxxxx
   Password:
   Login Succeeded

.. _section3-1-13-docker-push-label:

Docker push
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Docker pushはコンテナイメージやレポジトリを、レジストリにプッシュするコマンドである。

docker push [OPTIONS] NAME[:TAG]

.. sourcecode:: bash

	 Options:
	      --disable-content-trust   Skip image verification (default true)
	      --help                    Print usage

	[centos@ip-XXX-XXX-XXX-XXX ~]$ docker push debugroom/test
	The push refers to a repository [docker.io/debugroom/test]
	05064569aaee: Pushed
	b51149973e6a: Mounted from library/centos
	ver1.0: digest: sha256:c462439235fc131b4caacab2f8b71094bc096473762da6fd2841ec4d008ed4e9 size: 741


.. _section3-1-14-docker-build-label:

Docker build
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Docker buildはDockerfileからイメージを作成するコマンドである。

docker build [OPTIONS] PATH | URL | -

.. sourcecode:: bash

	 Options:
	      --build-arg value         Set build-time variables (default [])
	      --cgroup-parent string    Optional parent cgroup for the container
	      --cpu-period int          Limit the CPU CFS (Completely Fair Scheduler) period
	      --cpu-quota int           Limit the CPU CFS (Completely Fair Scheduler) quota
	  -c, --cpu-shares int          CPU shares (relative weight)
	      --cpuset-cpus string      CPUs in which to allow execution (0-3, 0,1)
	      --cpuset-mems string      MEMs in which to allow execution (0-3, 0,1)
	      --disable-content-trust   Skip image verification (default true)
	  -f, --file string             Name of the Dockerfile (Default is 'PATH/Dockerfile')
	      --force-rm                Always remove intermediate containers
	      --help                    Print usage
	      --isolation string        Container isolation technology
	      --label value             Set metadata for an image (default [])
	  -m, --memory string           Memory limit
	      --memory-swap string      Swap limit equal to memory plus swap: '-1' to enable unlimited swap
	      --no-cache                Do not use cache when building the image
	      --pull                    Always attempt to pull a newer version of the image
	  -q, --quiet                   Suppress the build output and print image ID on success
	      --rm                      Remove intermediate containers after a successful build (default true)
	      --shm-size string         Size of /dev/shm, default value is 64MB
	  -t, --tag value               Name and optionally a tag in the 'name:tag' format (default [])
	      --ulimit value            Ulimit options (default [])
	  -v, --volume value            Set build-time bind mounts (default [])

   [centos@ip-XXX-XXX-XXX-XXX ~]$ docker build -t debugroom/test:ver1.1 ~/build_test

.. _section3-1-15-docker-rmi-label:

Docker rmi
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Docker rmiは1つ、またはそれ以上のイメージを削除するコマンドである。

docker rmi [OPTIONS] IMAGE [IMAGE...]

.. sourcecode:: bash

	Options:
	  -f, --force      Force removal of the image
	      --help       Print usage
	      --no-prune   Do not delete untagged parents

	[centos@ip-XXX-XXX-XXX-XXX ~]$ docker rmi  f89bd8b7b355
	Deleted: sha256:f89bd8b7b35513aa6386d41673b3e91b0ca342f0dcedb0460a5f06abfbccdc16
	Deleted: sha256:a40107948975217080bff86dba927314ea4ae6ebe4b967b06ad2d1269f74c277

.. note:: イメージを削除する場合、コンテナが実行(登録)されていると削除できないため、docker ps -aで該当のコンテナを探し、docker rmで削除しておく。

.. _section3-1-16-docker-top-label:

Docker top
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Docker topはコンテナ内の実行プロセスを表示するコマンドである。

docker top CONTAINER [ps OPTIONS]

.. sourcecode:: bash

	 Options:
	      --help   Print usage

	[centos@ip-XXX-XXX-XXX-XXX ~]$ docker top webserver2
	UID                 PID                 PPID                C                   STIME               TTY                 TIME                CMD
	root                17251               17237               0                   15:17               ?                   00:00:00            /usr/sbin/init
	root                17282               17251               0                   15:17               ?                   00:00:00            /usr/lib/systemd/systemd-journald
	root                17297               17251               0                   15:17               ?                   00:00:00            /usr/lib/systemd/systemd-udevd
	root                17340               17251               0                   15:17               ?                   00:00:00            /usr/sbin/httpd -DFOREGROUND
	root                17342               17251               0                   15:17               ?                   00:00:00            /usr/lib/systemd/systemd-logind
	dbus                17343               17251               0                   15:17               ?                   00:00:00            /bin/dbus-daemon --system --address=systemd: --nofork --nopidfile --systemd-activation
	48                  17375               17340               0                   15:17               ?                   00:00:00            /usr/sbin/httpd -DFOREGROUND
	48                  17376               17340               0                   15:17               ?                   00:00:00            /usr/sbin/httpd -DFOREGROUND
	48                  17377               17340               0                   15:17               ?                   00:00:00            /usr/sbin/httpd -DFOREGROUND
	48                  17378               17340               0                   15:17               ?                   00:00:00            /usr/sbin/httpd -DFOREGROUND
	48                  17379               17340               0                   15:17               ?                   00:00:00            /usr/sbin/httpd -DFOREGROUND
	root                17423               17251               0                   15:17               ?                   00:00:00            (agetty)


.. _section3-1-17-docker-logs-label:

Docker logs
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Docker logsは指定したコンテナの標準出力を表示させるコマンドである。

docker logs [OPTIONS] CONTAINER

.. sourcecode:: bash

	 Fetch the logs of a container

	 Options:
	      --details        Show extra details provided to logs
	  -f, --follow         Follow log output
	      --help           Print usage
	      --since string   Show logs since timestamp
	      --tail string    Number of lines to show from the end of the logs (default "all")
	  -t, --timestamps     Show timestamps

   [centos@ip-XXX-XXX-XXX-XXX ~]$ docker logs apserver


.. _section3-1-18-docker-images-label:

Docker images
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Docker imagesはコンテナイメージの一覧を出力するコマンドである。

docker images [OPTIONS] [REPOSITORY[:TAG]]

.. sourcecode:: bash

   List images.

   Options:
     -a, --all             Show all images (default hides intermediate images)
         --digests         Show digests
     -f, --filter value    Filter output based on conditions provided (default [])
         --format string   Pretty-print images using a Go template
         --help            Print usage
         --no-trunc        Don't truncate output
     -q, --quiet           Only show numeric IDs

.. _section3-2-docker-app-environment-label:

アプリケーション実行環境の構築
------------------------------------------------------

.. _section3-2-1-docker-web-server-label:

Webサーバの構築
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

DockerコンテナでWebサーバの構築を行う。centosイメージで新たにWebサーバ用のコンテナイメージを作成する。

.. sourcecode:: bash

   # docker commitにて、新しくイメージを作成
   [centos@ip-XXX-XXX-XXX-XXX ~]$ docker commit centos7 test:ver1.0
   sha256:9ddcf0f1c82dfc725113c59cb76c022622fc6026bb4a99d917f7f2f3360051cc
   [centos@ip-XXX-XXX-XXX-XXX ~]$ docker images
   REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
   test                ver1.0              9ddcf0f1c82d        11 seconds ago      283.2 MB
   docker.io/centos    latest              8140d0c64310        2 weeks ago         192.5 MB

   # docker runにて、webserver1 という名前で、8000ポートを指定すると、コンテナの80ポートへリダイレクトするよう、コンテナイメージを実行。
   [centos@ip-XXX-XXX-XXX-XXX ~]$ docker run -d --privileged -p 8000:80 --name webserver1 test:ver1.0 /sbin/init
   a54c0b51062298797283194edd7ad7753fd92c507a0735488ae750f95b488316
   [centos@ip-XXX-XXX-XXX-XXX ~]$ docker exec -it webserver1 /bin/bash
   [root@a54c0b510622 /]#

   # Webサーバのインストール
   [root@a54c0b510622 /]# yum install httpd -y
	Loaded plugins: fastestmirror, ovl
	base                                                                                                      | 3.6 kB  00:00:00
	extras                                                                                                    | 3.4 kB  00:00:00
	updates                                                                                                   | 3.4 kB  00:00:00
	(1/2): extras/7/x86_64/primary_db                                                                         | 167 kB  00:00:00
	(2/2): updates/7/x86_64/primary_db                                                                        | 5.6 MB  00:00:00
	Loading mirror speeds from cached hostfile
	 * base: ftp.iij.ad.jp
	 * extras: ftp.iij.ad.jp
	 * updates: ftp.iij.ad.jp
	Resolving Dependencies
	--> Running transaction check
	---> Package httpd.x86_64 0:2.4.6-45.el7.centos.4 will be installed
	--> Processing Dependency: httpd-tools = 2.4.6-45.el7.centos.4 for package: httpd-2.4.6-45.el7.centos.4.x86_64
	--> Processing Dependency: system-logos >= 7.92.1-1 for package: httpd-2.4.6-45.el7.centos.4.x86_64
	--> Processing Dependency: /etc/mime.types for package: httpd-2.4.6-45.el7.centos.4.x86_64
	--> Processing Dependency: libaprutil-1.so.0()(64bit) for package: httpd-2.4.6-45.el7.centos.4.x86_64
	--> Processing Dependency: libapr-1.so.0()(64bit) for package: httpd-2.4.6-45.el7.centos.4.x86_64
	--> Running transaction check
	---> Package apr.x86_64 0:1.4.8-3.el7 will be installed
	---> Package apr-util.x86_64 0:1.5.2-6.el7 will be installed
	---> Package centos-logos.noarch 0:70.0.6-3.el7.centos will be installed
	---> Package httpd-tools.x86_64 0:2.4.6-45.el7.centos.4 will be installed
	---> Package mailcap.noarch 0:2.1.41-2.el7 will be installed
	--> Finished Dependency Resolution

	Dependencies Resolved

	=================================================================================================================================
	 Package                        Arch                     Version                                 Repository                 Size
	=================================================================================================================================
	Installing:
	 httpd                          x86_64                   2.4.6-45.el7.centos.4                   updates                   2.7 M
	Installing for dependencies:
	 apr                            x86_64                   1.4.8-3.el7                             base                      103 k
	 apr-util                       x86_64                   1.5.2-6.el7                             base                       92 k
	 centos-logos                   noarch                   70.0.6-3.el7.centos                     base                       21 M
	 httpd-tools                    x86_64                   2.4.6-45.el7.centos.4                   updates                    84 k
	 mailcap                        noarch                   2.1.41-2.el7                            base                       31 k

	Transaction Summary
	=================================================================================================================================
	Install  1 Package (+5 Dependent packages)

	Total download size: 24 M
	Installed size: 32 M
	Downloading packages:
	(1/6): apr-util-1.5.2-6.el7.x86_64.rpm                                                                    |  92 kB  00:00:00
	(2/6): httpd-tools-2.4.6-45.el7.centos.4.x86_64.rpm                                                       |  84 kB  00:00:00
	(3/6): apr-1.4.8-3.el7.x86_64.rpm                                                                         | 103 kB  00:00:00
	(4/6): mailcap-2.1.41-2.el7.noarch.rpm                                                                    |  31 kB  00:00:00
	(5/6): httpd-2.4.6-45.el7.centos.4.x86_64.rpm                                                             | 2.7 MB  00:00:00
	(6/6): centos-logos-70.0.6-3.el7.centos.noarch.rpm                                                        |  21 MB  00:00:00
	---------------------------------------------------------------------------------------------------------------------------------
	Total                                                                                             28 MB/s |  24 MB  00:00:00
	Running transaction check
	Running transaction test
	Transaction test succeeded
	Running transaction
	  Installing : apr-1.4.8-3.el7.x86_64                                                                                        1/6
	  Installing : apr-util-1.5.2-6.el7.x86_64                                                                                   2/6
	  Installing : httpd-tools-2.4.6-45.el7.centos.4.x86_64                                                                      3/6
	  Installing : centos-logos-70.0.6-3.el7.centos.noarch                                                                       4/6
	  Installing : mailcap-2.1.41-2.el7.noarch                                                                                   5/6
	  Installing : httpd-2.4.6-45.el7.centos.4.x86_64                                                                            6/6
	  Verifying  : apr-1.4.8-3.el7.x86_64                                                                                        1/6
	  Verifying  : mailcap-2.1.41-2.el7.noarch                                                                                   2/6
	  Verifying  : apr-util-1.5.2-6.el7.x86_64                                                                                   3/6
	  Verifying  : httpd-tools-2.4.6-45.el7.centos.4.x86_64                                                                      4/6
	  Verifying  : httpd-2.4.6-45.el7.centos.4.x86_64                                                                            5/6
	  Verifying  : centos-logos-70.0.6-3.el7.centos.noarch                                                                       6/6

	Installed:
	  httpd.x86_64 0:2.4.6-45.el7.centos.4

	Dependency Installed:
	  apr.x86_64 0:1.4.8-3.el7                       apr-util.x86_64 0:1.5.2-6.el7     centos-logos.noarch 0:70.0.6-3.el7.centos
	  httpd-tools.x86_64 0:2.4.6-45.el7.centos.4     mailcap.noarch 0:2.1.41-2.el7

	Complete!
   [root@a54c0b510622 /]#

   # HTMLページを作成する。
   [root@a54c0b510622 /]# echo '<h1>Hello, Docker!</h1>' > /var/www/html/index.html

   # Webサーバを起動
   [root@a54c0b510622 /]# systemctl enable httpd.service
    Created symlink from /etc/systemd/system/multi-user.target.wants/httpd.service to /usr/lib/systemd/system/httpd.service.
   [root@a54c0b510622 /]# systemctl start httpd.service
   [root@a54c0b510622 /]#

   # コンテナの外から8000ポートを指定してアクセス。
   [centos@ip-XXX-XXX-XXX-XXX ~]$ curl http://localhost:8000
   <h1>Hello, Docker!</h1>

.. _section3-2-4-docker-web-server-label:

Cassandraクラスタサーバの構築
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

DockerコンテナでCassandraクラスタの構築を行う。centos7で新たにコンテナイメージを作成する。

.. sourcecode:: bash

    # CentOS7のコンテナを起動
	[centos@ip-XXX-XXX-XXX-XXX ~]$ docker run -d --privileged --name cassandra-server1 centos:latest /sbin/init
	5a8bd32a59adf502b61599214fee16075fe10fe9a2bab564e4be437f1f1e7039
	[centos@ip-XXX-XXX-XXX-XXX ~]$ docker exec -ti cassandra-server1 /bin/bash
	# Java8とiprouteのインストール
	[root@5a8bd32a59ad /]# yum install -y java-1.8.0-openjdk java-1.8.0.openjdk-devel iproute
	Loaded plugins: fastestmirror, ovl
	base                                                                            | 3.6 kB  00:00:00
	extras                                                                          | 3.4 kB  00:00:00
	updates                                                                         | 3.4 kB  00:00:00
	(1/4): extras/7/x86_64/primary_db                                               | 167 kB  00:00:00
	(2/4): base/7/x86_64/group_gz                                                   | 155 kB  00:00:00
	(3/4): updates/7/x86_64/primary_db                                              | 5.6 MB  00:00:00
	(4/4): base/7/x86_64/primary_db                                                 | 5.6 MB  00:00:02
	Determining fastest mirrors
	 * base: ftp.iij.ad.jp
	 * extras: ftp.iij.ad.jp
	 * updates: ftp.iij.ad.jp
	No package java-1.8.0.openjdk-devel available.
	Resolving Dependencies
	--> Running transaction check
	---> Package iproute.x86_64 0:3.10.0-74.el7 will be installed
	--> Processing Dependency: libmnl.so.0(LIBMNL_1.0)(64bit) for package: iproute-3.10.0-74.el7.x86_64
	--> Processing Dependency: libxtables.so.10()(64bit) for package: iproute-3.10.0-74.el7.x86_64
	--> Processing Dependency: libmnl.so.0()(64bit) for package: iproute-3.10.0-74.el7.x86_64
	---> Package java-1.8.0-openjdk.x86_64 1:1.8.0.131-3.b12.el7_3 will be installed
	--> Processing Dependency: java-1.8.0-openjdk-headless = 1:1.8.0.131-3.b12.el7_3 for package: 1:java-1.8.0-openjdk-1.8.0.131-3.b12.el7_3.x86_64
	--> Processing Dependency: xorg-x11-fonts-Type1 for package: 1:java-1.8.0-openjdk-1.8.0.131-3.b12.el7_3.x86_64
	--> Processing Dependency: libpng15.so.15(PNG15_0)(64bit) for package: 1:java-1.8.0-openjdk-1.8.0.131-3.b12.el7_3.x86_64
	--> Processing Dependency: libjvm.so(SUNWprivate_1.1)(64bit) for package: 1:java-1.8.0-openjdk-1.8.0.131-3.b12.el7_3.x86_64
	--> Processing Dependency: libjpeg.so.62(LIBJPEG_6.2)(64bit) for package: 1:java-1.8.0-openjdk-1.8.0.131-3.b12.el7_3.x86_64
	--> Processing Dependency: libjli.so(SUNWprivate_1.1)(64bit) for package: 1:java-1.8.0-openjdk-1.8.0.131-3.b12.el7_3.x86_64
	--> Processing Dependency: libjava.so(SUNWprivate_1.1)(64bit) for package: 1:java-1.8.0-openjdk-1.8.0.131-3.b12.el7_3.x86_64
	--> Processing Dependency: libasound.so.2(ALSA_0.9.0rc4)(64bit) for package: 1:java-1.8.0-openjdk-1.8.0.131-3.b12.el7_3.x86_64
	--> Processing Dependency: libasound.so.2(ALSA_0.9)(64bit) for package: 1:java-1.8.0-openjdk-1.8.0.131-3.b12.el7_3.x86_64
	--> Processing Dependency: fontconfig(x86-64) for package: 1:java-1.8.0-openjdk-1.8.0.131-3.b12.el7_3.x86_64
	--> Processing Dependency: libpng15.so.15()(64bit) for package: 1:java-1.8.0-openjdk-1.8.0.131-3.b12.el7_3.x86_64
	--> Processing Dependency: libjvm.so()(64bit) for package: 1:java-1.8.0-openjdk-1.8.0.131-3.b12.el7_3.x86_64
	--> Processing Dependency: libjpeg.so.62()(64bit) for package: 1:java-1.8.0-openjdk-1.8.0.131-3.b12.el7_3.x86_64
	--> Processing Dependency: libjli.so()(64bit) for package: 1:java-1.8.0-openjdk-1.8.0.131-3.b12.el7_3.x86_64
	--> Processing Dependency: libjava.so()(64bit) for package: 1:java-1.8.0-openjdk-1.8.0.131-3.b12.el7_3.x86_64
	--> Processing Dependency: libgif.so.4()(64bit) for package: 1:java-1.8.0-openjdk-1.8.0.131-3.b12.el7_3.x86_64
	--> Processing Dependency: libawt.so()(64bit) for package: 1:java-1.8.0-openjdk-1.8.0.131-3.b12.el7_3.x86_64
	--> Processing Dependency: libasound.so.2()(64bit) for package: 1:java-1.8.0-openjdk-1.8.0.131-3.b12.el7_3.x86_64
	--> Processing Dependency: libXtst.so.6()(64bit) for package: 1:java-1.8.0-openjdk-1.8.0.131-3.b12.el7_3.x86_64
	--> Processing Dependency: libXrender.so.1()(64bit) for package: 1:java-1.8.0-openjdk-1.8.0.131-3.b12.el7_3.x86_64
	--> Processing Dependency: libXi.so.6()(64bit) for package: 1:java-1.8.0-openjdk-1.8.0.131-3.b12.el7_3.x86_64
	--> Processing Dependency: libXext.so.6()(64bit) for package: 1:java-1.8.0-openjdk-1.8.0.131-3.b12.el7_3.x86_64
	--> Processing Dependency: libXcomposite.so.1()(64bit) for package: 1:java-1.8.0-openjdk-1.8.0.131-3.b12.el7_3.x86_64
	--> Processing Dependency: libX11.so.6()(64bit) for package: 1:java-1.8.0-openjdk-1.8.0.131-3.b12.el7_3.x86_64
	--> Running transaction check
	---> Package alsa-lib.x86_64 0:1.1.1-1.el7 will be installed
	---> Package fontconfig.x86_64 0:2.10.95-10.el7 will be installed
	--> Processing Dependency: freetype for package: fontconfig-2.10.95-10.el7.x86_64
	--> Processing Dependency: fontpackages-filesystem for package: fontconfig-2.10.95-10.el7.x86_64
	--> Processing Dependency: libfreetype.so.6()(64bit) for package: fontconfig-2.10.95-10.el7.x86_64
	---> Package giflib.x86_64 0:4.1.6-9.el7 will be installed
	--> Processing Dependency: libSM.so.6()(64bit) for package: giflib-4.1.6-9.el7.x86_64
	--> Processing Dependency: libICE.so.6()(64bit) for package: giflib-4.1.6-9.el7.x86_64
	---> Package iptables.x86_64 0:1.4.21-17.el7 will be installed
	--> Processing Dependency: libnfnetlink.so.0()(64bit) for package: iptables-1.4.21-17.el7.x86_64
	--> Processing Dependency: libnetfilter_conntrack.so.3()(64bit) for package: iptables-1.4.21-17.el7.x86_64
	---> Package java-1.8.0-openjdk-headless.x86_64 1:1.8.0.131-3.b12.el7_3 will be installed
	--> Processing Dependency: tzdata-java >= 2015d for package: 1:java-1.8.0-openjdk-headless-1.8.0.131-3.b12.el7_3.x86_64
	--> Processing Dependency: copy-jdk-configs >= 1.1-3 for package: 1:java-1.8.0-openjdk-headless-1.8.0.131-3.b12.el7_3.x86_64
	--> Processing Dependency: lksctp-tools(x86-64) for package: 1:java-1.8.0-openjdk-headless-1.8.0.131-3.b12.el7_3.x86_64
	--> Processing Dependency: jpackage-utils for package: 1:java-1.8.0-openjdk-headless-1.8.0.131-3.b12.el7_3.x86_64
	---> Package libX11.x86_64 0:1.6.3-3.el7 will be installed
	--> Processing Dependency: libX11-common >= 1.6.3-3.el7 for package: libX11-1.6.3-3.el7.x86_64
	--> Processing Dependency: libxcb.so.1()(64bit) for package: libX11-1.6.3-3.el7.x86_64
	---> Package libXcomposite.x86_64 0:0.4.4-4.1.el7 will be installed
	---> Package libXext.x86_64 0:1.3.3-3.el7 will be installed
	---> Package libXi.x86_64 0:1.7.4-2.el7 will be installed
	---> Package libXrender.x86_64 0:0.9.8-2.1.el7 will be installed
	---> Package libXtst.x86_64 0:1.2.2-2.1.el7 will be installed
	---> Package libjpeg-turbo.x86_64 0:1.2.90-5.el7 will be installed
	---> Package libmnl.x86_64 0:1.0.3-7.el7 will be installed
	---> Package libpng.x86_64 2:1.5.13-7.el7_2 will be installed
	---> Package xorg-x11-fonts-Type1.noarch 0:7.5-9.el7 will be installed
	--> Processing Dependency: ttmkfdir for package: xorg-x11-fonts-Type1-7.5-9.el7.noarch
	--> Processing Dependency: ttmkfdir for package: xorg-x11-fonts-Type1-7.5-9.el7.noarch
	--> Processing Dependency: mkfontdir for package: xorg-x11-fonts-Type1-7.5-9.el7.noarch
	--> Processing Dependency: mkfontdir for package: xorg-x11-fonts-Type1-7.5-9.el7.noarch
	--> Running transaction check
	---> Package copy-jdk-configs.noarch 0:1.2-1.el7 will be installed
	---> Package fontpackages-filesystem.noarch 0:1.44-8.el7 will be installed
	---> Package freetype.x86_64 0:2.4.11-12.el7 will be installed
	---> Package javapackages-tools.noarch 0:3.4.1-11.el7 will be installed
	--> Processing Dependency: python-javapackages = 3.4.1-11.el7 for package: javapackages-tools-3.4.1-11.el7.noarch
	--> Processing Dependency: libxslt for package: javapackages-tools-3.4.1-11.el7.noarch
	---> Package libICE.x86_64 0:1.0.9-2.el7 will be installed
	---> Package libSM.x86_64 0:1.2.2-2.el7 will be installed
	---> Package libX11-common.noarch 0:1.6.3-3.el7 will be installed
	---> Package libnetfilter_conntrack.x86_64 0:1.0.6-1.el7_3 will be installed
	---> Package libnfnetlink.x86_64 0:1.0.1-4.el7 will be installed
	---> Package libxcb.x86_64 0:1.11-4.el7 will be installed
	--> Processing Dependency: libXau.so.6()(64bit) for package: libxcb-1.11-4.el7.x86_64
	---> Package lksctp-tools.x86_64 0:1.0.17-2.el7 will be installed
	---> Package ttmkfdir.x86_64 0:3.0.9-42.el7 will be installed
	---> Package tzdata-java.noarch 0:2017b-1.el7 will be installed
	---> Package xorg-x11-font-utils.x86_64 1:7.5-20.el7 will be installed
	--> Processing Dependency: libfontenc.so.1()(64bit) for package: 1:xorg-x11-font-utils-7.5-20.el7.x86_64
	--> Processing Dependency: libXfont.so.1()(64bit) for package: 1:xorg-x11-font-utils-7.5-20.el7.x86_64
	--> Running transaction check
	---> Package libXau.x86_64 0:1.0.8-2.1.el7 will be installed
	---> Package libXfont.x86_64 0:1.5.1-2.el7 will be installed
	---> Package libfontenc.x86_64 0:1.1.2-3.el7 will be installed
	---> Package libxslt.x86_64 0:1.1.28-5.el7 will be installed
	---> Package python-javapackages.noarch 0:3.4.1-11.el7 will be installed
	--> Processing Dependency: python-lxml for package: python-javapackages-3.4.1-11.el7.noarch
	--> Running transaction check
	---> Package python-lxml.x86_64 0:3.2.1-4.el7 will be installed
	--> Finished Dependency Resolution

	Dependencies Resolved

	=======================================================================================================
	 Package                            Arch          Version                         Repository      Size
	=======================================================================================================
	Installing:
	 iproute                            x86_64        3.10.0-74.el7                   base           618 k
	 java-1.8.0-openjdk                 x86_64        1:1.8.0.131-3.b12.el7_3         updates        233 k
	Installing for dependencies:
	 alsa-lib                           x86_64        1.1.1-1.el7                     base           415 k
	 copy-jdk-configs                   noarch        1.2-1.el7                       base            14 k
	 fontconfig                         x86_64        2.10.95-10.el7                  base           229 k
	 fontpackages-filesystem            noarch        1.44-8.el7                      base           9.9 k
	 freetype                           x86_64        2.4.11-12.el7                   base           391 k
	 giflib                             x86_64        4.1.6-9.el7                     base            40 k
	 iptables                           x86_64        1.4.21-17.el7                   base           426 k
	 java-1.8.0-openjdk-headless        x86_64        1:1.8.0.131-3.b12.el7_3         updates         31 M
	 javapackages-tools                 noarch        3.4.1-11.el7                    base            73 k
	 libICE                             x86_64        1.0.9-2.el7                     base            65 k
	 libSM                              x86_64        1.2.2-2.el7                     base            39 k
	 libX11                             x86_64        1.6.3-3.el7                     base           606 k
	 libX11-common                      noarch        1.6.3-3.el7                     base           162 k
	 libXau                             x86_64        1.0.8-2.1.el7                   base            29 k
	 libXcomposite                      x86_64        0.4.4-4.1.el7                   base            22 k
	 libXext                            x86_64        1.3.3-3.el7                     base            39 k
	 libXfont                           x86_64        1.5.1-2.el7                     base           150 k
	 libXi                              x86_64        1.7.4-2.el7                     base            40 k
	 libXrender                         x86_64        0.9.8-2.1.el7                   base            25 k
	 libXtst                            x86_64        1.2.2-2.1.el7                   base            20 k
	 libfontenc                         x86_64        1.1.2-3.el7                     base            30 k
	 libjpeg-turbo                      x86_64        1.2.90-5.el7                    base           134 k
	 libmnl                             x86_64        1.0.3-7.el7                     base            23 k
	 libnetfilter_conntrack             x86_64        1.0.6-1.el7_3                   updates         55 k
	 libnfnetlink                       x86_64        1.0.1-4.el7                     base            26 k
	 libpng                             x86_64        2:1.5.13-7.el7_2                base           213 k
	 libxcb                             x86_64        1.11-4.el7                      base           189 k
	 libxslt                            x86_64        1.1.28-5.el7                    base           242 k
	 lksctp-tools                       x86_64        1.0.17-2.el7                    base            88 k
	 python-javapackages                noarch        3.4.1-11.el7                    base            31 k
	 python-lxml                        x86_64        3.2.1-4.el7                     base           758 k
	 ttmkfdir                           x86_64        3.0.9-42.el7                    base            48 k
	 tzdata-java                        noarch        2017b-1.el7                     updates        183 k
	 xorg-x11-font-utils                x86_64        1:7.5-20.el7                    base            87 k
	 xorg-x11-fonts-Type1               noarch        7.5-9.el7                       base           521 k

	Transaction Summary
	=======================================================================================================
	Install  2 Packages (+35 Dependent packages)

	Total download size: 37 M
	Installed size: 119 M
	Downloading packages:
	warning: /var/cache/yum/x86_64/7/base/packages/copy-jdk-configs-1.2-1.el7.noarch.rpm: Header V3 RSA/SHA256 Signature, key ID f4a80eb5: NOKEY
	Public key for copy-jdk-configs-1.2-1.el7.noarch.rpm is not installed
	(1/37): copy-jdk-configs-1.2-1.el7.noarch.rpm                                   |  14 kB  00:00:00
	(2/37): alsa-lib-1.1.1-1.el7.x86_64.rpm                                         | 415 kB  00:00:00
	(3/37): fontpackages-filesystem-1.44-8.el7.noarch.rpm                           | 9.9 kB  00:00:00
	(4/37): fontconfig-2.10.95-10.el7.x86_64.rpm                                    | 229 kB  00:00:00
	(5/37): giflib-4.1.6-9.el7.x86_64.rpm                                           |  40 kB  00:00:00
	(6/37): iproute-3.10.0-74.el7.x86_64.rpm                                        | 618 kB  00:00:00
	(7/37): iptables-1.4.21-17.el7.x86_64.rpm                                       | 426 kB  00:00:00
	(8/37): javapackages-tools-3.4.1-11.el7.noarch.rpm                              |  73 kB  00:00:00
	(9/37): libICE-1.0.9-2.el7.x86_64.rpm                                           |  65 kB  00:00:00
	(10/37): libSM-1.2.2-2.el7.x86_64.rpm                                           |  39 kB  00:00:00
	Public key for java-1.8.0-openjdk-1.8.0.131-3.b12.el7_3.x86_64.rpm is not installed
	(11/37): java-1.8.0-openjdk-1.8.0.131-3.b12.el7_3.x86_64.rpm                    | 233 kB  00:00:00
	(12/37): libX11-1.6.3-3.el7.x86_64.rpm                                          | 606 kB  00:00:00
	(13/37): freetype-2.4.11-12.el7.x86_64.rpm                                      | 391 kB  00:00:00
	(14/37): libX11-common-1.6.3-3.el7.noarch.rpm                                   | 162 kB  00:00:00
	(15/37): libXau-1.0.8-2.1.el7.x86_64.rpm                                        |  29 kB  00:00:00
	(16/37): libXcomposite-0.4.4-4.1.el7.x86_64.rpm                                 |  22 kB  00:00:00
	(17/37): libXext-1.3.3-3.el7.x86_64.rpm                                         |  39 kB  00:00:00
	(18/37): libXfont-1.5.1-2.el7.x86_64.rpm                                        | 150 kB  00:00:00
	(19/37): libXi-1.7.4-2.el7.x86_64.rpm                                           |  40 kB  00:00:00
	(20/37): libXrender-0.9.8-2.1.el7.x86_64.rpm                                    |  25 kB  00:00:00
	(21/37): libXtst-1.2.2-2.1.el7.x86_64.rpm                                       |  20 kB  00:00:00
	(22/37): libfontenc-1.1.2-3.el7.x86_64.rpm                                      |  30 kB  00:00:00
	(23/37): libjpeg-turbo-1.2.90-5.el7.x86_64.rpm                                  | 134 kB  00:00:00
	(24/37): libmnl-1.0.3-7.el7.x86_64.rpm                                          |  23 kB  00:00:00
	(25/37): libnfnetlink-1.0.1-4.el7.x86_64.rpm                                    |  26 kB  00:00:00
	(26/37): libpng-1.5.13-7.el7_2.x86_64.rpm                                       | 213 kB  00:00:00
	(27/37): libxcb-1.11-4.el7.x86_64.rpm                                           | 189 kB  00:00:00
	(28/37): libxslt-1.1.28-5.el7.x86_64.rpm                                        | 242 kB  00:00:00
	(29/37): lksctp-tools-1.0.17-2.el7.x86_64.rpm                                   |  88 kB  00:00:00
	(30/37): python-javapackages-3.4.1-11.el7.noarch.rpm                            |  31 kB  00:00:00
	(31/37): ttmkfdir-3.0.9-42.el7.x86_64.rpm                                       |  48 kB  00:00:00
	(32/37): python-lxml-3.2.1-4.el7.x86_64.rpm                                     | 758 kB  00:00:00
	(33/37): java-1.8.0-openjdk-headless-1.8.0.131-3.b12.el7_3.x86_64.rpm           |  31 MB  00:00:00
	(34/37): libnetfilter_conntrack-1.0.6-1.el7_3.x86_64.rpm                        |  55 kB  00:00:00
	(35/37): tzdata-java-2017b-1.el7.noarch.rpm                                     | 183 kB  00:00:00
	(36/37): xorg-x11-font-utils-7.5-20.el7.x86_64.rpm                              |  87 kB  00:00:00
	(37/37): xorg-x11-fonts-Type1-7.5-9.el7.noarch.rpm                              | 521 kB  00:00:00
	-------------------------------------------------------------------------------------------------------
	Total                                                                   25 MB/s |  37 MB  00:00:01
	Retrieving key from file:///etc/pki/rpm-gpg/RPM-GPG-KEY-CentOS-7
	Importing GPG key 0xF4A80EB5:
	 Userid     : "CentOS-7 Key (CentOS 7 Official Signing Key) <security@centos.org>"
	 Fingerprint: 6341 ab27 53d7 8a78 a7c2 7bb1 24c6 a8a7 f4a8 0eb5
	 Package    : centos-release-7-3.1611.el7.centos.x86_64 (@CentOS)
	 From       : /etc/pki/rpm-gpg/RPM-GPG-KEY-CentOS-7
	Running transaction check
	Running transaction test
	Transaction test succeeded
	Running transaction
	  Installing : freetype-2.4.11-12.el7.x86_64                                                      1/37
	  Installing : libnfnetlink-1.0.1-4.el7.x86_64                                                    2/37
	  Installing : libICE-1.0.9-2.el7.x86_64                                                          3/37
	  Installing : libxslt-1.1.28-5.el7.x86_64                                                        4/37
	  Installing : libmnl-1.0.3-7.el7.x86_64                                                          5/37
	  Installing : libfontenc-1.1.2-3.el7.x86_64                                                      6/37
	  Installing : libjpeg-turbo-1.2.90-5.el7.x86_64                                                  7/37
	  Installing : libXfont-1.5.1-2.el7.x86_64                                                        8/37
	  Installing : 1:xorg-x11-font-utils-7.5-20.el7.x86_64                                            9/37
	  Installing : libnetfilter_conntrack-1.0.6-1.el7_3.x86_64                                       10/37
	  Installing : iptables-1.4.21-17.el7.x86_64                                                     11/37
	  Installing : python-lxml-3.2.1-4.el7.x86_64                                                    12/37
	  Installing : python-javapackages-3.4.1-11.el7.noarch                                           13/37
	  Installing : javapackages-tools-3.4.1-11.el7.noarch                                            14/37
	  Installing : libSM-1.2.2-2.el7.x86_64                                                          15/37
	  Installing : ttmkfdir-3.0.9-42.el7.x86_64                                                      16/37
	  Installing : libX11-common-1.6.3-3.el7.noarch                                                  17/37
	  Installing : alsa-lib-1.1.1-1.el7.x86_64                                                       18/37
	  Installing : libXau-1.0.8-2.1.el7.x86_64                                                       19/37
	  Installing : libxcb-1.11-4.el7.x86_64                                                          20/37
	  Installing : libX11-1.6.3-3.el7.x86_64                                                         21/37
	  Installing : libXext-1.3.3-3.el7.x86_64                                                        22/37
	  Installing : libXi-1.7.4-2.el7.x86_64                                                          23/37
	  Installing : libXtst-1.2.2-2.1.el7.x86_64                                                      24/37
	  Installing : giflib-4.1.6-9.el7.x86_64                                                         25/37
	  Installing : libXcomposite-0.4.4-4.1.el7.x86_64                                                26/37
	  Installing : libXrender-0.9.8-2.1.el7.x86_64                                                   27/37
	  Installing : tzdata-java-2017b-1.el7.noarch                                                    28/37
	  Installing : copy-jdk-configs-1.2-1.el7.noarch                                                 29/37
	  Installing : lksctp-tools-1.0.17-2.el7.x86_64                                                  30/37
	  Installing : 1:java-1.8.0-openjdk-headless-1.8.0.131-3.b12.el7_3.x86_64                        31/37
	  Installing : fontpackages-filesystem-1.44-8.el7.noarch                                         32/37
	  Installing : fontconfig-2.10.95-10.el7.x86_64                                                  33/37
	  Installing : xorg-x11-fonts-Type1-7.5-9.el7.noarch                                             34/37
	  Installing : 2:libpng-1.5.13-7.el7_2.x86_64                                                    35/37
	  Installing : 1:java-1.8.0-openjdk-1.8.0.131-3.b12.el7_3.x86_64                                 36/37
	  Installing : iproute-3.10.0-74.el7.x86_64                                                      37/37
	  Verifying  : libXext-1.3.3-3.el7.x86_64                                                         1/37
	  Verifying  : giflib-4.1.6-9.el7.x86_64                                                          2/37
	  Verifying  : libjpeg-turbo-1.2.90-5.el7.x86_64                                                  3/37
	  Verifying  : libfontenc-1.1.2-3.el7.x86_64                                                      4/37
	  Verifying  : libXtst-1.2.2-2.1.el7.x86_64                                                       5/37
	  Verifying  : python-lxml-3.2.1-4.el7.x86_64                                                     6/37
	  Verifying  : libnetfilter_conntrack-1.0.6-1.el7_3.x86_64                                        7/37
	  Verifying  : libxcb-1.11-4.el7.x86_64                                                           8/37
	  Verifying  : 2:libpng-1.5.13-7.el7_2.x86_64                                                     9/37
	  Verifying  : fontpackages-filesystem-1.44-8.el7.noarch                                         10/37
	  Verifying  : ttmkfdir-3.0.9-42.el7.x86_64                                                      11/37
	  Verifying  : 1:java-1.8.0-openjdk-headless-1.8.0.131-3.b12.el7_3.x86_64                        12/37
	  Verifying  : libmnl-1.0.3-7.el7.x86_64                                                         13/37
	  Verifying  : python-javapackages-3.4.1-11.el7.noarch                                           14/37
	  Verifying  : libXcomposite-0.4.4-4.1.el7.x86_64                                                15/37
	  Verifying  : iptables-1.4.21-17.el7.x86_64                                                     16/37
	  Verifying  : libXrender-0.9.8-2.1.el7.x86_64                                                   17/37
	  Verifying  : lksctp-tools-1.0.17-2.el7.x86_64                                                  18/37
	  Verifying  : copy-jdk-configs-1.2-1.el7.noarch                                                 19/37
	  Verifying  : xorg-x11-fonts-Type1-7.5-9.el7.noarch                                             20/37
	  Verifying  : libxslt-1.1.28-5.el7.x86_64                                                       21/37
	  Verifying  : freetype-2.4.11-12.el7.x86_64                                                     22/37
	  Verifying  : tzdata-java-2017b-1.el7.noarch                                                    23/37
	  Verifying  : libICE-1.0.9-2.el7.x86_64                                                         24/37
	  Verifying  : libXfont-1.5.1-2.el7.x86_64                                                       25/37
	  Verifying  : javapackages-tools-3.4.1-11.el7.noarch                                            26/37
	  Verifying  : 1:java-1.8.0-openjdk-1.8.0.131-3.b12.el7_3.x86_64                                 27/37
	  Verifying  : libnfnetlink-1.0.1-4.el7.x86_64                                                   28/37
	  Verifying  : libXi-1.7.4-2.el7.x86_64                                                          29/37
	  Verifying  : libXau-1.0.8-2.1.el7.x86_64                                                       30/37
	  Verifying  : libSM-1.2.2-2.el7.x86_64                                                          31/37
	  Verifying  : alsa-lib-1.1.1-1.el7.x86_64                                                       32/37
	  Verifying  : libX11-1.6.3-3.el7.x86_64                                                         33/37
	  Verifying  : libX11-common-1.6.3-3.el7.noarch                                                  34/37
	  Verifying  : iproute-3.10.0-74.el7.x86_64                                                      35/37
	  Verifying  : fontconfig-2.10.95-10.el7.x86_64                                                  36/37
	  Verifying  : 1:xorg-x11-font-utils-7.5-20.el7.x86_64                                           37/37

	Installed:
	  iproute.x86_64 0:3.10.0-74.el7           java-1.8.0-openjdk.x86_64 1:1.8.0.131-3.b12.el7_3

	Dependency Installed:
	  alsa-lib.x86_64 0:1.1.1-1.el7             copy-jdk-configs.noarch 0:1.2-1.el7
	  fontconfig.x86_64 0:2.10.95-10.el7        fontpackages-filesystem.noarch 0:1.44-8.el7
	  freetype.x86_64 0:2.4.11-12.el7           giflib.x86_64 0:4.1.6-9.el7
	  iptables.x86_64 0:1.4.21-17.el7           java-1.8.0-openjdk-headless.x86_64 1:1.8.0.131-3.b12.el7_3
	  javapackages-tools.noarch 0:3.4.1-11.el7  libICE.x86_64 0:1.0.9-2.el7
	  libSM.x86_64 0:1.2.2-2.el7                libX11.x86_64 0:1.6.3-3.el7
	  libX11-common.noarch 0:1.6.3-3.el7        libXau.x86_64 0:1.0.8-2.1.el7
	  libXcomposite.x86_64 0:0.4.4-4.1.el7      libXext.x86_64 0:1.3.3-3.el7
	  libXfont.x86_64 0:1.5.1-2.el7             libXi.x86_64 0:1.7.4-2.el7
	  libXrender.x86_64 0:0.9.8-2.1.el7         libXtst.x86_64 0:1.2.2-2.1.el7
	  libfontenc.x86_64 0:1.1.2-3.el7           libjpeg-turbo.x86_64 0:1.2.90-5.el7
	  libmnl.x86_64 0:1.0.3-7.el7               libnetfilter_conntrack.x86_64 0:1.0.6-1.el7_3
	  libnfnetlink.x86_64 0:1.0.1-4.el7         libpng.x86_64 2:1.5.13-7.el7_2
	  libxcb.x86_64 0:1.11-4.el7                libxslt.x86_64 0:1.1.28-5.el7
	  lksctp-tools.x86_64 0:1.0.17-2.el7        python-javapackages.noarch 0:3.4.1-11.el7
	  python-lxml.x86_64 0:3.2.1-4.el7          ttmkfdir.x86_64 0:3.0.9-42.el7
	  tzdata-java.noarch 0:2017b-1.el7          xorg-x11-font-utils.x86_64 1:7.5-20.el7
	  xorg-x11-fonts-Type1.noarch 0:7.5-9.el7

	Complete!
	# 環境変数を設定
	[root@5a8bd32a59ad /]# export JAVA_HOME="/etc/alternatives/java_sdk"
	[root@5a8bd32a59ad /]# exit
	[centos@ip-XXX-XXX-XXX-XXX ~]$ docker exec -ti cassandra-server1 /bin/bash
	# datastax.repoを作成する。
	[root@5a8bd32a59ad /]# vi /etc/yum.repos.d/datastax.repo

	[datastax]
	name = DataStax Repo for Apache Cassandra
	baseurl = http://rpm.datastax.com/community
	enabled = 1
	gpgcheck = 0

	# Cassandraのインストール
	[root@5a8bd32a59ad /]# yum -y install dsc30 cassandra30-tools
	Loaded plugins: fastestmirror, ovl
	datastax                                                                        | 2.5 kB  00:00:00
	datastax/primary_db                                                             | 117 kB  00:00:00
	Loading mirror speeds from cached hostfile
	 * base: ftp.iij.ad.jp
	 * extras: ftp.iij.ad.jp
	 * updates: ftp.iij.ad.jp
	Resolving Dependencies
	--> Running transaction check
	---> Package cassandra30-tools.noarch 0:3.0.9-1 will be installed
	--> Processing Dependency: cassandra30 = 3.0.9-1 for package: cassandra30-tools-3.0.9-1.noarch
	---> Package dsc30.noarch 0:3.0.9-1 will be installed
	--> Running transaction check
	---> Package cassandra30.noarch 0:3.0.9-1 will be installed
	--> Finished Dependency Resolution

	Dependencies Resolved

	=======================================================================================================
	 Package                        Arch                Version                Repository             Size
	=======================================================================================================
	Installing:
	 cassandra30-tools              noarch              3.0.9-1                datastax              5.1 k
	 dsc30                          noarch              3.0.9-1                datastax              1.9 k
	Installing for dependencies:
	 cassandra30                    noarch              3.0.9-1                datastax               24 M

	Transaction Summary
	=======================================================================================================
	Install  2 Packages (+1 Dependent package)

	Total download size: 24 M
	Installed size: 31 M
	Downloading packages:
	(1/3): cassandra30-tools-3.0.9-1.noarch.rpm                                     | 5.1 kB  00:00:00
	(2/3): dsc30-3.0.9-1.noarch.rpm                                                 | 1.9 kB  00:00:00
	(3/3): cassandra30-3.0.9-1.noarch.rpm                                           |  24 MB  00:00:02
	-------------------------------------------------------------------------------------------------------
	Total                                                                  9.1 MB/s |  24 MB  00:00:02
	Running transaction check
	Running transaction test
	Transaction test succeeded
	Running transaction
	  Installing : cassandra30-3.0.9-1.noarch                                                          1/3
	  Installing : cassandra30-tools-3.0.9-1.noarch                                                    2/3
	  Installing : dsc30-3.0.9-1.noarch                                                                3/3
	  Verifying  : cassandra30-tools-3.0.9-1.noarch                                                    1/3
	  Verifying  : cassandra30-3.0.9-1.noarch                                                          2/3
	  Verifying  : dsc30-3.0.9-1.noarch                                                                3/3

	Installed:
	  cassandra30-tools.noarch 0:3.0.9-1                       dsc30.noarch 0:3.0.9-1

	Dependency Installed:
	  cassandra30.noarch 0:3.0.9-1

	Complete!

	# Cassandra起動時のメモリオプションを設定。ここでは512M-1Gにする。

	[root@5a8bd32a59ad /]# sed -i s/\#-Xms4G/-Xms512M/g /etc/cassandra/conf/jvm.options
	[root@5a8bd32a59ad /]# sed -i s/\#-Xmx4G/-Xmx1G/g /etc/cassandra/conf/jvm.options

	# cassandra.yamlのクラスタ設定を行う。

	[root@5a8bd32a59ad /]# sed -i s/endpoint_snitch\:\ SimpleSnitch/endpoint_snitch\:\ GossipingPropertyFileSnitch/g /etc/cassandra/conf/cassandra.yaml

	[root@5a8bd32a59ad /]# vi /etc/cassandra/conf/cassandra.yaml

	- seeds: "XXX.XXX.XXX.XXX"
	- listen_address: "XXX.XXX.XXX.XXX"
	- rpc_address: "XXX.XXX.XXX.XXX"
	- auto_bootstrap: false

	[root@5a8bd32a59ad /]# systemctl enable cassandra
	cassandra.service is not a native service, redirecting to /sbin/chkconfig.
	Executing /sbin/chkconfig cassandra on
	[root@5a8bd32a59ad /]# systemctl start cassandra

.. note:: seedsサーバとするIPアドレス、クラスタのIPアドレス、seedサーバならば、auto_bootstrapをfalseに設定すること。例えば、172.17.0.2(シードサーバ)、172.17.0.3(クラスタノード#1)、172.17.0.4(クラスタノード#2)の３台で構成するのであれば、設定は以下の通りとなる。

    [Seedサーバ]
	- seeds: "172.17.0.2"
	- listen_address: "172.17.0.2"
	- rpc_address: "172.17.0.2"
	- auto_bootstrap: false

    [クラスタ#1]
	- seeds: "172.17.0.2"
	- listen_address: "172.17.0.3"
	- rpc_address: "172.17.0.3"

    [クラスタ#2]
	- seeds: "172.17.0.2"
	- listen_address: "172.17.0.4"
	- rpc_address: "172.17.0.4"

	[root@5a8bd32a59ad /]# nodetool status

    .. sourcecode:: bash

	Datacenter: dc1

	Status=Up/Down
	|/ State=Normal/Leaving/Joining/Moving
	--  Address     Load       Tokens       Owns (effective)  Host ID                               Rack
	UN  172.17.0.3  102.58 KB  256          48.6%             fc52ff8f-be44-4cc4-8241-9a158b3ff0d9  rack1
	UN  172.17.0.2  108.22 KB  256          50.4%             31480ecf-a9e0-49e7-a6a2-e8904cb6af28  rack1
	UN  172.17.0.4  83.96 KB   256          52.4%             ea14aede-00b6-4aff-bb48-9d1a3106c507  rack1

.. _section3-3-docker-hub-label:

Docker Hubの利用
------------------------------------------------------

.. _section3-3-1-docker-hub-create-repository-label:

レポジトリの作成
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Docker Hubでレポジトリを作成する。Create Repositoryボタンを押下し、名前と可視性を指定する。ここでは、testレポジトリをパブリックレポジトリとして作成する。

.. figure:: img/docker-hub.png
   :scale: 100%

.. figure:: img/docker-hub-create-repository.png
   :scale: 100%

.. figure:: img/docker-hub-repository.png
   :scale: 100%

.. _section3-3-2-docker-hub-push-repository-label:

レポジトリにプッシュ
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

前節で作成したレポジトリにコンテナイメージをプッシュする。

* Dockerレジストリ(Docker Hub)にログイン
* debugroom/testレポジトリを新規に作成し、イメージをプッシュ

.. sourcecode:: bash

	[centos@ip-XXX-XXX-XXX-XXX ~]$ docker login
	Login with your Docker ID to push and pull images from Docker Hub. If you don't have a Docker ID, head over to https://hub.docker.com to create one.
	Username: debugroom
	Password:
	Login Succeeded
	[centos@ip-XXX-XXX-XXX-XXX ~]$ docker commit centos7 debugroom/test:ver1.0
	sha256:fbacaee322f6ff1e4f6c2d2795aef57c90e4873c8181516676a89a2e50cca5bc
	[centos@ip-XXX-XXX-XXX-XXX ~]$ docker images
	REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
	debugroom/test      ver1.0              fbacaee322f6        5 seconds ago       283.2 MB
	test                ver1.0              9ddcf0f1c82d        26 hours ago        283.2 MB
	docker.io/centos    latest              8140d0c64310        3 weeks ago         192.5 MB
	[centos@ip-XXX-XXX-XXX-XXX ~]$ docker push debugroom/test
	The push refers to a repository [docker.io/debugroom/test]
	05064569aaee: Pushed
	b51149973e6a: Mounted from library/centos
	ver1.0: digest: sha256:c462439235fc131b4caacab2f8b71094bc096473762da6fd2841ec4d008ed4e9 size: 741

* Docker Hub上にTagが新たに追加される。

.. figure:: img/docker-hub-push-repository.png
   :scale: 100%


.. _section3-5-docker-file-label:

Dockerfileの記述
------------------------------------------------------

.. _section3-5-1-docker-file-web-server-label:

Dockerfileを使ったWebサーバの構築
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

前節「Webサーバの構築」で実施したコンテナ内でのHTTP Serverの実行をDockerファイルを用いて行う。

.. sourcecode:: bash

   # Dockerfiel for Apache HTTP Server

   FROM docker.io/centos:latest
   MAINTAINER debugroom

   RUN yum -y install httpd; yum clean all; systemctl enable httpd.service
   Add index.html /var/www/html/index.html

   EXPOSE 80

   CMD ["/usr/sbin/init"]

.. _section3-5-3-docker-file-postgresql-db-server-label:

PostgreSQL DBサーバの構築
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

同様に、DBサーバとして、PostgreSQLをインストールし、データベース・テーブル環境の構築をDockerファイルに記述して行う。構築には `リンク先のサイト <http://ossfan.net/setup/docker-05.html>`_ を参考にした。

.. sourcecode:: bash
   :caption: ~/build_postgres/Dockerfile

	FROM            centos:latest
	MAINTAINER      debugroom
	ENV             container docker
	RUN             yum update -y && yum clean all
	RUN             yum install -y postgresql postgresql-server && yum clean all
	RUN             su - postgres -c "initdb --encoding=UTF8 --no-locale --pgdata=/var/lib/pgsql/data --auth=ident"
	RUN             systemctl enable postgresql
	RUN             cp -piv /var/lib/pgsql/data/postgresql.conf /var/lib/pgsql/data/postgresql.conf.bk
	RUN             sed -e "s/#listen_addresses = 'localhost'/listen_addresses = '*'/g" /var/lib/pgsql/data/postgresql.conf > /var/lib/pgsql/data/postgresql.tmp
	RUN             sed -e "s/log_filename = 'postgresql-%a.log'/log_filename = 'postgresql-%Y%m%d.log'/g" /var/lib/pgsql/data/postgresql.tmp > /var/lib/pgsql/data/postgresql.conf
	RUN             sed -e "s/log_truncate_on_rotation = on/log_truncate_on_rotation = off/g" /var/lib/pgsql/data/postgresql.conf > /var/lib/pgsql/data/postgresql.tmp
	RUN             sed -e "s/log_rotation_age = 1d/log_rotation_age = 7d/g" /var/lib/pgsql/data/postgresql.tmp > /var/lib/pgsql/data/postgresql.conf
	RUN             sed -e "s/#log_line_prefix = ''/log_line_prefix = '%t [%p] '/g" /var/lib/pgsql/data/postgresql.conf > /var/lib/pgsql/data/postgresql.tmp
	RUN             mv /var/lib/pgsql/data/postgresql.tmp /var/lib/pgsql/data/postgresql.conf
	RUN             su - postgres -c "pg_ctl start -w;psql -c \"alter role postgres with password 'postgres';\";pg_ctl stop -m fast"
	RUN             cp -piv /var/lib/pgsql/data/pg_hba.conf /var/lib/pgsql/data/pg_hba.conf.bk
	RUN             sed -e "s/^host/#host/g" /var/lib/pgsql/data/pg_hba.conf > /var/lib/pgsql/data/pg_hba.tmp
	RUN             sed -e "s/^local/#local/g" /var/lib/pgsql/data/pg_hba.tmp > /var/lib/pgsql/data/pg_hba.conf
	RUN             echo "local    all             postgres                                peer" >> /var/lib/pgsql/data/pg_hba.conf
	RUN             echo "local    all             all                                     md5" >> /var/lib/pgsql/data/pg_hba.conf
	RUN             echo "host     all             all             0.0.0.0/0               md5" >> /var/lib/pgsql/data/pg_hba.conf
	RUN             rm -f /var/lib/pgsql/data/pg_hba.tmp
	EXPOSE          5432
	ADD             scripts /var/local/postgresql/scripts
	RUN             chmod 755 /var/local/postgresql/scripts/init_db.sh
	RUN             /var/local/postgresql/scripts/init_db.sh

PosgreSQLのインストール後は、自作したシェルスクリプトの中から、データベースやロール、テーブルを作成し、各テーブルに権限を付与している。

.. sourcecode:: bash
   :caption: ~/build_postgres/scripts/init_db.sh

	#!/bin/bash

	set PASSWORD=postgres

	su - postgres -c "pg_ctl start -w;psql -f /var/local/postgresql/scripts/create_role.sql;pg_ctl stop -m fast"
	su - postgres -c "pg_ctl start -w;psql -f /var/local/postgresql/scripts/create_db.sql;pg_ctl stop -m fast"
	su - postgres -c "pg_ctl start -w;psql -f /var/local/postgresql/scripts/create_table.sql -d sample-app;pg_ctl stop -m fast"
	su - postgres -c "pg_ctl start -w;psql -f /var/local/postgresql/scripts/grant_role.sql -d sample-app;pg_ctl stop -m fast"

.. note:: コンテナ起動前にpsqlを実行する場合は、pg_ctlを起動してから実行しなければ以下のようなエラーになる。
   psql: could not connect to server: No such file or directory
   Is the server running locally and accepting
   connections on Unix domain socket "/var/run/postgresql/.s.PGSQL.5432"?

.. sourcecode:: sql
   :caption: ~/build_postgres/scripts/create_role.sql

    create role app with LOGIN password 'app';

.. sourcecode:: sql
   :caption: ~/build_postgres/scripts/create_db.sql

    create database "sample-app" encoding 'utf8';

.. sourcecode:: sql
   :caption: ~/build_postgres/scripts/create_table.sql

	/* Drop Tables */

	DROP TABLE IF EXISTS ADDRESS;
	DROP TABLE IF EXISTS PHONE;
	DROP TABLE IF EXISTS EMAIL;
	DROP TABLE IF EXISTS CREDENTIAL;
	DROP TABLE IF EXISTS USR;
	DROP TABLE IF EXISTS COMPANY;

	/* Create Tables */

	CREATE TABLE ADDRESS
	(
		COMPANY_ID char(10) NOT NULL,
		USER_ID char(8) NOT NULL,
		ADDRESS_NO int NOT NULL,
		POST_CD char(7) NOT NULL,
		ADDRESS varchar(255),
		ADDRESS_DETAIL varchar(255),
		PRIMARY KEY (COMPANY_ID, USER_ID, ADDRESS_NO),
		UNIQUE (COMPANY_ID, USER_ID, ADDRESS_NO)
	) WITHOUT OIDS;


	CREATE TABLE COMPANY
	(
		COMPANY_ID char(10) NOT NULL,
		COMPANY_NAME varchar(255),
		PRIMARY KEY (COMPANY_ID)
	) WITHOUT OIDS;


	CREATE TABLE CREDENTIAL
	(
		COMPANY_ID char(10) NOT NULL,
		USER_ID char(8) NOT NULL,
		CREDENTIAL_NO int NOT NULL,
		CREDENTIAL_TYPE char(4) NOT NULL,
		CREDENTIAL_KEY varchar(255),
		KEY_EXPIRED_DATE timestamp,
		ACCESS_TOKEN varchar(512),
		TOKEN_EXPIRED_DATE timestamp with time zone,
		PRIMARY KEY (COMPANY_ID, USER_ID, CREDENTIAL_NO),
		UNIQUE (COMPANY_ID, USER_ID, CREDENTIAL_NO)
	) WITHOUT OIDS;


	CREATE TABLE EMAIL
	(
		COMPANY_ID char(10) NOT NULL,
		USER_ID char(8) NOT NULL,
		EMAIL_NO int NOT NULL,
		EMAIL varchar(255),
		PRIMARY KEY (COMPANY_ID, USER_ID, EMAIL_NO),
		UNIQUE (COMPANY_ID, USER_ID, EMAIL_NO)
	) WITHOUT OIDS;


	CREATE TABLE PHONE
	(
		COMPANY_ID char(10) NOT NULL,
		USER_ID char(8) NOT NULL,
		PHONE_NO int NOT NULL,
		PHONE_NUMBER char(11),
		RELATED_ADDRESS_NO int,
		PRIMARY KEY (COMPANY_ID, USER_ID, PHONE_NO),
		UNIQUE (COMPANY_ID, USER_ID, PHONE_NO)
	) WITHOUT OIDS;


	CREATE TABLE USR
	(
		COMPANY_ID char(10) NOT NULL,
		USER_ID char(8) NOT NULL,
		USER_NAME varchar(255),
		LOGIN_ID varchar(32) NOT NULL UNIQUE,
		BIRTHDAY char(8),
		AUTHORITY_LEVEL int NOT NULL,
		IS_LOGIN boolean NOT NULL,
		PRIMARY KEY (COMPANY_ID, USER_ID),
		UNIQUE (COMPANY_ID, USER_ID)
	) WITHOUT OIDS;


	/* Create Foreign Keys */

	ALTER TABLE USR
		ADD FOREIGN KEY (COMPANY_ID)
		REFERENCES COMPANY (COMPANY_ID)
		ON UPDATE RESTRICT
		ON DELETE RESTRICT
	;


	ALTER TABLE ADDRESS
		ADD FOREIGN KEY (COMPANY_ID, USER_ID)
		REFERENCES USR (COMPANY_ID, USER_ID)
		ON UPDATE RESTRICT
		ON DELETE RESTRICT
	;


	ALTER TABLE PHONE
		ADD FOREIGN KEY (COMPANY_ID, USER_ID)
		REFERENCES USR (COMPANY_ID, USER_ID)
		ON UPDATE RESTRICT
		ON DELETE RESTRICT
	;


	ALTER TABLE EMAIL
		ADD FOREIGN KEY (COMPANY_ID, USER_ID)
		REFERENCES USR (COMPANY_ID, USER_ID)
		ON UPDATE RESTRICT
		ON DELETE RESTRICT
	;


	ALTER TABLE CREDENTIAL
		ADD FOREIGN KEY (COMPANY_ID, USER_ID)
		REFERENCES USR (COMPANY_ID, USER_ID)
		ON UPDATE RESTRICT
		ON DELETE RESTRICT
	;


.. sourcecode:: sql
   :caption: ~/build_postgres/scripts/grant_role.sql

	GRANT USAGE ON SCHEMA public TO app;
	GRANT SELECT, UPDATE, INSERT, DELETE ON COMPANY TO app;
	GRANT SELECT, UPDATE, INSERT, DELETE ON USR TO app;
	GRANT SELECT, UPDATE, INSERT, DELETE ON ADDRESS TO app;
	GRANT SELECT, UPDATE, INSERT, DELETE ON CREDENTIAL TO app;
	GRANT SELECT, UPDATE, INSERT, DELETE ON EMAIL TO app;
	GRANT SELECT, UPDATE, INSERT, DELETE ON PHONE TO app;


.. _section3-5-4-docker-file-ap-server-using-springboot-label:

PostgreSQLに接続するAPサーバの構築
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

前節 :ref:`section3-5-3-docker-file-postgresql-db-server-label` に接続するアプリケーションをDockerfileを使って、構築する。アプリケーションサーバはTomcatを使用するが、Spring Bootで組み込みTomcatの形式でAPサーバを構築する。なお、Spring Bootの基本的な使い方については、`Spring Bootのサンプル <http://debugroom.github.io/doc/java/spring/springboot/index.html>`_ を参照すること。

まず、構築するアプリケーションは、以下の通り、Embedded Tomcatを使用した構成とするために、pom.xmlを以下の通り、作成する。

.. sourcecode:: xml

	<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	  <modelVersion>4.0.0</modelVersion>
	  <parent>
	    <groupId>org.springframework.boot</groupId>
	    <artifactId>spring-boot-starter-parent</artifactId>
	    <version>1.4.0.RELEASE</version>
	  </parent>
	  <groupId>org.debugroom</groupId>
	  <artifactId>spring-boot-app</artifactId>
	  <packaging>jar</packaging>
	  <name>spring-boot-app</name>
	  <version>1.0-SNAPSHOT</version>

	  <properties>
	    <java.version>1.8</java.version>
	  </properties>

	  <dependencies>
	    <dependency>
	      <groupId>org.springframework.boot</groupId>
	      <artifactId>spring-boot-starter-web</artifactId>
	    </dependency>
	    <dependency>
	      <groupId>org.springframework.boot</groupId>
	      <artifactId>spring-boot-starter-data-jpa</artifactId>
	    </dependency>
	    <dependency>
	      <groupId>org.hsqldb</groupId>
	      <artifactId>hsqldb</artifactId>
	    </dependency>
	    <dependency>
	      <groupId>org.postgresql</groupId>
	      <artifactId>postgresql</artifactId>
	    </dependency>
	    <dependency>
	      <groupId>org.projectlombok</groupId>
	      <artifactId>lombok</artifactId>
	      <scope>provided</scope>
	    </dependency>
	    <dependency>
	      <groupId>org.springframework.boot</groupId>
	      <artifactId>spring-boot-devtools</artifactId>
	      <scope>provided</scope>
	    </dependency>
	    <dependency>
	      <groupId>org.springframework.boot</groupId>
	      <artifactId>spring-boot-starter-test</artifactId>
	      <scope>test</scope>
	    </dependency>
	  </dependencies>

	  <build>
	    <plugins>
	      <plugin>
	        <groupId>org.apache.maven.plugins</groupId>
	        <artifactId>maven-compiler-plugin</artifactId>
	        <configuration>
	          <source>${java.version}</source>
	          <target>${java.version}</target>
	          <compilerArgument>-parameters</compilerArgument>
	        </configuration>
	      </plugin>
	      <plugin>
	        <groupId>org.springframework.boot</groupId>
	        <artifactId>spring-boot-maven-plugin</artifactId>
	        <configuration>
	          <mainClass>org.debugroom.sample.docker.boot.config.WebApp</mainClass>
	        </configuration>
	      </plugin>
	    </plugins>
	  </build>

	</project>

データベースへのアクセスはJPAを使用する。JPAを使ったデータベースアクセスについては、`JPAサンプル <http://debugroom.github.io/doc/java/spring/springdatajpa/index.html>`_ も合わせて参照すること。USRテーブルにアクセスする、エンティティクラスは以下の通り。

.. sourcecode:: java

	package org.debugroom.sample.docker.boot.domain.entity;

	import java.io.Serializable;
	import javax.persistence.*;
	import java.util.Set;

	import com.fasterxml.jackson.annotation.JsonIgnore;

	/**
	 * The persistent class for the usr database table.
	 *
	 */
	@Entity
	@Table(name="usr")
	@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
	public class User implements Serializable {
		private static final long serialVersionUID = 1L;

		@EmbeddedId
		private UserPK id;

		@Column(name="authority_level")
		private Integer authorityLevel;

		private String birthday;

		@Column(name="is_login")
		private Boolean isLogin;

		@Column(name="login_id")
		private String loginId;

		@Column(name="user_name")
		private String userName;

		//bi-directional many-to-one association to Address
		@OneToMany(mappedBy="usr")
		private Set<Address> addresses;

		//bi-directional many-to-one association to Credential
		@OneToMany(mappedBy="usr")
		private Set<Credential> credentials;

		//bi-directional many-to-one association to Email
		@OneToMany(mappedBy="usr")
		private Set<Email> emails;

		//bi-directional many-to-one association to Phone
		@OneToMany(mappedBy="usr")
		private Set<Phone> phones;

		//bi-directional many-to-one association to Company
		@ManyToOne
		@JoinColumn(name="company_id", insertable=false, updatable=false)
		@JsonIgnore
		private Company company;

		public User() {
		}

        // omit getter, setter

	}

.. note:: 後にJSON形式へコンバートするため、循環参照となるのを防止するために、@ManyToOneアノテーションを付与するプロパティには@JsonIgnoreアノテーションを付与しておく。

レポジトリクラスはJpaRepositoryインターフェースを使用して作成する。

.. sourcecode:: java

	package org.debugroom.sample.docker.boot.domain.repository;

	import org.springframework.data.jpa.repository.JpaRepository;

	import org.debugroom.sample.docker.boot.domain.entity.User;

	public interface UserRepository extends JpaRepository<User, String>{
	}

データベースへのアクセスを、JSON形式で返却するよう、サービス、コントローラを実装する。

.. sourcecode:: java

	package org.debugroom.sample.docker.boot.domain.service;

	import java.util.List;

	import org.springframework.beans.factory.annotation.Autowired;

	import org.debugroom.sample.docker.boot.domain.entity.User;
	import org.debugroom.sample.docker.boot.domain.repository.UserRepository;

	public class SampleServiceImpl implements SampleService {

		@Autowired
		UserRepository userRepository;

		@Override
		public List<User> getUsers() {
			return userRepository.findAll();
		}

	}

.. sourcecode:: java

	package org.debugroom.sample.docker.boot.app.web;

	import java.util.List;

	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.http.HttpStatus;
	import org.springframework.web.bind.annotation.RequestMapping;
	import org.springframework.web.bind.annotation.RequestMethod;
	import org.springframework.web.bind.annotation.ResponseStatus;
	import org.springframework.web.bind.annotation.RestController;

	import org.debugroom.sample.docker.boot.domain.service.SampleService;

	@RequestMapping("/api/v1")
	@RestController
	public class SampleRestController {

		@Autowired
		SampleService sampleService;

		@RequestMapping(value="users", method=RequestMethod.GET)
		@ResponseStatus(HttpStatus.OK)
		public List<?> getUsers(){
			return sampleService.getUsers();
		}

	}

Dockerで構築したDBサーバへアクセスするために、DB接続環境の設定ファイルを環境変数参照とするようにしておく。

.. sourcecode:: bash
   :caption: src/main/resoures/application-production.yml

   spring:
     profiles: production
     datasource:
       url: jdbc:postgresql://${dbserver.port.5432.tcp.addr}:${dbserver.port.5432.tcp.port}/sample-app
       username: ${dbserver.app.username}
       password: ${dbserver.app.password}
       driverClassName: org.postgresql.Driver

.. note:: DBサーバへの接続IPアドレスとポートは、linkオプションを使って、Dockerコンテナから環境変数<エイリアス名>_PORT_<ポート番号>_TCP_ADDRおよび、<エイリアス名>_PORT_<ポート番号>_TCP_PORTから参照可能である。エイリアス名はAPサーバのコンテナ起動時に、ポートはDBサーバのコンテナ起動時に指定するが、ここでは、エイリアス名をDBSERVER、ポートを5432とすることを前提に、DBSERVER_PORT_5432_TCP_ADDRおよび、DBSERVER_PORT_5432_TCP_PORTが環境変数として設定されるものとして定義しておく。なお、接続のデータベースユーザ名と、パスワードはDockerfileで環境変数を別途定義する。

こうして作成したSpring BootアプリケーションをDockerコンテナ上でMavenでビルドを行い、LaunchするためのDockerfileを作成する。ベースはCentOS7でOpenJDK1.8をインストールした後、Mavenをインストールし、アプリケーションをビルドしてポート8080でサーバ起動する。

.. sourcecode:: bash

	# Dockerfile for Spring boot app using embedded tomcat server

	FROM docker.io/centos:latest
	MAINTAINER debugroom

	RUN yum install -y \
	       java-1.8.0-openjdk \
	       java-1.8.0-openjdk-devel \
	       wget tar

	RUN wget http://repos.fedorapeople.org/repos/dchen/apache-maven/epel-apache-maven.repo -O /etc/yum.repos.d/epel-apache-maven.repo
	RUN sed -i s/\$releasever/6/g /etc/yum.repos.d/epel-apache-maven.repo
	RUN yum install -y apache-maven
	ENV JAVA_HOME /etc/alternatives/jre
	ADD spring-boot-app /var/local/springbootapp
	RUN mvn install -f /var/local/springbootapp/pom.xml
	ENV DBSERVER_APP_USERNAME=app
	ENV DBSERVER_APP_PASSWORD=app

	EXPOSE 8080

	CMD java -jar -Dspring.profiles.active=production,jpa /var/local/springbootapp/target/spring-boot-app-1.0-SNAPSHOT.jar

.. note:: アプリケーション起動時にはapplication-production.ymlが有効するよう、プロファイルにproductionを指定しておく。

コンテナイメージをtomcat-springbootというイメージ名でビルドしておく。

.. sourcecode:: bash

   [centos@ip-XXX-XXX-XXX-XXX ~]$ docker build -t debugroom/test:tomcat-springboot <Dockerファイルのパス>


DBサーバのコンテナイメージを起動する。

.. sourcecode:: bash

   [centos@ip-XXX-XXX-XXX-XXX ~]$ docker run -d --privileged --name dbserver1 -p 5432:5432 debugroom/test:postgres /sbin/init


DBサーバのコンテナのIPとポートを環境変数から取得できるよう、LINKオプションを指定して、APサーバのコンテナを起動する。ここではエイリアス名がdbserverとなるよう、上記で起動したdbserver1に対しdbserverというリンクを設定するもの(=エイリアス名)とする。また、8000ポートを指定すると。起動したコンテナの8080を経由するようポートして起動する。

.. sourcecode:: bash

   docker run -itd --name apserver -p 8000:8080 --link dbserver1:dbserver debugroom/test:tomcat-springboot

.. sourcecode:: bash

   docker exec -it apserver /bin/bash

.. note:: LINKオプションからIPアドレスとポートが参照可能なのは、２つのコンテナが同一のホスト上で実行されている場合である。異なるサーバの場合、コンテナ起動時に-eオプションを使って、直接IPアドレスとポートを指定する方法もあるが、Docker Ver1.9以降はマルチホストでIPアドレスとポートを共有する機能が追加されている。

.. .. todo:: マルチホストでIPアドレスとポートを共有する方法を検証する。

起動後、しばらく時間が経過してから(APサーバが起動するまで少々時間がかかる)、curlコマンドを実行し動作確認する。

.. sourcecode:: bash

   [centos@ip-XXX-XXX-XXX-XXX ~]$ curl http://localhost:8000/api/v1/users
   [{"id":{"companyId":"0000000000","userId":"00000000"},"authorityLevel":0,"birthday":"20170101","isLogin":false,"loginId":"test.com","userName":"test","addresses":[],"credentials":[],"emails":[],"phones":[]}][centos@ip-172-31-31-80 ~]


.. _section3-5-5-docker-file-ap-server-for-cassandra-label:

CassandraクラスタDBサーバに接続するAPサーバの構築
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

前節 :ref:`section3-2-4-docker-web-server-label` に接続するアプリケーションをDockerfileを使って、構築する。アプリケーションサーバはTomcatを使用するが、Spring Bootで組み込みTomcatの形式でAPサーバを構築する。なお、Spring Bootの基本的な使い方については、`Spring Bootのサンプル <http://debugroom.github.io/doc/java/spring/springboot/index.html>`_ を参照すること。また、Cassandraへ接続するアプリケーションは `Cassandraのデータモデル検証 <http://debugroom.github.io/doc/database/cassandra/datamodeling.html>`_ で構築したものを前提にCassandraクラスタへ接続するための設定を行う。実際の実装は `GitHubページ <https://github.com/debugroom/sample/tree/develop/sample-spring-cassandra>`_ を参照すること。

まず、Spring Data Cassandraを使って、クラスタ化したCassadraへアクセスするための設定クラスを作成する。CassandraClusterFactoryBeanのContactPointsプロパティに環境変数からクラスタ接続するDBサーバのIPとポートを取得できるよう、cluster()メソッドを実装する。

 .. sourcecode:: java

	package org.debugroom.sample.cassandra.config.env;

	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.context.annotation.Bean;
	import org.springframework.context.annotation.Configuration;
	import org.springframework.context.annotation.Profile;
	import org.springframework.core.env.Environment;
	import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;

	import org.debugroom.sample.cassandra.config.infra.CommonCassandraConfig;

	@Configuration
	@Profile("cluster")
	public class CommonClusterConfig extends CommonCassandraConfig{

	    @Autowired
	    private Environment env;

		@Override
		protected String getKeyspaceName() {
			return "sample";
		}

		@Bean
	    public CassandraClusterFactoryBean cluster() {
	        CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
	        cluster.setContactPoints(env.getProperty("cassandra.contactpoints"));
	        cluster.setPort(Integer.parseInt(env.getProperty("cassandra.port")));
	        return cluster;
	    }

	}

.. sourcecode:: xml
   :caption: src/main/resources/application-cluster.yml

   cassandra:
     contactpoints: ${dbserver.port.9042.tcp.addr}
     port: ${dbserver.port.9042.tcp.port}


.. note:: このアプリケーションでは、プロファイルclusterを指定した場合、当該設定クラスが有効になるように、@Profileアノテーションを指定している。設定ファイルapplication-cluster.ymlも同様、プロファイルをclusterに指定した場合、有効になる。

.. note:: DBサーバへの接続IPアドレスとポートは、Dockerのlinkオプションを使って、Dockerコンテナから環境変数<エイリアス名>_PORT_<ポート番号>_TCP_ADDRおよび、<エイリアス名>_PORT_<ポート番号>_TCP_PORTから参照可能である。エイリアス名はAPサーバのコンテナ起動時に、ポートはDBサーバのコンテナ起動時に指定するが、ここでは、エイリアス名をDBSERVER、ポートを9042とすることを前提に、DBSERVER_PORT_9042_TCP_ADDRおよび、DBSERVER_PORT_9042_TCP_PORTが環境変数として設定されるものとして定義しておく。

SpringBootでAPサーバを起動した時のログ(catalina.outに相当)確認のために、設定ファイルにログ出力の設定を追加しておく。

.. sourcecode:: xml
   :caption: src/main/resources/application.yml

   spring:
     profiles:
       active: pattern2, cluster
   logging:
     file: logs/app.log
       level:
         org.springframework.web: INFO


こうして作成したアプリケーションをDockerコンテナ上でMavenでビルドを行い、LaunchするためのDockerfileを作成する。ベースはCentOS7でOpenJDK1.8をインストールした後、Mavenをインストールし、アプリケーションをビルドしてポート8080でサーバ起動する。

 .. sourcecode:: bash

	# Dockerfile for Spring Data Cassandra app using embedded tomcat server
	# Run parent directory because this app needs sample-spring-boot-parent for maven build.
	# ex) docker build -t <your image> ~/sample/ -f ~/sample/sample-spring-cassandra/Dockerfile

	FROM docker.io/centos:latest
	MAINTAINER debugroom

	RUN yum install -y \
	       java-1.8.0-openjdk \
	       java-1.8.0-openjdk-devel \
	       wget

	RUN wget http://repos.fedorapeople.org/repos/dchen/apache-maven/epel-apache-maven.repo -O /etc/yum.repos.d/epel-apache-maven.repo
	RUN sed -i s/\$releasever/6/g /etc/yum.repos.d/epel-apache-maven.repo
	RUN yum install -y apache-maven
	ENV JAVA_HOME /etc/alternatives/jre
	ADD sample-spring-cassandra /var/local/sample-spring-cassandra
	ADD sample-spring-boot-parent /var/local/sample-spring-boot-parent
	RUN mvn install -f /var/local/sample-spring-cassandra/pom.xml

	EXPOSE 8080

	CMD java -jar -Dspring.profiles.active=pattern1,cluster /var/local/sample-spring-cassandra/target/sample-spring-cassandra-1.0-SNAPSHOT.jar

.. note:: 当該アプリケーションのMavenプロジェクトは別のparentプロジェクトが必要なため、Dockerファイルで別プロジェクトも追加できるよう、Dockerfileを配置した親ディレクトリを起点にDockerファイルを実行する。Dockerfileでは実行したディレクトリの親ディレクトリより上の相対パスは参照できないため、起点を親ディレクトリにしてfオプションを使って、Docker buildを実行する。

 .. sourcecode:: bash

	[centos@ip-XXX-XXX-XXX-XXX sample]$ docker build -t debugroom/test:spring-data-cassandra-app ~/sample/ -f ~/sample/sample-spring-cassandra/Dockerfile
	Sending build context to Docker daemon 3.997 MB
	Step 1 : FROM docker.io/centos:latest
	 ---> 3bee3060bfc8
	Step 2 : MAINTAINER debugroom
	 ---> Using cache
	 ---> 643ea12aaad5
	Step 3 : RUN yum install -y        java-1.8.0-openjdk        java-1.8.0-openjdk-devel        wget
	 ---> Using cache
	 ---> 2724d01e7577
	Step 4 : RUN wget http://repos.fedorapeople.org/repos/dchen/apache-maven/epel-apache-maven.repo -O /etc/yum.repos.d/epel-apache-maven.repo
	 ---> Using cache
	 ---> 4546d3011aa4
	Step 5 : RUN sed -i s/\$releasever/6/g /etc/yum.repos.d/epel-apache-maven.repo
	 ---> Using cache
	 ---> f32f19a4f1cb
	Step 6 : RUN yum install -y apache-maven
	 ---> Using cache
	 ---> 275915d7ecae
	Step 7 : ENV JAVA_HOME /etc/alternatives/jre
	 ---> Using cache
	 ---> 56863cbc1f04
	Step 8 : ADD sample-spring-cassandra /var/local/sample-spring-cassandra
	 ---> a316f1da2fd1
	Removing intermediate container 53967e151175
	Step 9 : ADD sample-spring-boot-parent /var/local/sample-spring-boot-parent
	 ---> 15471086ce61
	Removing intermediate container 252e325030e1
	Step 10 : RUN mvn install -f /var/local/sample-spring-cassandra/pom.xml
	 ---> Running in 83e4824e10d6
	[INFO] Scanning for projects...

   # omit...

	[INFO] Installing /var/local/sample-spring-cassandra/target/sample-spring-cassandra-1.0-SNAPSHOT.jar to /root/.m2/repository/org/debugroom/sample-spring-cassandra/1.0-SNAPSHOT/sample-spring-cassandra-1.0-SNAPSHOT.jar
	[INFO] Installing /var/local/sample-spring-cassandra/pom.xml to /root/.m2/repository/org/debugroom/sample-spring-cassandra/1.0-SNAPSHOT/sample-spring-cassandra-1.0-SNAPSHOT.pom
	[INFO] ------------------------------------------------------------------------
	[INFO] BUILD SUCCESS
	[INFO] ------------------------------------------------------------------------
	[INFO] Total time: 02:37 min
	[INFO] Finished at: 2017-06-14T22:28:41+00:00
	[INFO] Final Memory: 39M/561M
	[INFO] ------------------------------------------------------------------------
	 ---> 2f7a7d3ef8f5
	Removing intermediate container 83e4824e10d6
	Step 11 : EXPOSE 8080
	 ---> Running in 3671414783e2
	 ---> 153a150357aa
	Removing intermediate container 3671414783e2
	Step 12 : CMD java -jar -Dspring.profiles.active=pattern1,cluster /var/local/sample-spring-cassandra/target/sample-spring-cassandra-1.0-SNAPSHOT.jar
	 ---> Running in 23e7bf873912
	 ---> 956b57d2cb23
	Removing intermediate container 23e7bf873912
	Successfully built 956b57d2cb23

:ref:`section3-2-4-docker-web-server-label` で構築したCassandraクラスタのIPアドレスとポートが環境変数として、設定された状態となるようLINKオプションを指定して、Dockerコンテナを起動する。

.. sourcecode:: bash

	[centos@ip-XXX-XXX-XXX-XXX sample]$ docker run -itd --name apserver1 -p 8080:8080 --link cassandra-server1:dbserver debugroom/test:spring-data-cassandra-app
	abedde75c6a1f5497bc16e9ff1b9962d61b012c3e4c9f942a160bb40f8f33030
	[centos@ip-XXX-XXX-XXX-XXX sample]$ docker exec -ti apserver1 /bin/bash

.. note:: LINKオプションからIPアドレスとポートが参照可能なのは、２つのコンテナが同一のホスト上で実行されている場合である。異なるサーバの場合、コンテナ起動時に-eオプションを使って、直接IPアドレスとポートを指定する方法もあるが、Docker Ver1.9以降はマルチホストでIPアドレスとポートを共有する機能が追加されている。

.. todo:: マルチホストでIPアドレスとポートを共有する方法を検証する。
