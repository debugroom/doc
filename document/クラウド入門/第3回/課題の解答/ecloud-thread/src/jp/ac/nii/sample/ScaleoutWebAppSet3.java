package jp.ac.nii.sample;

import jp.ac.nii.sample.utility.PropertyLoader;

public class ScaleoutWebAppSet3 {
    public static void main(String[] args) {
        // �ݒ�t�@�C���̓ǂݍ��݂����܂�
        PropertyLoader config = new PropertyLoader("src/config.properties");
        // thread�̐����擾����
        Integer threadNumber = new Integer(config.getProperty("threads"));
        // thread���쐬����
        ScaleoutWebAppSetThread[] threads = new ScaleoutWebAppSetThread[threadNumber];
        // ����ŃX�P�[���A�E�g�����s����
        for (int i = 0; i < threadNumber; i++) {
            threads[i] = new ScaleoutWebAppSetThread();
            threads[i].start();
        }
        // thread�����ׂďI������܂ő҂�
        for (int i = 0; i < threadNumber; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }
}
