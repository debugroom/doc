#!/bin/bash
# -*- coding: utf-8 -*-

abs_path=$(cd $(dirname $0) && pwd)
prefix_path=$(cd ${abs_path}/../ && pwd)

instance_ids=$*

if [ ! "${instance_ids}" ]; then
    echo "usage ./delete_webnode.sh web_instance_id"
    exit 1
fi

# stop mcollective
echo "stop mcollective"
for instance_id in $instance_ids; do
  ipaddr=`${prefix_path}/bin/deploy instances describe --instanceids=${instance_id} --key=ipaddr | ${prefix_path}/bin/retrieve ip cloud`
  ${prefix_path}/bin/deploy ssh exec ${ipaddr} "/etc/init.d/mcollective stop"
done
# update nagios
echo "udpate nagios"

output_dir=/var/tmp
# create config nagios
mco facts ipaddress -F fqdn=/^lb/ -j | ${prefix_path}/bin/retrieve ip mco --output file > ${output_dir}/monitor/lb.ipset
mco facts ipaddress -F fqdn=/^web/ -j | ${prefix_path}/bin/retrieve ip mco --output file > ${output_dir}/monitor/web.ipset
mco facts ipaddress -F fqdn=/^db/ -j | ${prefix_path}/bin/retrieve ip mco --output file > ${output_dir}/monitor/db.ipset
mco facts ipaddress -F fqdn=/^deploy/ -j | ${prefix_path}/bin/retrieve ip mco --output file > ${output_dir}/monitor/deploy.ipset
mco facts ipaddress -F fqdn=/^monitor/ -j | ${prefix_path}/bin/retrieve ip mco --output file > ${output_dir}/monitor/monitor.ipset

# refresh nagios config
mco puppetd runonce -F fqdn=/^monitor/ -v

# restart nagios
mco service nagios3 restart -F fqdn=/^monitor/ -v


# update nginx
echo "udpate nginx"

# create nginx.conf
echo "create nginx.conf"
mco facts ipaddress -F fqdn=/^web/ -j | ${prefix_path}/bin/retrieve ip mco --output file > ${output_dir}/nginx/nginx.ipset
echo "output file: ${output_dir}/nginx/nginx.ipset"

# refresh nginx.conf of lb server
echo "refresh nginx.conf"
mco puppetd runonce -I lb.nii.localdomain -v

# restart nginx proccess
echo "restart nginx"
mco service nginx restart -F fqdn=/^lb/ -v


# restart ganglia-monitor
echo "restart ganglia-monitor"
yes | mco service ganglia-monitor restart -v

# terminate instances
echo "terminate instance"
if [ -n "${instance_ids}" ]; then
    ${prefix_path}/bin/deploy instances terminate --instanceids=${instance_ids}
fi

sleep 30
# restart gmetad
echo "restart gmetad"
mco service gmetad restart -F fqdn=/^monitor/ -v

