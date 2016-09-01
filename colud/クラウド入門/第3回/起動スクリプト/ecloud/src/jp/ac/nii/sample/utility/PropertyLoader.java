package jp.ac.nii.sample.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

// 指定された設定ファイルを読み込むユーティリティクラスです
public class PropertyLoader extends Properties {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PropertyLoader(String filename) {
		try {
			load(filename);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void load(String filename) throws Exception {
		InputStream inputStream = new FileInputStream(new File(filename));
		load(inputStream);
	}

	// 設定ファイルに記述されたイメージをすべて一覧にして返します
	public List<String> getImageList() {
		List<String> imageList = new ArrayList<String>();
		imageList.add(getProperty("dbImageId"));
		imageList.add(getProperty("appImageId"));
		imageList.add(getProperty("webImageId"));
		imageList.add(getProperty("lbImageId"));
		return imageList;
	}

	// 設定ファイルに記述されたイメージを名前つきですべて返します
	public HashMap<String, String> getImageMap() {
		HashMap<String, String> imageMap = new HashMap<String, String>();
		imageMap.put("dbImageId", getProperty("dbImageId"));
		imageMap.put("appImageId", getProperty("appImageId"));
		imageMap.put("webImageId", getProperty("webImageId"));
		imageMap.put("lbImageId", getProperty("lbImageId"));
		return imageMap;
	}

	// 設定ファイルに記述されたセキュリティグループを一覧にして返します
	public List<String> getSecurityGroups() {
		List<String> ret = new ArrayList<String>();
		ret.add(getProperty("securityGroup"));
		return ret;
	}

}
