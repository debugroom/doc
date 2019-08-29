.. include:: ../module.txt

.. _section1-swagger-overview-label:

Swagger Overview
======================================================

.. _section1-1-swagger-overview-label:

Swaggerの概要
------------------------------------------------------

SwaggerはSmartBear社が提供するRESTful APIのドキュメントや、サーバ、クライアントコード、エディタ、またそれらを扱うための仕様などを提供するフレームワークである。
Swaggerは以下の要素により構成される。

|br|

.. list-table:: Swaggerの構成要素
   :widths: 2, 8

   * - 要素
     - 説明

   * - Swagger Specification
     - Swaggerを扱う上で中心となる概念で、Swaggerの仕様に準じた、RESTful APIインターフェイスを記述するためのフォーマット(YAML/JSON)

   * - Swagger Core
     - JavaのコードからSwagger Specificationを生成するためのJavaライブラリ

   * - Swagger Editor
     - ブラウザ上で動くSwagger Specificationのエディタ、リアルタイムで構文チェック、可視化

   * - Swagger Codegen
     - Swagger Specificationからクライアントコード生成するコマンドラインツール

   * - Swagger UI
     - Swagger Specificationから動的にドキュメントを生成するツール

Swaggerは、バージョン3.0からOAS:Open API Specification 3.0として公開された。Swagger Specをよりシンプルに記述できるようになり、再利用性(componentsによる再利用部分の集約)が向上している。
