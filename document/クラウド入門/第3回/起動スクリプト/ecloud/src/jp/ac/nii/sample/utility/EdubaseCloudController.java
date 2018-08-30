package jp.ac.nii.sample.utility;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xerox.amazonws.ec2.ImageDescription;
import com.xerox.amazonws.ec2.InstanceStateChangeDescription;
import com.xerox.amazonws.ec2.InstanceType;
import com.xerox.amazonws.ec2.Jec2;
import com.xerox.amazonws.ec2.LaunchConfiguration;
import com.xerox.amazonws.ec2.ReservationDescription;
import com.xerox.amazonws.ec2.ReservationDescription.Instance;

public class EdubaseCloudController {

    private Jec2 ec2;
    private Log log = LogFactory.getLog(EdubaseCloudController.class);

    public EdubaseCloudController(String awsAccessId, String awsSecretKey,
                                  String hostName, String resourcePrefix, int port) {
        try {
            init(awsAccessId, awsSecretKey, hostName, resourcePrefix, port);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void init(String awsAccessId, String awsSecretKey, String hostName,
                     String resourcePrefix, int port) throws Exception {
        ec2 = new Jec2(awsAccessId, awsSecretKey, false, hostName, port);
        ec2.setSignatureVersion(1);
        ec2.setResourcePrefix(resourcePrefix);
    }

    // Running��Ԃɂ���C���X�^���X�����ׂċ����I�����܂�
    public void shutdownAllRunningInstances(String keyname) throws Exception {
        List<ReservationDescription> instances = null;
        instances = describeInstances();

        ArrayList<String> instanceIds = new ArrayList<String>();
        for (ReservationDescription res : instances) {
            for (Instance inst : res.getInstances()) {
                if (inst.getState().equals("running") && inst.getKeyName().equals(keyname)) {
                    instanceIds.add(inst.getInstanceId());
                }
            }
        }

        if (instanceIds.size() != 0) {
            List<InstanceStateChangeDescription> result = ec2
                    .terminateInstances(instanceIds);
            log.info(result);
        }
    }

    // �w�肳�ꂽ�����ŃC���X�^���X���P��N�����܂�
    public String runInstances(String imageId, String keyName,
                               List<String> securityGroups) throws Exception {

        System.out.println("Running instance");

        LaunchConfiguration lc = new LaunchConfiguration(imageId);
        InstanceType instanceType = InstanceType.DEFAULT;

        lc.setInstanceType(instanceType);
        lc.setKeyName(keyName);
        lc.setMinCount(1);
        lc.setMaxCount(1);
        lc.setSecurityGroup(securityGroups);

        ReservationDescription result = ec2.runInstances(lc);
        String resultInstanceId = null;

        for (Instance inst : result.getInstances()) {
            resultInstanceId = inst.getInstanceId();
        }

        return resultInstanceId;
    }

    public void waitInstance(List<String> instanceList) {
        String state = null;
        Integer count = null;
        try {
            do {
                count = 0;
                for (String instanceId : instanceList) {
                    state = getInstanceStatus(instanceId);
                    if (state.equals("pending")) {
                        count += 1;
                        System.out.println("waiting...");
                        Thread.sleep(1000);
                    }
                }
            } while (count != 0);
        } catch (Exception e) {
            log.error(e);
        }
        System.out.println("Success:Running instances");
    }

    // �N�����ꂽ�C���X�^���X�̏�Ԃ��擾���܂�
    public Instance describeInstance(String instanceId) throws Exception {
        List<String> params = new ArrayList<String>();
        params.add(instanceId);
        List<ReservationDescription> instances = ec2.describeInstances(params);
        Instance resultInstance = null;
        for (ReservationDescription res : instances) {
            for (Instance inst : res.getInstances()) {
                resultInstance = inst;
            }
        }
        return resultInstance;
    }

    // �N�����Ă���C���X�^���X�̈ꗗ���擾���܂�
    public List<ReservationDescription> describeInstances() throws Exception {
        List<String> params = new ArrayList<String>();
        params = new ArrayList<String>();
        List<ReservationDescription> instances = ec2.describeInstances(params);
        return instances;
    }

    // �w�肵���C���X�^���X�̏��(pending, running, shutting-down, terminated)��Ԃ��܂�
    public String getInstanceStatus(String instanceId) throws Exception {
        List<String> params = new ArrayList<String>();
        String state = new String();
        params.add(instanceId);
        List<ReservationDescription> instances = ec2.describeInstances(params);
        for (ReservationDescription res : instances) {
            for (Instance inst : res.getInstances()) {
                state = inst.getState();
            }
        }

        return state;
    }

    // �}�V���C���[�W�̈ꗗ��Ԃ��܂�
    public List<ImageDescription> describeImages() throws Exception {
        List<String> params = new ArrayList<String>();
        List<ImageDescription> images = ec2.describeImages(params);
        return images;
    }
}
