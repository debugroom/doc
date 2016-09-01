package jp.ac.nii.sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.ac.nii.sample.utility.EdubaseCloudController;
import jp.ac.nii.sample.utility.StartupProcess;
import jp.ac.nii.sample.utility.PropertyLoader;

import com.xerox.amazonws.ec2.ReservationDescription;
import com.xerox.amazonws.ec2.ReservationDescription.Instance;

public class ScaleoutWebAppSet {

	// 演習：スケールアウトを実装する
	public static void main(String[] args) throws Exception {

		// 各種ユーティリティクラスの初期化を行います

		// 設定ファイルの読み込みをします
		PropertyLoader config = new PropertyLoader("src/config.properties");

		// edubase Cloudへのアクセス情報を取得します
		String awsSecretKey = config.getProperty("secretKey");
		String awsAccessId = config.getProperty("accessKey");
		String hostName = config.getProperty("hostName");
		String resourcePrefix = config.getProperty("resourcePrefix");
		int port = Integer.parseInt(config.getProperty("port"));

		// 起動するマシンイメージと、起動後に使うSSHキー、セキュリティグループ名を取得します
		List<String> imageList = config.getImageList();
		HashMap<String, String> imageMap = config.getImageMap();
		String keyName = config.getProperty("keyName");
		List<String> securityGroups = config.getSecurityGroups();

		// edubase Cloudのコントローラを初期化します
		EdubaseCloudController cloud = new EdubaseCloudController(awsAccessId,
				awsSecretKey, hostName, resourcePrefix, port);

		// マシンイメージ分のサーバをインスタンスとして１台ずつ起動します
		// Tomcat(アプリケーションサーバ)を起動する
		String appImageId = imageMap.get("appImageId"); // Tomcat(アプリケーションサーバ)のマシンイメージ取得
		// TODO: インスタンスを起動してください
		
		// Apache(Webサーバ)を起動する
		String webImageId = imageMap.get("webImageId"); // Apache(Webサーバ)のマシンイメージ取得
		// TODO: インスタンスを起動してください
		
		// TODO: インスタンスの状態がすべてrunningになるまで待ちます。

		// TODO: 起動されたインスタンスの情報を取得します		
		Instance appInstance = null;
		Instance webInstance = null;

		// TODO: すでに起動しているPostgreSQL(データベースサーバ)と、nginx(ロードバランサー)のインスタンスの情報を取得します
		Instance dbInstance = null;
		Instance lbInstance = null;
		
		// インスタンスそれぞれのIPアドレスを取得します
		String dbPrivateDnsName = dbInstance.getPrivateDnsName();
		String appDnsName = appInstance.getDnsName();
		String appPrivateDnsName = appInstance.getPrivateDnsName();
		String webPrivateDnsName = webInstance.getPrivateDnsName();
		String webDnsName = webInstance.getDnsName();
		String lbDnsName = lbInstance.getDnsName();

		// サービスに必要な設定ファイルが置いてある場所を取得します
		String srcDir = config.getProperty("srcDir");
		
		// Tomcat(アプリケーションサーバ)を起動します
		// TODO: StartupProcess.appServer(appDnsName, dbPrivateDnsName, srcDir);

		// Apache(Webサーバ)を起動します
		// TODO: StartupProcess.webServer(webDnsName, appPrivateDnsName);

		// nginx(ロードバランサー)を起動します
		// TODO: StartupProcess.lbServer(lbDnsName, webPrivateDnsName, srcDir, true);

	}
}