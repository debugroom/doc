package jp.ac.nii.sample;

import jp.ac.nii.sample.utility.PropertyLoader;

public class ScaleoutWebAppSet3 {
	public static void main(String[] args){
		// 設定ファイルの読み込みをします
		PropertyLoader config = new PropertyLoader("src/config.properties");
		// threadの数を取得する
		Integer threadNumber = new Integer(config.getProperty("threads"));
		// threadを作成する
		ScaleoutWebAppSetThread[] threads = new ScaleoutWebAppSetThread[threadNumber];
		// 並列でスケールアウトを実行する
		for(int i = 0; i < threadNumber; i++){
			threads[i] = new ScaleoutWebAppSetThread();
			threads[i].start();
		}
		// threadがすべて終了するまで待つ
		for(int i = 0; i < threadNumber; i++){
			try{
				threads[i].join();
			}catch (InterruptedException e) {
				System.out.println(e);
			}
		}
	}
}
