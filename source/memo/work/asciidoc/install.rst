

ASCIIDocの導入とGitHubスライドページでの公開
================================================================================

Rubyのインストール(MacOS X)
--------------------------------------------------------------------------------

.. note:: Mac High Sierraでは、システムにデフォルトでインストールされているRubyを使用して管理者権限が必要など煩雑になるので、rbenvをインストールし、ユーザーローカル環境にRubyをインストールする。

   1. homebrewでrbenvをインストール

   .. sourcecode:: bash
      :linenos:

      > brew install rbenv ruby-build

   2. ターミナルを開いたときに、rbenv initコマンドを実行するよう、~/.bash_profileに以下のコマンドを追記する。

   .. sourcecode:: bash
      :linenos:

      # rbenv PATH

      if which rbenv > /dev/null; then eval "$(rbenv init -)"; fi
      export PATH="$HOME/.rbenv/bin:$PATH"
      eval "$(rbenv init -)"

   3. 設定を読み込み、rbenvのバージョンやインストールされているRubyのバージョンを確認する。

   .. sourcecode:: bash
      :linenos:

      > source ~/.bash_profile
      > rbenv -v
      > rbenv versions
        * system (set by /Users/XXXXXX/.rbenv/version)

   3. 最新版のRubyのバージョンを確認し、インストール、使用するRubyを変更する。

   .. sourcecode:: bash
      :linenos:

      > rbenv install -l
        ...
        2.6.0
        2.6.1
        2.6.2
        2.6.3
        ...

      > rbenv install 2.6.1
      > rbenv global 2.6.1
      > rbenv versions
        system
      * 2.6.1 (set by /Users/XXXXXX/.rbenv/version)

AsciiDocをインストール

.. sourcecode:: bash
   :linenos:

   > gem install asciidoctor
   > gem install asciidoctor-diagram
   > gem install --pre asciidoctor-pdf
   > asciidoctor --version

HTMLを生成

.. sourcecode:: bash
   :linenos:

   > asciidoctor sample.adoc
