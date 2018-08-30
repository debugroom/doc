package jp.ac.nii.sample.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

// �w�肳�ꂽ�ݒ�t�@�C����ǂݍ��ރ��[�e�B���e�B�N���X�ł�
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

    // �ݒ�t�@�C���ɋL�q���ꂽ�C���[�W�����ׂĈꗗ�ɂ��ĕԂ��܂�
    public List<String> getImageList() {
        List<String> imageList = new ArrayList<String>();
        imageList.add(getProperty("dbImageId"));
        imageList.add(getProperty("appImageId"));
        imageList.add(getProperty("webImageId"));
        imageList.add(getProperty("lbImageId"));
        return imageList;
    }

    // �ݒ�t�@�C���ɋL�q���ꂽ�C���[�W�𖼑O���ł��ׂĕԂ��܂�
    public HashMap<String, String> getImageMap() {
        HashMap<String, String> imageMap = new HashMap<String, String>();
        imageMap.put("dbImageId", getProperty("dbImageId"));
        imageMap.put("appImageId", getProperty("appImageId"));
        imageMap.put("webImageId", getProperty("webImageId"));
        imageMap.put("lbImageId", getProperty("lbImageId"));
        return imageMap;
    }

    // �ݒ�t�@�C���ɋL�q���ꂽ�Z�L�����e�B�O���[�v���ꗗ�ɂ��ĕԂ��܂�
    public List<String> getSecurityGroups() {
        List<String> ret = new ArrayList<String>();
        ret.add(getProperty("securityGroup"));
        return ret;
    }

}
