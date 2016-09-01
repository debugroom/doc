#!/bin/bash

PATH=/bin:/usr/bin:/sbin:/usr/sbin
LANG=C
LC_ALL=C

function get_metadata() {
       local param=$1
       for i in {1..10}; do
               curl -s --retry 3 http://169.254.169.254/latest/meta-data/${param} && exit 0
               sleep 3
       done
       exit 1
}

instance_type=$1
	
case "$instance_type" in
  "deploy")
    HN="deploy.nii.localdomain"
  ;;
  "lb")
    HN="lb.nii.localdomain"
  ;;
  "web")
    inst_id=`get_metadata instance-id`
    HN="web.$inst_id.nii.localdomain"
  ;;
  "db")
    HN="db.nii.localdomain"
  ;;
  "monitor")
    HN="monitor.nii.localdomain"
  ;;
  "mail")
    HN="mail.nii.localdomain"
  ;;
  *)
    echo "unknown instance type"
    exit 1
  ;;
esac

# Set up the host name
echo "Setting the hostname: $HN"
if [ -n "$HN" ]; then
        grep -q "$HN" /etc/hostname
        if [ $? -ne 0 ]; then
                hostname $HN
                echo $HN > /etc/hostname
        fi

        # Add it to the hosts file if not there yet
        grep -q "$HN" /etc/hosts
        if [ $? -ne 0 ]; then
                echo '127.0.0.1 '$HN >> /etc/hosts
        fi
fi

