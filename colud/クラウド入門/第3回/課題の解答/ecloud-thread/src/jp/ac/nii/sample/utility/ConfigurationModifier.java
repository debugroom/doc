package jp.ac.nii.sample.utility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;

public class ConfigurationModifier {

	private static final PropertyLoader config = new PropertyLoader("src/config.properties");
	private static final File keyFile = new File(config.getProperty("keyFile"));
	private static final int BUFFER_SIZE = 4096;
	
	//コマンドの実行結果を表示します。
	private static String streamToString(InputStream in) throws IOException {
		byte[] buf = new byte[BUFFER_SIZE];
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int len;
		while((len = in.read(buf, 0, BUFFER_SIZE))> 0 )out.write(buf, 0, len);
		return out.toString();
	}
	
	//対象のhostとのコネクションをはります。
	private static Connection connectionHost(String targetHost){
		Connection conn = new Connection(targetHost);
		Integer res = null;
		do {
			try {
				conn.connect();
				res = 0;
				Thread.sleep(10000);
			}catch (Exception e) {
				res = 1;
				System.out.println("connection retry ...");
			}
		}while(res != 0);
		return conn;
	}

	// SSHを用いてリモートでコマンドを実行します
	public static void ssh(String user, String targetHost, String command)
			throws IOException {
		Connection conn = connectionHost(targetHost);
		conn.authenticateWithPublicKey(user, keyFile, null);
		Session session = conn.openSession();
		session.execCommand(command);
		System.out.println(streamToString(session.getStdout()));
		session.close();
		conn.close();
	}

	// SCPを用いてファイルを転送します
	public static void scp(String sourceFile, String user, String targetHost,
			String distinationPath) throws IOException {
		Connection conn = connectionHost(targetHost);
		conn.authenticateWithPublicKey(user, keyFile, null);
		SCPClient scp = conn.createSCPClient();
		scp.put(sourceFile, distinationPath);
		conn.close();
	}
}
