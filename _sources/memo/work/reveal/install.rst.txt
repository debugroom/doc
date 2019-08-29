

reveal-ckの導入とGitHubスライドページの作成
================================================================================

Rubyのインストール(MacOS X)
--------------------------------------------------------------------------------

.. note:: reveal-ckのインストールはgem installを使った方法が簡易だが、Mac High Sierraでは、システムにデフォルトでインストールされているRubyを使用して管理者権限が必要など煩雑になるので、rbenvをインストールし、ユーザーローカル環境にRubyをインストールする。

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

reveal-ckをインストール

.. sourcecode:: bash
   :linenos:

   > gem install reveal-ck
   > reveal-ck -v
     reveal-ck version 3.9.2


GitHub Pageの作成
-------------------------------------------------

1. Github上でレポジトリを作成した後、masterブランチを作成、docsフォルダを作成する。

   .. sourcecode:: bash

      git clone https://github.com/debugroom/sample-githubpages-slide
      cd sample-githubpages-slide
      git branch master
      mkdir docs

2. 配下にslides.mdを作成し、コミットし、githubへプッシュ

   .. sourcecode:: bash

      cd docs
      touch slides.md
      reveal-ck generate
      cd ../
      git add -A .
      git commit -m "Add slides.md."
      git push origin master

3. GitHubのレポジトリ上のSettingsからGitHub PagesでSourceをmaster branch/docs フォルダに設定してパブリッシュされたURLにslidesを加えてアクセス


.. figure:: img/github-project-settings.png
   :scale: 100%
