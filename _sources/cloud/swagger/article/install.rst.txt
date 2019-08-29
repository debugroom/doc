.. include:: ../module.txt

.. _section2-swagger-install-label:

Swagger Editor、Swgger UIのインストール
==============================================================

Dockerを用いてSwagger EditorおよびUIのインストールを行う。
なお手順は `Swagger Editor公式サイト <https://github.com/swagger-api/swagger-editor>`_ および `Swagger UI公式サイト <https://github.com/swagger-api/swagger-ui/blob/master/docs/usage/installation.md>`_ も参照のこと。

1. Dockerがインストールされたマシン上で、SwaggerEditorおよびUIのコンテナイメージをプルする。

.. sourcecode:: bash

   [centos@ip-XXX-XXX-XXX-XXX ~]$ docker pull swaggerapi/swagger-editor
   [centos@ip-XXX-XXX-XXX-XXX ~]$ docker pull swaggerapi/swagger-ui

2. プルしたコンテナイメージをそれぞれ起動する。

.. sourcecode:: bash

   [centos@ip-XXX-XXX-XXX-XXX ~]$ docker run -d -p 8081:8080 swaggerapi/swagger-editor
   [centos@ip-XXX-XXX-XXX-XXX ~]$ docker run -d -p 8082:8080 swaggerapi/swagger-ui

3. http://localhost:8081/、http://localhost:8082にそれぞれアクセスし、ツールが表示されればOK。

|br|

.. figure:: img/swagger-editor.png
   :scale: 100%

|br|
