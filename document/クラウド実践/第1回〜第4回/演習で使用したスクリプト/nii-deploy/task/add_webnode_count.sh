#!/bin/bash
# -*- coding: utf-8 -*-

abs_path=$(cd $(dirname $0) && pwd)
prefix_path=$(cd ${abs_path}/../ && pwd)

count=$1

if [ ! "${count}" ]; then
    echo "usage: ./add_webnode_count.sh num :num is number"
    exit 1
fi

# launch instance
instance_ids=`${prefix_path}/bin/deploy instances launch web --count=${count} | ${prefix_path}/bin/retrieve instance_id cloud`
echo "launch instance: ${instance_ids}"

# wait instance state running
${prefix_path}/bin/deploy instances wait --instanceids=${instance_ids}
echo "running instance"

# add deploy server ipaddr to webnode
mail=`mco facts ipaddress -F fqdn=/^mail/ -j`
mail_ipaddr=`ruby ${prefix_path}/bin/retrieve ip mco ${mail}`
echo "mail server: $mail_ipaddr"

db=`mco facts ipaddress -F fqdn=/^db/ -j`
db_ipaddr=`ruby ${prefix_path}/bin/retrieve ip mco ${db}`
echo "db server: $db_ipaddr"

deploy_ipaddr=`/sbin/ip route get 8.8.8.8 |head -1 |awk '{print $7}'`
for instance_id in ${instance_ids}; do
  ipaddr=`${prefix_path}/bin/deploy instances describe --instanceids=${instance_id} --key=ipaddr | ${prefix_path}/bin/retrieve ip cloud`
  ${prefix_path}/bin/deploy ssh exec ${ipaddr} "echo \"${deploy_ipaddr} deploy.nii.localdomain\" >> /etc/hosts"
  ${prefix_path}/bin/deploy ssh exec ${ipaddr} "echo \"${mail_ipaddr} mail.nii.localdomain\" >> /etc/hosts"
  ${prefix_path}/bin/deploy ssh exec ${ipaddr} "echo \"${db_ipaddr} db.nii.localdomain\" >> /etc/hosts"
  ${prefix_path}/bin/deploy ssh exec ${ipaddr} "/etc/init.d/puppet start"
  ${prefix_path}/bin/deploy ssh exec ${ipaddr} "/etc/init.d/mcollective start"
done

# add puppetca to puppetmaster
echo 'start puppetca'
puppetca -l
puppetca -s --all

echo 'end puppetca'

# wait for it to finish the execution of the puppet agent
for instance_id in ${instance_ids}; do
    fqdn="web.${instance_id}.nii.localdomain"
    while :
    do
        ip=`mco facts ipaddress -I ${fqdn} | ${prefix_path}/bin/retrieve ip mco`
	if [ $ip ]; then
            break
	fi
	sleep 10
    done
done

# restart the tomcat to run war file that was distributed
for instance_id in ${instance_ids}; do
    mco service tomcat6 restart -F fqdn=${fqdn} -v
done

echo 'restart ganglia-monitor'
yes | mco service ganglia-monitor restart -v

echo 'start nagios'
output_dir=/var/tmp
# create config nagios
mco facts ipaddress -F fqdn=/^lb/ -j | ${prefix_path}/bin/retrieve ip mco --format file > ${output_dir}/monitor/lb.ipset
mco facts ipaddress -F fqdn=/^web/ -j | ${prefix_path}/bin/retrieve ip mco --format file > ${output_dir}/monitor/web.ipset
mco facts ipaddress -F fqdn=/^db/ -j | ${prefix_path}/bin/retrieve ip mco --format file > ${output_dir}/monitor/db.ipset
mco facts ipaddress -F fqdn=/^deploy/ -j | ${prefix_path}/bin/retrieve ip mco --format file > ${output_dir}/monitor/deploy.ipset
mco facts ipaddress -F fqdn=/^monitor/ -j | ${prefix_path}/bin/retrieve ip mco --format file > ${output_dir}/monitor/monitor.ipset

# refresh nagios config
mco puppetd runonce -F fqdn=/^monitor/ -v

# restart nagios
mco service nagios3 restart -F fqdn=/^monitor/ -v
mco service gmetad restart -F fqdn=/^monitor/ -v

# update nginx
echo "start nginx"

# create nginx.conf
echo "create nginx.conf"
mco facts ipaddress -F fqdn=/^web/ -j | ${prefix_path}/bin/retrieve ip mco --format file > ${output_dir}/nginx/nginx.ipset
echo "output file: ${output_dir}/nginx/nginx.ipset"

# refresh nginx.conf of lb server
echo "refresh nginx.conf"
mco puppetd runonce -I lb.nii.localdomain -v

# restart nginx proccess
echo "restart nginx"
mco service nginx restart -F fqdn=/^lb/ -v

# prevent the automatic update ot puppet agent
mco puppetd disable -F fqdn=/^web/ -v
