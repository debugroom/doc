

Sphinxの導入とGitHubページの作成
================================================================================

Sphinxのインストール(MacOS X)
--------------------------------------------------------------------------------

.. note::

   Mac High Sierraでは以下の手順でインストールを実行

   .. sourcecode:: bash
      :linenos:

      sudo easy_install pip
      sudo pip install sphinx --upgrade --ignore-installed six
      sudo pip install sphinx_rtd_theme

.. note::

   MacOS Sierra以降はHomebrewからpythonをインストールする。以下のコマンドにより、標準インストールされているpython(/usr/bin/python)ではなく、/usr/local/bin/pythonが使用されるようになる。

   .. sourcecode:: bash
      :linenos:

      brew update
      brew install python


1.portを使用して、Sphinxのインストールを行う。

.. sourcecode:: bash
   :linenos:

   sudo port install py27-sphinx


2. pythonコマンドの切り替え

.. sourcecode:: bash
   :linenos:

   sudo port select --set python python27
   sudo port select --set sphinx py27-sphinx

3. pipのインストール

.. sourcecode:: bash
   :linenos:

   curl -kL https://raw.github.com/pypa/pip/master/contrib/get-pip.py | python

何故か効かなかったので、easy_installにて再インストール

.. sourcecode:: bash
   :linenos:

   sudo easy_install pip

4. Sphinxの拡張テーマ(sphinx_rtd_theme)をインストール

.. sourcecode:: bash
   :linenos:

   sudo pip install sphinx_rtd_theme

上記のコマンドでは、portでインストールしたpython(/opt/local/Library/Frameworks/Python.framework/Versions/Current/bin/pip)ではなく、MacOSデフォルトで用意されているpyhtonの方(/opt/local/bin/pip)へインストールされる模様。portでインストールした方のpythonの方に適用する。

.. sourcecode:: bash
   :linenos:

   sudo /opt/local/Library/Frameworks/Python.framework/Versions/Current/bin/pip install sphinx_rtd_theme


Sphinxプロジェクトの作成
----------------------------

1. sphinx-quickstartコマンドでプロジェクトを作成する。

.. sourcecode:: bash
   :linenos:

   sphinx-quickstart

以下の要領で設定を行う。

::

   Welcome to the Sphinx 1.2.3 quickstart utility.

   Please enter values for the following settings (just press Enter to
   accept a default value, if one is given in brackets).

   Enter the root path for documentation.
   > Root path for the documentation [.]:

   You have two options for placing the build directory for Sphinx output.
   Either, you use a directory "_build" within the root path, or you separate
   "source" and "build" directories within the root path.
   > Separate source and build directories (y/n) [n]:y

   Inside the root directory, two more directories will be created; "_templates"
   for custom HTML templates and "_static" for custom stylesheets and other static
   files. You can enter another prefix (such as ".") to replace the underscore.
   > Name prefix for templates and static dir [_]:

   The project name will occur in several places in the built documentation.
   > Project name: test_sphinx
   > Author name(s): org.debugroom

   Sphinx has the notion of a "version" and a "release" for the
   software. Each version can have multiple releases. For example, for
   Python the version is something like 2.5 or 3.0, while the release is
   something like 2.5.1 or 3.0a1.  If you don't need this dual structure,
   just set both to the same value.
   > Project version: 0.1-SNAPSHOTS
   > Project release [0.1-SNAPSHOTS]:

   The file name suffix for source files. Commonly, this is either ".txt"
   or ".rst".  Only files with this suffix are considered documents.
   > Source file suffix [.rst]:

   One document is special in that it is considered the top node of the
   "contents tree", that is, it is the root of the hierarchical structure
   of the documents. Normally, this is "index", but if your "index"
   document is a custom template, you can also set this to another filename.
   > Name of your master document (without suffix) [index]:

   Sphinx can also add configuration for epub output:
   > Do you want to use the epub builder (y/n) [n]: y

   Please indicate if you want to use one of the following Sphinx extensions:
   > autodoc: automatically insert docstrings from modules (y/n) [n]: n
   > doctest: automatically test code snippets in doctest blocks (y/n) [n]:
   > intersphinx: link between Sphinx documentation of different projects (y/n) [n]:
   > todo: write "todo" entries that can be shown or hidden on build (y/n) [n]:
   > coverage: checks for documentation coverage (y/n) [n]:
   > pngmath: include math, rendered as PNG images (y/n) [n]:
   > mathjax: include math, rendered in the browser by MathJax (y/n) [n]:
   > ifconfig: conditional inclusion of content based on config values (y/n) [n]:
   > viewcode: include links to the source code of documented Python objects (y/n) [n]:

   A Makefile and a Windows command file can be generated for you so that you
   only have to run e.g. `make html' instead of invoking sphinx-build
   directly.
   > Create Makefile? (y/n) [y]:
   > Create Windows command file? (y/n) [y]:

   Creating file ./conf.py.
   Creating file ./index.rst.
   Creating file ./Makefile.
   Creating file ./make.bat.

   Finished: An initial directory structure has been created.

   You should now populate your master file ./index.rst and create other documentation
   source files. Use the Makefile to build the docs, like so:
      make builder
   where "builder" is one of the supported builders, e.g. html, latex or linkcheck.


2. 拡張テーマの適用のため、copy.pyに以下を追記・書き換えする。

.. sourcecode:: bash

   import sphinx_rtd_theme

   html_theme = 'sphinx_rtd_theme'
   html_theme_path = [sphinx_rtd_theme.get_html_theme_path()]

3. 後述するGithubページへの適用のため、ビルドのディレクトリ構成を変更する。以下の通りMakeFileを変更する。

   1. L8行目の_buildをカレントディレクトリに変更。

   .. sourcecode:: bash

      BUILDDIR = .

   2. L53行目のHTMLをビルドするためのコマンドの出力先の末尾のhtmlを削除し、カレントディレクトリに出力するよう修正

   .. sourcecode:: bash

      $(SPHINXBUILD) -b html $(ALLSPHINXOPTS) $(BUILDDIR)/


GitHub Pageの作成
----------------------------

1. Github上でレポジトリ(doc)を作成した後、masterと gh-pagesという名前のブランチを作成する。

   .. sourcecode:: bash

      git clone https://github.com/debugroom/doc.git
      cd doc
      git branch master
      git branch gh-pages

2. 作成したgh-pagesブランチに切り替え、sphinxsプロジェクトコピーし、htmlでビルドする。(index.htmlがルートに来るようにする。)

   .. sourcecode:: bash

      git checkout gh-pages
      cp somepath/test_sphinx .
      make html

3. ビルドして出来たHTMLをプッシュし、http://xxxxxx.github.io/projectへアクセスする。

   .. sourcecode:: bash

      git push origin gh-pages
