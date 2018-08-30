package jp.ac.nii.sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.ac.nii.sample.utility.EdubaseCloudController;
import jp.ac.nii.sample.utility.StartupProcess;
import jp.ac.nii.sample.utility.PropertyLoader;

import com.xerox.amazonws.ec2.ReservationDescription;
import com.xerox.amazonws.ec2.ReservationDescription.Instance;

public class ScaleoutWebAppSet {

    // ���K�F�X�P�[���A�E�g����������
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
        HashMap<String, String> imageMap = config.getImageMap();
        String keyName = config.getProperty("keyName");
        List<String> securityGroups = config.getSecurityGroups();

        // edubase Cloud�̃R���g���[�������������܂�
        EdubaseCloudController cloud = new EdubaseCloudController(awsAccessId,
                awsSecretKey, hostName, resourcePrefix, port);

        // �}�V���C���[�W���̃T�[�o���C���X�^���X�Ƃ��ĂP�䂸�N�����܂�
        // Tomcat(�A�v���P�[�V�����T�[�o)���N������
        String appImageId = imageMap.get("appImageId"); // Tomcat(�A�v���P�[�V�����T�[�o)�̃}�V���C���[�W�擾
        // TODO: �C���X�^���X���N�����Ă�������

        // Apache(Web�T�[�o)���N������
        String webImageId = imageMap.get("webImageId"); // Apache(Web�T�[�o)�̃}�V���C���[�W�擾
        // TODO: �C���X�^���X���N�����Ă�������

        // TODO: �C���X�^���X�̏�Ԃ����ׂ�running�ɂȂ�܂ő҂��܂��B

        // TODO: �N�����ꂽ�C���X�^���X�̏����擾���܂�
        Instance appInstance = null;
        Instance webInstance = null;

        // TODO: ���łɋN�����Ă���PostgreSQL(�f�[�^�x�[�X�T�[�o)�ƁAnginx(���[�h�o�����T�[)�̃C���X�^���X�̏����擾���܂�
        Instance dbInstance = null;
        Instance lbInstance = null;

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
        // TODO: StartupProcess.appServer(appDnsName, dbPrivateDnsName, srcDir);

        // Apache(Web�T�[�o)���N�����܂�
        // TODO: StartupProcess.webServer(webDnsName, appPrivateDnsName);

        // nginx(���[�h�o�����T�[)���N�����܂�
        // TODO: StartupProcess.lbServer(lbDnsName, webPrivateDnsName, srcDir, true);

    }
}