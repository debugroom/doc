package jp.ac.nii.sample.utility;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

// �����[�g�œ���̎菇�����s�����邽�߂̃��[�e�B���e�B�ł�
public class StartupProcess {

    // Tomcat(�A�v���P�[�V�����T�[�o)���N�����܂�
    public static void appServer(String appDnsName, String dbPrivateDnsName,
                                 String srcDir) throws IOException {

        // hibernate�̐ݒ���s���܂�
        try {
            // �e���v���[�g�t�@�C���̓ǂݍ���
            FileReader fr = new FileReader("src/hibernate-template.xml");
            BufferedReader br = new BufferedReader(fr);

            StringBuffer tmp_str = new StringBuffer();
            String tmp;
            while ((tmp = br.readLine()) != null) {
                tmp_str.append(tmp);
                tmp_str.append("\n");
            }
            br.close();

            // �f�[�^�x�[�X�T�[�o�̐ڑ���IP�A�h���X���e���v���[�g�ɖ��ߍ��݂܂�
            String xmlValue = String.format(tmp_str.toString(),
                    dbPrivateDnsName);

            // �ݒ�t�@�C���Ƃ��ď����o���܂�
            FileWriter fw = new FileWriter("src/hibernate.cfg.xml");
            fw.write(xmlValue);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println("created hibernate.cfg.xml");

        // ���������ݒ�t�@�C�����C���X�^���X��scp���A�㏑�����܂�
        ConfigurationModifier.scp(srcDir + "hibernate.cfg.xml", "root",
                appDnsName,
                "/opt/sample-kejiban/src/main/webapp/WEB-INF/classes/");
        System.out.println("send file hibernate.cfg.xml: " + appDnsName);

        // Tomcat���N�����܂�
        ConfigurationModifier.ssh("root", appDnsName,
                "/etc/init.d/tomcat start");
        System.out.println("app server start: " + appDnsName);

    }

    // Apache(Web�T�[�o)���N�����܂�
    public static void webServer(String webDnsName, String appPrivateDnsName)
            throws IOException {

        // Tomcat�̐ڑ���IP���w�肷�邽�߁AApache���N������C���X�^���X�ɑ΂��ݒ�t�@�C���𒼐ڕύX���܂�
        ConfigurationModifier
                .ssh("root",
                        webDnsName,
                        String.format(
                                "echo ProxyPass /sample-kejiban ajp://%1$1s:8009/sample-kejiban >> /etc/httpd/conf.d/proxy_ajp.conf",
                                appPrivateDnsName));
        System.out.println("Added ProxyPass: " + webDnsName);

        // Apache���ċN�����܂�
        ConfigurationModifier.ssh("root", webDnsName,
                "/etc/init.d/httpd restart");
        System.out.println("web server restart: " + webDnsName);
    }

    // nginx(���[�h�o�����T�[)���N�����܂�
    public static void lbServer(String lbDnsName, String webPrivateDnsName,
                                String srcDir, Boolean update) throws IOException {

        // ���[�h�o�����T�[��V�K�ɋN������ꍇ�̓e���v���[�g��p���܂����A
        // �����̐ݒ���X�V����ꍇ�͏o�͌��ʂł���nginx.conf�t�@�C����ҏW���܂��B
        String tmpFile = (update) ? "src/nginx.conf"
                : "src/nginx-template.conf";

        try {
            // �w�肳�ꂽ�ݒ�t�@�C�����J���܂�
            FileReader fr = new FileReader(tmpFile);
            BufferedReader br = new BufferedReader(fr);

            StringBuffer tmp_str = new StringBuffer();
            String tmp;
            while ((tmp = br.readLine()) != null) {
                tmp_str.append(tmp);
                tmp_str.append("\n");
                // �ݒ�t�@�C���̕ύX�Y���ӏ������o���܂�
                if (tmp.endsWith("upstream backend {")) {
                    // �o�����X������Web�T�[�o�̐ڑ���IP�A�h���X���w�肵�܂�
                    tmp_str.append(String.format("	server %1$1s;",
                            webPrivateDnsName));
                    tmp_str.append("\n");
                }
            }
            br.close();

            // nginx�̐ݒ�t�@�C���Ƃ��ďo�͂��܂�
            String confValue = tmp_str.toString();
            FileWriter fw = new FileWriter("src/nginx.conf");
            fw.write(confValue);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // SCP��nginx�������Ă���C���X�^���X�֐ݒ�t�@�C����]���A�㏑�����܂�
        ConfigurationModifier.scp(srcDir + "nginx.conf", "root", lbDnsName,
                "/usr/local/nginx/conf/");

        // nginx���ċN�����܂�
        ConfigurationModifier.ssh("root", lbDnsName,
                "/etc/init.d/nginx restart");
        System.out.println("lb server restart: " + lbDnsName);
    }
}
