package jp.ac.nii.sample;

import jp.ac.nii.sample.utility.EdubaseCloudController;
import jp.ac.nii.sample.utility.PropertyLoader;

public class ShutdownCluster {
    public static void main(String[] args) throws Exception {

        // �ݒ�t�@�C���̓ǂݍ��݂����܂�
        PropertyLoader config = new PropertyLoader("src/config.properties");
        String awsSecretKey = config.getProperty("secretKey");
        String awsAccessId = config.getProperty("accessKey");
        String hostName = config.getProperty("hostName");
        String resourcePrefix = config.getProperty("resourcePrefix");
        String keyname = config.getProperty("keyName");
        int port = Integer.parseInt(config.getProperty("port"));
        // edubase Cloud�̃R���g���[�������������܂�
        EdubaseCloudController cloud = new EdubaseCloudController(awsAccessId,
                awsSecretKey, hostName, resourcePrefix, port);

        // ���ׂẴC���X�^���X���I�����܂�
        cloud.shutdownAllRunningInstances(keyname);
    }
}
