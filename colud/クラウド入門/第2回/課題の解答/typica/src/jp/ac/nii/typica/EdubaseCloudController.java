package jp.ac.nii.typica;

import java.util.ArrayList;
import java.util.List;

import com.xerox.amazonws.ec2.ImageDescription;
import com.xerox.amazonws.ec2.InstanceType;
import com.xerox.amazonws.ec2.Jec2;
import com.xerox.amazonws.ec2.LaunchConfiguration;
import com.xerox.amazonws.ec2.ReservationDescription;
import com.xerox.amazonws.ec2.ReservationDescription.Instance;

public class EdubaseCloudController {
    static Jec2 ec2;

    // Eucalyptus�ւ̐ڑ��ݒ���s���܂�
    private static void init() throws Exception {
        String awsAccessId = "awsAccessId";
        String awsSecretKey = "awsSecretKey";
        String endpoint = "hostName";
        int port = 8773;
        ec2 = new Jec2(awsAccessId, awsSecretKey, false, endpoint, port);
        ec2.setSignatureVersion(1);
        ec2.setResourcePrefix("/services/Eucalyptus");
    }

    //�C���[�W�̃��X�g���擾���܂�
    public static List<ImageDescription> describeImages() throws Exception {
        List<String> params = new ArrayList<String>();
        List<ImageDescription> images = ec2.describeImages(params);
        return images;
    }

    //�C���X�^���X���N�����܂�
    public static void runInstances(String imageId) throws Exception {
        LaunchConfiguration lc = new LaunchConfiguration(imageId);
        InstanceType instanceType = InstanceType.DEFAULT;

        List<String> securityGroup = new ArrayList<String>();
        securityGroup.add("securityGroupName");

        lc.setInstanceType(instanceType);
        lc.setKeyName("keyName");
        lc.setMinCount(1);
        lc.setMaxCount(1);
        lc.setSecurityGroup(securityGroup);

        ec2.runInstances(lc);
    }

    //�C���X�^���X���I�����܂�
    public static void terminateInstances(String instanceId) throws Exception {
        ArrayList<String> instanceIds = new ArrayList<String>();
        instanceIds.add(instanceId);
        ec2.terminateInstances(instanceIds);
    }

    //�N�����Ă���C���X�^���X�̈ꗗ���擾���܂�
    public static List<ReservationDescription> describeInstances() throws Exception {
        List<String> params = new ArrayList<String>();
        List<ReservationDescription> instances = ec2.describeInstances(params);
        return instances;
    }

    public static void main(String[] args) throws Exception {
        init();
        //�C���[�W�̃��X�g���擾���܂�

        List<ImageDescription> images = describeImages();
        for (ImageDescription img : images) {
            System.out.println(img);
        }

        //�C���X�^���X���N�����܂�
        runInstances("********");

        //�N�����Ă���C���X�^���X�̈ꗗ���擾���܂�
        List<ReservationDescription> instances = describeInstances();
        for (ReservationDescription res : instances) {
            for (Instance inst : res.getInstances()) {
                System.out.println(inst);
            }
        }

        //�C���X�^���X���I�����܂�
        terminateInstances("********");

    }

}
