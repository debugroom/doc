package jp.ac.nii.sample.utility;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

// リモートで特定の手順を実行させるためのユーティリティです
public class StartupProcess {

	// Tomcat(アプリケーションサーバ)を起動します
	public static void appServer(String appDnsName, String dbPrivateDnsName,
			String srcDir) throws IOException {

		// hibernateの設定を行います
		try {
			// テンプレートファイルの読み込み
			FileReader fr = new FileReader("src/hibernate-template.xml");
			BufferedReader br = new BufferedReader(fr);

			StringBuffer tmp_str = new StringBuffer();
			String tmp;
			while ((tmp = br.readLine()) != null) {
				tmp_str.append(tmp);
				tmp_str.append("\n");
			}
			br.close();

			// データベースサーバの接続先IPアドレスをテンプレートに埋め込みます
			String xmlValue = String.format(tmp_str.toString(),
					dbPrivateDnsName);

			// 設定ファイルとして書き出します
			FileWriter fw = new FileWriter("src/hibernate.cfg.xml");
			fw.write(xmlValue);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("created hibernate.cfg.xml");

		// 完成した設定ファイルをインスタンスへscpし、上書きします
		ConfigurationModifier.scp(srcDir + "hibernate.cfg.xml", "root",
				appDnsName,
				"/opt/sample-kejiban/src/main/webapp/WEB-INF/classes/");
		System.out.println("send file hibernate.cfg.xml: " + appDnsName);

		// Tomcatを起動します
		ConfigurationModifier.ssh("root", appDnsName,
				"/etc/init.d/tomcat start");
		System.out.println("app server start: " + appDnsName);

	}

	// Apache(Webサーバ)を起動します
	public static void webServer(String webDnsName, String appPrivateDnsName)
			throws IOException {

		// Tomcatの接続先IPを指定するため、Apacheが起動するインスタンスに対し設定ファイルを直接変更します
		ConfigurationModifier
				.ssh("root",
						webDnsName,
						String.format(
								"echo ProxyPass /sample-kejiban ajp://%1$1s:8009/sample-kejiban >> /etc/httpd/conf.d/proxy_ajp.conf",
								appPrivateDnsName));
		System.out.println("Added ProxyPass: " + webDnsName);

		// Apacheを再起動します
		ConfigurationModifier.ssh("root", webDnsName,
				"/etc/init.d/httpd restart");
		System.out.println("web server restart: " + webDnsName);
	}

	// nginx(ロードバランサー)を起動します
	public static void lbServer(String lbDnsName, String webPrivateDnsName,
			String srcDir, Boolean update) throws IOException {

		// ロードバランサーを新規に起動する場合はテンプレートを用いますが、
		// 既存の設定を更新する場合は出力結果であるnginx.confファイルを編集します。
		String tmpFile = (update) ? "src/nginx.conf"
				: "src/nginx-template.conf";

		try {
			// 指定された設定ファイルを開きます
			FileReader fr = new FileReader(tmpFile);
			BufferedReader br = new BufferedReader(fr);

			StringBuffer tmp_str = new StringBuffer();
			String tmp;
			while ((tmp = br.readLine()) != null) {
				tmp_str.append(tmp);
				tmp_str.append("\n");
				// 設定ファイルの変更該当箇所を検出します
				if (tmp.endsWith("upstream backend {")) {
					// バランスする先にWebサーバの接続先IPアドレスを指定します
					tmp_str.append(String.format("	server %1$1s;",
							webPrivateDnsName));
					tmp_str.append("\n");
				}
			}
			br.close();
			
			// nginxの設定ファイルとして出力します
			String confValue = tmp_str.toString();
			FileWriter fw = new FileWriter("src/nginx.conf");
			fw.write(confValue);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		// SCPでnginxが動いているインスタンスへ設定ファイルを転送、上書きします
		ConfigurationModifier.scp(srcDir + "nginx.conf", "root", lbDnsName,
				"/usr/local/nginx/conf/");

		// nginxを再起動します
		ConfigurationModifier.ssh("root", lbDnsName,
				"/etc/init.d/nginx restart");
		System.out.println("lb server restart: " + lbDnsName);
	}
}
