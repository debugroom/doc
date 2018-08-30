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

        // �e�탆�[�e�B���e�B�N���X�̏��������s���܂�

        // �ݒ�t�@�C���̓ǂݍ��݂����܂�
        PropertyLoader config = new PropertyLoader("src/config.properties");

        // edubase Cloud�ւ̃A�N�Z�X�����擾���܂�
        String awsSecretKey = config.getProperty("secretKey");
        String awsAccessId = config.getProperty("accessKey");
        String hostName = config.getProperty("hostName");
        String resourcePrefix = config.getProperty("resourcePrefix");
        int port = Integer.parseInt(config.getProperty("port"));

        // �N������}�V���C���[�W�ƁA�N����Ɏg��SSH�L�[�A�Z�L�����e�B�O���[�v�����擾���܂�
        List<String> imageList = config.getImageList();
        List<String> instanceList = new ArrayList<String>();
        HashMap<String, String> imageMap = config.getImageMap();
        HashMap<String, String> instanceMap = new HashMap<String, String>();
        String keyName = config.getProperty("keyName");
        List<String> securityGroups = config.getSecurityGroups();

        // edubase Cloud�̃R���g���[�������������܂�
        EdubaseCloudController cloud = new EdubaseCloudController(awsAccessId,
                awsSecretKey, hostName, resourcePrefix, port);

        // �}�V���C���[�W���̃T�[�o���C���X�^���X�Ƃ��ĂP�䂸�N�����܂�
        for (String imageId : imageList) {
            String instanceId = cloud.runInstances(imageId, keyName,
                    securityGroups);
            instanceMap.put(imageId, instanceId);
            instanceList.add(instanceId);
        }

        // �C���X�^���X�̏�Ԃ����ׂ�running�ɂȂ�܂ő҂��܂��B
        cloud.waitInstance(instanceList);

        // �N�����ꂽ�C���X�^���X�̏����擾���܂�
        Instance dbInstance = cloud.describeInstance(instanceMap.get(imageMap
                .get("dbImageId")));
        Instance appInstance = cloud.describeInstance(instanceMap.get(imageMap
                .get("appImageId")));
        Instance webInstance = cloud.describeInstance(instanceMap.get(imageMap
                .get("webImageId")));
        Instance lbInstance = cloud.describeInstance(instanceMap.get(imageMap
                .get("lbImageId")));

        // �C���X�^���X���ꂼ���IP�A�h���X���擾���܂�
        String dbPrivateDnsName = dbInstance.getPrivateDnsName();
        String appDnsName = appInstance.getDnsName();
        String appPrivateDnsName = appInstance.getPrivateDnsName();
        String webPrivateDnsName = webInstance.getPrivateDnsName();
        String webDnsName = webInstance.getDnsName();
        String lbDnsName = lbInstance.getDnsName();

        // �T�[�r�X�ɕK�v�Ȑݒ�t�@�C�����u���Ă���ꏊ���擾���܂�
        String srcDir = config.getProperty("srcDir");

        //File keyFile = new File(config.getProperty("keyFile"));
        // Tomcat(�A�v���P�[�V�����T�[�o)���N�����܂�
        StartupProcess.appServer(appDnsName, dbPrivateDnsName, srcDir);

        // Apache(Web�T�[�o)���N�����܂�
        StartupProcess.webServer(webDnsName, appPrivateDnsName);

        // nginx(���[�h�o�����T�[)���N�����܂�
        StartupProcess.lbServer(lbDnsName, webPrivateDnsName, srcDir, false);

    }
}
