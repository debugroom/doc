#!/bin/bash
# -*- coding: utf-8 -*-

abs_path=$(cd $(dirname $0) && pwd)
prefix_path=$(cd ${abs_path}/../ && pwd)

# get Web servers information
instance_facts=`mco facts ipaddress -F fqdn=/^web/ -v -j`
old_instance_ids=($(ruby ${prefix_path}/bin/retrieve instance_id mco ${instance_facts}))
echo "old_instances: ${old_instance_ids[@]}"

if [ ${#old_instance_ids[*]} -eq 0 ] ; then
  exit 0
fi

array_length=${#old_instance_ids[*]}

# launch instance
instance_ids=($(${prefix_path}/bin/deploy instances launch web --count=${array_length} | ${prefix_path}/bin/retrieve instance_id cloud))
echo "launch instance: ${instance_ids[@]}"

# wait instance state running
${prefix_path}/bin/deploy instances wait --instanceids=${instance_ids[@]}
echo "running instance"

# add deploy server ipaddr to webnode
mail_ipaddr=`mco facts ipaddress -F fqdn=/^mail/ -j | ${prefix_path}/bin/retrieve ip mco`
echo "mail server: $mail_ipaddr"

db_ipaddr=`mco facts ipaddress -F fqdn=/^db/ -j | ${prefix_path}/bin/retrieve ip mco`
echo "db server: $db_ipaddr"

deploy_ipaddr=`/sbin/ip route get 8.8.8.8 |head -1 |awk '{print $7}'`
for instance_id in "${instance_ids[@]}"; do
  ipaddr=`${prefix_path}/bin/deploy instances describe --instanceids=${instance_id} --key=ipaddr | ${prefix_path}/bin/retrieve ip cloud`
  echo "deploy ${instance_id}:${ipaddr}"
  ${prefix_path}/bin/deploy ssh exec ${ipaddr} "echo \"${deploy_ipaddr} deploy.nii.localdomain\" >> /etc/hosts"
  ${prefix_path}/bin/deploy ssh exec ${ipaddr} "echo \"${mail_ipaddr} mail.nii.localdomain\" >> /etc/hosts"
  ${prefix_path}/bin/deploy ssh exec ${ipaddr} "echo \"${db_ipaddr} db.nii.localdomain\" >> /etc/hosts"
  ${prefix_path}/bin/deploy ssh exec ${ipaddr} "/etc/init.d/puppet start"
  ${prefix_path}/bin/deploy ssh exec ${ipaddr} "/etc/init.d/mcollective start"

  #${prefix_path}/bin/deploy instances setup --instanceid=${instance_id} --mail=${mail_ipaddr} --db=${db_ipaddr}
done

# add puppetca to puppetmaster
echo 'start puppetca'
puppetca -l
puppetca -s --all

echo 'end puppetca'

# wait for it to finish the execution of the puppet agent
for instance_id in "${instance_ids[@]}"; do
  fqdn="web.${instance_id}.nii.localdomain"
  while :
  do
    ip=`mco facts ipaddress -I ${fqdn} -j | ${prefix_path}/bin/retrieve ip mco`
    if [ $ip ]; then
      break
    fi
    sleep 10
  done
  echo "facts created: ${instance_id}"
  # restart the tomcat to run war file that was distributed  
  mco service tomcat6 restart -F fqdn=${fqdn}
  # prevent the automatic update ot puppet agent
  mco puppetd disable -F fqdn=${fqdn}
done

# shutdown old instances
echo "stop old webnode"
${prefix_path}/task/delete_webnode.sh ${old_instance_ids[@]}
