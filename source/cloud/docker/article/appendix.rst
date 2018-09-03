.. include:: ../module.txt

.. _section9-docker-appendix-label:

Appendix - 付録
======================================================

.. _section9-1-setting-local-and-timezone-label:

CentOS7コンテナにおけるロケールとタイムゾーン設定
------------------------------------------------------------------------------

CentOS7をベースとしたコンテナではロケールとタイムゾーンがデフォルトでen.US.UTF-8、UTCとなっているので、
ロケールをjp_JP.UTF-8に、タイムゾーンをJSTに設定する。

.. sourcecode:: bash

   FROM centos:centos7
   MAINTAINER debugroom

   RUN rm -f /etc/rpm/macros.image-language-conf && \
       sed -i '/^override_install_langs=/d' /etc/yum.conf && \
       yum -y reinstall glibc-common && \
       yum clean all

   ENV LANG="ja_JP.UTF-8" \
       LANGUAGE="ja_JP:ja" \
       LC_ALL="ja_JP.UTF-8"

   RUN cp /etc/localtime /etc/localtime.org
   RUN ln -sf  /usr/share/zoneinfo/Asia/Tokyo /etc/localtime
