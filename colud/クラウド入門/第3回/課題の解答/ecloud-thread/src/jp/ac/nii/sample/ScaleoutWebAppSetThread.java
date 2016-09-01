package jp.ac.nii.sample;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.ac.nii.sample.utility.EdubaseCloudController;
import jp.ac.nii.sample.utility.PropertyLoader;
import jp.ac.nii.sample.utility.StartupProcess;

import com.xerox.amazonws.ec2.ReservationDescription;
import com.xerox.amazonws.ec2.ReservationDescription.Instance;

public class ScaleoutWebAppSetThread extends Thread {
	public void run(){
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
		List<String> instanceList = new ArrayList<String>();
		HashMap<String, String> imageMap = config.getImageMap();
		String keyName = config.getProperty("keyName");
		List<String> securityGroups = config.getSecurityGroups();

		// edubase Cloudのコントローラを初期化します
		EdubaseCloudController cloud = new EdubaseCloudController(awsAccessId,
				awsSecretKey, hostName, resourcePrefix, port);

		// マシンイメージ分のサーバをインスタンスとして１台ずつ起動します
		String appInstanceId = null;
		try {
			appInstanceId = cloud.runInstances(imageMap.get("appImageId"), keyName, securityGroups);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String webInstanceId = null;
		try {
			webInstanceId = cloud.runInstances(imageMap.get("webImageId"), keyName, securityGroups);
		} catch (Exception e) {
			e.printStackTrace();
		}
		instanceList.add(appInstanceId);
		instanceList.add(webInstanceId);
		
		// インスタンスの状態がすべてrunningになるまで待ちます。
		cloud.waitInstance(instanceList);

		// 起動されたインスタンスの情報を取得します		
		Instance appInstance = null;
		try {
			appInstance = cloud.describeInstance(appInstanceId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Instance webInstance = null;
		try {
			webInstance = cloud.describeInstance(webInstanceId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 既に起動しているPostgreSQL(データベースサーバ)とnginx(ロードバランサー)のインスタンスの情報を取得します
		String dbImageId = imageMap.get("dbImageId");
		String lbImageId = imageMap.get("lbImageId");
		Instance dbInstance = null;
		Instance lbInstance = null;
		List<ReservationDescription> instances = null;
		try {
			instances = cloud.describeInstances();
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (ReservationDescription res : instances){
			for (Instance inst : res.getInstances()){
				// PostgreSQLのイメージIDと同一で、キー情報も同一なインスタンスかどうかを確認する
				if (dbImageId.equals(inst.getImageId()) && keyName.equals(inst.getKeyName())) {
					dbInstance = inst;
				}
				// nginxのイメージIDと同一で、キー情報も同一なインスタンスかどうかを確認する
				if (lbImageId.equals(inst.getImageId()) && keyName.equals(inst.getKeyName())){
					lbInstance = inst;
				}
			}
		}
		if(dbInstance==null || lbInstance==null) {
			System.err.println("PostgreSQL(Database) or nginx(Load Balancer) not found.\n");
			System.exit(1);
		}

		// インスタンスそれぞれのIPアドレスを取得します
		String dbPrivateDnsName = dbInstance.getPrivateDnsName();
		String appDnsName = appInstance.getDnsName();
		String appPrivateDnsName = appInstance.getPrivateDnsName();
		String webPrivateDnsName = webInstance.getPrivateDnsName();
		String webDnsName = webInstance.getDnsName();
		String lbDnsName = lbInstance.getDnsName();

		// サービスに必要な設定ファイルが置いてある場所を取得します
		String srcDir = config.getProperty("srcDir");
		
		synchronized (this) {
			try {
				// Tomcat(アプリケーションサーバ)を起動します
				StartupProcess.appServer(appDnsName, dbPrivateDnsName, srcDir);

				// Apache(Webサーバ)を起動します
				StartupProcess.webServer(webDnsName, appPrivateDnsName);

				// nginx(ロードバランサー)を起動します
				StartupProcess.lbServer(lbDnsName, webPrivateDnsName, srcDir, true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
