package jp.ac.nii.typica;

import java.util.ArrayList;
import java.util.List;

import com.xerox.amazonws.ec2.ImageDescription;
import com.xerox.amazonws.ec2.Jec2;
import com.xerox.amazonws.ec2.ReservationDescription;

public class EdubaseCloudController {
	static Jec2 ec2;
	
	// Eucalyptusへの接続設定を行います
	private static void init() throws Exception {
		String awsAccessId = "awsAccessId";
		String awsSecretKey = "awsSecretKey";
		String endpoint="hostName";
		int port = 8773;
		ec2 = new Jec2(awsAccessId, awsSecretKey, false, endpoint, port);
		ec2.setSignatureVersion(1);
		ec2.setResourcePrefix("/services/Eucalyptus");
    }
	//イメージのリストを取得します
	public static List<ImageDescription> describeImages() throws Exception {
		List<String> params = new ArrayList<String>();		
		List<ImageDescription> images = ec2.describeImages(params);
		return images;
	}
	
	//インスタンスを起動します
	public static void runInstances(String imageId) throws Exception {
	}
	
	//インスタンスを終了します
	public static void terminateInstances(String instanceId) throws Exception {
	}
	
	//起動しているインスタンスの一覧を取得します
	public static List<ReservationDescription> describeInstances() throws Exception {
		List<ReservationDescription> instances = null;
		return instances;
	}
	
	public static void main(String[] args) throws Exception {
		init();
		List<ImageDescription> images = describeImages();
		for(ImageDescription img : images) {
			System.out.println(img);
		}
	}
}
