.. include:: ../module.txt

.. _section-app-infra-logging-label:


ログ処理方式
=================================

.. _section-app-infra-logging-format-label:

出力ログの種類とフォーマット
--------------------------------------------------------------

システムで出力するログの要件を以下の通りまとめる。

.. raw:: html

   <table border=1 cellpadding=3 border-collapse="collapse" style="font-size : 12px;">
     <tr bgcolor="#33CCFF">
       <th width="100px">ログ種別</th>
       <th>ログレベル</th>
       <th width="250px">出力フォーマット</th>
       <th>出力タイミング</th>
       <th>出力先</th>
       <th>備考</th>
     </tr>
     <tr>
       <td>
       システム運転ログ<br>
       （開局／閉局時のメッセージプロセスの起動／停止時のメッセージなど）
       </td>
       <td>INFO</td>
       <td>
       時刻（yyyy-mm-dd hh:mm:ss.sss）<br>
       サーバ名<br>
       スレッドID<br>
       トラックID<br>
       ログレベル<br>
       ロガー名<br>
       メッセージ[メッセージID, メッセージ本文]<br>
       </td>
       <td>サービス</td>
       <td>ファイル</td>
       <td></td>
     </tr>
     <tr>
       <td>システムエラーログ</td>
       <td>ERROR</td>
       <td>
       時刻（yyyy-mm-dd hh:mm:ss.sss）<br>
       サーバ名<br>
       スレッドID<br>
       トラックID<br>
       ログレベル<br>
       ロガー名<br>
       メッセージ[メッセージID, メッセージ本文, スタックトレース]<br>
       </td>
       <td>ー</td>
       <td>ファイル</td>
       <td>ー</td>
     <tr>
       <td>業務エラーログ</td>
       <td>WARN</td>
       <td>
       時刻（yyyy-mm-dd hh:mm:ss.sss）<br>
       サーバ名<br>
       スレッドID<br>
       トラックID<br>
       ログレベル<br>
       ロガー名<br>
       メッセージ[メッセージID, メッセージ本文]<br>
       </td>
       <td>
       サービス
       </td>
       <td>ファイル</td>
       <td></td>
     </tr>
     <tr>
       <td>監査ログ</td>
       <td>INFO</td>
       <td>
       監査ログテーブル<br>
       セッションID<br>
       作成日時<br>
       サーバ名<br>
       ユーザID<br>
       ＩＰアドレス(要求元)<br>
       呼び出し先コントローラ名orフォームのaction<br>
       呼び出し先メソッド名orフォームのmethod<br>
       更新日時<br>
       </td>
       <td>Controller</td>
       <td> DB</td>
       <td></td>
     </tr>
     <tr>
        <td>アクセスログ</td>
        <td>INFO</td>
        <td>
        時刻（yyyy-mm-dd hh:mm:ss.sss）<br>
        サーバ名<br>
        スレッドID<br>
        トラックID<br>
        セッションID<br>
        ユーザID<br>
        ＩＰアドレス(要求元)<br>
        ログレベル<br>
        ロガー名<br>
        呼び出し先コントローラ名<br>
        呼び出し先メソッド名<br>
        メッセージ本文（メッセージID、メッセージ本文など）<br>
        </td>
		    <td>Controller</td>
        <td>ファイル</td>
        <td></td>
      </tr>
      <tr>
        <td> デバッグログ</td>
        <td>DEBUG</td>
        <td>
        時刻（yyyy-mm-dd hh:mm:ss.sss）<br>
        サーバ名<br>
        スレッドID<br>
        トラックID<br>
        ログレベル<br>
        ロガー名<br>
        任意のメッセージ<br>
        </td>
        <td>ー</td>
        <td>ファイル</td>
        <td>ー</td>
     </tr>
     <tr>
        <td>性能ログ</td>
        <td>TRACE</td>
        <td>
        時刻（yyyy-mm-dd hh:mm:ss.sss）<br>
        サーバ名<br>
        スレッドID<br>
        トラックID<br>
        ログレベル<br>
        ロガー名<br>
        呼び出し先コントローラ名<br>
        呼び出し先メソッド名<br>
        処理時間<br>
        </td>
        <td>Controller</td>
        <td>ファイル</td>
        <td></td>
     </tr>
   </table>

|
