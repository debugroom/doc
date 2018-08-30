package jp.ac.nii.sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.ac.nii.sample.utility.EdubaseCloudController;
import jp.ac.nii.sample.utility.StartupProcess;
import jp.ac.nii.sample.utility.PropertyLoader;

import com.xerox.amazonws.ec2.ReservationDescription;
import com.xerox.amazonws.ec2.ReservationDescription.Instance;

public class ScaleoutWebAppSet2 {

    // �𓚁F �X�P�[���A�E�g
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
        List<String> instanceList = new ArrayList<String>();
        HashMap<String, String> imageMap = config.getImageMap();
        String keyName = config.getProperty("keyName");
        List<String> securityGroups = config.getSecurityGroups();

        // edubase Cloud�̃R���g���[�������������܂�
        EdubaseCloudController cloud = new EdubaseCloudController(awsAccessId,
                awsSecretKey, hostName, resourcePrefix, port);

        // �}�V���C���[�W���̃T�[�o���C���X�^���X�Ƃ��ĂP�䂸�N�����܂�
        String appInstanceId = cloud.runInstances(imageMap.get("appImageId"), keyName, securityGroups);
        String webInstanceId = cloud.runInstances(imageMap.get("webImageId"), keyName, securityGroups);
        instanceList.add(appInstanceId);
        instanceList.add(webInstanceId);

        // �C���X�^���X�̏�Ԃ����ׂ�running�ɂȂ�܂ő҂��܂��B
        cloud.waitInstance(instanceList);

        // �N�����ꂽ�C���X�^���X�̏����擾���܂�
        Instance appInstance = cloud.describeInstance(appInstanceId);
        Instance webInstance = cloud.describeInstance(webInstanceId);

        // ���ɋN�����Ă���PostgreSQL(�f�[�^�x�[�X�T�[�o)��nginx(���[�h�o�����T�[)�̃C���X�^���X�̏����擾���܂�
        String dbImageId = imageMap.get("dbImageId");
        String lbImageId = imageMap.get("lbImageId");
        Instance dbInstance = null;
        Instance lbInstance = null;
        List<ReservationDescription> instances = cloud.describeInstances();
        for (ReservationDescription res : instances) {
            for (Instance inst : res.getInstances()) {
                // PostgreSQL�̃C���[�WID�Ɠ���ŁA�L�[��������ȃC���X�^���X���ǂ������m�F����
                if (dbImageId.equals(inst.getImageId()) && keyName.equals(inst.getKeyName())) {
                    dbInstance = inst;
                }
                // nginx�̃C���[�WID�Ɠ���ŁA�L�[��������ȃC���X�^���X���ǂ������m�F����
                if (lbImageId.equals(inst.getImageId()) && keyName.equals(inst.getKeyName())) {
                    lbInstance = inst;
                }
            }
        }
        if (dbInstance == null || lbInstance == null) {
            System.err.println("PostgreSQL(Database) or nginx(Load Balancer) not found.\n");
            System.exit(1);
        }

        // �C���X�^���X���ꂼ���IP�A�h���X���擾���܂�
        String dbPrivateDnsName = dbInstance.getPrivateDnsName();
        String appDnsName = appInstance.getDnsName();
        String appPrivateDnsName = appInstance.getPrivateDnsName();
        String webPrivateDnsName = webInstance.getPrivateDnsName();
        String webDnsName = webInstance.getDnsName();
        String lbDnsName = lbInstance.getDnsName();

        // �T�[�r�X�ɕK�v�Ȑݒ�t�@�C�����u���Ă���ꏊ���擾���܂�
        String srcDir = config.getProperty("srcDir");

        // Tomcat(�A�v���P�[�V�����T�[�o)���N�����܂�
        StartupProcess.appServer(appDnsName, dbPrivateDnsName, srcDir);

        // Apache(Web�T�[�o)���N�����܂�
        StartupProcess.webServer(webDnsName, appPrivateDnsName);

        // nginx(���[�h�o�����T�[)���N�����܂�
        StartupProcess.lbServer(lbDnsName, webPrivateDnsName, srcDir, true);

    }
}