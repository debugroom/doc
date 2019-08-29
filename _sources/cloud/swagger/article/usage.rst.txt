.. include:: ../module.txt

.. _section3-swagger-usage-label:

Usage
======================================================

.. _section3-1-swagger-editor-label:

SwaggerEditor Usage
------------------------------------------------------

.. todo:: SwaggerEditorの記述方法などを追加する。

.. _section3-2-swagger-UI-label:

SwaggerUI Usage
------------------------------------------------------

.. todo:: SwaggerUIの記述方法などを追加する。

.. _section3-2-swagger-markup2html-label:

SwaggerMarkup2HTML Usage
------------------------------------------------------

|br|

SwaggerSpecを元にasciidocへ変換し、asciidoctorを使用して静的なHTMLへ変換する。Swagger2MarkupやGradleの実行環境、rbenvおよびRubyのインストールを行い、変換スクリプトを実行するDockerfileは以下の通り。

|br|

.. sourcecode:: bash

   FROM centos:centos7
   MAINTAINER debugroom

   RUN yum install -y java-1.8.0-openjdk java-1.8.0-openjdk-devel wget tar iproute git epel-release
   RUN yum -y install gcc-c++ git glibc-headers libffi-devel libxml2 libxml2-devel libxslt libxslt-devel libyaml-devel make nodejs npm openssl-devel readline readline-devel sqlite-devel zlib zlib-devel bzip2

   RUN rm -f /etc/rpm/macros.image-language-conf && \
     sed -i '/^override_install_langs=/d' /etc/yum.conf && \
     yum -y reinstall glibc-common && \
     yum clean all

   ENV LANG="ja_JP.UTF-8" \
       LANGUAGE="ja_JP:ja" \
       LC_ALL="ja_JP.UTF-8"

   ENV JAVA_HOME /etc/alternatives/jre
   RUN git clone https://github.com/sstephenson/rbenv.git ~/.rbenv
   RUN git clone https://github.com/sstephenson/ruby-build.git ~/.rbenv/plugins/ruby-build
   RUN git clone https://github.com/sstephenson/rbenv-gem-rehash.git ~/.rbenv/plugins/rbenv-gem-rehash
   RUN echo 'export PATH="$HOME/.rbenv/plugins/ruby-build/bin:$PATH"' >> ~/.bash_profile
   RUN echo 'export PATH="$HOME/.rbenv/bin:$PATH"' >> ~/.bash_profile
   RUN echo 'eval "$(rbenv init -)"' >> ~/.bash_profile
   RUN source ~/.bash_profile
   ENV PATH /root/.rbenv/bin:$PATH
   RUN rbenv -v
   RUN rbenv install 2.0.0-p648
   RUN rbenv global 2.0.0-p648
   RUN bash -l -c 'gem install asciidoctor'
   RUN git clone -b v1.3.3 https://github.com/Swagger2Markup/swagger2markup-cli.git && cd swagger2markup-cli && ./gradlew assemble
   RUN mkdir /work
   COPY ./convert.sh /root
   ENTRYPOINT ["sh", "/root/convert.sh"]

|br|

SwaggerSpec→asciidoc→HTML変換スクリプトは以下の通り。

|br|

.. sourcecode:: bash

   #!/bin/sh

   set -e

   java -jar /swagger2markup-cli/build/libs/swagger2markup-cli-1.3.3.jar convert -i $1 -f tmp
   source ~/.bash_profile && asciidoctor -a toc=left tmp.adoc
   rm tmp.adoc
   mv tmp.html $2

|br|

コンテナイメージをビルドしたのち、以下のコマンドを実行し、SwaggerSpecから静的HTMLを生成する。

|br|

.. sourcecode:: bash

   docker run -v $(pwd):/work debugroom/swagger-static-html:latest /work/swagger.yaml /work/swagger.html

|br|

.. note:: docker runのvオプションでホストマシンのカレントディレクトリを、コンテナの/workへマウントすることができる。コンテナイメージのマウント先/work/＊＊＊＊(ローカルのカレントファイル)でインプットファイルを指定し、アウトプット/work/swagger.htmlとして出力できる。
