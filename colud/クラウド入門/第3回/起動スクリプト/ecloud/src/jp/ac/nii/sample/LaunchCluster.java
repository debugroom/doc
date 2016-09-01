package jp.ac.nii.sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.ac.nii.sample.utility.EdubaseCloudController;
import jp.ac.nii.sample.utility.StartupProcess;
import jp.ac.nii.sample.utility.PropertyLoader;

import com.xerox.amazonws.ec2.ReservationDescription.Instance;

public class LaunchCluster {

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
		List<String> instanceList = new ArrayList<String>();
		HashMap<String, String> imageMap = config.getImageMap();
		HashMap<String, String> instanceMap = new HashMap<String, String>();
		String keyName = config.getProperty("keyName");
		List<String> securityGroups = config.getSecurityGroups();

		// edubase Cloudのコントローラを初期化します
		EdubaseCloudController cloud = new EdubaseCloudController(awsAccessId,
				awsSecretKey, hostName, resourcePrefix, port);

		// マシンイメージ分のサーバをインスタンスとして１台ずつ起動します
		for (String imageId : imageList) {
			String instanceId = cloud.runInstances(imageId, keyName,
					securityGroups);
			instanceMap.put(imageId, instanceId);
			instanceList.add(instanceId);
		}
		
		// インスタンスの状態がすべてrunningになるまで待ちます。
		cloud.waitInstance(instanceList);

		// 起動されたインスタンスの情報を取得します
		Instance dbInstance = cloud.describeInstance(instanceMap.get(imageMap
				.get("dbImageId")));
		Instance appInstance = cloud.describeInstance(instanceMap.get(imageMap
				.get("appImageId")));
		Instance webInstance = cloud.describeInstance(instanceMap.get(imageMap
				.get("webImageId")));
		Instance lbInstance = cloud.describeInstance(instanceMap.get(imageMap
				.get("lbImageId")));

		// インスタンスそれぞれのIPアドレスを取得します
		String dbPrivateDnsName = dbInstance.getPrivateDnsName();
		String appDnsName = appInstance.getDnsName();
		String appPrivateDnsName = appInstance.getPrivateDnsName();
		String webPrivateDnsName = webInstance.getPrivateDnsName();
		String webDnsName = webInstance.getDnsName();
		String lbDnsName = lbInstance.getDnsName();

		// サービスに必要な設定ファイルが置いてある場所を取得します
		String srcDir = config.getProperty("srcDir");
		
		//File keyFile = new File(config.getProperty("keyFile"));
		// Tomcat(アプリケーションサーバ)を起動します
		StartupProcess.appServer(appDnsName, dbPrivateDnsName, srcDir);

		// Apache(Webサーバ)を起動します
		StartupProcess.webServer(webDnsName, appPrivateDnsName);

		// nginx(ロードバランサー)を起動します
		StartupProcess.lbServer(lbDnsName, webPrivateDnsName, srcDir, false);

	}
}
